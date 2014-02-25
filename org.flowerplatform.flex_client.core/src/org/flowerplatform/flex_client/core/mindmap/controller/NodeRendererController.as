package org.flowerplatform.flex_client.core.mindmap.controller {
	import mx.core.IVisualElement;
	
	import org.flowerplatform.flex_client.core.mindmap.remote.Node;
	import org.flowerplatform.flex_client.core.mindmap.update.event.NodeRemovedEvent;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.mindmap.controller.MindMapModelRendererController;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class NodeRendererController extends MindMapModelRendererController {
		
		public function NodeRendererController(rendererClass:Class) {
			super(rendererClass);
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
			context.diagramShell.unassociateModelFromRenderer(event.node, context.diagramShell.getRendererForModel(event.node), true);			
		}
		
	}
}