package org.flowerplatform.flex_client.core.node_tree {
	import mx.core.IFactory;
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flexdiagram.FlexDiagramConstants;
	import org.flowerplatform.flexdiagram.controller.renderer.ClassReferenceRendererController;
	import org.flowerplatform.flexdiagram.controller.renderer.RendererController;
	import org.flowerplatform.flexutil.controller.TypeDescriptor;
	
	public class RendererControllerFactory implements IFactory {

		protected var renderer:RendererController;
		protected var _node:Node;
		
		var rendererClass:Class;
		public function RendererControllerFactory(node:Node) {
			_node = node;
			var descriptor:TypeDescriptor = CorePlugin.getInstance().nodeTypeDescriptorRegistry.getExpectedTypeDescriptor(node.type);
			rendererClass = ClassReferenceRendererController(RendererController(descriptor.getSingleController(FlexDiagramConstants.RENDERER_CONTROLLER, node))).rendererClass;			
		}
		
		public function newInstance():* {
//			return new NodeTreeItemRenderer();
			return new rendererClass();
		}
		
	}
	
}
