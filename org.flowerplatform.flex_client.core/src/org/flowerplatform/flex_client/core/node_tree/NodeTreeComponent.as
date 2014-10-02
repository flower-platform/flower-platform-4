/* license-start
 * 
 * Copyright (C) 2008 - 2014 Crispico Software, <http://www.crispico.com/>.
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
package org.flowerplatform.flex_client.core.node_tree {
	import mx.core.ClassFactory;
	import mx.events.TreeEvent;
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flexutil.tree.TreeList;
	import org.flowerplatform.js_client.common_js_as.node.INodeChangeListener;
	
	/**
	 * @author Cristina Constantinescu
	 * @author Claudiu Matei
	 */ 
	public class NodeTreeComponent extends TreeList implements INodeChangeListener {
			
		protected var nodeRegistry:*;
		
		protected var _nodeUri:String;
		
		public function get nodeUri():String {
			return _nodeUri;
		}
		
		public function NodeTreeComponent() {
			super();
			nodeRegistry = CorePlugin.getInstance().nodeRegistryManager.createNodeRegistry();
			nodeRegistry.addNodeChangeListener(this);
			
			hierarchicalModelAdapter = new NodeTreeHierarchicalModelAdapter();
			itemRenderer = new ClassFactory(NodeTreeItemRenderer);
			
			addEventListener("itemOpen", treeListItemOpenCloseHandler);
			addEventListener("itemClose", treeListItemOpenCloseHandler);
			
			CorePlugin.getInstance().nodeRegistryManager.addListener(this);
		}
		
		public function initializeTree(nodeUri:String):void {
			_nodeUri = nodeUri;	
			CorePlugin.getInstance().nodeRegistryManager.subscribe(nodeUri, nodeRegistry, subscribeResultCallback);
		}
		
		public function finalizeTree():void {
			CorePlugin.getInstance().nodeRegistryManager.unlinkResourceNodeFromNodeRegistry(nodeUri, nodeRegistry);
			CorePlugin.getInstance().nodeRegistryManager.removeListener(this);
		}
				
		protected function subscribeResultCallback(rootNode:Node, resourceNode:Node):void {
			this.rootNode = rootNode;			
			nodeRegistry.expand(Node(this.rootNode), null);
		}
					
		protected function treeListItemOpenCloseHandler(event:TreeEvent):void {		
			if (event.type == TreeEvent.ITEM_OPEN) {
				nodeRegistry.expand(Node(event.item), null);
				event.preventDefault();
			}
			else if (event.type == TreeEvent.ITEM_CLOSE) {
				nodeRegistry.collapse(Node(event.item), false);
				event.preventDefault();
			}
		}
		
		public function nodeUpdated(node:Node, property:String, oldValue:Object, newValue:Object):void {
			// do nothing
		}
		
		public function nodeAdded(node:Node):void {
			requestRefreshLinearizedDataProvider();
			invalidateDisplayList();
		}
		
		public function nodeRemoved(node:Node):void {
			requestRefreshLinearizedDataProvider();
			invalidateDisplayList();
		}		
		
		public function resourceNodeRemoved(resourceNodeUri:String, nodeRegistry:*):void {
			// do nothing
		}
		
	}
}
