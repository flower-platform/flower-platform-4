package org.flowerplatform.flex_client.core.editor {
	import flash.events.EventDispatcher;
	
	import mx.collections.ArrayList;
	import mx.collections.IList;
	import mx.core.UIComponent;
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.action.SaveAction;
	import org.flowerplatform.flex_client.core.editor.action.SaveAllAction;
	import org.flowerplatform.flex_client.core.editor.update.NodeUpdateProcessor;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.layout.IWorkbench;
	import org.flowerplatform.flexutil.layout.event.ActiveViewChangedEvent;
	import org.flowerplatform.flexutil.layout.event.ViewsRemovedEvent;
	import org.flowerplatform.flexutil.shortcut.Shortcut;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class ResourceNodesManager {
			
		private var _saveAction:SaveAction;
		private var _saveAllAction:SaveAllAction;
		
		public function get saveAction():SaveAction {
			if (_saveAction == null) {
				_saveAction = new SaveAction();
				FlexUtilGlobals.getInstance().keyBindings.registerBinding(new Shortcut(true, false, "s"), saveAction); // Ctrl + S
			}
			return _saveAction;
		}
		
		public function get saveAllAction():SaveAllAction {
			if (_saveAllAction == null) {
				_saveAllAction = new SaveAllAction();
				FlexUtilGlobals.getInstance().keyBindings.registerBinding(new Shortcut(true, true, "s"), saveAllAction); // Ctrl + Shift + S
			}
			return _saveAllAction;
		}
				
		public function activeViewChangedHandler(evt:ActiveViewChangedEvent):void {			
			updateSaveActionEnablement();
		}
		
		public function updateSaveActionEnablement():void {
			var workbench:IWorkbench = FlexUtilGlobals.getInstance().workbench;			
			var activeComponent:UIComponent = workbench.getEditorFromViewComponent(workbench.getActiveView());
			
			if (activeComponent is EditorFrontend) {
				saveAction.editorFrontend = EditorFrontend(activeComponent);
				saveAction.enabled = EditorFrontend(activeComponent).isDirty();
			} else {
				saveAction.editorFrontend = null;
				saveAction.enabled = false;					
			}			
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
			updateSaveActionEnablement();			
			
			FlexUtilGlobals.getInstance().workbench.refreshLabels();			
		}
		
		// TODO CC: replace id with proper label
		public function getResourceNodeLabel(resourceNodeId:String):String {			
			return resourceNodeId;
		}
				
		public function viewsRemovedHandler(e:ViewsRemovedEvent):void {
			var editors:Array = [];
			for each (var view:Object in e.removedViews) {
				var viewComponent:UIComponent = FlexUtilGlobals.getInstance().workbench.getEditorFromViewComponent(UIComponent(view));			
				if (viewComponent is EditorFrontend) {
					editors.push(EditorFrontend(viewComponent));
					e.dontRemoveViews.addItem(view);
				}
			}
			showSaveDialogIfDirtyStateOrCloseEditors(editors);					
		}
		
		/**
		 * Closes all editors without dispatching events and updates global state for save actions.
		 */ 
		public function closeEditors(editors:Array):void {
			var workbench:IWorkbench = FlexUtilGlobals.getInstance().workbench;
			
			for (var i:int = 0; i < editors.length; i++) {
				var editor:EditorFrontend = EditorFrontend(editors[i]);
				
				for each (var resourceNodeId:String in editor.nodeUpdateProcessor.resourceNodeIds) {
					CorePlugin.getInstance().resourceNodeIdsToNodeUpdateProcessors.removeNodeUpdateProcessor(resourceNodeId, editor.nodeUpdateProcessor);
				}					
				workbench.closeView(workbench.getViewComponentForEditor(editor), false);							
			}
			
			updateGlobalDirtyState(false);
		}
		
		/**
		 * @param dirtyResourceNodeHandler function will be executed each time a dirty resourceNode is found.
		 * @return all dirty resourceNodeIds found in <code>editors</code>, without duplicates.
		 */ 
		public function getEditorsDirtyResourceNodeIds(editors:Array, dirtyResourceNodeHandler:Function = null):Array {			
			var dirtyResourceNodeIds:Array = [];
			for (var i:int = 0; i < editors.length; i++) {
				var nodeUpdateProcessor:NodeUpdateProcessor = NodeUpdateProcessor(EditorFrontend(editors[i]).nodeUpdateProcessor);				
				for each (var resourceNodeId:String in nodeUpdateProcessor.resourceNodeIds) {
					if (nodeUpdateProcessor.isResourceNodeDirty(resourceNodeId) && dirtyResourceNodeIds.indexOf(resourceNodeId) == -1) {
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
			for each (var resourceNodeId:String in CorePlugin.getInstance().resourceNodeIdsToNodeUpdateProcessors.getResourceNodeIds()) {				
				for each (var nodeUpdateProcessor:NodeUpdateProcessor in CorePlugin.getInstance().resourceNodeIdsToNodeUpdateProcessors.getNodeUpdateProcessors(resourceNodeId)) {										
					if (nodeUpdateProcessor.isResourceNodeDirty(resourceNodeId) && dirtyResourceNodeIds.indexOf(resourceNodeId) == -1) {
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
		 * If at least one dirty resourceNode found, shows the save dialog, else closes the editors.
		 * 
		 * @param editors if <code>null</code>, all workbench editors will be used.
		 * @param dirtyResourceNodeIds if <code>null</code>, all dirty resourceNodes from <code>editors</code> will be used.
		 */ 
		public function showSaveDialogIfDirtyStateOrCloseEditors(editors:Array = null, dirtyResourceNodeIds:Array = null):void {	
			if (editors == null) {
				editors = CorePlugin.getInstance().getAllEditorFrontends();
			}
			
			var dirtyResourceNodes:ArrayList = new ArrayList();
			if (dirtyResourceNodeIds == null) {
				getEditorsDirtyResourceNodeIds(editors, function(dirtyResourceNodeId:String):void {
					dirtyResourceNodes.addItem(new ResourceNode(dirtyResourceNodeId, true));
				});				
			} else {
				for (var i:int = 0; i < dirtyResourceNodeIds.length; i++) {
					dirtyResourceNodes.addItem(new ResourceNode(dirtyResourceNodeIds[i], true));
				}
			}
			if (dirtyResourceNodes.length == 0) {
				closeEditors(editors);
				return;
			}
					
			var saveView:SaveResourceNodesView = new SaveResourceNodesView();
			saveView.editors = editors;
			saveView.dirtyResourceNodes = dirtyResourceNodes;
			
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()				
				.setViewContent(saveView)
				.setWidth(400)
				.setHeight((dirtyResourceNodes.length == 1) ? 150 : 300)
				.show();				
		}
		
		/**
		 * @return global dirty state for all open editors = saveAll action enablement.
		 */ 
		public function getGlobalDirtyState():Boolean {			
			return saveAllAction != null ? saveAllAction.enabled : false;
		}
		
	}
}
