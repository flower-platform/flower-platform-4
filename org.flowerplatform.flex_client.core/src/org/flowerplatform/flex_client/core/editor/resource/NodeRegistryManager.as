package org.flowerplatform.flex_client.core.editor.resource {
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	import mx.collections.ArrayList;
	import mx.core.mx_internal;
	import mx.rpc.events.FaultEvent;
	import mx.utils.ObjectUtil;
	
	import org.flowerplatform.flex_client.core.CoreConstants;
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.core.editor.remote.SubscriptionInfo;
	import org.flowerplatform.flex_client.core.editor.resource.event.NodeRegistryRemovedEvent;
	import org.flowerplatform.flex_client.core.editor.resource.event.ResourceNodeRemovedEvent;
	import org.flowerplatform.flex_client.core.node.NodeRegistry;
	import org.flowerplatform.flex_client.core.node.controller.NodeControllerUtils;
	import org.flowerplatform.flex_client.core.node.event.NodeUpdatedEvent;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.Pair;
	
	public class NodeRegistryManager {
		
		/**
		 * Keep maps between node registries and their resource node ids and vice versa.
		 */ 
		private var resourceSetToNodeRegistries:Dictionary = new Dictionary();		
		private var nodeRegistryToResourceUris:Dictionary = new Dictionary();
		
		private var resourceSetToResourceUris:Dictionary = new Dictionary();
		private var resourceUriToResourceSet:Dictionary = new Dictionary();
		
		private var resourceOperationsManager:ResourceOperationsManager;
		
		public function NodeRegistryManager(resourceOperationsManager:ResourceOperationsManager) {
			super();
			this.resourceOperationsManager = resourceOperationsManager;			
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
			for (var nodeRegistry:String in nodeRegistryToResourceUris) {
				nodeRegistries.push(nodeRegistry);
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
			// listen for resourceNode properties modifications like isDirty
			nodeRegistry.getNodeById(resourceUri).addEventListener(NodeUpdatedEvent.NODE_UPDATED, resourceNodeUpdated);
			
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
		}
		
		public function unlinkResourceNodeFromNodeRegistry(resourceUri:String, nodeRegistry:NodeRegistry):void {
			// change isDirty to false and dispatch event
			var resourceNodeFromRegistry:Node = nodeRegistry.getNodeById(resourceUri);
			resourceNodeFromRegistry.properties[CoreConstants.IS_DIRTY] = false;
			resourceNodeFromRegistry.dispatchEvent(new NodeUpdatedEvent(resourceNodeFromRegistry, new ArrayList([CoreConstants.IS_DIRTY])));
			resourceNodeFromRegistry.removeEventListener(NodeUpdatedEvent.NODE_UPDATED, resourceNodeUpdated);
			
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
		
		public function expand(nodeRegistry:NodeRegistry, node:Node, additionalHandler:Function = null):void {
			var subscribableResources:ArrayCollection = node == null ? null : ArrayCollection(node.properties[CoreConstants.SUBSCRIBABLE_RESOURCES]);
			if (subscribableResources == null || subscribableResources.length == 0) {
				nodeRegistry.expand(node, additionalHandler);
			} else {
				// a subscribable node => subscribe to the first resource
				var subscribableResource:Pair = Pair(subscribableResources.getItemAt(0));
				subscribe(String(subscribableResource.a), nodeRegistry, function(rootNode:Node):void {
					nodeRegistry.expand(node, additionalHandler);
				});
			}
		}
		
		public function collapse(nodeRegistry:NodeRegistry, node:Node, refreshChildren:Boolean = true):void {
			// get all dirty resourceNodes starting from node
			var dirtyResourceUris:Array = [];
			var savedResourceUris:Array = [];
			
			for each (var obj:Object in getResourceUrisForNodeRegistry(nodeRegistry)) {
				var resourceNodeId:String = String(obj);
				var resourceNode:Node = nodeRegistry.getNodeById(resourceNodeId);
				var parent:Node = resourceNode;
				while (parent != null && parent != node) {					
					parent = parent.parent;					
				}
				if (parent == node) {
					var isSubscribable:Boolean = resourceNode.properties[CoreConstants.IS_SUBSCRIBABLE];
					if (isSubscribable) {
						if (isResourceNodeDirty(resourceNodeId, nodeRegistry) && dirtyResourceUris.indexOf(resourceNodeId) == -1) {
							dirtyResourceUris.push(resourceNodeId);
						} else {
							savedResourceUris.push(resourceNodeId);
						}
					}
				}
			}
			
			// remove nodes that are already saved
			for each (var savedResourceNodeId:String in savedResourceUris) {
				unlinkResourceNodeFromNodeRegistry(savedResourceNodeId, nodeRegistry);	
			}
			
			if (dirtyResourceUris.length > 0) { // at least one dirty resourceNode found -> show dialog
				resourceOperationsManager.showSaveDialogIfDirtyStateOrCloseEditors([this], dirtyResourceUris, 
					function():void {
						for (var i:int = 0; i < dirtyResourceUris.length; i++) {
							unlinkResourceNodeFromNodeRegistry(dirtyResourceUris[i], nodeRegistry);
						}
						// wait for server response before collapse			
						nodeRegistry.collapse(node, refreshChildren);
					}
				);
			} else {
				nodeRegistry.collapse(node, refreshChildren);
			}
		}
		
		/**
		 * Calls subscribe for <code>nodeId</code> on server. Callback functions:
		 * <ul>
		 * 	<li><code>subscribeResultCallback(resourceNode:Node):void</code>
		 * 	<li><code>subscribeFaultCallback(event:FaultEvent):void</code>
		 * </ul>
		 */ 
		public function subscribe(nodeId:String, nodeRegistry:NodeRegistry, subscribeResultCallback:Function = null, subscribeFaultCallback:Function = null):void {
			CorePlugin.getInstance().serviceLocator.invoke("resourceService.subscribeToParentResource", [nodeId], 
				function(subscriptionInfo:SubscriptionInfo):void {
					nodeRegistry.mx_internal::registerNode(subscriptionInfo.rootNode, null);
					if (subscriptionInfo.resourceNode != null && subscriptionInfo.rootNode.nodeUri != subscriptionInfo.resourceNode.nodeUri) {
						// register resource node if different from root node
						nodeRegistry.mx_internal::registerNode(subscriptionInfo.resourceNode, null);
					}
					if (subscriptionInfo.resourceNode != null) {
						linkResourceNodeWithNodeRegistry(subscriptionInfo.resourceNode.nodeUri, subscriptionInfo.resourceSet, nodeRegistry);
					}
					if (subscribeResultCallback != null) {
						subscribeResultCallback(subscriptionInfo.rootNode);
					}
				},
				function(event:FaultEvent):void {
					FlexUtilGlobals.getInstance().messageBoxFactory.createMessageBox()
					.setText(Resources.getMessage("editor.error.subscribe.message", [event.fault.faultString]))
					.setTitle(Resources.getMessage("editor.error.subscribe.title"))
					.setWidth(300)
					.setHeight(200)
					.showMessageBox();
					
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
				nodeRegistry.dispatchEvent(new NodeRegistryRemovedEvent());							
			}
			
			resourceOperationsManager.updateGlobalDirtyState(false);
		}
		
		/**
		 * @param dirtyResourceNodeHandler function will be executed each time a dirty resourceNode is found.
		 * @return all dirty resourceUris found in <code>nodeRegistries</code>, without duplicates.
		 */ 
		public function getDirtyResourceUrisFromNodeRegistries(nodeRegistries:Array, dirtyResourceNodeHandler:Function = null):Array {			
			var dirtyResourceUris:Array = [];
			for (var i:int = 0; i < nodeRegistries.length; i++) {	
				var nodeRegistry:NodeRegistry = NodeRegistry(nodeRegistries[i]);
				for each (var obj:Object in getResourceUrisForNodeRegistry(nodeRegistry)) {
					var resourceNodeId:String = String(obj);
					if (isResourceNodeDirty(resourceNodeId, nodeRegistry) && dirtyResourceUris.indexOf(resourceNodeId) == -1) {
						if (dirtyResourceNodeHandler != null) {
							dirtyResourceNodeHandler(resourceNodeId);
						}
						dirtyResourceUris.push(resourceNodeId);						
					}
				}
			}
			return dirtyResourceUris;
		}
		
		/**
		 * @param returnIfAtLeastOneDirtyResourceNodeFound if <code>true</code>, returns the first dirty resourceNodeId found.
		 * @param dirtyResourceNodeHandler function will be executed each time a dirty resourceNode is found.
		 * @return all dirty resourceUris, without duplicates.
		 */ 
		public function getAllDirtyResourceUris(returnIfAtLeastOneDirtyResourceNodeFound:Boolean = false, dirtyResourceNodeHandler:Function = null):Array {
			var dirtyResourceUris:Array = [];
			for each (var resourceNodeId:String in getResourceSets()) {				
				for each (var nodeRegistry:NodeRegistry in getNodeRegistriesForResourceSet(resourceNodeId)) {										
					if (isResourceNodeDirty(resourceNodeId, nodeRegistry) && dirtyResourceUris.indexOf(resourceNodeId) == -1) {
						if (returnIfAtLeastOneDirtyResourceNodeFound) {
							return [dirtyResourceUris];
						}											
						if (dirtyResourceNodeHandler != null) {
							dirtyResourceNodeHandler(resourceNodeId);
						}
						dirtyResourceUris.push(resourceNodeId);						
					}
				}		
			}
			return dirtyResourceUris;
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
					nodeRegistry.dispatchEvent(new ResourceNodeRemovedEvent(resourceNodeId));
				}
				idsList += "\n* " + resourceNodeId;
			}
			FlexUtilGlobals.getInstance().messageBoxFactory.createMessageBox()
				.setText(Resources.getMessage("editor.error.subscribe.message", [idsList]))
				.setTitle(Resources.getMessage("editor.error.subscribe.title"))
				.setWidth(300)
				.setHeight(200)
				.showMessageBox();
		}
		
		public function isResourceNodeDirty(resourceNodeId:String, nodeRegistry:NodeRegistry):Boolean {
			var node:Node = nodeRegistry.getNodeById(resourceNodeId);	
			return node == null ? false : node.properties[CoreConstants.IS_DIRTY];
		}
					
		protected function resourceNodeUpdated(event:NodeUpdatedEvent):void {
			var resourceNode:Node = event.node;
			if (NodeControllerUtils.hasPropertyChanged(resourceNode, CoreConstants.IS_DIRTY)) {
				resourceOperationsManager.updateGlobalDirtyState(resourceNode.properties[CoreConstants.IS_DIRTY]);
			}
		}
		
	}
}