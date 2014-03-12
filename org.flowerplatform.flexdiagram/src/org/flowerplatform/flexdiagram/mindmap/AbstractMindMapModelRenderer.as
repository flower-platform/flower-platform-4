package org.flowerplatform.flexdiagram.mindmap
{
	import mx.core.DPIClassification;
	import mx.core.FlexGlobals;
	import mx.core.IVisualElement;
	import mx.core.UIComponent;
	import mx.events.FlexEvent;
	import mx.events.PropertyChangeEvent;
	import mx.events.ResizeEvent;
	
	import org.flowerplatform.flexdiagram.ControllerUtils;
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.IDiagramShellContextAware;
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
	public class AbstractMindMapModelRenderer extends DataRenderer implements IDiagramShellContextAware, IIconsComponentExtensionProvider {
					
		protected static const BACKGROUND_COLOR_DEFAULT:uint = 0xFFFFFFFF;
		
		protected static const circleRadius:int = 3;
		
		protected var _context:DiagramShellContext;
			
		protected var labelDisplay:Label;
		
		protected var iconsComponentExtension:IconsComponentExtension;
		
		protected var backgroundColor:uint = BACKGROUND_COLOR_DEFAULT;
		protected var allowBaseRendererToClearGraphics:Boolean = true;
		
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
			
			iconsComponentExtension = new IconsComponentExtension(this);
			
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
			
			labelDisplay = new Label();		
			labelDisplay.percentHeight = 100;
			labelDisplay.percentWidth = 100;
			labelDisplay.setStyle("verticalAlign" , "middle");		
			addElement(labelDisplay);
		}
		
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {				
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			
			if (allowBaseRendererToClearGraphics) {
				graphics.clear();
			}
			
			graphics.lineStyle(1, 0x808080);
			graphics.beginFill(backgroundColor, 1);
			graphics.drawRoundRect(0, 0, unscaledWidth, unscaledHeight, 10, 10);		
						
			if (canDrawCircle(data)) {
				graphics.beginFill(BACKGROUND_COLOR_DEFAULT, 1);
				var side:int = MindMapDiagramShell(diagramShellContext.diagramShell).getModelController(diagramShellContext, data).getSide(diagramShellContext, data);
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
