package org.flowerplatform.flex_client.core.mindmap.controller {
	
	import org.flowerplatform.flex_client.core.mindmap.MindMapEditorDiagramShell;
	import org.flowerplatform.flex_client.core.mindmap.remote.Node;
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.controller.ITypeProvider;
	import org.flowerplatform.flexdiagram.mindmap.MindMapRootModelWrapper;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class NodeTypeProvider implements ITypeProvider {
		
		public function getType(context:DiagramShellContext, model:Object):String {
			if (model is MindMapRootModelWrapper) {
				return MindMapRootModelWrapper.ID;				
			} else if (model is Node) {
				return Node(model).type;
			}			
			return null;
		}
	}
}