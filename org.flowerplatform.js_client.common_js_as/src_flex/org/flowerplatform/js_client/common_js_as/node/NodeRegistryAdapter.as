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
package org.flowerplatform.js_client.common_js_as.node {
	
	public class NodeRegistryAdapter {

		protected var jsInstance;
		
		public function NodeRegistryAdapter(nodeRegistryManager) {
			jsInstance = new NodeRegistry(nodeRegistryManager);
		}
		
		public function getRootNodeUri() {
			return jsInstance.getRootNodeUri();
		}
		
		public function getNodeById(id) {
			return jsInstance.getNodeById(id);
		}
		
		public function addNodeChangeListener(listener) {
			jsInstance.addNodeChangeListener(listener);
		}
		
		public function removeNodeChangeListener(listener) {
			jsInstance.removeNodeChangeListener(listener);
		}
		
		public function unregisterNode(node, parent=null) {
			jsInstance.unregisterNode(node, parent);
		}
		
		public function collapse(node) {
			jsInstance.collapse(node);
		}
		
		public function expand(node, context) {
			jsInstance.expand(node, context);
		}
		
		/**
		 * Handles updates received from server.
		 * 
		 * There are 2 cases:
		 * <ul>
		 * 	<li> updates == null (server cannot provide any updates because no updates were registered before timestampOfLastRequest), a full refresh is needed </li>
		 * 	<li> otherwise
		 * 		<ul>
		 * 			<li> property update (PropertyUpdate) -> set/unset property from <code>Node.properties</code> <br>
		 * 				A <code>NodeUpdatedEvent</code> is dispatched at the end; it contains all updated & removed properties keys.
		 * 			<li> child added (ChildrenUpdate.ADDED) -> adds new node in parent as last child OR, if <code>targetNodeAddedBefore</code> set, before given child's index.
		 * 			<li> child removed (ChildrenUpdate.REMOVED) -> removes node from parent
		 * 		</ul>
		 * </ul>
		 * 
		 * Note: If a given <code>fullNodeId</code> doesn't exist in <code>nodeRegistry</code>, it means it isn't visible for current client, so ignore it.		
		 */ 
		public function processUpdates(updates) {
			jsInstance.processUpdates(updates);
		}
		
		public function expandCallbackHandler(node, children) {
			jsInstance.expandCallbackHandler(node, children);
		}
		
		public function refresh(node) {
			jsInstance.refresh(node);
		}
		
		/**
		 * @return an hierarchical structure of <code>fullNodeId</code>s starting from <code>node</code>.
		 */ 
		public function getFullNodeIdWithChildren(node) {
			return jsInstance.getFullNodeIdWithChildren(node);
		}
		
		/**
		 * Handles refresh response from server.
		 * 
		 * For each node:
		 * <ul>
		 * 	<li> re-set its properties and dispatch a <code>NodeUpdateEvent</code> without specifying the updated & removed properties keys
		 * 	<li> if no new children, remove all current node's children
		 *  <li> else, iterate on current list
		 * 		<ul>
		 * 			<li> if current child id exists in new list -> get its new structure
		 * 			<li> else -> remove current child
		 * 		</ul>
		 * 	<li> at this point, the current list will contain only the children that haven't been removed
		 *  <li> next, iterate on new list, there are 2 cases:
		 * 		<ul>
		 * 			<li> child added -> add new child in current list at the same index
		 * 			<li> child moved to a different index -> remove child from current index, add it to new index
		 * 		</ul>
		 * </ul>
		 */ 
		public function refreshHandler(node, nodeWithVisibleChildren) {
			jsInstance.refreshHandler(node, nodeWithVisibleChildren);
		}
		
		public function mergeOrRegisterNode(node) {
			return jsInstance.mergeOrRegisterNode(node);
		}
		
		/**
		 * Adds <code>node</code> to <code>parent</code> at given index and registers it in registry. <br>
		 * 
		 * If <code>parent</code> is null, the node will only be registered
		 * If <code>index</code> is -1, the node will be added last.
		 */
		public function registerNode(node, parent, index) {
			jsInstance.registerNode(node, parent, index);
		}
		
		public function setPropertyValue(node, property, newValue) {
			jsInstance.setPropertyValue(node, property, newValue);
		}
		
		public function setNodeProperties(node, newProperties) {
			jsInstance.setNodeProperties(node, newProperties);
		}
		
	}

}

include "../../../../../../../org.flowerplatform.js_client.common_js_as/WebContent/js/NodeRegistry.js";
include "../../../../../../../org.flowerplatform.js_client.common_js_as/WebContent/js/NodeRegistryManager.js";	
include "../../../../../../../org.flowerplatform.js_client.common_js_as/WebContent/js/ResourceOperationsManager.js";	

