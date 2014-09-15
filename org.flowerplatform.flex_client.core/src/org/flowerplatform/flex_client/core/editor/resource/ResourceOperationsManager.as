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
package org.flowerplatform.flex_client.core.editor.resource {
	import mx.collections.ArrayList;
	import mx.collections.IList;
	import mx.core.UIComponent;
	
	import org.flowerplatform.flex_client.core.editor.EditorFrontend;
	import org.flowerplatform.flex_client.core.editor.action.EditorFrontendAwareAction;
	import org.flowerplatform.flex_client.core.editor.action.ReloadAction;
	import org.flowerplatform.flex_client.core.editor.action.SaveAction;
	import org.flowerplatform.flex_client.core.editor.action.SaveAllAction;
	import org.flowerplatform.flex_client.core.node.IExternalInvocator;
	import org.flowerplatform.flex_client.core.node.IResourceOperationsHandler;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.layout.IWorkbench;
	import org.flowerplatform.flexutil.layout.event.ActiveViewChangedEvent;
	import org.flowerplatform.flexutil.layout.event.ViewsRemovedEvent;
	import org.flowerplatform.flex_client.core.node.NodeRegistryManager;
	
	/**
	 * @author Cristina Constantinescu
	 * @author Mariana Gheorghe
	 */ 
	public class ResourceOperationsManager implements IResourceOperationsHandler, IExternalInvocator {
			
		public var saveAction:SaveAction = new SaveAction();
		public var saveAllAction:SaveAllAction = new SaveAllAction();		
		public var reloadAction:ReloadAction = new ReloadAction();
				
		private var _nodeRegistryManager:NodeRegistryManager;
				
		public function get nodeRegistryManager():NodeRegistryManager {
			return _nodeRegistryManager;
		}

		public function set nodeRegistryManager(value:NodeRegistryManager):void {
			_nodeRegistryManager = value;
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
			if (nodeRegistryManager.getAllDirtyResourceSets(true).length == 0) {
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
			if (!e.canPreventDefault) {
				return;
			}
			var nodeRegistries:Array = [];
			for each (var view:Object in e.removedViews) {
				var viewComponent:UIComponent = FlexUtilGlobals.getInstance().workbench.getEditorFromViewComponent(UIComponent(view));			
				if (viewComponent is EditorFrontend) {
					nodeRegistries.push(EditorFrontend(viewComponent).nodeRegistry);
					e.dontRemoveViews.addItem(view);
				}
			}
			_nodeRegistryManager.resourceOperationsManager.showSaveDialog(nodeRegistries);					
		}
				
		
		/**
		 * If at least one dirty resourceNode found, shows the save dialog, else closes the editors OR 
		 * calls the <code>handler</code> if not null.
		 * 
		 * @param editors if <code>null</code>, all workbench editors node registries will be used.
		 * @param dirtyResourceNodeIds if <code>null</code>, all dirty resourceNodes from <code>nodeRegistries</code> will be used.
		 * @param handler Is called before closing the save view. If <code>null</code>, <code>nodeRegistries</code> will be removed.
		 */ 
		public function showSaveDialog(nodeRegistries:Array = null, dirtyResourceNodes:IList = null, handler:Function = null):void {			
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
		public function showReloadDialog(nodeRegistries:Array = null, resourceSets:Array = null):void {
			var resourceNodes:ArrayList = new ArrayList();
			for each (var resourceSet:String in resourceSets) {
				resourceNodes.addItem(new ResourceNode(resourceSet, true));
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
		
		public function showMessageBox(text:String, title:String):void {			
			FlexUtilGlobals.getInstance().messageBoxFactory.createMessageBox()
				.setText(text)
				.setTitle(title)
				.setWidth(300)
				.setHeight(200)
				.showMessageBox();
		}
				
	}
}
