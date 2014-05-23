package org.flowerplatform.flex_client.mindmap.controller {
	import mx.core.IVisualElement;
	
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.core.editor.update.event.NodeRemovedEvent;
	import org.flowerplatform.flex_client.mindmap.MindMapConnector;
	import org.flowerplatform.flex_client.mindmap.MindMapConstants;
	import org.flowerplatform.flex_client.mindmap.renderer.MindMapNodeWithDetailsRenderer;
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.mindmap.controller.MindMapModelRendererController;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class NodeRendererController extends MindMapModelRendererController {
		
		public function NodeRendererController(rendererClass:Class, orderIndex:int = 0) {
			super(rendererClass, orderIndex);
			mindMapConnectorClass = MindMapConnector;
		}
		
		/**
		 * Return the <code>MindMapNodeWithDetailsRenderer</code> for nodes with details or notes.
		 * 
		 * @author Mariana Gheorghe
		 */
		override public function getRendererClass(context:DiagramShellContext, model:Object):Class {
			var node:Node = Node(model);
			if ((node.properties.hasOwnProperty(MindMapConstants.NOTE) && String(node.properties[MindMapConstants.NOTE]).length > 0) ||
				(node.properties.hasOwnProperty(MindMapConstants.NODE_DETAILS) && String(node.properties[MindMapConstants.NODE_DETAILS]).length > 0)) {
				return MindMapNodeWithDetailsRenderer;
			}
			return super.getRendererClass(context, model);
		}
		
		override public function associatedModelToRenderer(context:DiagramShellContext, model:Object, renderer:IVisualElement):void {				
			Node(model).addEventListener(NodeRemovedEvent.NODE_REMOVED, function (event:NodeRemovedEvent):void {nodeRemovedHandler(event, context);});			
		}
		
		override public function unassociatedModelFromRenderer(context:DiagramShellContext, model:Object, renderer:IVisualElement, isModelDisposed:Boolean):void {		
			if (isModelDisposed) {			
				Node(model).removeEventListener(NodeRemovedEvent.NODE_REMOVED, function (event:NodeRemovedEvent):void {nodeRemovedHandler(event, context);});				
			}
			super.unassociatedModelFromRenderer(context, model, renderer, isModelDisposed);	
		}
		
		protected function nodeRemovedHandler(event:NodeRemovedEvent, context:DiagramShellContext):void {			
			context.diagramShell.unassociateModelFromRenderer(context, event.node, context.diagramShell.getRendererForModel(context, event.node), true);			
		}
		
	}
}