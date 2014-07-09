/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * license-end
 */
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