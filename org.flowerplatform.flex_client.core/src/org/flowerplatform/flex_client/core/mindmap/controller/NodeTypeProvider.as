package org.flowerplatform.flex_client.core.mindmap.controller {
	
	import org.flowerplatform.flex_client.core.mindmap.MindMapEditorDiagramShell;
	import org.flowerplatform.flex_client.core.mindmap.remote.Node;
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.controller.ITypeProvider;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class NodeTypeProvider implements ITypeProvider {
		
		public function getType(context:DiagramShellContext, model:Object):String {
			if (model is Node) {
				if (context.diagramShell.rootModel == model) { 
					// the root node has the same type, but different controllers -> use this constant instead
					return MindMapEditorDiagramShell.MINDMAP_ROOT_NODE_TYPE;
				}
				return Node(model).type;
			}
			return null;
		}
	}
}