package org.flowerplatform.flexdiagram.mindmap
{
	import mx.core.DPIClassification;
	import mx.core.FlexGlobals;
	import mx.events.FlexEvent;
	import mx.events.PropertyChangeEvent;
	import mx.events.ResizeEvent;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.renderer.IDiagramShellAware;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.FlowerArrayList;
	import org.flowerplatform.flexutil.renderer.IIconsComponentExtensionProvider;
	import org.flowerplatform.flexutil.renderer.IconsComponentExtension;
	
	import spark.components.DataRenderer;
	import spark.components.Label;
	import spark.layouts.HorizontalLayout;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class AbstractMindMapModelRenderer extends DataRenderer implements IDiagramShellAware, IIconsComponentExtensionProvider {
						
		protected static const circleRadius:int = 3;
		
		protected var _diagramShell:MindMapDiagramShell;
			
		protected var labelDisplay:Label;
		
		protected var iconsExtension:IconsComponentExtension;
		
		public function AbstractMindMapModelRenderer() {
			super();
			addEventListener(FlexEvent.INITIALIZE, initializeHandler);	
									
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
			
			iconsExtension = new IconsComponentExtension(this);
			var hLayout:HorizontalLayout = new HorizontalLayout();
			hLayout.gap = 2;
			hLayout.paddingBottom = 2;
			hLayout.paddingTop = 2;
			hLayout.paddingLeft = 2;
			hLayout.paddingRight = 2;
			hLayout.verticalAlign = "middle";
			
			this.layout = hLayout;
		}
		
		public function get diagramShell():DiagramShell {
			return _diagramShell;
		}
		
		public function set diagramShell(value:DiagramShell):void {
			_diagramShell = MindMapDiagramShell(value);
		}
				
		public function get icons():FlowerArrayList	{			
			return iconsExtension.icons;
		}
		
		public function set icons(value:FlowerArrayList):void {
			iconsExtension.icons = value;
		}
		
		override public function set data(value:Object):void {
			if (super.data != null) {
				data.removeEventListener(PropertyChangeEvent.PROPERTY_CHANGE, modelChangedHandler);
			}
			
			super.data = value;
			
			if (data != null) {
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
			
			labelDisplay = new Label();		
			labelDisplay.percentHeight = 100;
			labelDisplay.percentWidth = 100;
			labelDisplay.setStyle("verticalAlign" , "middle");		
			addElement(labelDisplay);
		}
		
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {				
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			
			graphics.clear();
			graphics.lineStyle(1, 0x808080);
			graphics.beginFill(0xCCCCCC, 0);
			graphics.drawRoundRect(0, 0, unscaledWidth, unscaledHeight, 10, 10);		
			
			if (canDrawCircle(data)) {
				var side:int = _diagramShell.getModelController(data).getSide(_diagramShell.context, data);
				if (side == MindMapDiagramShell.POSITION_LEFT) {
					graphics.drawCircle(-circleRadius, height/2, circleRadius);
				} else if (side == MindMapDiagramShell.POSITION_RIGHT) {						
					graphics.drawCircle(width + circleRadius, height/2, circleRadius);
				}
			}
		}
				
		protected function canDrawCircle(model:Object):Boolean {
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
		
		public function newIconIndex():int {			
			return numElements - 1; // add icon before label
		}
				
		override public function validateDisplayList():void {
			super.validateDisplayList();			
			iconsExtension.validateDisplayList();
		}
		
		override public function validateProperties():void {
			super.validateProperties();
			iconsExtension.validateProperties();
		}
		
		override public function validateSize(recursive:Boolean=false):void	{
			iconsExtension.validateSize();
			super.validateSize(recursive);
		}
				
	}
}