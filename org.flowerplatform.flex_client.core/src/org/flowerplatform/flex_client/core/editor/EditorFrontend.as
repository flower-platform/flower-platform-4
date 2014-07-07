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
package org.flowerplatform.flex_client.core.editor {
	import flash.events.IEventDispatcher;
	
	import mx.collections.IList;
	import mx.managers.IFocusManagerComponent;
	import mx.rpc.events.FaultEvent;
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.core.editor.resource.event.NodeRegistryRemovedEvent;
	import org.flowerplatform.flex_client.core.node.NodeRegistry;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ComposedActionProvider;
	import org.flowerplatform.flexutil.action.IAction;
	import org.flowerplatform.flexutil.action.IActionProvider;
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
	public class EditorFrontend extends VGroup implements IViewContent, IFocusManagerComponent, ISelectionProvider, IViewHostAware, ITitleDecorator, IActionProvider {
		
		private var _editorInput:String;
				
		public var actionProvider:ComposedActionProvider = new ComposedActionProvider();
		
		protected var _viewHost:IViewHost;
		
		public var nodeRegistry:NodeRegistry;
		
		public function EditorFrontend() {
			super();
			nodeRegistry = new NodeRegistry();
			
			nodeRegistry.addEventListener(NodeRegistryRemovedEvent.REMOVED, nodeRegistryRemovedHandler);				
		}
					
		public function get editorInput():String {
			return _editorInput;
		}
		
		public function set editorInput(editorInput:String):void {
			_editorInput = editorInput;
			CorePlugin.getInstance().resourceNodesManager.nodeRegistryManager.subscribe(editorInput, nodeRegistry, subscribeResultCallback, subscribeFaultCallback);
		}

		protected function subscribeResultCallback(rootNode:Node, resourceNode:Node):void {
			// nothing to do
		}
		
		protected function subscribeFaultCallback(event:FaultEvent):void {
			// close editor
			FlexUtilGlobals.getInstance().workbench.closeView(IEventDispatcher(viewHost), true, true);
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
			for each (var resourceNodeId:Object in CorePlugin.getInstance().resourceNodesManager.nodeRegistryManager.getResourceUrisForNodeRegistry(nodeRegistry)) {
				if (CorePlugin.getInstance().resourceNodesManager.nodeRegistryManager.isResourceNodeDirty(String(resourceNodeId), nodeRegistry)) {
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
			workbench.closeView(workbench.getViewComponentForEditor(this), true, false);		
		}
		
		public function additionalCloseHandler():void {	
			// nothing to do
		}

	}
}
