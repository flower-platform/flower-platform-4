package org.flowerplatform.flex_client.core.editor.resource {
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	import mx.collections.ArrayList;
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
	
	public class NodeRegistryManager {
		
		/**
		 * Keep maps between node registries and their resource node ids and vice versa.
		 */ 
		private var resourceNodeIdToNodeRegistries:Dictionary = new Dictionary();		
		private var nodeRegistryToResourceNodeIds:Dictionary = new Dictionary();
		
		private var resourceOperationsManager:ResourceOperationsManager;
		
		public function NodeRegistryManager(resourceOperationsManager:ResourceOperationsManager) {
			super();
			resourceOperationsManager = resourceOperationsManager;			
		}
		
		public function getResourceNodeIds():Array {
			var resourceNodeIds:Array = [];
			for (var resourceNodeId:String in resourceNodeIdToNodeRegistries) {
				resourceNodeIds.push(resourceNodeId);
			}
			return resourceNodeIds;
		}
		
		public function getNodeRegistriesForResourceNodeId(resourceNodeId:String):Array {
			var nodeRegistries:Array = resourceNodeIdToNodeRegistries[resourceNodeId];
			if (nodeRegistries == null) {
				nodeRegistries = [];
			}
			return nodeRegistries;
		}
		
		public function getResourceNodeIdsForNodeRegistry(nodeRegistry:NodeRegistry):Array {
			var resourceNodeIds:Array = nodeRegistryToResourceNodeIds[nodeRegistry];
			if (resourceNodeIds == null) {
				resourceNodeIds = [];
			}
			return resourceNodeIds;
		}
		
		public function getNodeRegistries():Array {
			var nodeRegistries:Array = [];
			for (var nodeRegistry:String in nodeRegistryToResourceNodeIds) {
				nodeRegistries.push(nodeRegistry);
			}
			return nodeRegistries;
		}
		
		public function linkResourceNodeWithNodeRegistry(resourceNodeId:String, nodeRegistry:NodeRegistry):void {
			var nodeRegistries:Array = resourceNodeIdToNodeRegistries[resourceNodeId];
			if (nodeRegistries == null) {
				nodeRegistries = [];
				resourceNodeIdToNodeRegistries[resourceNodeId] = nodeRegistries;
			}
			nodeRegistries.push(nodeRegistry);
			
			var resourceNodeIds:Array = nodeRegistryToResourceNodeIds[nodeRegistry];
			if (resourceNodeIds == null) {
				resourceNodeIds = [];
				nodeRegistryToResourceNodeIds[nodeRegistry] = resourceNodeIds;
			}
			resourceNodeIds.push(resourceNodeId);
		}
		
		public function unlinkResourceNodeFromNodeRegistry(resourceNodeId:String, nodeRegistry:NodeRegistry):void {
			var nodeRegistries:Array = resourceNodeIdToNodeRegistries[resourceNodeId];
			if (nodeRegistries != null) {
				nodeRegistries.splice(nodeRegistries.indexOf(nodeRegistry), 1);
				if (nodeRegistries.length == 0) {
					delete resourceNodeIdToNodeRegistries[resourceNodeId];
				}
			}
			
			var resourceNodeIds:Array = nodeRegistryToResourceNodeIds[nodeRegistry];
			if (resourceNodeIds != null) {
				resourceNodeIds.splice(resourceNodeIds.indexOf(resourceNodeId), 1);
				if (resourceNodeIds.length == 0) {
					delete nodeRegistryToResourceNodeIds[nodeRegistry];
				}
			}
		}
		
		public function expand(nodeRegistry:NodeRegistry, node:Node, additionalHandler:Function = null):void {
			var isSubscribable:Boolean = node == null ? false : node.properties[CoreConstants.IS_SUBSCRIBABLE];
			if (!isSubscribable) {
				nodeRegistry.expand(node, additionalHandler);
			} else {
				subscribe(node.nodeUri, nodeRegistry, function(resourceNode:Node):void {
					nodeRegistry.expand(node, additionalHandler);
				});
			}
		}
		
		public function collapse(nodeRegistry:NodeRegistry, node:Node, refreshChildren:Boolean = true):void {
			// get all dirty resourceNodes starting from node
			var dirtyResourceNodeIds:Array = [];
			var savedResourceNodeIds:Array = [];
			
			for each (var obj:Object in getResourceNodeIdsForNodeRegistry(nodeRegistry)) {
				var resourceNodeId:String = String(obj);
				var resourceNode:Node = nodeRegistry.getNodeById(resourceNodeId);
				var parent:Node = resourceNode;
				while (parent != null && parent != node) {					
					parent = parent.parent;					
				}
				if (parent == node) {
					var isSubscribable:Boolean = resourceNode.properties[CoreConstants.IS_SUBSCRIBABLE];
					if (isSubscribable) {
						if (isResourceNodeDirty(resourceNodeId, nodeRegistry) && dirtyResourceNodeIds.indexOf(resourceNodeId) == -1) {
							dirtyResourceNodeIds.push(resourceNodeId);
						} else {
							savedResourceNodeIds.push(resourceNodeId);
						}
					}
				}
			}
			
			// remove nodes that are already saved
			for each (var savedResourceNodeId:String in savedResourceNodeIds) {
				unregisterResourceNode(nodeRegistry.getNodeById(savedResourceNodeId), nodeRegistry);	
			}
			
			if (dirtyResourceNodeIds.length > 0) { // at least one dirty resourceNode found -> show dialog
				resourceOperationsManager.showSaveDialogIfDirtyStateOrCloseEditors([this], dirtyResourceNodeIds, 
					function():void {
						for (var i:int = 0; i < dirtyResourceNodeIds.length; i++) {
							unregisterResourceNode(nodeRegistry.getNodeById(dirtyResourceNodeIds[i]), nodeRegistry);
						}
						// wait for server response before collapse			
						nodeRegistry.collapse(node, refreshChildren);
					}
				);
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
					if (subscriptionInfo.resourceNode != null) {
						registerResourceNode(subscriptionInfo.resourceNode, subscriptionInfo.resourceSet, nodeRegistry);
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
		
		protected function registerResourceNode(resourceNode:Node, resourceSet:String, nodeRegistry:NodeRegistry):void {
			// we have a resource node
			// link it with this registry and update its properties
			
			var resourceNodeFromRegistry:Node = nodeRegistry.getNodeById(resourceNode.nodeUri);
			if (resourceNodeFromRegistry) { 
				// do nothing
				// node already in registry -> probably we want to update only its properties					
			} else {
				nodeRegistry.registerNode(resourceNode);
				resourceNodeFromRegistry = nodeRegistry.getNodeById(resourceNode.nodeUri);
			}				
			linkResourceNodeWithNodeRegistry(resourceSet, nodeRegistry);
			
			// listen for resourceNode properties modifications like isDirty
			resourceNodeFromRegistry.addEventListener(NodeUpdatedEvent.NODE_UPDATED, resourceNodeUpdated);
			
			// copy needed -> otherwise, if same instances, the setter isn't called
			resourceNodeFromRegistry.properties = ObjectUtil.copy(resourceNode.properties);				
		}
		
		/**
		 * Closes all editors without dispatching events and updates global state for save actions.
		 */ 
		public function removeNodeRegistries(nodeRegistries:Array):void {			
			for (var i:int = 0; i < nodeRegistries.length; i++) {
				var nodeRegistry:NodeRegistry = NodeRegistry(nodeRegistries[i]);
				for each (var resourceNodeId:Object in getResourceNodeIdsForNodeRegistry(nodeRegistry)) {
					unlinkResourceNodeFromNodeRegistry(String(resourceNodeId), nodeRegistry);
				}	
				nodeRegistry.dispatchEvent(new NodeRegistryRemovedEvent());							
			}
			
			resourceOperationsManager.updateGlobalDirtyState(false);
		}
		
		/**
		 * @param dirtyResourceNodeHandler function will be executed each time a dirty resourceNode is found.
		 * @return all dirty resourceNodeIds found in <code>nodeRegistries</code>, without duplicates.
		 */ 
		public function getDirtyResourceNodeIdsFromNodeRegistries(nodeRegistries:Array, dirtyResourceNodeHandler:Function = null):Array {			
			var dirtyResourceNodeIds:Array = [];
			for (var i:int = 0; i < nodeRegistries.length; i++) {	
				var nodeRegistry:NodeRegistry = NodeRegistry(nodeRegistries[i]);
				for each (var obj:Object in getResourceNodeIdsForNodeRegistry(nodeRegistry)) {
					var resourceNodeId:String = String(obj);
					if (isResourceNodeDirty(resourceNodeId, nodeRegistry) && dirtyResourceNodeIds.indexOf(resourceNodeId) == -1) {
						if (dirtyResourceNodeHandler != null) {
							dirtyResourceNodeHandler(resourceNodeId);
						}
						dirtyResourceNodeIds.push(resourceNodeId);						
					}
				}
			}
			return dirtyResourceNodeIds;
		}
		
		/**
		 * @param returnIfAtLeastOneDirtyResourceNodeFound if <code>true</code>, returns the first dirty resourceNodeId found.
		 * @param dirtyResourceNodeHandler function will be executed each time a dirty resourceNode is found.
		 * @return all dirty resourceNodeIds, without duplicates.
		 */ 
		public function getAllDirtyResourceNodeIds(returnIfAtLeastOneDirtyResourceNodeFound:Boolean = false, dirtyResourceNodeHandler:Function = null):Array {
			var dirtyResourceNodeIds:Array = [];
			for each (var resourceNodeId:String in getResourceNodeIds()) {				
				for each (var nodeRegistry:NodeRegistry in getNodeRegistriesForResourceNodeId(resourceNodeId)) {										
					if (isResourceNodeDirty(resourceNodeId, nodeRegistry) && dirtyResourceNodeIds.indexOf(resourceNodeId) == -1) {
						if (returnIfAtLeastOneDirtyResourceNodeFound) {
							return [dirtyResourceNodeIds];
						}											
						if (dirtyResourceNodeHandler != null) {
							dirtyResourceNodeHandler(resourceNodeId);
						}
						dirtyResourceNodeIds.push(resourceNodeId);						
					}
				}		
			}
			return dirtyResourceNodeIds;
		}
				
		public function handleResourceNodesUpdates(resourceNodeIdToUpdates:Object):void {
			for (var resourceNodeId:String in resourceNodeIdToUpdates) {
				var updates:ArrayCollection = resourceNodeIdToUpdates[resourceNodeId];			
				for each (var nodeRegistry:NodeRegistry in getNodeRegistriesForResourceNodeId(resourceNodeId)) {
					nodeRegistry.processUpdates(updates);
				}
			}
		}	
				
		/**
		 * Unlink <code>resourceNodeIds</code> from node registries.
		 * 
		 * <p>
		 * Dispatches <code>NodeRegistry_ResourceNodeRemovedEvent</code> for each <code>NodeRegistry</code> found.
		 * Note: Additional behavior can be added by listening to this event (e.g. in case of an editor, close it).
		 */
		public function unlinkResourceNodesForcefully(resourceNodeIds:ArrayCollection):void {
			var idsList:String = "";
			for each (var resourceNodeId:String in resourceNodeIds) {
				var nodeRegistries:Array = getNodeRegistriesForResourceNodeId(resourceNodeId);
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
				
		public function unregisterResourceNode(resourceNode:Node, nodeRegistry:NodeRegistry):void {	
			// change isDirty to false and dispatch event
			var resourceNodeFromRegistry:Node = nodeRegistry.getNodeById(resourceNode.nodeUri);
			resourceNodeFromRegistry.properties[CoreConstants.IS_DIRTY] = false;
			resourceNodeFromRegistry.dispatchEvent(new NodeUpdatedEvent(resourceNodeFromRegistry, new ArrayList([CoreConstants.IS_DIRTY])));
			
			// refresh maps
			unlinkResourceNodeFromNodeRegistry(resourceNode.nodeUri, nodeRegistry);
		}
		
		protected function resourceNodeUpdated(event:NodeUpdatedEvent):void {
			var resourceNode:Node = event.node;
			if (NodeControllerUtils.hasPropertyChanged(resourceNode, CoreConstants.IS_DIRTY)) {
				resourceOperationsManager.updateGlobalDirtyState(resourceNode.properties[CoreConstants.IS_DIRTY]);
			}
		}
		
	}
}