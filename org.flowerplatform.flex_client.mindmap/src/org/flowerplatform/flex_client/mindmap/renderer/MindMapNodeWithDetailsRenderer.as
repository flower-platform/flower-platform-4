package org.flowerplatform.flex_client.mindmap.renderer {
	import flash.events.MouseEvent;
	
	import flashx.textLayout.conversion.TextConverter;
	
	import mx.events.FlexEvent;
	import mx.events.PropertyChangeEvent;
	import mx.events.ResizeEvent;
	
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.core.editor.update.event.NodeUpdatedEvent;
	import org.flowerplatform.flex_client.core.node.controller.NodeControllerUtils;
	import org.flowerplatform.flex_client.mindmap.MindMapConstants;
	import org.flowerplatform.flex_client.mindmap.MindMapEditorDiagramShell;
	import org.flowerplatform.flex_client.mindmap.ui.NoteAndDetailsComponentExtension;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexdiagram.ControllerUtils;
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.IDiagramShellContextAware;
	import org.flowerplatform.flexdiagram.mindmap.IAbstractMindMapModelRenderer;
	import org.flowerplatform.flexdiagram.renderer.DiagramRenderer;
	import org.flowerplatform.flexutil.Utils;
	
	import spark.components.DataRenderer;
	import spark.components.Group;
	import spark.components.Image;
	import spark.components.RichText;
	import spark.layouts.HorizontalLayout;
	import spark.layouts.VerticalLayout;
	
	/**
	 * @author Sebastian Solomon
	 * @author Mariana Gheorghe
	 */ 
	public class MindMapNodeWithDetailsRenderer extends DataRenderer implements IAbstractMindMapModelRenderer, IDiagramShellContextAware {
		
		protected var embeddedRenderer:MindMapNodeRenderer;
		protected var detailsGroup:Group;
		protected var detailsIcon:Image;
		protected var detailsText:RichText;
		protected var noteComponentExtension:NoteAndDetailsComponentExtension = new NoteAndDetailsComponentExtension();
		
		public function MindMapNodeWithDetailsRenderer() {
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
		
		public function getLabelDisplay():RichText {
			return embeddedRenderer.getLabelDisplay();
		}
		
		public function getEmbeddedRenderer():MindMapNodeRenderer {
			return embeddedRenderer;
		}
		
		/**
		 * Duplicate from AbstractMindMapModelRenderer.
		 */
		override public function set data(value:Object):void {
			if (data != null) {
				data.removeEventListener(NodeUpdatedEvent.NODE_UPDATED, nodeUpdatedHandler);
				data.removeEventListener(PropertyChangeEvent.PROPERTY_CHANGE, modelChangedHandler);
				depth = 0;
				unassignData();
			}

			embeddedRenderer.data = value;
			super.data = value;
			
			if (data != null) {
				// set depth from model's dynamic object if available
				// model's children must have a greater depth than the model because 
				// when drawing more complex graphics (like clouds), they must be displayed above them
				var dynamicObject:Object = diagramShellContext.diagramShell.getDynamicObject(diagramShellContext, data);
				if (dynamicObject.depth) {
					depth = dynamicObject.depth;
				}
				data.addEventListener(NodeUpdatedEvent.NODE_UPDATED, nodeUpdatedHandler);
				data.addEventListener(PropertyChangeEvent.PROPERTY_CHANGE, modelChangedHandler);				
				assignData();
			}
		}
		
		/**
		 * Duplicate from NodeRenderer
		 */
		protected function unassignData():void {
			// Important: measuredHeight/measuredWidth are reset to their default values; otherwise the renderer will use recycled values for width/height 
			measuredWidth = 0;
			measuredHeight = 0;
		}
		
		/**
		 * Duplicate from NodeRenderer.
		 */
		protected function assignData():void {
			x = mindMapDiagramShell.getPropertyValue(diagramShellContext, data, "x");	
			y = mindMapDiagramShell.getPropertyValue(diagramShellContext, data, "y");		
			var detailsTextVisible:Number = 0;
			if (data != null) {
				detailsTextVisible = mindMapDiagramShell.getPropertyValue(diagramShellContext, data, "detailsTextVisible");
			}
			detailsText.visible = detailsTextVisible == 1;
			detailsText.includeInLayout = detailsTextVisible == 1;
			
			nodeUpdatedHandler();
		}
		
		/**
		 * Duplicate from NodeRenderer + MindMapNodeRenderer.
		 */
		protected function modelChangedHandler(event:PropertyChangeEvent):void {
			switch (event.property) {
				case "x":
					x = mindMapDiagramShell.getPropertyValue(diagramShellContext, data, "x");					
					break;
				case "y":
					y = mindMapDiagramShell.getPropertyValue(diagramShellContext, data, "y");				
					break;
				case "depth":
					depth = mindMapDiagramShell.getPropertyValue(diagramShellContext, data, "depth");				
					break;
				case "hasChildren":
					invalidateSize();
				case "children":
					invalidateDisplayList();
				case "expandedHeight":
				case "expandedWidth":				
					invalidateDisplayList();
			}
		}
		
		protected function get mindMapDiagramShell():MindMapEditorDiagramShell {
			return MindMapEditorDiagramShell(embeddedRenderer.diagramShellContext.diagramShell);	
		}
		
		override protected function createChildren():void {
			super.createChildren();
			
			createEmbeddedRenderer();
			addElement(embeddedRenderer);
			
			createDetailsGroup();
			addElement(detailsGroup);
		}
		
		private function createEmbeddedRenderer():void {
			embeddedRenderer = new MindMapNodeRenderer();
			embeddedRenderer.percentWidth = 100;
			embeddedRenderer.addEventListener(FlexEvent.INITIALIZE, nodeRendererInitialized);
		}
		
		/**
		 * Must be added after its own listeners are registered on the embedded renderer.
		 */
		private function nodeRendererInitialized(event:FlexEvent):void {	
			embeddedRenderer.addEventListener(MouseEvent.MOUSE_OVER, mouseOverHandler);
			embeddedRenderer.addEventListener(MouseEvent.MOUSE_OUT, mouseOutHandler);
			embeddedRenderer.addEventListener(ResizeEvent.RESIZE, resizeHandler);
		}
		
		private function createDetailsGroup():void {
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
				detailsIcon.source = Resources.arrowDownIcon;
			} else {
				detailsText.includeInLayout = true;
				detailsText.visible = true;
				detailsIcon.source = Resources.arrowUpIcon;				
			}
			
			if (detailsText.includeInLayout) {
				var text:String = node.properties[MindMapConstants.NODE_DETAILS] as String;
				text = Utils.getCompatibleHTMLText(text);
				detailsText.textFlow = TextConverter.importToFlow(text , Utils.isHTMLText(text) ? TextConverter.TEXT_FIELD_HTML_FORMAT : TextConverter.PLAIN_TEXT_FORMAT);
			}
			
			mindMapDiagramShell.setPropertyValue(diagramShellContext, data, "detailsTextVisible", detailsText.includeInLayout ? 1 : 0);	
		}
		
		protected function nodeUpdatedHandler(event:NodeUpdatedEvent = null):void {
			if (node.properties[MindMapConstants.NODE_DETAILS] != null && String(node.properties[MindMapConstants.NODE_DETAILS]).length > 0) {
				setDetailsGroupVisibile(true);
				if (detailsText.includeInLayout) {
					detailsIcon.source = Resources.arrowUpIcon;
				} else {
					detailsIcon.source = Resources.arrowDownIcon;
				}
			}  else {
				setDetailsGroupVisibile(false);
			}
			
			if (detailsText.includeInLayout) {
				var text:String = node.properties[MindMapConstants.NODE_DETAILS] as String;
				text = Utils.getCompatibleHTMLText(text);
				detailsText.textFlow = TextConverter.importToFlow(text , Utils.isHTMLText(text) ? TextConverter.TEXT_FIELD_HTML_FORMAT : TextConverter.PLAIN_TEXT_FORMAT);
			}
			
			var minWidthChanged:Boolean = NodeControllerUtils.hasPropertyChanged(node, MindMapConstants.MIN_WIDTH, event);
			if (minWidthChanged) {
				minWidth = node.properties[MindMapConstants.MIN_WIDTH];
			}	
			
			var maxWidthChanged:Boolean = NodeControllerUtils.hasPropertyChanged(node, MindMapConstants.MAX_WIDTH, event);
			if (maxWidthChanged) {
				maxWidth = node.properties[MindMapConstants.MAX_WIDTH];
			}
		}
		
		private function setDetailsGroupVisibile(value:Boolean):void {
			detailsGroup.includeInLayout = value;
			detailsGroup.visible = value;
		}
	
		protected function get node():Node {
			return Node(data);	
		}
		
		protected function mouseOverHandler(event:MouseEvent):void {
			if (noteComponentExtension.parent == null) {
				var text:String;
				var hasText:Boolean = false;
				if (hasNodeDetails(node) && !detailsText.includeInLayout) {
					text = node.properties[MindMapConstants.NODE_DETAILS] as String;
					text = Utils.getCompatibleHTMLText(text);
					noteComponentExtension.nodeDetails.textFlow = TextConverter.importToFlow(text , Utils.isHTMLText(text) ? TextConverter.TEXT_FIELD_HTML_FORMAT : TextConverter.PLAIN_TEXT_FORMAT);
					noteComponentExtension.nodeDetails.includeInLayout = true;
					noteComponentExtension.nodeDetails.visible = true;
					hasText = true;
				} else {
					noteComponentExtension.nodeDetails.includeInLayout = false;
					noteComponentExtension.nodeDetails.visible = false;
				}
				
				if (hasNote(node)) {
					text = node.properties[MindMapConstants.NOTE] as String
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
					
					var dynamicObject:Object = mindMapDiagramShell.getDynamicObject(diagramShellContext, node);
					noteComponentExtension.x = dynamicObject.x ;
					noteComponentExtension.y = dynamicObject.y + dynamicObject.height;	
				}
			}
		}
		
		private function hasNote(node:Node):Boolean {
			return node.properties.hasOwnProperty(MindMapConstants.NOTE) && String(node.properties[MindMapConstants.NOTE]).length > 0
		}
		
		private function hasNodeDetails(node:Node):Boolean {
			return node.properties.hasOwnProperty(MindMapConstants.NODE_DETAILS) && String(node.properties[MindMapConstants.NODE_DETAILS]).length > 0
		}
		
		protected function mouseOutHandler(event:MouseEvent):void {	
			if (noteComponentExtension.parent != null) {
				DiagramRenderer(diagramShellContext.diagramShell.diagramRenderer).removeElement(noteComponentExtension);
			}				
		}
		
		/**
		 * Duplicate from NodeRenderer.
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
				var parent:Object = ControllerUtils.getModelChildrenController(diagramShellContext, data).getParent(diagramShellContext, data);
				mindMapDiagramShell.refreshModelPositions(diagramShellContext, parent != null ? parent : data);
			}
		}
		
	}
		
}
