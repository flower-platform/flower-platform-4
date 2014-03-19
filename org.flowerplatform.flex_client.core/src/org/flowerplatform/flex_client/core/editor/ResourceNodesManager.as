package org.flowerplatform.flex_client.core.editor {
	import com.crispico.flower.util.layout.WorkbenchViewHost;
	import com.crispico.flower.util.layout.event.ActiveViewChangedEvent;
	
	import flash.events.EventDispatcher;
	
	import mx.collections.ArrayCollection;
	import mx.collections.ArrayList;
	import mx.collections.IList;
	import mx.core.UIComponent;
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.action.SaveAction;
	import org.flowerplatform.flex_client.core.editor.action.SaveAllAction;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.layout.event.ViewsRemovedEvent;
	import org.flowerplatform.flexutil.shortcut.Shortcut;
	import org.flowerplatform.flexutil.view_content_host.IViewHost;
	
	public class ResourceNodesManager {
		
		public var rootNodeIdToEditors:RootNodeIdsToEditors = new RootNodeIdsToEditors();
			
		public var saveAction:SaveAction;
		public var saveAllAction:SaveAllAction;
		
		public function ResourceNodesManager() {
			saveAction = new SaveAction();
			saveAllAction = new SaveAllAction();
			FlexUtilGlobals.getInstance().keyBindings.registerBinding(new Shortcut(true, false, "s"), saveAction); // Ctrl + S
			FlexUtilGlobals.getInstance().keyBindings.registerBinding(new Shortcut(true, true, "s"), saveAllAction); // Ctrl + Shift + S
			
			EventDispatcher(FlexUtilGlobals.getInstance().workbench).addEventListener(ActiveViewChangedEvent.ACTIVE_VIEW_CHANGED, activeViewChangedHandler);
		}
		
		private function activeViewChangedHandler(evt:ActiveViewChangedEvent):void {
			if (evt.newView is IDirtyStateProvider) {
				editorInputChangedForComponent(IDirtyStateProvider(evt.newView));
			} else {
				saveAction.enabled = false;
				saveAction.currentEditorFrontend = null;
			}
		}
		
		public function editorInputChangedForComponent(component:IDirtyStateProvider):void {
			var uiComponent:UIComponent = FlexUtilGlobals.getInstance().workbench.getActiveView();
			
			if (uiComponent is IViewHost) {
				uiComponent = UIComponent(IViewHost(uiComponent).activeViewContent);
			}
			if (uiComponent == component) {				
				if (component is EditorFrontend) {
					saveAction.currentEditorFrontend = EditorFrontend(component);
					saveAction.enabled = EditorFrontend(component).isDirty();
				} else {
					saveAction.currentEditorFrontend = null;
					saveAction.enabled = false;					
				}
			}
		}
		
		/**
		 * Update the save/save all actions enablement, and refreshes the
		 * labels of the Workbench.
		 */ 
		public function dirtyStateUpdated(editorFontend:EditorFrontend):void {
			if (saveAllAction.enabled) {
				// at least one dirty resource
				if (editorFontend == null || !editorFontend.isDirty()) {
					// either an ER has dissapeared or an ER has been saved
					// => refresh the global dirty state, by looking at all ERs
					
					var dirtyEFFound:Boolean = false;
					var count:int = 0;
					var resourceNodes:ArrayCollection = CorePlugin.getInstance().resourceNodesManager.rootNodeIdToEditors.getRootNodeIds();
					for each (var sc:String in resourceNodes) {
						if (true) { //currentESC.editableResourceStatus != null && currentESC.editableResourceStatus.dirty) {
							dirtyEFFound = true;
							count++;
							break;
						}
					}
					if (count == 0 || !dirtyEFFound) {
						saveAllAction.enabled = false;
					}
				}
			} else {
				if (editorFontend != null) {
					// everything is saved
					saveAllAction.enabled = editorFontend.isDirty(); // the value of this variable should always be true
				} // otherwise we are not interested; i.e. global dirty = false and an ER has left (which was non dirty for sure)
			}
			FlexUtilGlobals.getInstance().workbench.refreshLabels();			
//			dispatchEvent(new DirtyStateUpdatedEvent(editorStatefulClient));
		}
		
		public function getDirtyResourceNodes(allResourceNodes:ArrayList):ArrayList {
			var dirtyResourceNodes:ArrayList = new ArrayList();
			for (var i:int = 0; i < allResourceNodes.length; i++) {
				var resourceNodeId:String = String(allResourceNodes.getItemAt(i));
				dirtyResourceNodes.addItem(new ResourceNodeData(resourceNodeId, true));
			}
			return dirtyResourceNodes;
		}
		
		public function getResourceNodesFromEditors(editors:ArrayList):ArrayList {
			var resourceNodes:ArrayList = new ArrayList();
			for (var i:int = 0; i < editors.length; i++) {
				var editor:EditorFrontend = EditorFrontend(editors.getItemAt(i));
				var resourceNodeIds:ArrayCollection = editor.rootNodeIds;
				for each (var resourceNodeId:String in resourceNodeIds) {
					if (resourceNodes.getItemIndex(resourceNodeId) == -1) {
						resourceNodes.addItem(resourceNodeId);					
					}
				}
			}
			return resourceNodes;
		}
		
		public function getResourceNodeLabel(resourceNode:String):String {			
			return resourceNode;
		}
		
		/**
		 * @author Sebastian Solomon
		 * @author Cristina Constantinescu
		 */  
		public function viewsRemovedHandler(e:ViewsRemovedEvent):void {
			var editors:ArrayList = new ArrayList();
			for each (var view:Object in e.removedViews) {
				var viewComponent:UIComponent = UIComponent(view);
				if (!FlexUtilGlobals.getInstance().isMobile && viewComponent is IViewHost) {
					// diagram case: viewContent is wrapped in WorkbenchViewHost, so get the exact component registered in layout
					viewComponent = UIComponent(IViewHost(viewComponent).activeViewContent);
				}
				if (viewComponent is EditorFrontend) {
					editors.addItem(EditorFrontend(viewComponent));
					e.dontRemoveViews.addItem(view);
				}
			}
			new SaveResourceNodesView().show(null, editors);					
		}
		
		public function invokeSaveResourceNodesView(resourceNodes:IList = null):void {
			if (resourceNodes == null) { // when it comes from js
				resourceNodes = rootNodeIdToEditors.getRootNodeIds();
			}
			if (resourceNodes == null || resourceNodes.length == 0) {
				return;
			}
			new SaveResourceNodesView().show(resourceNodes);
		}
		
		public function getGlobalDirtyState():Boolean {			
			return saveAllAction != null ? saveAllAction.enabled : false;
		}
	}
}
