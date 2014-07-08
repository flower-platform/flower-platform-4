package org.flowerplatform.flex_client.core.node_tree
{
	import mx.events.PropertyChangeEvent;
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.core.node.controller.GenericValueProviderFromDescriptor;
	import org.flowerplatform.flex_client.core.node.controller.NodeControllerUtils;
	import org.flowerplatform.flex_client.core.node.event.NodeUpdatedEvent;
	import org.flowerplatform.flexutil.tree.HierarchicalModelWrapper;
	import org.flowerplatform.flexutil.tree.TreeListItemRenderer;

	/**
	 * @author Claudiu Matei
	 */
	public class NodeTreeItemRenderer extends TreeListItemRenderer {
		
		public function NodeTreeItemRenderer() {
			super();
			labelFunction = getLabel;
			iconFunction = getIcon;
		}
		
		override public function set data(value:Object):void {
			//TODO CC: to be replaced with impl INodeChangeListener.nodeUpdated
			
			if (data != null) {
				data.removeEventListener(NodeUpdatedEvent.NODE_UPDATED, modelChangedHandler);
			}
			super.data = value;
			if (data != null) {
				data.addEventListener(NodeUpdatedEvent.NODE_UPDATED, modelChangedHandler);
			}
		}
		
		protected function modelChangedHandler(event:PropertyChangeEvent):void {
			invalidateDisplayList();
		}
		
		public function getLabel(data:HierarchicalModelWrapper):String {
			var node:Node = Node(data.treeNode);
			var label:String = String(NodeControllerUtils.getTitleProvider(CorePlugin.getInstance().nodeTypeDescriptorRegistry, node).getValue(node)); 
			return label;
		}

		public function getIcon(data:HierarchicalModelWrapper):String {
			var node:Node = Node(data.treeNode);
			var iconsProvider:GenericValueProviderFromDescriptor =  NodeControllerUtils.getIconsProvider(CorePlugin.getInstance().nodeTypeDescriptorRegistry, node);
			var icon:String = String(iconsProvider.getValue(node)); 
			return icon;
		}

		
	}
}