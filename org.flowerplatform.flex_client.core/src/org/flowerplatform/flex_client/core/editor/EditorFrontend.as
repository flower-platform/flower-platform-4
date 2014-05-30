/* license-start
* 
* Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
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
* Contributors:
*   Crispico - Initial API and implementation
*
* license-end
*/
package org.flowerplatform.flex_client.core.editor {
	import flash.events.IEventDispatcher;
	
	import mx.collections.IList;
	import mx.managers.IFocusManagerComponent;
	import mx.rpc.events.FaultEvent;
	
	import org.flowerplatform.flex_client.core.CoreConstants;
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.core.editor.resource.event.NodeRegistryRemovedEvent;
	import org.flowerplatform.flex_client.core.node.NodeRegistry;
	import org.flowerplatform.flex_client.core.node.event.BeforeRemoveNodeChildrenEvent;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ComposedActionProvider;
	import org.flowerplatform.flexutil.action.IAction;
	import org.flowerplatform.flexutil.layout.ITitleDecorator;
	import org.flowerplatform.flexutil.layout.IWorkbench;
	import org.flowerplatform.flexutil.selection.ISelectionProvider;
	import org.flowerplatform.flexutil.view_content_host.IViewContent;
	import org.flowerplatform.flexutil.view_content_host.IViewHost;
	import org.flowerplatform.flexutil.view_content_host.IViewHostAware;
	
	import spark.components.VGroup;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class EditorFrontend extends VGroup implements IViewContent, IFocusManagerComponent, ISelectionProvider, IViewHostAware, ITitleDecorator {
		
		private var _editorInput:String;
		
		public var hideRootNode:Boolean;
		
		public var actionProvider:ComposedActionProvider = new ComposedActionProvider();
		
		protected var _viewHost:IViewHost;
		
		public var nodeRegistry:NodeRegistry;
		
		public function EditorFrontend() {
			super();
			nodeRegistry = new NodeRegistry();
			
			nodeRegistry.addEventListener(NodeRegistryRemovedEvent.REMOVED, nodeRegistryRemovedHandler);		
			nodeRegistry.addEventListener(BeforeRemoveNodeChildrenEvent.BEFORE_REMOVED, nodeRegistryBeforeRemoveChildrenHandler);	
		}
					
		public function get editorInput():String {
			return _editorInput;
		}
		
		public function set editorInput(editorInput:String):void {
			_editorInput = editorInput;
			CorePlugin.getInstance().resourceNodesManager.subscribeToSelfOrParentResource(editorInput, nodeRegistry, subscribeResultCallback, subscribeFaultCallback);
		}
		
		protected function subscribeResultCallback(resourceNode:Node):void {
			// nothing to do
		}
		
		protected function subscribeFaultCallback(event:FaultEvent):void {
			// close editor
			FlexUtilGlobals.getInstance().workbench.closeView(IEventDispatcher(viewHost));
		}
		
		public function getActions(selection:IList):Vector.<IAction> {
			return actionProvider.getActions(selection);
		}
		
		public function getSelection():IList {			
			return null;
		}
		
		public function set viewHost(value:IViewHost):void {
			_viewHost = value;
		}
		
		public function get viewHost():IViewHost {
			return _viewHost;
		}
		
		/**
		 * @author Cristina Constantinescu
		 */
		public function isDirty():Boolean {	
			for each (var resourceNodeId:Object in CorePlugin.getInstance().resourceNodesManager.getResourceNodeIdsForNodeRegistry(nodeRegistry)) {
				if (CorePlugin.getInstance().resourceNodesManager.isResourceNodeDirty(String(resourceNodeId), nodeRegistry)) {
					return true;
				}
			}
			return false;
		}
		
		/**
		 * @author Cristina Constantinescu
		 */
		public function decorateTitle(title:String):String {
			if (isDirty()) { 
				return "* " + title;
			}
			return title;
		}
		
		protected function nodeRegistryRemovedHandler(event:NodeRegistryRemovedEvent):void {
			var workbench:IWorkbench = FlexUtilGlobals.getInstance().workbench;			
			workbench.closeView(workbench.getViewComponentForEditor(this), false);		
		}
		
		protected function nodeRegistryBeforeRemoveChildrenHandler(event:BeforeRemoveNodeChildrenEvent):void {
			// get all dirty resourceNodes starting from node
			var dirtyResourceNodeIds:Array = [];
			var savedResourceNodeIds:Array = [];
			for each (var obj:Object in CorePlugin.getInstance().resourceNodesManager.getResourceNodeIdsForNodeRegistry(nodeRegistry)) {
				var resourceNodeId:String = String(obj);
				var resourceNode:Node = nodeRegistry.getNodeById(resourceNodeId);
				var parent:Node = resourceNode;
				while (parent != null && parent != event.node) {					
					parent = parent.parent;					
				}
				if (parent == event.node) {
					var isSubscribable:Boolean = resourceNode.properties[CoreConstants.IS_SUBSCRIBABLE];
					if (isSubscribable) {
						if (CorePlugin.getInstance().resourceNodesManager.isResourceNodeDirty(resourceNodeId, nodeRegistry) && dirtyResourceNodeIds.indexOf(resourceNodeId) == -1) {
							dirtyResourceNodeIds.push(resourceNodeId);
						} else {
							savedResourceNodeIds.push(resourceNodeId);
						}
					}
				}
			}
			
			// remove nodes that are already saved
			for each (var savedResourceNodeId:String in savedResourceNodeIds) {
				CorePlugin.getInstance().resourceNodesManager.unregisterResourceNode(nodeRegistry.getNodeById(savedResourceNodeId), nodeRegistry);	
			}
			
			if (dirtyResourceNodeIds.length > 0) { // at least one dirty resourceNode found -> show dialog
				CorePlugin.getInstance().resourceNodesManager.showSaveDialogIfDirtyStateOrCloseEditors([this], dirtyResourceNodeIds, 
					function():void {
						for (var i:int = 0; i < dirtyResourceNodeIds.length; i++) {
							CorePlugin.getInstance().resourceNodesManager.unregisterResourceNode(nodeRegistry.getNodeById(dirtyResourceNodeIds[i]), nodeRegistry);
						}
						// wait for server response before collapse			
						nodeRegistry.removeChildren(event.node, event.refreshChildren, false);
					}
				);
				event.dontRemoveChildren = true;
			} 			
		}
		
	}
}
