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
package org.flowerplatform.flexdiagram.mindmap {
	import flash.display.GradientType;
	import flash.events.Event;
	import flash.geom.Matrix;
	import flash.system.Capabilities;
	
	import mx.collections.IList;
	import mx.core.DPIClassification;
	import mx.core.FlexGlobals;
	import mx.core.UIComponent;
	import mx.events.CollectionEvent;
	import mx.events.PropertyChangeEvent;
	import mx.events.ResizeEvent;
	
	import spark.components.DataGroup;
	import spark.components.DataRenderer;
	import spark.components.IItemRenderer;
	import spark.components.supportClasses.InteractionState;
	import spark.components.supportClasses.InteractionStateDetector;
	import spark.layouts.HorizontalLayout;
	import spark.primitives.BitmapImage;
	
	import flashx.textLayout.conversion.TextConverter;
	
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.IDiagramShellContextAware;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.Utils;
	import org.flowerplatform.flexutil.focusable_component.FocusableRichText;
	
	/**
	 * @author Cristina Constantinescu
	 * @author Alexandra Topoloaga
	 */
	public class AbstractMindMapNodeRenderer extends DataRenderer implements IDiagramShellContextAware, IItemRenderer {
		
		protected static const BACKGROUND_COLOR_DEFAULT:uint = 0xFFFFFFFF;

		protected static const circleRadius:int = 3;
			
		protected static const TEXT_COLOR_DEFAULT:uint = 0x000000;
		
		protected static const FONT_FAMILY_DEFAULT:String = "SansSerif";
		
		protected static const SCREEN_DPI:Number = (Capabilities.screenDPI == 72 ? 96 : Capabilities.screenDPI) / 72;
		
		public static const CLOUD_TYPE_NONE:String = "ARC";
		
		public static const CLOUD_TYPE_RECTANGLE:String = "RECT";
		
		public static const CLOUD_TYPE_ROUNDED_RECTANGLE:String = "ROUND_RECT";
				
		protected var _label:FocusableRichText;
		
		protected var backgroundColor:uint = BACKGROUND_COLOR_DEFAULT;
		
		protected var _context:DiagramShellContext;
		
		protected var _cloudColor:uint;
		
		protected var _cloudType:String;
		
		protected var _icons:IList;
		
		public var showSelected:Boolean = false;
		
		/**************************************************************************
		 * Graphic properties supported by this renderer.
		 *************************************************************************/
		public function set text(value:String):void {
			if (value != null) {
				_label.textFlow = TextConverter.importToFlow(value , Utils.isHTMLText(value) ? TextConverter.TEXT_FIELD_HTML_FORMAT : TextConverter.PLAIN_TEXT_FORMAT);	
			}
		}
		
		public function set fontFamily(value:String):void {
			if (value == null) {
				value = FONT_FAMILY_DEFAULT;
			}
			_label.setStyle("fontFamily", Utils.getSupportedFontFamily(value));
		}
		
		public function set fontSize(value:Number):void {
			if (value == 0) {
				value = 9;
			} 
			_label.setStyle("fontSize", (SCREEN_DPI * value));
		}
		
		public function set fontWeight(value:Boolean):void {
			_label.setStyle("fontWeight", value == true ? "bold" : "normal");
		}
		
		public function set fontStyle(value:Boolean):void {
			_label.setStyle("fontStyle", value == true ? "italic" : "normal");
		}
		
		public function set textColor(value:uint):void {
			if (value == 0) {
				value = TEXT_COLOR_DEFAULT;
			} 
			_label.setStyle("color", value);
		}
		
		public function set background(value:uint):void {
			if (value == 0) {
				value = BACKGROUND_COLOR_DEFAULT;
			}
			backgroundColor = value;
		}
		
		public function set cloudColor(value:uint):void {
			_cloudColor = value;
			invalidateDisplayList();
		}
		
		public function set cloudType(value:String):void {
			switch (value) {
				case "No shape": 
					value = CLOUD_TYPE_NONE;
					break;
				case "Rectangle shape": 
					value = CLOUD_TYPE_RECTANGLE;
					break;
				case "Rounded rectangle shape": 
					value = CLOUD_TYPE_ROUNDED_RECTANGLE;
					break;
			}
			_cloudType = value;
			invalidateDisplayList();
		}
		
		public function set icons(value:IList):void {		
			if (_icons != null) {
				_icons.removeEventListener(CollectionEvent.COLLECTION_CHANGE, handleIconsChanged);
			}
			if (value == _icons) {
				return;
			}
			_icons = value;
			if (_icons != null) {
				_icons.addEventListener(CollectionEvent.COLLECTION_CHANGE, handleIconsChanged);				
			}
			handleIconsChanged(null);
		}
		
		/**************************************************************************
		 * Other functions.
		 *************************************************************************/
		
		public function AbstractMindMapNodeRenderer() {
			mouseEnabledWhereTransparent = true;
			
			if (!FlexUtilGlobals.getInstance().isMobile) {
				minHeight = 22;
				minWidth = 10;
			} else {
				switch (FlexGlobals.topLevelApplication.applicationDPI) {
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
			setLayout();
			
			interactionStateDetector = new InteractionStateDetector(this);
			interactionStateDetector.addEventListener(Event.CHANGE, interactionStateDetector_changeHandler);
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
			if (!hasEventListener(ResizeEvent.RESIZE)) {
				addEventListener(ResizeEvent.RESIZE, resizeHandler);	
			}
		}
		
		protected function get mindMapDiagramShell():MindMapDiagramShell {
			return diagramShellContext != null ? MindMapDiagramShell(diagramShellContext.diagramShell) : null;
		}
		
		override public function set data(value:Object):void {
			if (data != null) {
				endModelListen();	
			}
			super.data = value;
			if (data != null) {
				beginModelListen();
			}
		}
		
		protected function beginModelListen():void {
			data.addEventListener(PropertyChangeEvent.PROPERTY_CHANGE, modelChangedHandler);	
			modelChangedHandler(null);
		}
		
		protected function endModelListen():void {
			data.removeEventListener(PropertyChangeEvent.PROPERTY_CHANGE, modelChangedHandler);
		}
				
		/**
		 * We need to inform the system about the real dimensions of the renderer. The system will
		 * process the nodes again, with the real dimensions.
		 */
		protected function resizeHandler(event:ResizeEvent):void {
			if (height == 0 || width == 0) {
				// don't change values if first resize, wait until component fully initialized
				return;
			}
			var refresh:Boolean = false;
			if (mindMapDiagramShell.getPropertyValue(diagramShellContext, data, "width") != width) {
				mindMapDiagramShell.setPropertyValue(diagramShellContext, data, "width", width);
				refresh = true;
			}
			if (mindMapDiagramShell.getPropertyValue(diagramShellContext, data, "height") != height) {			
				mindMapDiagramShell.setPropertyValue(diagramShellContext, data, "height", height);
				refresh = true;
			}
			
			if (refresh) {					
				mindMapDiagramShell.shouldRefreshModelPositions(diagramShellContext, mindMapDiagramShell.rootModel);
				mindMapDiagramShell.shouldRefreshVisualChildren(diagramShellContext, mindMapDiagramShell.rootModel);
			}
		}
		
		/**
		 * Invoked when <code>data</code> changes or when the mind map system
		 * recalculates data in the dynamic object.
		 */
		protected function modelChangedHandler(event:PropertyChangeEvent):void {
		}
		
		override protected function createChildren():void {
			super.createChildren();
			_label = new FocusableRichText();
			_label.percentHeight = 100;
			_label.percentWidth = 100;			
			_label.setStyle("verticalAlign" , "middle");
			addElement(_label);
		}
		
		protected function drawCloud(unscaledWidth:Number, unscaledHeight:Number):void {			
			if (_cloudType == CLOUD_TYPE_RECTANGLE || _cloudType == CLOUD_TYPE_ROUNDED_RECTANGLE) {				
				graphics.lineStyle(2, 0x808080); // gray line with bigger thickness
				graphics.beginFill(Utils.convertValueToColor(_cloudColor), 1);
				
				var diagramShell:MindMapDiagramShell = MindMapDiagramShell(diagramShellContext.diagramShell);
				var cloudPadding:Number = diagramShell.getPropertyValue(diagramShellContext, data, "additionalPadding");
				var side:int = diagramShell.getModelController(diagramShellContext, data).getSide(diagramShellContext, data);
				
				var width:Number = diagramShell.getPropertyValue(diagramShellContext, data, "width");
				var height:Number = diagramShell.getPropertyValue(diagramShellContext, data, "height");
				var expandedWidth:Number = diagramShell.getPropertyValue(diagramShellContext, data, "expandedWidth");
				var expandedHeight:Number = diagramShell.getPropertyValue(diagramShellContext, data, "expandedHeight");
				
				var shapeX:Number = - cloudPadding/2;
				var shapeY:Number = - diagramShell.getDeltaBetweenExpandedHeightAndHeight(diagramShellContext, data, true)/2;
				var shapeWidth:Number = expandedWidth + cloudPadding;
				var shapeHeight:Number = Math.max(expandedHeight, height + cloudPadding);
				
				var expandedWidthLeft:Number = diagramShell.getPropertyValue(diagramShellContext, data, "expandedWidthLeft");
				var expandedWidthRight:Number = diagramShell.getPropertyValue(diagramShellContext, data, "expandedWidthRight");
				var expandedHeightLeft:Number = diagramShell.getPropertyValue(diagramShellContext, data, "expandedHeightLeft");
				var expandedHeightRight:Number = diagramShell.getPropertyValue(diagramShellContext, data, "expandedHeightRight");
				var additionalPadding:Number = diagramShell.getPropertyValue(diagramShellContext, data, "additionalPadding");
				
				if (side == MindMapDiagramShell.POSITION_LEFT) {
					shapeX -= (expandedWidth - width);
				}
				
				if (side == MindMapDiagramShell.POSITION_CENTER) {
					shapeX -= expandedWidthLeft - width;
					shapeY = - (Math.max(expandedHeightLeft, expandedHeightRight) - height - additionalPadding)/2;
					shapeWidth = expandedWidthLeft + expandedWidthRight - width; 
					shapeHeight = Math.max(expandedHeightLeft, expandedHeightRight);
				}
				
				if (_cloudType == CLOUD_TYPE_RECTANGLE) {
					graphics.drawRect(shapeX - 3, shapeY - 3, shapeWidth + 5, shapeHeight + 5);
				} else {
					graphics.drawRoundRect(shapeX - 3, shapeY - 3, shapeWidth + 5, shapeHeight + 5, 25, 25);					
				}
				graphics.endFill();
			}
		}
		
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {			
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			graphics.clear();

			drawCloud(unscaledWidth, unscaledHeight);
			
			graphics.lineStyle(1, 0x808080);
			graphics.beginFill(backgroundColor, 1);
			graphics.drawRoundRect(0, 0, unscaledWidth, unscaledHeight, 10, 10);
			
			if (shouldDrawCircle()) {
				drawLittleCircle();
			}
			graphics.endFill();
			drawBackground(unscaledWidth, unscaledHeight);
		}
		
		protected function shouldDrawCircle():Boolean {			
			return false;
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
		
		protected function handleIconsChanged(event:CollectionEvent):void {			
			/* 
			// delete all the icons displayed and display the whole list again
			while (getElementAt(0) is BitmapImage) {
				removeElementAt(0);
			}
			if (_icons != null) {
			for (var i:Number = 0; i < _icons.length; i++) {
				var iconDisplay:BitmapImage = new BitmapImage();
				iconDisplay.contentLoader = FlexUtilGlobals.getInstance().imageContentCache;
				iconDisplay.source =FlexUtilGlobals.getInstance().adjustImageBeforeDisplaying(_icons.getItemAt(i));
				iconDisplay.verticalAlign = "middle";
				iconDisplay.depth = UIComponent(this).depth;
				addElementAt(iconDisplay, i);
				}
			}*/ 
			var iconDisplay:BitmapImage;
			if (_icons != null) {
				for (var i:Number = 0; i < _icons.length; i++) {
					if (getElementAt(i) is BitmapImage) {
						iconDisplay = BitmapImage (getElementAt(i));
						iconDisplay.source = FlexUtilGlobals.getInstance().adjustImageBeforeDisplaying(_icons.getItemAt(i));
					} else {
						iconDisplay = new BitmapImage();
						iconDisplay.contentLoader = FlexUtilGlobals.getInstance().imageContentCache;
						iconDisplay.source =FlexUtilGlobals.getInstance().adjustImageBeforeDisplaying(_icons.getItemAt(i));
						iconDisplay.verticalAlign = "middle";
						iconDisplay.depth = UIComponent(this).depth;
						addElementAt(iconDisplay, i);
					}
				}
				while (getElementAt(_icons.length) is BitmapImage) {
					removeElementAt(_icons.length);
				}
			}
		}
		
		/**************************************************************************
		 * Functions used for list selection.
		 *************************************************************************/
		
		private var interactionStateDetector:InteractionStateDetector;
		
		private var _dragging:Boolean = false;
		
		public function get dragging():Boolean {
			return _dragging;
		}
		
		public function set dragging(value:Boolean):void {
			_dragging = value;
			
		}
		
		private var _itemIndex:int;
		protected var isLastItem:Boolean = false;
		
		public function get itemIndex():int {
			return _itemIndex;
		}
		
		public function set itemIndex(value:int):void {
			var wasLastItem:Boolean = isLastItem;       
			var dataGroup:DataGroup = parent as DataGroup;
			isLastItem = (dataGroup && (value == dataGroup.numElements - 1));
			
			// if whether or not we are the last item in the last has changed then
			// invalidate our display. note:  even if our new index has not changed,
			// whether or not we're the last item may have so we perform this check 
			// before the value == _itemIndex check below
			if (wasLastItem != isLastItem) 
				invalidateDisplayList();
			
			if (value == _itemIndex)
				return;
			
			_itemIndex = value;
			
			// only invalidateDisplayList() if this causes use to redraw which
			// is only if alternatingItemColors are defined (and technically also
			// only if we are not selected or down, etc..., but we'll ignore those
			// as this will shortcut 95% of the time anyways)
			if (getStyle("alternatingItemColors") !== undefined)
				invalidateDisplayList();
		}

		public function get label():String {
			return _label as String;
		}
		
		public function set label(value:String):void {

		}
		
		private var _selected:Boolean = false;
		
		public function get selected():Boolean {
			return _selected;
		}
		
		public function set selected(value:Boolean):void {
			if (value == _selected)
				return;
			
			_selected = value; 
			invalidateDisplayList();
		}
		
		private var _showsCaret:Boolean = false;
		
		public function get showsCaret():Boolean {
			return _showsCaret;
		}
		
		public function set showsCaret(value:Boolean):void {
			if (value == _showsCaret)
				return;
			
			_showsCaret = value;
			invalidateDisplayList();
		}
		
		private var _down:Boolean = false;
		
		protected function get down():Boolean {
			return _down;
		}
		
		protected function set down(value:Boolean):void {
			if (value == _down)
				return;
			
			_down = value; 
			invalidateDisplayList();
		}
		
		private var _hovered:Boolean = false;
		
		protected function get hovered():Boolean {
			return _hovered;
		}
		
		protected function set hovered(value:Boolean):void {
			if (value == _hovered)
				return;
			
			_hovered = value; 
			invalidateDisplayList();
		}
		
		protected function drawBackground(unscaledWidth:Number, unscaledHeight:Number):void {
			// figure out backgroundColor
			var backgroundColor:*;
			var downColor:* = getStyle("downColor");
			var drawBackground:Boolean = true;
			var opaqueBackgroundColor:* = undefined;
			
			if (down && downColor !== undefined) {
				backgroundColor = downColor;
			} else if (selected) {
				backgroundColor = getStyle("selectionColor");
			} else if (hovered) {
				backgroundColor = getStyle("rollOverColor");
			} else if (showsCaret) {
				backgroundColor = getStyle("selectionColor");
			} else {
				var alternatingColors:Array;
				var alternatingColorsStyle:Object = getStyle("alternatingItemColors");
				
				if (alternatingColorsStyle)
					alternatingColors = (alternatingColorsStyle is Array) ? (alternatingColorsStyle as Array) : [alternatingColorsStyle];
				
				if (alternatingColors && alternatingColors.length > 0) {
					// translate these colors into uints
					styleManager.getColorNames(alternatingColors);
					
					backgroundColor = alternatingColors[itemIndex % alternatingColors.length];
				} else {
					// don't draw background if it is the contentBackgroundColor. The
					// list skin handles the background drawing for us. 
					drawBackground = false;
				}
				
			} 
			
			// draw backgroundColor
			// the reason why we draw it in the case of drawBackground == 0 is for
			// mouse hit testing purposes
			if (showSelected) {
				graphics.beginFill(backgroundColor, drawBackground ? 1 : 0);
				graphics.lineStyle();
				graphics.drawRect(0, 0, unscaledWidth, unscaledHeight);
				graphics.endFill();
			}
			// Selected and down states have a gradient overlay as well
			// as different separators colors/alphas
			if (selected || down) {
				var colors:Array = [0x000000, 0x000000 ];
				var alphas:Array = [.2, .1];
				var ratios:Array = [0, 255];
				var matrix:Matrix = new Matrix();
				
				// gradient overlay
				matrix.createGradientBox(unscaledWidth, unscaledHeight, Math.PI / 2, 0, 0 );
				graphics.beginGradientFill(GradientType.LINEAR, colors, alphas, ratios, matrix);
				graphics.drawRect(0, 0, unscaledWidth, unscaledHeight);
				graphics.endFill();
			}
		}
		
		private function interactionStateDetector_changeHandler(event:Event):void {
			if (showSelected) {
				down = (interactionStateDetector.state == InteractionState.DOWN);
				hovered = (interactionStateDetector.state == InteractionState.OVER);
			}
		}
	}
}