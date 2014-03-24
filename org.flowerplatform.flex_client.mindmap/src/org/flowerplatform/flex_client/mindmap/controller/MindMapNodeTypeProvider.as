package org.flowerplatform.flex_client.mindmap.controller {
	
	import org.flowerplatform.flex_client.core.editor.NodeTypeProvider;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.mindmap.MindMapRootModelWrapper;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class MindMapNodeTypeProvider extends NodeTypeProvider {
		
		override public function getType(context:DiagramShellContext, model:Object):String {
			if (model is MindMapRootModelWrapper) {
				return MindMapRootModelWrapper.ID;				
			}
			return super.getType(context, model); 
		}
	}
}