/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * license-end
 */
package org.flowerplatform.flexdiagram.mindmap
{
	import mx.core.DPIClassification;
	import mx.core.FlexGlobals;
	import mx.events.FlexEvent;
	import mx.events.PropertyChangeEvent;
	import mx.events.ResizeEvent;
	import mx.managers.IFocusManagerComponent;
	
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.IDiagramShellContextAware;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.FlowerArrayList;
	import org.flowerplatform.flexutil.focusable_component.FocusableRichText;
	import org.flowerplatform.flexutil.renderer.IIconsComponentExtensionProvider;
	import org.flowerplatform.flexutil.renderer.IconsComponentExtension;
	
	import spark.components.DataRenderer;
	import spark.components.Group;
	import spark.components.RichText;
	import spark.layouts.HorizontalLayout;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class AbstractMindMapModelRenderer extends DataRenderer implements IAbstractMindMapModelRenderer, IDiagramShellContextAware, IIconsComponentExtensionProvider, IFocusManagerComponent {
					
		protected static const BACKGROUND_COLOR_DEFAULT:uint = 0xFFFFFFFF;
		
		protected static const circleRadius:int = 3;
		
		protected var _context:DiagramShellContext;
			
		protected var labelDisplay:RichText;
		
		protected var iconsComponentExtension:IconsComponentExtension;
		
		protected var backgroundColor:uint = BACKGROUND_COLOR_DEFAULT;
				
		/**
		 * If <code>true</code>, draw only this class graphics (border and small circle on right).
		 */ 
		public var drawOnlyBorderGraphics:Boolean = false;
		
		public function AbstractMindMapModelRenderer() {
			super();
			addEventListener(FlexEvent.INITIALIZE, initializeHandler);	
			mouseEnabledWhereTransparent = true;
			
			if (!FlexUtilGlobals.getInstance().isMobile) {
				minHeight = 22;
				minWidth = 10;
			} else {
				switch (applicationDPI) {
					case DPIClassification.DPI_320:	{
						minHeight = 88;
						break;
					}
					case DPIClassification.DPI_240:	{
						minHeight = 66;
						break;
					}
					default: {
						// default PPI160
						minHeight = 44;
						break;
					}
				}
			}
			
			iconsComponentExtension = new IconsComponentExtension(this);
			
			setLayout();
		}
		
		override public function drawFocus(isFocused:Boolean):void {			
		}
				
		public function setLayout():void {
			var hLayout:HorizontalLayout = new HorizontalLayout();
			hLayout.gap = 2;
			hLayout.paddingBottom = 2;
			hLayout.paddingTop = 2;
			hLayout.paddingLeft = 2;
			hLayout.paddingRight = 2;
			hLayout.verticalAlign = "middle";
			
			this.layout = hLayout;
		}
		
		public function get diagramShellContext():DiagramShellContext {			
			return _context;
		}
		
		public function set diagramShellContext(value:DiagramShellContext):void {
			this._context = value;
		}		
				
		public function get icons():FlowerArrayList	{			
			return iconsComponentExtension.icons;
		}
		
		public function set icons(value:FlowerArrayList):void {
			iconsComponentExtension.icons = value;
		}
		
		override public function set data(value:Object):void {
			if (data != null) {
				data.removeEventListener(PropertyChangeEvent.PROPERTY_CHANGE, modelChangedHandler);
				depth = 0;
				unassignData();
			}
			
			super.data = value;
			
			if (data != null) {
				// set depth from model's dynamic object if available
				// model's children must have a greater depth than the model because 
				// when drawing more complex graphics (like clouds), they must be displayed above them
				var dynamicObject:Object = diagramShellContext.diagramShell.getDynamicObject(diagramShellContext, data);
				if (dynamicObject.depth) {
					depth = dynamicObject.depth;
				}
				data.addEventListener(PropertyChangeEvent.PROPERTY_CHANGE, modelChangedHandler);				
				assignData();
			}
		}
				
		public function get applicationDPI():Number	{
			return FlexGlobals.topLevelApplication.applicationDPI;
		}
					
		protected function initializeHandler(event:FlexEvent):void {				
			addEventListener(ResizeEvent.RESIZE, resizeHandler);			
		}
		
		override protected function createChildren():void {			
			super.createChildren();
			
			labelDisplay = new FocusableRichText();		
			labelDisplay.percentHeight = 100;
			labelDisplay.percentWidth = 100;			
			labelDisplay.setStyle("verticalAlign" , "middle");	
			addElement(labelDisplay);
		}
		
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {			
			super.updateDisplayList(unscaledWidth, unscaledHeight);
						
			graphics.clear();
			
			drawBorder(unscaledWidth, unscaledHeight);
			
			if (!drawOnlyBorderGraphics) {
				drawGraphics(unscaledWidth, unscaledHeight);
			}			
		}
		
		protected function drawBorder(unscaledWidth:Number, unscaledHeight:Number):void {
			graphics.lineStyle(1, 0x808080);
			graphics.beginFill(backgroundColor, 1);
			graphics.drawRoundRect(0, 0, unscaledWidth, unscaledHeight, 10, 10);		
			
			if (canDrawCircle()) {
				drawLittleCircle();
			}
			graphics.endFill();
		}
		
		protected function drawLittleCircle(circleY:Number=NaN):void {
			if (isNaN(circleY)) {
				circleY = height/2;
			}
			graphics.beginFill(BACKGROUND_COLOR_DEFAULT, 1);
			var side:int = MindMapDiagramShell(diagramShellContext.diagramShell).getModelController(diagramShellContext, data).getSide(diagramShellContext, data);
			if (side == MindMapDiagramShell.POSITION_LEFT) {
				graphics.drawCircle(-circleRadius, circleY, circleRadius);
			} else if (side == MindMapDiagramShell.POSITION_RIGHT) {						
				graphics.drawCircle(width + circleRadius, circleY, circleRadius);
			}
		}
		
		protected function drawGraphics(unscaledWidth:Number, unscaledHeight:Number):void {	
			drawBorder(unscaledWidth, unscaledHeight);
		}
		
		protected function canDrawCircle():Boolean {
			throw new Error("This method needs to be implemented.");
		}
		
		protected function modelChangedHandler(event:PropertyChangeEvent):void {
			throw new Error("This method needs to be implemented.");
		}
		
		protected function resizeHandler(event:ResizeEvent):void {
			throw new Error("This method needs to be implemented.");
		}
		
		protected function assignData():void {
			throw new Error("This method needs to be implemented.");
		}
		
		protected function unassignData():void {
			throw new Error("This method needs to be implemented.");
		}
		
		public function getLabelDisplay():RichText {
			return labelDisplay;
		}
		
		public function newIconIndex():int {			
			return numElements - 1; // add icon before label
		}
				
		override public function validateDisplayList():void {
			iconsComponentExtension.validateDisplayList();
			super.validateDisplayList();			
		}
		
		override public function validateProperties():void {			
			super.validateProperties();
			iconsComponentExtension.validateProperties();
		}
		
		override public function validateSize(recursive:Boolean=false):void	{
			iconsComponentExtension.validateSize();
			super.validateSize(recursive);
		}
				
	}
}
