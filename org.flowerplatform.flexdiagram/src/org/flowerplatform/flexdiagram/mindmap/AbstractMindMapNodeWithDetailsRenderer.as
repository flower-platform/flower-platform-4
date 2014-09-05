package org.flowerplatform.flexdiagram.mindmap {
	import flash.events.MouseEvent;
	
	import mx.events.FlexEvent;
	import mx.events.PropertyChangeEvent;
	import mx.events.ResizeEvent;
	
	import spark.components.DataRenderer;
	import spark.components.Group;
	import spark.components.Image;
	import spark.components.RichText;
	import spark.layouts.HorizontalLayout;
	import spark.layouts.VerticalLayout;
	
	import flashx.textLayout.conversion.TextConverter;
	
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.IDiagramShellContextAware;
	import org.flowerplatform.flexdiagram.renderer.DiagramRenderer;
	import org.flowerplatform.flexdiagram.ui.NoteAndDetailsComponentExtension1;
	import org.flowerplatform.flexutil.Utils;
	
	/**
	 *  * @author Alexandra Topoloaga
	 */
	
	public class AbstractMindMapNodeWithDetailsRenderer extends DataRenderer implements IDiagramShellContextAware  {
		
		protected var _note:String;
		
		protected var _details:String;
		
		protected var _context:DiagramShellContext;
		
		protected var embeddedRenderer:AbstractMindMapNodeRenderer;
		
		protected var detailsGroup:Group;
		
		protected var detailsIcon:Image;
		
		protected var detailsText:RichText;
		
		protected var noteComponentExtension:NoteAndDetailsComponentExtension1 = new NoteAndDetailsComponentExtension1();

		[Embed(source="../icons/arrowUp.png")]
		public static const arrowUpIcon:Class;
		
		[Embed(source="../icons/arrowDown.png")]
		public static const arrowDownIcon:Class;				
		/**************************************************************************
		 * Graphic properties supported by this renderer.
		 *************************************************************************/
		public function set note(value:String):void {
			_note = value;
		}
		
		public function set details(value:String):void {
			_details = value;
			if (_details != null) {
				setDetailsGroupVisibile(true);
				if (detailsText.includeInLayout) {
					detailsIcon.source = arrowUpIcon;
				} else {
					detailsIcon.source = arrowDownIcon;
				}
			}  else {
				setDetailsGroupVisibile(false);
			}
			
			if (detailsText.includeInLayout) {
				var text:String = _details;
				text = Utils.getCompatibleHTMLText(text);
				detailsText.textFlow = TextConverter.importToFlow(text , Utils.isHTMLText(text) ? TextConverter.TEXT_FIELD_HTML_FORMAT : TextConverter.PLAIN_TEXT_FORMAT);
			}	
		}
				
		/**************************************************************************
		 * Other functions.
		 *************************************************************************/
		public function AbstractMindMapNodeWithDetailsRenderer() {
			var vLayout:VerticalLayout = new VerticalLayout();
			vLayout.gap = 2;
			this.layout = vLayout;
			
			addEventListener(ResizeEvent.RESIZE, resizeHandler);
		}
		
		public function get diagramShellContext():DiagramShellContext {
			return embeddedRenderer.diagramShellContext;
		}
		
		public function set diagramShellContext(value:DiagramShellContext):void {
			embeddedRenderer.diagramShellContext = value;
		}
		
		public function getEmbeddedRenderer():AbstractMindMapNodeRenderer {
			return embeddedRenderer;
		}
		
		/**
		 * Duplicate from AbstractMindMapNodeRenderer.
		 */
		override public function set data(value:Object):void {
			if (data != null) {
				endModelListen();
			}
			embeddedRenderer.data = value;
			super.data = value;
			if (data != null) {
				beginModelListen();
			}
		}
		
		/**
		 * Duplicate from AbstractMindMapNodeRenderer.
		 */
		protected function beginModelListen():void {
			data.addEventListener(PropertyChangeEvent.PROPERTY_CHANGE, modelChangedHandler);	
			modelChangedHandler(null);
		}
		
		/**
		 * Duplicate from AbstractMindMapNodeRenderer.
		 */
		protected function endModelListen():void {
			data.removeEventListener(PropertyChangeEvent.PROPERTY_CHANGE, modelChangedHandler);
		}
		
		protected function get mindMapDiagramShell():MindMapDiagramShell {
			return MindMapDiagramShell(embeddedRenderer.diagramShellContext.diagramShell);	
		}
		
		override protected function createChildren():void {
			super.createChildren();
			createEmbeddedRenderer();
			addElement(embeddedRenderer);
			
			createDetailsGroup();
			addElement(detailsGroup);
		}
		
		protected function createDetailsGroup():void {
			detailsGroup = new Group();
			detailsGroup.layout = new HorizontalLayout();
			detailsGroup.percentWidth = 100;
			
			detailsText = new RichText();
			detailsText.percentWidth = 100;
			
			detailsIcon = new Image();
			detailsIcon.addEventListener(MouseEvent.CLICK, detailsIconClickHandler);
			
			detailsGroup.addElement(detailsIcon);
			detailsGroup.addElement(detailsText);
			
			detailsText.visible = false;
			detailsText.includeInLayout = false;
		}
		
		protected function detailsIconClickHandler(event:MouseEvent=null):void {
			if (detailsText.includeInLayout) {
				detailsText.visible = false;
				detailsText.includeInLayout = false;
				detailsIcon.source = arrowDownIcon;
			} else {
				detailsText.includeInLayout = true;
				detailsText.visible = true;
				detailsIcon.source = arrowUpIcon;				
			}
			
			if (detailsText.includeInLayout) {
				var text:String = _note;
				if (text != null) {
					text = Utils.getCompatibleHTMLText(text);
					detailsText.textFlow = TextConverter.importToFlow(text , Utils.isHTMLText(text) ? TextConverter.TEXT_FIELD_HTML_FORMAT : TextConverter.PLAIN_TEXT_FORMAT);
				}
			}
			mindMapDiagramShell.setPropertyValue(diagramShellContext, data, "detailsTextVisible", detailsText.includeInLayout ? 1 : 0);
		}
		
		protected function nodeRendererInitialized(event:FlexEvent):void {	
			embeddedRenderer.addEventListener(MouseEvent.MOUSE_OVER, mouseOverHandler);
			embeddedRenderer.addEventListener(MouseEvent.MOUSE_OUT, mouseOutHandler);
			embeddedRenderer.addEventListener(ResizeEvent.RESIZE, resizeHandler);
		}
		
		
		protected function setDetailsGroupVisibile(value:Boolean):void {
			detailsGroup.includeInLayout = value;
			detailsGroup.visible = value;
		}
		
		protected function mouseOverHandler(event:MouseEvent):void {
			if (noteComponentExtension.parent == null) {
				var text:String;
				var hasText:Boolean = false;
				if (hasNodeDetails() && !detailsText.includeInLayout) {
					text = _details;
					if (text != null) {
						text = Utils.getCompatibleHTMLText(text);
						noteComponentExtension.nodeDetails.textFlow = TextConverter.importToFlow(text , Utils.isHTMLText(text) ? TextConverter.TEXT_FIELD_HTML_FORMAT : TextConverter.PLAIN_TEXT_FORMAT);
						noteComponentExtension.nodeDetails.includeInLayout = true;
						noteComponentExtension.nodeDetails.visible = true;
						hasText = true;	
					}
				} else {
					noteComponentExtension.nodeDetails.includeInLayout = false;
					noteComponentExtension.nodeDetails.visible = false;
				}
				
				if (hasNote()) {
					text = _note;
					text = Utils.getCompatibleHTMLText(text);
					noteComponentExtension.noteText.textFlow = TextConverter.importToFlow(text , Utils.isHTMLText(text) ? TextConverter.TEXT_FIELD_HTML_FORMAT : TextConverter.PLAIN_TEXT_FORMAT);
					noteComponentExtension.noteIcon.includeInLayout = true;
					noteComponentExtension.noteText.includeInLayout = true;
					hasText = true;
				} else {
					noteComponentExtension.noteIcon.includeInLayout = false;
					noteComponentExtension.noteText.includeInLayout = false;
				}
				
				if (hasText) {
					DiagramRenderer(mindMapDiagramShell.diagramRenderer).addElement(noteComponentExtension)
					
					var dynamicObject:Object = mindMapDiagramShell.getDynamicObject(diagramShellContext, data);
					noteComponentExtension.x = dynamicObject.x ;
					noteComponentExtension.y = dynamicObject.y + dynamicObject.height;	
				}
			}
		}
		
		protected function mouseOutHandler(event:MouseEvent):void {	
			if (noteComponentExtension.parent != null) {
				DiagramRenderer(diagramShellContext.diagramShell.diagramRenderer).removeElement(noteComponentExtension);
			}				
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
		
		protected function hasNote():Boolean {
			return false;
		}
		
		protected function hasNodeDetails():Boolean {
			return false;
		}
		
		protected function createEmbeddedRenderer():void {
		}
		
		protected function modelChangedHandler(event:PropertyChangeEvent):void {
		}
	}
}