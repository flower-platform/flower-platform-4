package org.flowerplatform.flexdiagram.mindmap
{
	import mx.collections.ArrayList;
	import mx.collections.IList;
	import mx.core.DPIClassification;
	import mx.core.FlexGlobals;
	import mx.events.FlexEvent;
	import mx.events.PropertyChangeEvent;
	import mx.events.ResizeEvent;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.IDiagramShellAware;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.FlowerArrayList;
	
	import spark.components.DataRenderer;
	import spark.components.Label;
	import spark.core.ContentCache;
	import spark.layouts.HorizontalLayout;
	import spark.primitives.BitmapImage;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class AbstractMindMapModelRenderer extends DataRenderer implements IDiagramShellAware {
				
		protected static var imageCache:ContentCache;
		protected static const circleRadius:int = 3;
		
		protected var _diagramShell:MindMapDiagramShell;
		
		protected var iconDisplays:IList;
		protected var _icons:FlowerArrayList = new FlowerArrayList();		
		
		protected var labelDisplay:Label;
		
		public function AbstractMindMapModelRenderer() {
			addEventListener(FlexEvent.INITIALIZE, initializeHandler);	
			setStyle("verticalGap", 0);
			setStyle("iconsGap", 1);
			
			if (imageCache == null) {
				imageCache = new ContentCache();
				imageCache.enableCaching = true;
				imageCache.maxCacheEntries = 200;
			}
			
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
		
		public function get icons():FlowerArrayList {
			return _icons;
		}
		
		public function set icons(value:FlowerArrayList):void {
			if (value == _icons)
				return;
			if (value == null) {
				_icons.removeAll();
			} else {
				var i:int;
				var j:int = 0;
				if (_icons.length > 0 && value.length > 0) {
					while (j < value.length && j < _icons.length) {
						BitmapImage(iconDisplays.getItemAt(j)).source = value.getItemAt(j);
						j++;
					}
				}
				if (j < _icons.length) {
					i = j;
					while (i < iconDisplays.length)  {
						removeIconDisplay(BitmapImage(iconDisplays.getItemAt(i)));
					}
				}
				if (j < value.length) {
					for (i = j; i < value.length; i++) {
						addIconDisplay(value.getItemAt(i));
					}
				}
				_icons = value;
			}			
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
				var side:int = _diagramShell.getModelController(data).getSide(data);
				if (side == MindMapDiagramShell.POSITION_LEFT) {
					graphics.drawCircle(-circleRadius, height/2, circleRadius);
				} else if (side == MindMapDiagramShell.POSITION_RIGHT) {						
					graphics.drawCircle(width + circleRadius, height/2, circleRadius);
				}
			}
		}
		
		protected function addIconDisplay(icon:Object):void {
			var iconDisplay:BitmapImage = new BitmapImage();
			iconDisplay.contentLoader = imageCache;
			iconDisplay.source = icon;
			iconDisplay.verticalAlign = "middle";
			
			addElementAt(iconDisplay, numElements - 1);
			
			if (iconDisplays == null) {
				iconDisplays = new ArrayList();
			}
			iconDisplays.addItem(iconDisplay);
		}
		
		protected function removeIconDisplay(iconDisplay:BitmapImage):void {
			removeElement(iconDisplay);
			iconDisplays.removeItemAt(iconDisplays.getItemIndex(iconDisplay));
			
			invalidateSize();
		}			
				
		override public function validateDisplayList():void {
			super.validateDisplayList();
			
			if (iconDisplays && iconDisplays.length > 0) {
				for (var i:int=0; i < iconDisplays.length; i++) {
					var iconDisplay:BitmapImage = BitmapImage(iconDisplays.getItemAt(i));
					iconDisplay.validateDisplayList();
				}
			}
		}
		
		override public function validateProperties():void {
			super.validateProperties();
			
			if (iconDisplays && iconDisplays.length > 0) {
				for (var i:int=0; i < iconDisplays.length; i++) {
					var iconDisplay:BitmapImage = BitmapImage(iconDisplays.getItemAt(i));
					iconDisplay.validateProperties();
				}
			}
		}
		
		override public function validateSize(recursive:Boolean = false):void {
			if (iconDisplays && iconDisplays.length > 0) {
				for (var i:int=0; i < iconDisplays.length; i++) {
					var iconDisplay:BitmapImage = BitmapImage(iconDisplays.getItemAt(i));
					iconDisplay.validateSize();
				}
			}				
			super.validateSize(recursive);
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
		
	}
}
