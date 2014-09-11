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
package org.flowerplatform.flex_client.host_app.mobile.view_content_host {
	import flash.events.Event;
	import flash.events.IEventDispatcher;
	
	import mx.collections.ArrayCollection;
	import mx.collections.ArrayList;
	import mx.collections.IList;
	import mx.core.UIComponent;
	import mx.events.FlexEvent;
	
	import org.flowerplatform.flex_client.core.CoreConstants;
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.IAction;
	import org.flowerplatform.flexutil.layout.IWorkbench;
	import org.flowerplatform.flexutil.layout.LayoutData;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	import org.flowerplatform.flexutil.layout.event.ActiveViewChangedEvent;
	import org.flowerplatform.flexutil.layout.event.ViewRemovedEvent;
	import org.flowerplatform.flexutil.layout.event.ViewsRemovedEvent;
	import org.flowerplatform.flexutil.mobile.view_content_host.split.MobileSplitViewHost;
	import org.flowerplatform.flexutil.view_content_host.IViewContent;
	
	/**
	 * @author Cristian Spiescu
	 * @author Cristina Constantinescu
	 */
	public class WorkbenchMobileSplitViewHost extends MobileSplitViewHost implements IWorkbench {
		
		protected var showOpenEditorsCalloutButton:ShowOpenEditorsCalloutButton;
		
		public var rightComponents:ArrayList = new ArrayList();
		
		public function WorkbenchMobileSplitViewHost() {
			showOpenEditorsCalloutButton = new ShowOpenEditorsCalloutButton();
			showOpenEditorsCalloutButton.visible = false;
			showOpenEditorsCalloutButton.includeInLayout = false;
			addEventListener(FlexEvent.CREATION_COMPLETE, creationCompleteHandler);
			
			FlexUtilGlobals.getInstance().workbench = this;
		}
		
		override protected function appendToActionContent(actionContent:Array):void {
			actionContent.push(showOpenEditorsCalloutButton);
			super.appendToActionContent(actionContent);			
		}
		
		protected function creationCompleteHandler(event:FlexEvent):void {
//			leftActiveComponent = FlexUtilGlobals.getInstance().composedViewProvider.createView(new ViewLayoutData(ExplorerViewProvider.ID));
			
			this.addEventListener(ViewsRemovedEvent.VIEWS_REMOVED, CorePlugin.getInstance().resourceNodesManager.viewsRemovedHandler);
			this.addEventListener(ActiveViewChangedEvent.ACTIVE_VIEW_CHANGED, CorePlugin.getInstance().resourceNodesManager.activeViewChangedHandler);
			
			showOpenEditorsCalloutButton.splitView = this;
			
			CorePlugin.getInstance().handleLinkForCommand(CoreConstants.OPEN_RESOURCES, "virtual:user/repo|root");
		}
		
		public function load(layoutData:Object, reuseExistingViews:Boolean = false, keepNewLayoutEditors:Boolean = false):void {
			// TODO
		}
		
		public function addEditorView(viewLayoutData:ViewLayoutData, setFocusOnView:Boolean=false, existingComponent:UIComponent=null, addViewInOtherStack:Boolean = false):UIComponent {
			var comp:UIComponent = FlexUtilGlobals.getInstance().composedViewProvider.createView(viewLayoutData);
			rightActiveComponent = comp;
			rightComponents.addItem(comp);
			showOpenEditorsCalloutButton.addEditorFrontend(comp, viewLayoutData);
			if (oneViewMode && oneViewModeLeftViewActive) {
				oneViewModeLeftViewActive = false;
			}
			return comp;
		}
		
		public function getAllEditorViews(root:LayoutData, array:ArrayCollection):void {
			for (var i:int = 0; i < rightComponents.length; i++) {
				array.addItem(rightComponents.getItemAt(i));
			}
		}
			
		public function closeView(view:IEventDispatcher, shouldDispatchEvent:Boolean = true, canPreventDefault:Boolean = true):void {
			view.dispatchEvent(new Event("dispose"));
			closeViews(new ArrayCollection([view]), shouldDispatchEvent, canPreventDefault);
		}
		
		public function closeViews(views:ArrayCollection, shouldDispatchEvent:Boolean = true, canPreventDefault:Boolean = true):void {
			var viewsRemovedEvent:ViewsRemovedEvent = new ViewsRemovedEvent(views);			
			if (shouldDispatchEvent) {
				viewsRemovedEvent.canPreventDefault = canPreventDefault;
				dispatchEvent(viewsRemovedEvent);
			}

			for each (var view:UIComponent in views) {
				if (!viewsRemovedEvent.dontRemoveViews.contains(view)) {
					var viewRemovedEvent:ViewRemovedEvent = new ViewRemovedEvent();
					if (shouldDispatchEvent) {
						viewRemovedEvent.canPreventDefault = canPreventDefault;
						view.dispatchEvent(viewRemovedEvent);
					}
					view.dispatchEvent(viewRemovedEvent);
				}				
			}
		}
		
		public function getComponent(viewLayoutData:ViewLayoutData):UIComponent {
			return null;
		}
		
		public function getComponentById(viewId:String, customData:String=null):UIComponent {
			return null;
		}
		
		public function setActiveView(newActiveView:UIComponent, setFocusOnNewView:Boolean = true, dispatchActiveViewChangedEvent:Boolean = true, restoreIfMinimized:Boolean = true):void {
			var oldActiveView:UIComponent = UIComponent(rightActiveComponent);
			
			rightActiveComponent = newActiveView;
			
			if (dispatchActiveViewChangedEvent) {
				var event:ActiveViewChangedEvent = new ActiveViewChangedEvent(newActiveView, oldActiveView); 
				newActiveView.dispatchEvent(event);
				// it seems that an event cannot be redispatched; I had a strange error like: 
				// "Event" cannot be coerced to ActiveViewChangedEvent 
				event = new ActiveViewChangedEvent(newActiveView, oldActiveView); 
				dispatchEvent(event);
			}
		}
		
		public function getActiveView():UIComponent {
			return UIComponent(rightActiveComponent);
		}
		
		public function refreshLabels(viewLayoutData:ViewLayoutData=null):void {	
			showOpenEditorsCalloutButton.refreshLabels();
		}	
		
		public function getEditorFromViewComponent(viewComponent:UIComponent):UIComponent {			
			return viewComponent;
		}
		
		public function getViewComponentForEditor(editor:UIComponent):UIComponent {			
			return editor;
		}
				
		/**
		 * @author Cristina Constantinescu
		 */ 
		override protected function getActionsFromViewContent(viewContent:IViewContent, selection:IList):Vector.<IAction> {
			var result:Vector.<IAction> = super.getActionsFromViewContent(viewContent, selection);
			
			var globalActions:Vector.<IAction> = CorePlugin.getInstance().globalMenuActionProvider.getActions(selection);
			if (globalActions != null) {
				for each (var action:IAction in globalActions) {
					result.push(action);
				}
			}
			return result;
		}
		
		public function moveComponentNearWorkbench(sourceComponent:UIComponent, side:Number):void {
			// do nothing
		}
		
	}
}
