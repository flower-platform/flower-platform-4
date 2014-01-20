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
package com.crispico.flower.util.layout {
	import com.crispico.flower.util.layout.event.ActiveViewChangedEvent;
	
	import flash.geom.Point;
	import flash.geom.Rectangle;
	
	import mx.collections.ArrayList;
	import mx.collections.IList;
	import mx.containers.Canvas;
	import mx.containers.VBox;
	import mx.core.FlexGlobals;
	import mx.core.UIComponent;
	
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionUtil;
	import org.flowerplatform.flexutil.action.IAction;
	import org.flowerplatform.flexutil.context_menu.ContextMenu;
	import org.flowerplatform.flexutil.context_menu.FillContextMenuEvent;
	import org.flowerplatform.flexutil.layout.event.ViewRemovedEvent;
	import org.flowerplatform.flexutil.selection.ISelectionForServerProvider;
	import org.flowerplatform.flexutil.selection.ISelectionProvider;
	import org.flowerplatform.flexutil.view_content_host.IViewContent;
	import org.flowerplatform.flexutil.view_content_host.IViewHost;
	
	import spark.components.Button;
	import spark.components.HGroup;
	
	/**
	 * There is an issue with NavigatorContent: https://github.com/flex-users/flexlib/issues/301
	 * so we use Canvas instead of a NavigatorContent that should wrap spark components.
	 * 
	 * @author Cristian Spiescu
	 */
	[DefaultProperty("activeViewContent")]
	public class WorkbenchViewHost extends VBox implements IViewHost {
		
		public var allActions:Vector.<IAction>
		
		public var selection:IList;
		
		protected var rootActionsAlreadyCalculated:ArrayList;
		
		/**		
		 * @author Cristina Constantinescu
		 */
		public var contextForActions:Object;
		
		protected var buttonBar:HGroup;
		
		protected var _viewContent:IViewContent;

		public function get activeViewContent():IViewContent {
			return _viewContent;
		}

		public function set activeViewContent(value:IViewContent):void {
			setActiveViewContent(value);
		}
		
		public function setActiveViewContent(value:IViewContent, viaFocusIn:Boolean = false):void {
			if (value == null) {
				return;
			}
			if (_viewContent != null) {
				throw new Error("Illegal usage. This setter can be called only once!");
			}
			_viewContent = value;
			activeViewContent.percentHeight = 100;
			activeViewContent.percentWidth = 100;
			activeViewContent.viewHost = this;
			FlexUtilGlobals.getInstance().selectionManager.viewContentActivated(this, activeViewContent, false);
		}
		
		public function WorkbenchViewHost(viewContent:IViewContent = null) {
			super();
			percentHeight = 100;
			percentWidth = 100;
			addEventListener(FillContextMenuEvent.FILL_CONTEXT_MENU, fillContextMenuHandler); 
			setStyle("verticalGap", 0);
			setActiveViewContent(viewContent);
			addEventListener(ActiveViewChangedEvent.ACTIVE_VIEW_CHANGED, viewActivatedHandler);
			addEventListener(ViewRemovedEvent.VIEW_REMOVED, viewRemovedHandler);
		}
		
		protected function viewActivatedHandler(event:ActiveViewChangedEvent):void {
			FlexUtilGlobals.getInstance().selectionManager.viewContentActivated(this, activeViewContent, true);
		}
		
		protected function viewRemovedHandler(event:ViewRemovedEvent):void {
			FlexUtilGlobals.getInstance().selectionManager.viewContentRemoved(this, activeViewContent);
		}
		
		override protected function createChildren():void {
			if (activeViewContent == null) {
				throw new Error("Illegal state. The viewContent shouldn't be null.");
			}
			super.createChildren();
			buttonBar = new HGroup();
			buttonBar.percentWidth = 100;
			buttonBar.gap = 1;
			buttonBar.paddingTop = 2;
			buttonBar.paddingBottom = 2;
			buttonBar.paddingLeft = 2;
			buttonBar.paddingRight = 2;
			buttonBar.horizontalAlign = "right";
			buttonBar.height = 24;
			addChild(buttonBar);
			
			addElement(activeViewContent);
		}
		
		protected function fillContextMenuHandler(event:FillContextMenuEvent):void {
			event.allActions = allActions;
			event.selection = selection;
			event.rootActionsAlreadyCalculated = rootActionsAlreadyCalculated;
						
			contextForActions = new Object();			
			contextForActions.rectangle = new Rectangle(
				UIComponent(FlexGlobals.topLevelApplication).mouseX, 
				UIComponent(FlexGlobals.topLevelApplication).mouseY, 
				NaN, NaN);
			event.context = contextForActions;
		}
		
		/**
		 * Fills the action bar with the corresponding actions and caches the selection and
		 * actions, to be able to provide it, if right click menu is caught.
		 */
		public function selectionChanged():IList {
			var viewContent:IViewContent = activeViewContent;
			
			if (!(viewContent is ISelectionProvider) || buttonBar == null) {
				// testing buttonBar because this call may happen very quickly, when
				// the children are not yet created
				return null;
			}
			
			selection = ISelectionProvider(viewContent).getSelection();
			allActions = viewContent.getActions(selection);
			contextForActions = null;
			
			buttonBar.removeAllElements();
			rootActionsAlreadyCalculated = new ArrayList();
			ActionUtil.processAndIterateActions(null, allActions, selection, contextForActions, this, function (action:IAction):void {
				if (action.preferShowOnActionBar) {
					var actionButton:ActionButton = new ActionButton();
					if (action.label == null) {
						actionButton.width = 22;
						actionButton.height = 22;
					} else {
						actionButton.label = action.label;
					}
					actionButton.setStyle("icon", FlexUtilGlobals.getInstance().adjustImageBeforeDisplaying(action.icon));
					actionButton.action = action;
					actionButton.viewWrapper = this;
					buttonBar.addElement(actionButton);
				} else {
					rootActionsAlreadyCalculated.addItem(action);
				}
			});
			return selection;
		}
		
		public function setLabel(value:String):void
		{
		}
		
		public function setIcon(value:Object):void
		{
		}
		
		/**
		 * @author Mariana
		 */
		public function displayCloseButton(value:Boolean):void {
		}
		
		/**
		 * @author Mariana
		 */
		public function addToControlBar(value:Object):void {
		}
		
		/**
		 * @author Cristina Constantinescu
		 */
		public function openMenu(x:Number, y:Number, context:Object, parentActionId:String = null):Boolean {
			// merge with viewHost contextForActions
			if (contextForActions == null) {
				contextForActions = context;
			} else {				
				for (var key:String in context) {
					contextForActions[key] = context[key];
				}
			}	
			
			// open menu
			return FlexUtilGlobals.getInstance().contextMenuManager.openContextMenu(x, y, allActions, null, parentActionId, selection, contextForActions);
		}	
		
		/**
		 * @author Cristina Constantinescu
		 */
		public function showSpinner(text:String):void {			
		}
		
		/**
		 * @author Cristina Constantinescu
		 */
		public function hideSpinner():void {			
		}
		
		public function getCachedActions():Vector.<IAction> {		
			return allActions;
		}
		
		public function getCachedSelection():IList {			
			return selection;
		}
	}
}
