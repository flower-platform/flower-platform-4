package org.flowerplatform.flex_client.core.mindmap.controller {
	import mx.core.IVisualElement;
	
	import org.flowerplatform.flex_client.core.mindmap.event.NodeRemovedEvent;
	import org.flowerplatform.flex_client.core.mindmap.remote.Node;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.mindmap.controller.MindMapModelRendererController;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class NodeRendererController extends MindMapModelRendererController {
		
		public function NodeRendererController(diagramShell:DiagramShell, rendererClass:Class) {
			super(diagramShell, rendererClass);
		}
		
		override public function associatedModelToRenderer(model:Object, renderer:IVisualElement):void {				
			Node(model).addEventListener(NodeRemovedEvent.NODE_REMOVED, nodeRemovedHandler);			
		}
		
		override public function unassociatedModelFromRenderer(model:Object, renderer:IVisualElement, isModelDisposed:Boolean):void {		
			if (isModelDisposed) {			
				Node(model).removeEventListener(NodeRemovedEvent.NODE_REMOVED, nodeRemovedHandler);				
			}
			super.unassociatedModelFromRenderer(model, renderer, isModelDisposed);	
		}
		
		protected function nodeRemovedHandler(event:NodeRemovedEvent):void {			
			diagramShell.unassociateModelFromRenderer(event.node, diagramShell.getRendererForModel(event.node), true);			
		}
		
	}
}