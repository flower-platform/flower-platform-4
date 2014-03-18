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
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.update.NodeUpdateProcessor;
	import org.flowerplatform.flex_client.core.mindmap.remote.Node;
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ComposedActionProvider;
	import org.flowerplatform.flexutil.action.IAction;
	import org.flowerplatform.flexutil.layout.event.ViewRemovedEvent;
	import org.flowerplatform.flexutil.selection.ISelectionProvider;
	import org.flowerplatform.flexutil.view_content_host.IViewContent;
	import org.flowerplatform.flexutil.view_content_host.IViewHost;
	import org.flowerplatform.flexutil.view_content_host.IViewHostAware;
	
	import spark.components.VGroup;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class EditorFrontend extends VGroup implements IViewContent, IFocusManagerComponent, ISelectionProvider, IViewHostAware {
		
		private var _editorInput:String;
		
		public var actionProvider:ComposedActionProvider = new ComposedActionProvider();;
		
		protected var _viewHost:IViewHost;
		
		public var nodeUpdateProcessor:NodeUpdateProcessor;
		
		public function EditorFrontend() {
			super();
			nodeUpdateProcessor = new NodeUpdateProcessor();
		}
		
		public function get editorInput():String {
			return _editorInput;
		}
		
		public function set editorInput(editorInput:String):void {
			_editorInput = editorInput;
			nodeUpdateProcessor.subscribeToSelfOrParentResource(editorInput, subscribeResultCallback, subscribeFaultCallback);
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
			IEventDispatcher(viewHost).addEventListener(ViewRemovedEvent.VIEW_REMOVED, viewRemovedHandler);
		}
		
		public function get viewHost():IViewHost {
			return _viewHost;
		}
		
		protected function viewRemovedHandler(event:ViewRemovedEvent):void {
			for each (var rootNodeId:String in nodeUpdateProcessor.resourceNodeIds) {
				CorePlugin.getInstance().resourceNodeIdsToNodeUpdateProcessors
					.removeNodeUpdateProcessor(rootNodeId, nodeUpdateProcessor);
			}
		}
	}
}