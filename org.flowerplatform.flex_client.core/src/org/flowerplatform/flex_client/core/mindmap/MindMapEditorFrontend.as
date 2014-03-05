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
package org.flowerplatform.flex_client.core.mindmap {
	
	import flash.events.Event;
	import flash.events.IEventDispatcher;
	import flash.events.TimerEvent;
	import flash.utils.Timer;
	
	import mx.collections.IList;
	import mx.events.CollectionEvent;
	import mx.events.CollectionEventKind;
	import mx.events.FlexEvent;
	import mx.managers.IFocusManagerComponent;
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.mindmap.action.AddChildActionProvider;
	import org.flowerplatform.flex_client.core.mindmap.action.ReloadAction;
	import org.flowerplatform.flex_client.core.mindmap.remote.Node;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.DiagramShellAwareProcessor;
	import org.flowerplatform.flexdiagram.renderer.DiagramRenderer;
	import org.flowerplatform.flexdiagram.util.infinitegroup.InfiniteScroller;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ComposedActionProvider;
	import org.flowerplatform.flexutil.action.IAction;
	import org.flowerplatform.flexutil.layout.event.ViewRemovedEvent;
	import org.flowerplatform.flexutil.selection.ISelectionProvider;
	import org.flowerplatform.flexutil.view_content_host.IViewContent;
	import org.flowerplatform.flexutil.view_content_host.IViewHost;
	import org.flowerplatform.flexutil.view_content_host.IViewHostAware;
	
	import spark.components.CheckBox;
	import spark.components.HGroup;
	import spark.components.VGroup;


	/**
	 * @author Cristina Constantinescu
	 */
	public class MindMapEditorFrontend extends VGroup implements IViewContent, IFocusManagerComponent, ISelectionProvider, IViewHostAware {
		
		// TODO MG: temp; move to a super class EditorFrontend
		public var editorInput:String;
		
		public var diagramShell:DiagramShell;
		
		public var actionProvider:ComposedActionProvider;
		
		protected var _viewHost:IViewHost;
		
		/**
		 * Sends request to server each 5 sec to check for new updates.
		 * @see autoRefresh
		 */ 
		protected var autoRefreshTimer:Timer;
			
		public function MindMapEditorFrontend() {
			addEventListener(FlexEvent.CREATION_COMPLETE, creationCompleteHandler);	
			
			actionProvider = new ComposedActionProvider();
			actionProvider.actionProviders.push(CorePlugin.getInstance().mindmapEditorClassFactoryActionProvider);
			actionProvider.actionProviders.push(new AddChildActionProvider());
		}
		
		override protected function createChildren():void {	
			// toolbar
			var toolbarsArea:HGroup = new HGroup();
			toolbarsArea.percentWidth = 100;
			toolbarsArea.verticalAlign = "middle";
			toolbarsArea.gap = 10;
			toolbarsArea.paddingLeft = 5;
			addElement(toolbarsArea);	
									
			// Auto Refresh checkbox
			var ckBox:CheckBox = new CheckBox();
			ckBox.label = CorePlugin.getInstance().getMessage("mindmap.autorefresh");
			ckBox.addEventListener(Event.CHANGE, function(event:Event):void {
				if (ckBox.selected) {
					if (autoRefreshTimer == null) {
						autoRefreshTimer = new Timer(5000);
						autoRefreshTimer.addEventListener(TimerEvent.TIMER, autoRefresh);
					}
					autoRefreshTimer.start();
				} else {
					if (autoRefreshTimer != null && autoRefreshTimer.running) {
						autoRefreshTimer.stop();
					}
				}
			});
			toolbarsArea.addElement(ckBox);
						
			var scroller:InfiniteScroller = new InfiniteScroller();
			scroller.percentWidth = 100;
			scroller.percentHeight = 100;
			addElement(scroller);
			
			var diagramRenderer:DiagramRenderer = new DiagramRenderer();
			diagramRenderer.useGrid = false;		
			scroller.viewport = diagramRenderer;
			diagramRenderer.horizontalScrollPosition = diagramRenderer.verticalScrollPosition = 0;
							
			diagramShell = new MindMapEditorDiagramShell();
			diagramShell.registry = CorePlugin.getInstance().nodeTypeDescriptorRegistry;
			diagramShell.diagramRenderer = diagramRenderer;
			
			actionProvider.composedActionProviderProcessors.push(new DiagramShellAwareProcessor(diagramShell));
			
			super.createChildren();					
		}
		
		private function creationCompleteHandler(event:FlexEvent):void {	
			var root:Node = new Node();
			root.type = "freeplaneNode";
			root.resource = editorInput;
			diagramShell.rootModel = root;
			
			CorePlugin.getInstance().fullRootNodeIdToDiagramShell[root.fullNodeId] = diagramShell;
			
			CorePlugin.getInstance().serviceLocator.invoke("nodeService.subscribe",	[Node(diagramShell.rootModel).fullNodeId]);
			
			// TODO CC: Temporary code
			var reloadAction:ReloadAction = new ReloadAction();
			reloadAction.diagramShellContext = diagramShell.getNewDiagramShellContext();
			reloadAction.run();
			
			diagramShell.selectedItems.addEventListener(CollectionEvent.COLLECTION_CHANGE, selectionChangedHandler);
			
			IEventDispatcher(viewHost).addEventListener(ViewRemovedEvent.VIEW_REMOVED, viewRemovedHandler);
		}
		
		/**
		 * @author Mariana Gheorghe
		 */
		protected function viewRemovedHandler(event:ViewRemovedEvent):void {
			CorePlugin.getInstance().serviceLocator.invoke("nodeService.unsubscribe", [Node(diagramShell.rootModel).fullNodeId]);
		}
		
		protected function selectionChangedHandler(e:CollectionEvent):void {
			// CollectionEvent.COLLECTION_CHANGE will be triggered even when an item is updated (CollectionEventKind.UPDATE), so ignore it
			if (!diagramShell.selectedItems.eventsCanBeIgnored && e.kind != CollectionEventKind.UPDATE) { // catch events only if necessary
				FlexUtilGlobals.getInstance().selectionManager.selectionChanged(viewHost, this);
			}
		}
		
		/**
		 * @author Cristina Constantinescu
		 * @author Mariana Gheorghe
		 */
		public function getActions(selection:IList):Vector.<IAction> {
			return actionProvider.getActions(selection);
		}
		
		public function getSelection():IList {			
			return diagramShell.selectedItems;
		}
		
		public function set viewHost(value:IViewHost):void {
			_viewHost = value;			
		}
		
		public function get viewHost():IViewHost {
			return _viewHost;
		}
						
		private function autoRefresh(event:TimerEvent):void {			
			MindMapEditorDiagramShell(diagramShell).updateProcessor.checkForUpdates();
		}
		
	}
}
