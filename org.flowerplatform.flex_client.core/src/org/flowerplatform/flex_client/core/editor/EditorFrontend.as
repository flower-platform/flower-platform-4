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
	
	import mx.collections.ArrayCollection;
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
	import org.flowerplatform.flexutil.layout.ITitleDecorator;
	import org.flowerplatform.flexutil.layout.event.ViewRemovedEvent;
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
		
		public var actionProvider:ComposedActionProvider = new ComposedActionProvider();;
		
		protected var _viewHost:IViewHost;
		
		public var rootNodeIds:ArrayCollection = new ArrayCollection();
		
		public var updateProcessor:NodeUpdateProcessor;
		
		public function get editorInput():String {
			return _editorInput;
		}
		
		public function set editorInput(editorInput:String):void {
			_editorInput = editorInput;
			CorePlugin.getInstance().serviceLocator.invoke("resourceInfoService.subscribeToParentResource",	[editorInput], subscribeResultCallback, subscribeFaultCallback);
		}
		
		protected function subscribeResultCallback(rootNode:Node):void {
			rootNodeIds.addItem(rootNode.fullNodeId);
			CorePlugin.getInstance().resourceNodesManager.rootNodeIdToEditors.addEditor(rootNode.fullNodeId, this);
		}
		
		protected function subscribeFaultCallback(event:FaultEvent):void {
			FlexUtilGlobals.getInstance().messageBoxFactory.createMessageBox()
			.setText(CorePlugin.getInstance().getMessage("editor.error.subscribe.message", [editorInput]))
			.setTitle(CorePlugin.getInstance().getMessage("editor.error.subscribe.title"))
			.setWidth(300)
			.setHeight(200)
			.showMessageBox();
			
			// close editor
			FlexUtilGlobals.getInstance().workbench.closeView(IEventDispatcher(viewHost));
		}
		
		public function getContext():DiagramShellContext {
			throw new Error("Must provide a context!");
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
		
		public function isDirty():Boolean {	
			return true;
		}
		
		public function decorateTitle(title:String):String {
			if (isDirty()) { 
				return "* " + title;
			}
			return title;
		}
	}
}