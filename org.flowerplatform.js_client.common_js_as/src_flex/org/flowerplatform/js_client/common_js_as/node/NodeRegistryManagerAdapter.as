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
	
	
	public class NodeRegistryManagerAdapter {
		
		protected var jsInstance;
		
		public function NodeRegistryManagerAdapter(resourceOperationsHandler, hostServiceInvocator, hostInvocator) {
			jsInstance = new NodeRegistryManager(resourceOperationsHandler, hostServiceInvocator, hostInvocator);
			_nodeRegistryManager = jsInstance;
		}
		
		public function createNodeRegistry() {
			return new NodeRegistryAdapter(this);
		}
		
		public function get resourceOperationsManager() {
			return jsInstance.resourceOperationsManager;
		}

		public function get serviceInvocator() {
			return jsInstance.serviceInvocator;
		}

		public function get hostInvocator() {
			return jsInstance.hostInvocator;
		}

		public function get hostServiceInvocator() {
			return jsInstance.hostServiceInvocator;
		}
		
		public function addListener(listener) {
			jsInstance.addListener(listener)
		}		
		
		public function removeListener(listener) {
			jsInstance.removeListener(listener)
		}
		
		public function getResourceSets() {
			return jsInstance.getResourceSets();
		}
		
		public function getNodeRegistriesForResourceSet(resourceSet) {	
			return jsInstance.getNodeRegistriesForResourceSet(resourceSet);
		}
		
		public function getResourceUrisForNodeRegistry(nodeRegistry) {
			return jsInstance.getResourceUrisForNodeRegistry(nodeRegistry);
		}
		
		public function getResourceUrisForResourceSet(resourceSet) {
			return jsInstance.getResourceUrisForResourceSet(resourceSet);
		}
		
		public function getNodeRegistries() {
			return jsInstance.getNodeRegistries();
		}
		
		public function getResourceUris() {
			return jsInstance.getResourceUris();
		}
		
		public function linkResourceNodeWithNodeRegistry(resourceUri, resourceSet, nodeRegistry) {
			jsInstance.linkResourceNodeWithNodeRegistry(resourceUri, resourceSet, nodeRegistry)
		}
		
		public function unlinkResourceNodeFromNodeRegistry(resourceUri, nodeRegistry) {
			jsInstance.unlinkResourceNodeFromNodeRegistry(resourceUri, nodeRegistry)
		}
		
		public function expand(nodeRegistry, node, context) {
			jsInstance.expand(nodeRegistry, node, context)
		}
		
		public function collapse(nodeRegistry, node) {
			jsInstance.collapse(nodeRegistry, node)
		}
		
		public function collapseHandler(node, nodeRegistry, dirtyResourceUris, savedResourceUris) {
			jsInstance.collapseHandler(node, nodeRegistry, dirtyResourceUris, savedResourceUris)
		}
		
		/**
		 * Iterate the subtree starting from <code>node</code> and add all the resources to the dirty or saved resource arrays.
		 */
		public function getResourceUrisForSubTree(node, nodeRegistry, dirtyResourceUris, savedResourceUris) {
			jsInstance.getResourceUrisForSubTree(node, nodeRegistry, dirtyResourceUris, savedResourceUris)
		}
		
		public function hasSubscribableResource(node, resourceUri) {
			return jsInstance.hasSubscribableResource(node, resourceUri);
		}
		
		/**
		 * Calls subscribe for <code>nodeId</code> on server. Callback functions:
		 * <ul>
		 * 	<li><code>subscribeResultCallback(resourceNode:Node):void</code>
		 * 	<li><code>subscribeFaultCallback(event:FaultEvent):void</code>
		 * </ul>
		 */ 
		public function subscribe(nodeId, nodeRegistry, subscribeResultCallback, subscribeFaultCallback) {
			jsInstance.subscribe(nodeId, nodeRegistry, subscribeResultCallback, subscribeFaultCallback)
		}
		
		/**
		 * Closes all editors without dispatching events and updates global state for save actions.
		 */ 
		public function removeNodeRegistries(nodeRegistries) {
			jsInstance.removeNodeRegistries(nodeRegistries)
		}
		
		public function getResourceSetsForResourceUris(resourceUris) {
			return jsInstance.getResourceSetsForResourceUris(resourceUris);
		}
		
		/**
		 * @param dirtyResourceNodeHandler function will be executed each time a dirty resourceNode is found.
		 * @return all dirty resourceUris found in <code>nodeRegistries</code>, without duplicates.
		 */ 
		public function getDirtyResourceSetsFromNodeRegistries(nodeRegistries, dirtyResourceNodeHandler) {			
			return jsInstance.getDirtyResourceSetsFromNodeRegistries(nodeRegistries, dirtyResourceNodeHandler);
		}
		
		/**
		 * @param returnIfAtLeastOneDirtyResourceNodeFound if <code>true</code>, returns the first dirty resourceNodeId found.
		 * @param dirtyResourceNodeHandler function will be executed each time a dirty resourceNode is found.
		 * @return all dirty resourceUris, without duplicates.
		 */ 
		public function getAllDirtyResourceSets(returnIfAtLeastOneDirtyResourceNodeFound, dirtyResourceNodeHandler) {
			return jsInstance.getAllDirtyResourceSets(returnIfAtLeastOneDirtyResourceNodeFound, dirtyResourceNodeHandler);
		}
		
		public function processUpdates(resourceNodeIdToUpdates) {
			jsInstance.processUpdates(resourceNodeIdToUpdates)
		}
		
		/**
		 * Unlink <code>resourceUris</code> from node registries.
		 * 
		 * <p>
		 * Dispatches <code>resourceNodeRemoved</code> for each <code>NodeRegistry</code> found.
		 * Note: Additional behavior can be added by listening to this event (e.g. in case of an editor, close it).
		 */
		public function unlinkResourceNodesForcefully(resourceUris) {
			jsInstance.unlinkResourceNodesForcefully(resourceUris)
		}
		
		public function isResourceNodeDirty(resourceNodeId, nodeRegistry) {
			return jsInstance.isResourceNodeDirty(resourceNodeId, nodeRegistry);
		}
		
		public function resourceNodeUpdated(event) {		
			jsInstance.resourceNodeUpdated(event)
		}
		
		public function contains(list, obj) {
			return jsInstance.contains(list, obj);
		}
		
	}

}

include "../../../../../../../org.flowerplatform.js_client.common_js_as/WebContent/js/NodeRegistry.js";
include "../../../../../../../org.flowerplatform.js_client.common_js_as/WebContent/js/NodeRegistryManager.js";	
include "../../../../../../../org.flowerplatform.js_client.common_js_as/WebContent/js/ResourceOperationsManager.js";	

