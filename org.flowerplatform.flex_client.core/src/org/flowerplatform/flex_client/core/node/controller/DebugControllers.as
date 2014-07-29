package org.flowerplatform.flex_client.core.node.controller {
	import org.flowerplatform.flex_client.core.CoreConstants;
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flexdiagram.FlexDiagramConstants;
	import org.flowerplatform.flexdiagram.mindmap.controller.MindMapModelController;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class DebugControllers {

		protected function addNodeController(type:String, controller:MindMapModelController):void {
			CorePlugin.getInstance().nodeTypeDescriptorRegistry.getOrCreateTypeDescriptor(type)
				.addSingleController(FlexDiagramConstants.MINDMAP_MODEL_CONTROLLER, controller);
		}
		
		protected function createNode(type:String, ssp:String, fragment:String, name:String, icons:String = null, hasChildren:Boolean = true):Node {
			var node:Node = new Node(CoreConstants.VIRTUAL_NODE_SCHEME + ":" + ssp + "#" + fragment);
			node.type = type;			
			node.properties[CoreConstants.NAME] = name;
			node.properties[CoreConstants.ICONS] = icons;
			node.properties[CoreConstants.HAS_CHILDREN] = hasChildren;
			return node;
		}
		
	}
}