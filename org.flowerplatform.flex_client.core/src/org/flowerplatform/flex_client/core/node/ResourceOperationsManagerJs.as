package org.flowerplatform.flex_client.core.node {
	
	import mx.collections.ArrayList;
	
	import org.flowerplatform.flex_client.core.editor.resource.ResourceNode;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class ResourceOperationsManagerJs {
	
		public var resourceOperationsHandler:IResourceOperationsHandler;
		
		protected var nodeRegistryManager:NodeRegistryManager;
		
		public function ResourceOperationsManagerJs(nodeRegistryManager:NodeRegistryManager, resourceOperationsHandler:IResourceOperationsHandler) {
			this.nodeRegistryManager = nodeRegistryManager;
			this.resourceOperationsHandler = resourceOperationsHandler;
			this.resourceOperationsHandler.nodeRegistryManager = this.nodeRegistryManager;
		}
		
		public function showSaveDialog(nodeRegistries:Array = null, dirtyResourceNodeIds:Array = null, handler:Function = null):void {
			if (nodeRegistries == null) {
				nodeRegistries = nodeRegistryManager.getNodeRegistries();
			}
			
			var dirtyResourceNodes:ArrayList = new ArrayList();
			if (dirtyResourceNodeIds == null) {
				nodeRegistryManager.getDirtyResourceSetsFromNodeRegistries(nodeRegistries, function(dirtyResourceNodeId:String):void {
					dirtyResourceNodes.addItem(new ResourceNode(dirtyResourceNodeId, true));
				});				
			} else {
				for (var i:int = 0; i < dirtyResourceNodeIds.length; i++) {
					dirtyResourceNodes.addItem(new ResourceNode(dirtyResourceNodeIds[i], true));
				}
			}
			if (dirtyResourceNodes.length == 0) {
				if (handler != null) {
					handler();
				} else {
					nodeRegistryManager.removeNodeRegistries(nodeRegistries);
				}
				return;
			}
			resourceOperationsHandler.showSaveDialog(nodeRegistries, dirtyResourceNodes, handler);
		}
		
		public function showReloadDialog(nodeRegistries:Array = null, resourceSets:Array = null):void {
			if (nodeRegistries == null) {
				nodeRegistries = nodeRegistryManager.getNodeRegistries();
			}
			if (resourceSets == null) {
				resourceSets = nodeRegistryManager.getDirtyResourceSetsFromNodeRegistries(nodeRegistries);
			}
			resourceOperationsHandler.showReloadDialog(nodeRegistries, resourceSets);
		}
		
		public function save(nodeRegistry:NodeRegistry):void {
			var dirtyResourceNodeIds:Array = nodeRegistryManager.getDirtyResourceSetsFromNodeRegistries([nodeRegistry]);
			if (dirtyResourceNodeIds.length == 1) { 
				// single resourceNode to save -> save without asking
				nodeRegistryManager.serviceInvocator.invoke("resourceService.save", [dirtyResourceNodeIds[0]]);
			} else { 
				// multiple resourceNodes to save -> show dialog
				showSaveDialog([nodeRegistry], dirtyResourceNodeIds, function():void {});
			}
		}
		
		public function saveAll():void {
			nodeRegistryManager.getAllDirtyResourceSets(false, function(dirtyResourceNodeId:String):void {
				// for each dirty resourceNode found -> save it
				nodeRegistryManager.serviceInvocator.invoke("resourceService.save", [dirtyResourceNodeId]);
			});
		}
		
		public function reload(nodeRegistry:NodeRegistry):void {
			var resourceSets:Array = nodeRegistryManager.getDirtyResourceSetsFromNodeRegistries([nodeRegistry]);
			if (resourceSets.length == 1) {
				// single resourceNode to reload -> reload without asking
				nodeRegistryManager.serviceInvocator.invoke("resourceService.reload", [resourceSets[0]]);
			} else {
				// multiple resourceNodes -> show dialog
				showReloadDialog([nodeRegistry], resourceSets);
			}
		}
		
	}
}