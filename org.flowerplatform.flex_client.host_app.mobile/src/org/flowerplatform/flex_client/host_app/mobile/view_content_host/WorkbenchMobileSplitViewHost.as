package org.flowerplatform.flex_client.host_app.mobile.view_content_host {
	import flash.events.IEventDispatcher;
	
	import mx.collections.ArrayCollection;
	import mx.collections.ArrayList;
	import mx.core.UIComponent;
	import mx.events.FlexEvent;
	
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.layout.IWorkbench;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	import org.flowerplatform.flexutil.layout.event.ViewRemovedEvent;
	import org.flowerplatform.flexutil.layout.event.ViewsRemovedEvent;
	import org.flowerplatform.flexutil.mobile.view_content_host.split.MobileSplitViewHost;
	
	/**
	 * @author Cristian Spiescu
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
			
			FlexUtilGlobals.getInstance().workbench = this;
//			this.addEventListener(ViewsRemovedEvent.VIEWS_REMOVED, EditorPlugin.getInstance().globalEditorOperationsManager.viewsRemovedHandler);
			
			showOpenEditorsCalloutButton.splitView = this;
		}
		
		public function load(layoutData:Object, reuseExistingViews:Boolean = false, keepNewLayoutEditors:Boolean = false):void {
			// TODO
		}
		
		public function addEditorView(viewLayoutData:ViewLayoutData, setFocusOnView:Boolean=false, existingComponent:UIComponent=null):UIComponent {
			var comp:UIComponent = FlexUtilGlobals.getInstance().composedViewProvider.createView(viewLayoutData);
			rightActiveComponent = comp;
			rightComponents.addItem(comp);
			showOpenEditorsCalloutButton.addEditorFrontend(comp);
			if (oneViewMode && oneViewModeLeftViewActive) {
				oneViewModeLeftViewActive = false;
			}
			return comp;
		}
		
		public function closeView(view:IEventDispatcher, shouldDispatchEvent:Boolean=true):void {
			closeViews(new ArrayCollection([view]), shouldDispatchEvent);
		}
		
		public function closeViews(views:ArrayCollection, shouldDispatchEvent:Boolean=true):void {
			var viewsRemovedEvent:ViewsRemovedEvent = new ViewsRemovedEvent(views);			
			if (shouldDispatchEvent) {
				dispatchEvent(viewsRemovedEvent);
			}
			for each (var view:UIComponent in views) {
				if (!viewsRemovedEvent.dontRemoveViews.contains(view)) {
					var viewRemovedEvent:ViewRemovedEvent = new ViewRemovedEvent();
					if (shouldDispatchEvent) {
						view.dispatchEvent(viewRemovedEvent);
					}
				}				
			}
		}
		
		public function getComponent(viewId:String, customData:String=null):UIComponent {
			return null;
		}
		
		public function setActiveView(newActiveView:UIComponent, setFocusOnNewView:Boolean = true, dispatchActiveViewChangedEvent:Boolean = true, restoreIfMinimized:Boolean = true):void {
			rightActiveComponent = newActiveView;
		}
	}
}