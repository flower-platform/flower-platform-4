package org.flowerplatform.flexdiagram.mindmap {
	import mx.collections.IList;
	import mx.core.UIComponent;
	import mx.events.CollectionEvent;
	import mx.events.PropertyChangeEvent;
	import mx.events.ResizeEvent;
	
	import spark.components.DataRenderer;
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
	public class AbstractMindMapNodeRenderer extends DataRenderer implements IDiagramShellContextAware {
		
		protected static const BACKGROUND_COLOR_DEFAULT:uint = 0xFFFFFFFF;
		
		public static const CLOUD_TYPE_NONE:String = "No shape";
		
		public static const CLOUD_TYPE_RECTANGLE:String = "Rectangle shape";
		
		public static const CLOUD_TYPE_ROUNDEDRECTANGLE:String = "Rounded rectangle shape";
		
		protected var _label:FocusableRichText;
		
		protected var backgroundColor:uint = BACKGROUND_COLOR_DEFAULT;
		
		protected var _context:DiagramShellContext;
		
		protected var _cloudColor:uint;
		
		protected var _cloudType:String;
		
		protected var _icons:IList;
		
		/**************************************************************************
		 * Graphic properties supported by this renderer.
		 *************************************************************************/
		public function set text(value:String):void {
			_label.textFlow = TextConverter.importToFlow(value , Utils.isHTMLText(value) ? TextConverter.TEXT_FIELD_HTML_FORMAT : TextConverter.PLAIN_TEXT_FORMAT); 
		}
		
		public function set fontFamily(value:String):void {
			_label.setStyle("fontFamily", Utils.getSupportedFontFamily(value));
		}
		
		public function set fontSize(value:Number):void {
			_label.setStyle("fontSize", value);
		}
		
		public function set fontWeight(value:Boolean):void {
			_label.setStyle("fontWeight", value == true ? "bold" : "normal");
		}
		
		public function set fontStyle(value:Boolean):void {
			_label.setStyle("fontStyle", value == true ? "italic" : "normal");
		}
		
		public function set textColor(value:uint):void {
			_label.setStyle("color", value);
		}
		
		public function set background(value:uint):void {
			backgroundColor = value;
		}
		
		public function set cloudColor(value:uint):void {
			_cloudColor = value;
			invalidateDisplayList();
		}
		
		public function set cloudType(value:String):void {
			_cloudType = value; 
			invalidateDisplayList();
		}
		
		public function set icons(value:IList):void {		
			if (_icons != null) {
				_icons.removeEventListener(CollectionEvent.COLLECTION_CHANGE, handleIconsChanged);
			}
			if (value == _icons)
				return;
			_icons = value;
			if (_icons != null) {
				_icons.addEventListener(CollectionEvent.COLLECTION_CHANGE, handleIconsChanged);				
			}
			handleIconsChanged(null);
		}
		
		/**************************************************************************
		 * Other functions.
		 *************************************************************************/
		
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
		 * Invoked when <code>data</code> changes or when the mind map system
		 * recalculates data in the dynamic object.
		 */
		protected function modelChangedHandler(event:PropertyChangeEvent):void {
		}
		
		override protected function createChildren():void {
			super.createChildren();
			layout = new HorizontalLayout;
			_label = new FocusableRichText();
			_label.percentHeight = 100;
			_label.percentWidth = 100;			
			_label.setStyle("verticalAlign" , "middle");
			addElement(_label);
		}
		
		protected function drawCloud(unscaledWidth:Number, unscaledHeight:Number):void {			
			if (_cloudType == CLOUD_TYPE_RECTANGLE || _cloudType == CLOUD_TYPE_ROUNDEDRECTANGLE) {				
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
				
				if (side == MindMapDiagramShell.POSITION_LEFT) {
					shapeX -= (expandedWidth - width);
				}
				
				if (side == MindMapDiagramShell.POSITION_CENTER) {
					shapeX -= expandedWidthLeft - width;
					shapeY = - diagramShell.getDeltaBetweenExpandedHeightMaxAndHeight(diagramShellContext, data, true)/2;
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
			graphics.endFill();
		}
		
		protected function handleIconsChanged(event:CollectionEvent):void {			
			/* // delete all the icons displayed and display the whole list again
			while (getElementAt(0) is BitmapImage) {
			removeElementAt(0);
			}
			if (_icons != null) {
			for (var i:Number = 0; i < _icons.length; i++) {
			var iconDisplay:BitmapImage = new BitmapImage();
			iconDisplay.contentLoader = FlexUtilGlobals.getInstance().imageContentCache;
			iconDisplay.source = FlexUtilGlobals.getInstance().adjustImageBeforeDisplaying("../../org.flowerplatform.flexdiagram.samples/icons/" + _icons.getItemAt(i));
			iconDisplay.verticalAlign = "middle";
			iconDisplay.depth = UIComponent(this).depth;
			addElementAt(iconDisplay, i);
			}
			}*/
			var i:Number;
			var j:Number;
			var iconDisplay:BitmapImage;
			var deleted:Boolean;
			
			if (_icons == null) {
				while (getElementAt(0) is BitmapImage) {
					removeElementAt(0);
				}
			} else {
				var n:int = 0;
				while (getElementAt(n) is BitmapImage) {
					n++;
				}	//number of icons displayed at this moment
				for (i = 0; i < Math.min(n,_icons.length); i++) {
					if (getElementAt(i) != _icons.getItemAt(i)) {
						while (getElementAt(i) is BitmapImage) {
							removeElementAt(i);
						}
						for (j = i; j < Math.min(n,_icons.length); j++) {
							iconDisplay = new BitmapImage();
							iconDisplay.contentLoader = FlexUtilGlobals.getInstance().imageContentCache;
							iconDisplay.source = FlexUtilGlobals.getInstance().adjustImageBeforeDisplaying("../../org.flowerplatform.flexdiagram.samples/icons/" + _icons.getItemAt(j));
							iconDisplay.verticalAlign = "middle";
							iconDisplay.depth = UIComponent(this).depth;
							addElementAt(iconDisplay, j);
						}
						break;
						deleted = true;
					}
				}
				if (n >= _icons.length) {
					if (deleted == false) {
						while (getElementAt(_icons.length) is BitmapImage) {
							removeElementAt(_icons.length);	
						}
					}
				} else {
					for (i = n; i < _icons.length; i++) {
						iconDisplay = new BitmapImage();
						iconDisplay.contentLoader = FlexUtilGlobals.getInstance().imageContentCache;
						iconDisplay.source = FlexUtilGlobals.getInstance().adjustImageBeforeDisplaying("../../org.flowerplatform.flexdiagram.samples/icons/" + _icons.getItemAt(i));
						iconDisplay.verticalAlign = "middle";
						iconDisplay.depth = UIComponent(this).depth;
						addElementAt(iconDisplay, i);
					}	
				}
			}
		}
	}
}