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
package org.flowerplatform.flex_client.core.node {
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	import mx.collections.IList;
	import mx.core.mx_internal;
	import mx.events.PropertyChangeEvent;
	import mx.rpc.events.FaultEvent;
	
	import org.flowerplatform.flex_client.core.CoreConstants;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.core.editor.remote.SubscriptionInfo;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexutil.Pair;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class NodeRegistryManager {
		
		/**
		 * Keep maps between node registries and their resource node ids and vice versa.
		 */ 
		private var resourceSetToNodeRegistries:Dictionary = new Dictionary();		
		private var nodeRegistryToResourceUris:Dictionary = new Dictionary();
		
		private var resourceSetToResourceUris:Dictionary = new Dictionary();
		private var resourceUriToResourceSet:Dictionary = new Dictionary();
		
		public var resourceOperationsManager:ResourceOperationsManagerJs;		
		public var serviceInvocator:IServiceInvocator;
		public var externalInvocator:IExternalInvocator;
		
		public var listeners:IList = new ArrayCollection();
		
		public function NodeRegistryManager(resourceOperationsHandler:IResourceOperationsHandler, serviceInvocator:IServiceInvocator, externalInvocator:IExternalInvocator) {
			this.resourceOperationsManager = new ResourceOperationsManagerJs(this, resourceOperationsHandler);
			this.serviceInvocator = serviceInvocator;
			this.externalInvocator = externalInvocator;
		}
				
		public function createNodeRegistry():NodeRegistry {
			return new NodeRegistry(this);
		}
		
		public function addListener(listener:INodeRegistryManagerListener):void {
			listeners.addItem(listener);
		}
		
		public function getResourceSets():Array {
			var resourceSets:Array = [];
			for (var resourceSet:String in resourceSetToNodeRegistries) {
				resourceSets.push(resourceSet);
			}
			return resourceSets;
		}
		
		public function getNodeRegistriesForResourceSet(resourceSet:String):Array {
			var nodeRegistries:Array = resourceSetToNodeRegistries[resourceSet];
			if (nodeRegistries == null) {
				nodeRegistries = [];
			}
			return nodeRegistries;
		}
		
		public function getResourceUrisForNodeRegistry(nodeRegistry:NodeRegistry):Array {
			var resourceUris:Array = nodeRegistryToResourceUris[nodeRegistry];
			if (resourceUris == null) {
				resourceUris = [];
			}
			return resourceUris;
		}
		
		public function getResourceUrisForResourceSet(resourceSet:String):Array {
			var resourceUris:Array = resourceSetToResourceUris[resourceSet];
			if (resourceSet == null) {
				resourceUris = [];
			}
			return resourceUris;
		}
		
		public function getNodeRegistries():Array {
			var nodeRegistries:Array = [];
			for (var nodeRegistry:Object in nodeRegistryToResourceUris) {
				nodeRegistries.push(NodeRegistry(nodeRegistry));
			}
			return nodeRegistries;
		}
		
		public function getResourceUris():Array {
			var resourceUris:Array = [];
			for (var resourceUri:String in resourceUriToResourceSet) {
				resourceUris.push(resourceUri);
			}
			return resourceUris;
		}
		
		public function linkResourceNodeWithNodeRegistry(resourceUri:String, resourceSet:String, nodeRegistry:NodeRegistry):void {
			// add resourceUri to resourceSet
			var resourceUris:Array = resourceSetToResourceUris[resourceSet];
			if (resourceUris == null) {
				resourceUris = [];
				resourceSetToResourceUris[resourceSet] = resourceUris;
			}
			resourceUris.push(resourceUri);
			resourceUriToResourceSet[resourceUri] = resourceSet;
			
			// add resourceUri to registry
			resourceUris = nodeRegistryToResourceUris[nodeRegistry];
			if (resourceUris == null) {
				resourceUris = [];
				nodeRegistryToResourceUris[nodeRegistry] = resourceUris;
			}
			resourceUris.push(resourceUri);
			
			// add resourceSet to registry
			var nodeRegistries:Array = resourceSetToNodeRegistries[resourceSet];
			if (nodeRegistries == null) {
				nodeRegistries = [];
				resourceSetToNodeRegistries[resourceSet] = nodeRegistries;
			}
			nodeRegistries.push(nodeRegistry);
			
			// listen for resourceNode properties modifications like isDirty
			var resourceNode:Node = nodeRegistry.getNodeById(resourceUri);
			resourceNode.properties.addEventListener(PropertyChangeEvent.PROPERTY_CHANGE, resourceNodeUpdated);
			resourceNodeUpdated(PropertyChangeEvent.createUpdateEvent(resourceNode, CoreConstants.IS_DIRTY, false, resourceNode.properties[CoreConstants.IS_DIRTY]));
		}
		
		public function unlinkResourceNodeFromNodeRegistry(resourceUri:String, nodeRegistry:NodeRegistry):void {
			// change isDirty to false and dispatch event
			var resourceNodeFromRegistry:Node = nodeRegistry.getNodeById(resourceUri);
			nodeRegistry.setPropertyValue(resourceNodeFromRegistry, CoreConstants.IS_DIRTY, false);
			
			resourceNodeFromRegistry.removeEventListener(PropertyChangeEvent.PROPERTY_CHANGE, resourceNodeUpdated);
			
			var resourceSet:String = resourceUriToResourceSet[resourceUri];
			
			// remove resourceUri from resourceSet
			var resourceUris:Array = resourceSetToResourceUris[resourceSet];
			if (resourceUris != null) {
				resourceUris.splice(resourceUris.indexOf(resourceUri), 1);
				if (resourceUris.length == 0) {
					delete resourceSetToResourceUris[resourceSet];
				}
			}
			
			// remove resourceUri from registry
			resourceUris = nodeRegistryToResourceUris[nodeRegistry];
			if (resourceUris != null) {
				resourceUris.splice(resourceUris.indexOf(resourceUri), 1);
				if (resourceUris.length == 0) {
					delete nodeRegistryToResourceUris[nodeRegistry];
				}
				if (resourceNodeFromRegistry.nodeUri != nodeRegistry.getRootNodeUri() && 
					resourceNodeFromRegistry.parent == null && resourceUris.indexOf(resourceUri) < 0) {
					nodeRegistry.mx_internal::unregisterNode(resourceNodeFromRegistry);
				}
			}
			
			// remove resourceSet from registry
			var nodeRegistries:Array = resourceSetToNodeRegistries[resourceSet];
			if (nodeRegistries != null) {
				nodeRegistries.splice(nodeRegistries.indexOf(nodeRegistry), 1);
				if (nodeRegistries.length == 0 && resourceSetToResourceUris[resourceSet] == null) {
					delete resourceSetToNodeRegistries[resourceSet];
					delete resourceUriToResourceSet[resourceUri];
				}
			}
		}
		
		public function expand(nodeRegistry:NodeRegistry, node:Node, context:Object):void {
			if (node == null || !node.properties[CoreConstants.AUTO_SUBSCRIBE_ON_EXPAND]) {
				nodeRegistry.expand(node, context);
			} else {
				var subscribableResources:ArrayCollection = node == null ? null : ArrayCollection(node.properties[CoreConstants.SUBSCRIBABLE_RESOURCES]);
				if (subscribableResources != null && subscribableResources.length > 0) {
					// a subscribable node => subscribe to the first resource
					var subscribableResource:Pair = Pair(subscribableResources.getItemAt(0));
					subscribe(String(subscribableResource.a), nodeRegistry, function(rootNode:Node, resourceNode:Node):void {
						nodeRegistry.expand(node, context);
					});
				}
			}
		}
		
		public function collapse(nodeRegistry:NodeRegistry, node:Node, refreshChildren:Boolean = true):void {
			// get all the resources starting from node
			var dirtyResourceUris:Array = [];
			var savedResourceUris:Array = [];
			
			getResourceUrisForSubTree(node, nodeRegistry, dirtyResourceUris, savedResourceUris);
			
			if (dirtyResourceUris.length > 0) { // at least one dirty resourceNode found -> show dialog			
				resourceOperationsManager.showSaveDialog([this], getResourceSetsForResourceUris(dirtyResourceUris), 
					function():void {
						// wait for server response before collapse	
						collapseHandler(node, nodeRegistry, refreshChildren, dirtyResourceUris, savedResourceUris);
					}
				);
			} else {
				collapseHandler(node, nodeRegistry, refreshChildren, dirtyResourceUris, savedResourceUris);
			}
		}
		
		private function collapseHandler(node:Node, nodeRegistry:NodeRegistry,refreshChildren:Boolean,
											dirtyResourceUris:Array, savedResoureUris:Array):void {
			for each (var dirtyResourceUri:String in dirtyResourceUris) {
				unlinkResourceNodeFromNodeRegistry(dirtyResourceUri, nodeRegistry);
			}
			for each (var savedResourceUri:String in savedResoureUris) {
				unlinkResourceNodeFromNodeRegistry(savedResourceUri, nodeRegistry);
			}
			nodeRegistry.collapse(node, refreshChildren);
		}
		
		/**
		 * Iterate the subtree starting from <code>node</code> and add all the resources to the dirty or saved resource arrays.
		 * 
		 * @author Mariana Gheorghe
		 */
		private function getResourceUrisForSubTree(node:Node, nodeRegistry:NodeRegistry, dirtyResourceUris:Array, savedResourceUris:Array):void {
			var subscribableResources:ArrayCollection = ArrayCollection(node.properties[CoreConstants.SUBSCRIBABLE_RESOURCES]);
			if (subscribableResources != null) {
				for each (var pair:Pair in subscribableResources) {
					var resourceUri:String = String(pair.a);
					var resourceNode:Node = nodeRegistry.getNodeById(resourceUri);
					if (resourceNode != null) {
						if (resourceNode.properties[CoreConstants.IS_DIRTY]) {
							dirtyResourceUris.push(resourceUri);
						} else {
							savedResourceUris.push(resourceUri);
						}
					}
				}
			}
			
			// recurse
			for each (var child:Node in node.children) {
				getResourceUrisForSubTree(child, nodeRegistry, dirtyResourceUris, savedResourceUris);
			}
		}
		
		private function hasSubscribableResource(node:Node, resourceUri:String):Boolean {
			var subscribableResources:ArrayCollection = ArrayCollection(node.properties[CoreConstants.SUBSCRIBABLE_RESOURCES]);
			if (subscribableResources == null || subscribableResources.length == 0) {
				return false;
			}
			for each (var pair:Pair in subscribableResources) {
				if (pair.a == resourceUri) {
					return true;
				}
			}
			return false;
		}
		
		/**
		 * Calls subscribe for <code>nodeId</code> on server. Callback functions:
		 * <ul>
		 * 	<li><code>subscribeResultCallback(resourceNode:Node):void</code>
		 * 	<li><code>subscribeFaultCallback(event:FaultEvent):void</code>
		 * </ul>
		 */ 
		public function subscribe(nodeId:String, nodeRegistry:NodeRegistry, subscribeResultCallback:Function = null, subscribeFaultCallback:Function = null):void {
			serviceInvocator.invoke("resourceService.subscribeToParentResource", [nodeId], 
				function(subscriptionInfo:SubscriptionInfo):void {
					subscriptionInfo.rootNode = nodeRegistry.mx_internal::mergeOrRegisterNode(subscriptionInfo.rootNode);
					if (subscriptionInfo.resourceNode != null) {
						subscriptionInfo.resourceNode = nodeRegistry.mx_internal::mergeOrRegisterNode(subscriptionInfo.resourceNode);
						linkResourceNodeWithNodeRegistry(subscriptionInfo.resourceNode.nodeUri, subscriptionInfo.resourceSet, nodeRegistry);
					}
					if (subscribeResultCallback != null) {
						subscribeResultCallback(subscriptionInfo.rootNode, subscriptionInfo.resourceNode);
					}
				},
				function(event:FaultEvent):void {
					externalInvocator.showMessageBox(Resources.getMessage("editor.error.subscribe.message", [event.fault.faultString]), Resources.getMessage("editor.error.subscribe.title"));
										
					if (subscribeFaultCallback != null) {
						subscribeFaultCallback(event);
					}
				});
		}
				
		/**
		 * Closes all editors without dispatching events and updates global state for save actions.
		 */ 
		public function removeNodeRegistries(nodeRegistries:Array):void {			
			for (var i:int = 0; i < nodeRegistries.length; i++) {
				var nodeRegistry:NodeRegistry = NodeRegistry(nodeRegistries[i]);
				for each (var resourceNodeId:Object in getResourceUrisForNodeRegistry(nodeRegistry)) {
					unlinkResourceNodeFromNodeRegistry(String(resourceNodeId), nodeRegistry);
				}	
				for each (var listener:INodeRegistryManagerListener in listeners) {
					listener.nodeRegistryRemoved(nodeRegistry);
				}							
			}
			
			resourceOperationsManager.resourceOperationsHandler.updateGlobalDirtyState(false);
		}
		
		public function getResourceSetsForResourceUris(resourceUris:Array):Array {
			var resourceSets:Array = [];
			for each (var resourceUri:String in resourceUris) {
				var resourceSet:String = resourceUriToResourceSet[resourceUri];
				if (resourceSets.indexOf(resourceSet) < 0) {
					resourceSets.push(resourceSet);
				}
			}
			return resourceSets;
		}
		
		/**
		 * @param dirtyResourceNodeHandler function will be executed each time a dirty resourceNode is found.
		 * @return all dirty resourceUris found in <code>nodeRegistries</code>, without duplicates.
		 */ 
		public function getDirtyResourceSetsFromNodeRegistries(nodeRegistries:Array, dirtyResourceNodeHandler:Function = null):Array {			
			var dirtyResourceSets:Array = [];
			for (var i:int = 0; i < nodeRegistries.length; i++) {	
				var nodeRegistry:NodeRegistry = NodeRegistry(nodeRegistries[i]);
				for each (var obj:Object in getResourceUrisForNodeRegistry(nodeRegistry)) {
					var resourceUri:String = String(obj);
					var resourceSet:String = resourceUriToResourceSet[resourceUri];
					if (isResourceNodeDirty(resourceUri, nodeRegistry) && dirtyResourceSets.indexOf(resourceSet) == -1) {
						if (dirtyResourceNodeHandler != null) {
							dirtyResourceNodeHandler(resourceSet);
						}
						dirtyResourceSets.push(resourceSet);						
					}
				}
			}
			return dirtyResourceSets;
		}
		
		/**
		 * @param returnIfAtLeastOneDirtyResourceNodeFound if <code>true</code>, returns the first dirty resourceNodeId found.
		 * @param dirtyResourceNodeHandler function will be executed each time a dirty resourceNode is found.
		 * @return all dirty resourceUris, without duplicates.
		 */ 
		public function getAllDirtyResourceSets(returnIfAtLeastOneDirtyResourceNodeFound:Boolean = false, dirtyResourceNodeHandler:Function = null):Array {
			var dirtyResourceSets:Array = [];
			for each (var nodeRegistry:NodeRegistry in getNodeRegistries()) {			
				for each (var resourceUri:String in getResourceUrisForNodeRegistry(nodeRegistry)) {	
					var resourceSet:String = resourceUriToResourceSet[resourceUri];
					if (isResourceNodeDirty(resourceUri, nodeRegistry) && dirtyResourceSets.indexOf(resourceSet) == -1) {
						if (returnIfAtLeastOneDirtyResourceNodeFound) {
							return [resourceSet];
						}											
						if (dirtyResourceNodeHandler != null) {
							dirtyResourceNodeHandler(resourceSet);
						}
						dirtyResourceSets.push(resourceSet);						
					}
				}		
			}
			return dirtyResourceSets;
		}
				
		public function processUpdates(resourceNodeIdToUpdates:Object):void {
			for (var resourceNodeId:String in resourceNodeIdToUpdates) {
				var updates:ArrayCollection = resourceNodeIdToUpdates[resourceNodeId];			
				for each (var nodeRegistry:NodeRegistry in getNodeRegistriesForResourceSet(resourceNodeId)) {
					nodeRegistry.processUpdates(updates);
				}
			}
		}	
				
		/**
		 * Unlink <code>resourceUris</code> from node registries.
		 * 
		 * <p>
		 * Dispatches <code>NodeRegistry_ResourceNodeRemovedEvent</code> for each <code>NodeRegistry</code> found.
		 * Note: Additional behavior can be added by listening to this event (e.g. in case of an editor, close it).
		 */
		public function unlinkResourceNodesForcefully(resourceUris:ArrayCollection):void {
			var idsList:String = "";
			for each (var resourceNodeId:String in resourceUris) {
				var nodeRegistries:Array = getNodeRegistriesForResourceSet(resourceNodeId);
				for each (var nodeRegistry:NodeRegistry in nodeRegistries) {					
					unlinkResourceNodeFromNodeRegistry(resourceNodeId, nodeRegistry);
					for each (var listener:INodeRegistryManagerListener in listeners) {
						listener.resourceNodeRemoved(resourceNodeId, nodeRegistry);
					}
				}
				idsList += "\n* " + resourceNodeId;
			}
			externalInvocator.showMessageBox(Resources.getMessage("editor.error.subscribe.message", [idsList]), Resources.getMessage("editor.error.subscribe.title"));
		}
		
		public function isResourceNodeDirty(resourceNodeId:String, nodeRegistry:NodeRegistry):Boolean {
			var node:Node = nodeRegistry.getNodeById(resourceNodeId);	
			return node == null ? false : node.properties[CoreConstants.IS_DIRTY];
		}
					
		protected function resourceNodeUpdated(event:PropertyChangeEvent):void {		
			if (event.property == CoreConstants.IS_DIRTY) {				
				resourceOperationsManager.resourceOperationsHandler.updateGlobalDirtyState(event.newValue);
			}
		}
		
	}
}