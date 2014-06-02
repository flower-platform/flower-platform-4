package org.flowerplatform.flex_client.core.editor.resource {
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	import mx.collections.ArrayList;
	import mx.core.UIComponent;
	import mx.rpc.events.FaultEvent;
	import mx.utils.ObjectUtil;
	
	import org.flowerplatform.flex_client.core.CoreConstants;
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.EditorFrontend;
	import org.flowerplatform.flex_client.core.editor.action.EditorFrontendAwareAction;
	import org.flowerplatform.flex_client.core.editor.action.ReloadAction;
	import org.flowerplatform.flex_client.core.editor.action.SaveAction;
	import org.flowerplatform.flex_client.core.editor.action.SaveAllAction;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.core.editor.resource.event.NodeRegistryRemovedEvent;
	import org.flowerplatform.flex_client.core.editor.resource.event.ResourceNodeRemovedEvent;
	import org.flowerplatform.flex_client.core.node.NodeRegistry;
	import org.flowerplatform.flex_client.core.node.controller.NodeControllerUtils;
	import org.flowerplatform.flex_client.core.node.event.NodeUpdatedEvent;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.layout.IWorkbench;
	import org.flowerplatform.flexutil.layout.event.ActiveViewChangedEvent;
	import org.flowerplatform.flexutil.layout.event.ViewsRemovedEvent;
	
	/**
	 * @author Cristina Constantinescu
	 * @author Mariana Gheorghe
	 */ 
	public class ResourceNodesManager {
			
		private var _saveAction:SaveAction;
		private var _saveAllAction:SaveAllAction;
		
		private var _reloadAction:ReloadAction;
		
		public function get saveAction():SaveAction {
			if (_saveAction == null) {
				_saveAction = new SaveAction();				
			}
			return _saveAction;
		}
		
		public function get saveAllAction():SaveAllAction {
			if (_saveAllAction == null) {
				_saveAllAction = new SaveAllAction();				
			}
			return _saveAllAction;
		}
		
		public function get reloadAction():ReloadAction {
			if (_reloadAction == null) {
				_reloadAction = new ReloadAction();
			}
			return _reloadAction;
		}
				
		public var lastUpdateTimestampOfServer:Number = -1;
		public var lastUpdateTimestampOfClient:Number = -1;
		
		/**
		 * Keep maps between node registries and their resource node ids and vice versa.
		 */ 
		private var resourceNodeIdToNodeRegistries:Dictionary = new Dictionary();		
		private var nodeRegistryToResourceNodeIds:Dictionary = new Dictionary();
		
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
		
		public function activeViewChangedHandler(evt:ActiveViewChangedEvent):void {			
			updateEditorFrontendActionsEnablement();
		}
		
		public function updateEditorFrontendActionsEnablement():void {
			var workbench:IWorkbench = FlexUtilGlobals.getInstance().workbench;			
			var activeComponent:UIComponent = workbench.getEditorFromViewComponent(workbench.getActiveView());
			
			if (activeComponent is EditorFrontend) {
				var editorFrontend:EditorFrontend = EditorFrontend(activeComponent);
				enableDisableAction(saveAction, editorFrontend.isDirty(), editorFrontend);
				enableDisableAction(reloadAction, true, editorFrontend);
			} else {
				enableDisableAction(saveAction, false, null);
				enableDisableAction(reloadAction, false, null);
			}			
		}
		
		private function enableDisableAction(action:EditorFrontendAwareAction, enabled:Boolean, editorFrontend:EditorFrontend):void {
			action.enabled = enabled;
			action.editorFrontend = editorFrontend;
		}
		
		public function updateSaveAllActionEnablement(someResourceNodeHasBecomeDirty:Boolean):void {			
			if (!saveAllAction.enabled) {
				// saveAll is disabled -> set the resourceNode dirty state as action's enablement
				saveAllAction.enabled = someResourceNodeHasBecomeDirty;
				return;
			}			
			if (someResourceNodeHasBecomeDirty) {
				// saveAll is enabled, resourceNode has become dirty -> same enablement state, no need to continue
				return;
			}
			// saveAll is enabled, resourceNode isn't dirty -> verify if there is at least one resourceNode dirty left
			if (getAllDirtyResourceNodeIds(true).length == 0) {
				// no resourceNode dirty -> disable action
				saveAllAction.enabled = false;
			}						
		}
		
		/**
		 * Update the save/save all actions enablement, and refreshes the editors labels.
		 */ 
		public function updateGlobalDirtyState(someResourceNodeHasBecomeDirty:Boolean):void {
			updateSaveAllActionEnablement(someResourceNodeHasBecomeDirty);
			updateEditorFrontendActionsEnablement();			
			
			FlexUtilGlobals.getInstance().workbench.refreshLabels();			
		}
		
		// TODO CC: replace id with proper label
		public function getResourceNodeLabel(resourceNodeId:String):String {			
			return resourceNodeId;
		}
				
		/**
		 * Don't remove editors immediately. If there are dirty editors, show save dialog.
		 */ 
		public function viewsRemovedHandler(e:ViewsRemovedEvent):void {
			var nodeRegistries:Array = [];
			for each (var view:Object in e.removedViews) {
				var viewComponent:UIComponent = FlexUtilGlobals.getInstance().workbench.getEditorFromViewComponent(UIComponent(view));			
				if (viewComponent is EditorFrontend) {
					nodeRegistries.push(EditorFrontend(viewComponent).nodeRegistry);
					e.dontRemoveViews.addItem(view);
				}
			}
			showSaveDialogIfDirtyStateOrCloseEditors(nodeRegistries);					
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
			
			updateGlobalDirtyState(false);
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
					if (CorePlugin.getInstance().resourceNodesManager.isResourceNodeDirty(resourceNodeId, nodeRegistry) && dirtyResourceNodeIds.indexOf(resourceNodeId) == -1) {
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
					if (CorePlugin.getInstance().resourceNodesManager.isResourceNodeDirty(resourceNodeId, nodeRegistry) && dirtyResourceNodeIds.indexOf(resourceNodeId) == -1) {
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
		
		/**
		 * If at least one dirty resourceNode found, shows the save dialog, else closes the editors OR 
		 * calls the <code>handler</code> if not null.
		 * 
		 * @param editors if <code>null</code>, all workbench editors node registries will be used.
		 * @param dirtyResourceNodeIds if <code>null</code>, all dirty resourceNodes from <code>nodeRegistries</code> will be used.
		 * @param handler Is called before closing the save view. If <code>null</code>, <code>nodeRegistries</code> will be removed.
		 */ 
		public function showSaveDialogIfDirtyStateOrCloseEditors(nodeRegistries:Array = null, dirtyResourceNodeIds:Array = null, handler:Function = null):void {	
			if (nodeRegistries == null) {
				nodeRegistries = getAllNodeUpdatesProcessors();
			}
			
			var dirtyResourceNodes:ArrayList = new ArrayList();
			if (dirtyResourceNodeIds == null) {
				getDirtyResourceNodeIdsFromNodeRegistries(nodeRegistries, function(dirtyResourceNodeId:String):void {
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
					removeNodeRegistries(nodeRegistries);
				}
				return;
			}
					
			var saveView:ResourceNodesListView = new ResourceNodesListView();
			saveView.nodeRegistries = nodeRegistries;
			saveView.resourceNodes = dirtyResourceNodes;
			saveView.handler = handler;
			saveView.serverMethodToInvokeForSelection = "resourceService.save";
			saveView.title = Resources.getMessage("save.title");
			saveView.labelForSingleResourceNode = Resources.getMessage('save.singleResourceNode.message', 
				[getResourceNodeLabel(dirtyResourceNodes.getItemAt(0).resourceNodeId)])
			saveView.labelForMultipleResourceNodes = Resources.getMessage('save.multipleResourceNodes.label');
			saveView.iconForSingleResourceNode = Resources.saveIcon;
			saveView.iconForMultipleResourceNodes = Resources.saveAllIcon;
			
			showPopup(saveView, dirtyResourceNodes.length == 1);
		}
		
		/**
		 * @author Mariana Gheorghe
		 */
		public function showReloadDialog(nodeRegistries:Array = null):void {
			if (nodeRegistries == null) {
				nodeRegistries = getNodeRegistries();
			}
			
			var resourceNodes:ArrayList = new ArrayList();
			for each (var nodeRegistry:NodeRegistry in nodeRegistries) {
				for each (var resourceNodeId:String in getResourceNodeIdsForNodeRegistry(nodeRegistry)) {
					resourceNodes.addItem(new ResourceNode(resourceNodeId, true));
				}
			}
			
			var reloadView:ResourceNodesListView = new ResourceNodesListView();
			reloadView.nodeRegistries = nodeRegistries;
			reloadView.resourceNodes = resourceNodes;
			reloadView.handler = function():void {};
			reloadView.serverMethodToInvokeForSelection = "resourceService.reload";
			reloadView.title = Resources.getMessage("reload.title");
			reloadView.labelForMultipleResourceNodes = Resources.getMessage('reload.multipleResourceNodes.label');
			reloadView.iconForSingleResourceNode = reloadView.iconForMultipleResourceNodes = Resources.reloadIcon;
			
			showPopup(reloadView, resourceNodes.length == 1);
		}
		
		private function showPopup(view:ResourceNodesListView, small:Boolean):void {
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()				
				.setViewContent(view)
				.setWidth(400)
				.setHeight(small ? 150 : 300)
				.show();	
		}
		
		
		/**
		 * @return global dirty state for all open editors = saveAll action enablement.
		 */ 
		public function getGlobalDirtyState():Boolean {			
			return saveAllAction != null ? saveAllAction.enabled : false;
		}
		
		/**
		 * @return a list with all <code>NodeRegistry</code>s found on workbench.
		 */ 
		protected function getAllNodeUpdatesProcessors():Array {			
			var nodeRegistries:Array = new Array();
			
			var components:ArrayCollection = new ArrayCollection();
			FlexUtilGlobals.getInstance().workbench.getAllEditorViews(null, components);
			
			for each (var component:UIComponent in components) {								
				if (component is EditorFrontend) {
					nodeRegistries.push(EditorFrontend(component).nodeRegistry);
				}
			}
			return nodeRegistries;
		}
		
		public function handleResourceNodesUpdates(resourceNodeIdToUpdates:Object):void {
			for (var resourceNodeId:String in resourceNodeIdToUpdates) {
				var updates:ArrayCollection = resourceNodeIdToUpdates[resourceNodeId];			
				for each (var nodeRegistry:NodeRegistry in getNodeRegistriesForResourceNodeId(resourceNodeId)) {
					nodeRegistry.nodeUpdatesHandler(updates);
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
		
		/**
		 * Calls subscribe for <code>nodeId</code> on server. Callback functions:
		 * <ul>
		 * 	<li><code>subscribeResultCallback(resourceNode:Node):void</code>
		 * 	<li><code>subscribeFaultCallback(event:FaultEvent):void</code>
		 * </ul>
		 */ 
		public function subscribeToSelfOrParentResource(nodeId:String, nodeRegistry:NodeRegistry, subscribeResultCallback:Function = null, subscribeFaultCallback:Function = null):void {
			CorePlugin.getInstance().serviceLocator.invoke("resourceService.subscribeToParentResource", [nodeId], 
				function(resourceNode:Node):void {
					if (resourceNode != null) {
						registerResourceNode(resourceNode, nodeRegistry);
					}
					if (subscribeResultCallback != null) {
						subscribeResultCallback(resourceNode);
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
		
		protected function registerResourceNode(resourceNode:Node, nodeRegistry:NodeRegistry):void {
			// we have a resource node
			// link it with this registry and update its properties
			
			var resourceNodeFromRegistry:Node = nodeRegistry.getNodeById(resourceNode.fullNodeId);
			if (resourceNodeFromRegistry) { 
				// do nothing
				// node already in registry -> probably we want to update only its properties					
			} else {
				nodeRegistry.registerNode(resourceNode);
				resourceNodeFromRegistry = nodeRegistry.getNodeById(resourceNode.fullNodeId);
			}				
			linkResourceNodeWithNodeRegistry(resourceNode.fullNodeId, nodeRegistry);
			
			// listen for resourceNode properties modifications like isDirty
			resourceNodeFromRegistry.addEventListener(NodeUpdatedEvent.NODE_UPDATED, resourceNodeUpdated);
			
			// copy needed -> otherwise, if same instances, the setter isn't called
			resourceNodeFromRegistry.properties = ObjectUtil.copy(resourceNode.properties);				
		}
		
		public function unregisterResourceNode(resourceNode:Node, nodeRegistry:NodeRegistry):void {	
			// change isDirty to false and dispatch event
			var resourceNodeFromRegistry:Node = nodeRegistry.getNodeById(resourceNode.fullNodeId);
			resourceNodeFromRegistry.properties[CoreConstants.IS_DIRTY] = false;
			resourceNodeFromRegistry.dispatchEvent(new NodeUpdatedEvent(resourceNodeFromRegistry, new ArrayList([CoreConstants.IS_DIRTY])));
			
			// refresh maps
			unlinkResourceNodeFromNodeRegistry(resourceNode.fullNodeId, nodeRegistry);
		}
		
		protected function resourceNodeUpdated(event:NodeUpdatedEvent):void {
			var resourceNode:Node = event.node;
			if (NodeControllerUtils.hasPropertyChanged(resourceNode, CoreConstants.IS_DIRTY)) {
				updateGlobalDirtyState(resourceNode.properties[CoreConstants.IS_DIRTY]);
			}
		}
		
	}
}
