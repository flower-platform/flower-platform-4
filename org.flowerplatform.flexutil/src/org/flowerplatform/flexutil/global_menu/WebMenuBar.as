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
package org.flowerplatform.flexutil.global_menu {
	import flash.display.DisplayObject;
	import flash.events.Event;
	import flash.events.KeyboardEvent;
	import flash.geom.Point;
	import flash.geom.Rectangle;
	import flash.ui.Keyboard;
	import flash.utils.clearTimeout;
	import flash.utils.setTimeout;
	
	import mx.collections.ArrayCollection;
	import mx.collections.IList;
	import mx.controls.Menu;
	import mx.controls.MenuBar;
	import mx.controls.menuClasses.IMenuBarItemRenderer;
	import mx.controls.menuClasses.MenuBarItem;
	import mx.core.ClassFactory;
	import mx.core.IFactory;
	import mx.core.LayoutDirection;
	import mx.events.MenuEvent;
	import mx.managers.ISystemManager;
	
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionUtil;
	import org.flowerplatform.flexutil.action.IAction;
	import org.flowerplatform.flexutil.action.IActionProvider;
	import org.flowerplatform.flexutil.selection.SelectionChangedEvent;
	
	/**
	 * Extends the existing MenuBar so it can use an actionProvider.
	 * 
	 * @author Mircea Negreanu
	 */
	public class WebMenuBar extends MenuBar {
		
		protected var _actionProvider:IActionProvider;
		
		/**
		 *  currently shown menu 
		 */
		protected var menu:Menu;
		
		/**
		 *  after the submenu is closed the menuBar needs to be reset
		 */
		protected var resetMenuBarOnMenuClosingTimout:int = 0;
		
		/**
		 * Menu descriptor for the popup menus.
		 */
		protected var menuDescriptor:WebMenuDataDescriptor;
		
		/**
		 * Item renderer for the menu items so that the icon is correctly rendered.
		 */
		protected var menuItemRender:IFactory;
		
		public function WebMenuBar(ap:IActionProvider = null):void {
			super();
			
			// Prepare the item renderers
			menuBarItemRenderer = new ClassFactory(WebMenuBarItem);
			menuItemRender = new ClassFactory(WebMenuItemRenderer);
			
			actionProvider = ap;
			
			// Since we do this dynamically, we need the menuBar to tell us
			// when it thinks the menu should be shown/hidden
			addEventListener(MenuEvent.MENU_SHOW, menu_showHandler);
			addEventListener(MenuEvent.MENU_HIDE, menu_hideHandler);
			
			// put a listener on the selection so we can update the menuBar when selection changes
			FlexUtilGlobals.getInstance().selectionManager.addEventListener(
				SelectionChangedEvent.SELECTION_CHANGED,
				selectionChangedHandler
			);

		}
		
		public function get actionProvider():IActionProvider {
			return _actionProvider;
		}
		
		public function set actionProvider(ap:IActionProvider):void {
			_actionProvider = ap;
		
			// also build the menuBar
			if (actionProvider != null) {
				var selection:IList = null;
				// obtaining the selection can give us a NPE so put it in a try/catch block
				try {
					selection = FlexUtilGlobals.getInstance().selectionManager.activeSelectionProvider.getSelection();
				} catch (e:Error) {
					
				}
				
				// Also create the new menuDescriptor based on the new actionProvider
				menuDescriptor = new WebMenuDataDescriptor(actionProvider);
				
				dataProvider = getMenusList(selection, null);
			}
		}
		
		/**
		 * Called when the selection changes.
		 * <p>It obtains the current list of actions for the menu based on the curent selection
		 * and regenerates the menu bar if the number, order of actions or the actions themselves are
		 * different than the current actions on the menu bar</p>
		 * <p>If the actions are the same, just updates the selection on the current actions and
		 * asks the menu to update itself</p>
		 */
		protected function selectionChangedHandler(event:SelectionChangedEvent):void {
			// no sense in continuing if no actionProvider
			if (actionProvider == null) {
				return;
			}
			// get the current list of actions based on the selection
			// if it is different from the current list of actions (in menu)
			// regenerate the menu
			var menuActions:ArrayCollection = getMenusList(event.selection, null);
			if (menuActions.length != menuBarItems.length) {
				// different actions, regenerate the menu
				dataProvider = menuActions;
				return;
			}
			for (var i:int = 0; i < menuActions.length; i++) {
				if (menuActions[i] != ((menuBarItems[i] as IMenuBarItemRenderer).data as IAction)) {
					// different action, regenerate the menu
					dataProvider = menuActions;
					return;
				}
			}
			
			// update the selection for the actions in the menuBar
			for each (var item:IMenuBarItemRenderer in menuBarItems) {
				var action:IAction = item.data as IAction;
				action.selection = event.selection;
				item.enabled = action.enabled;
				
				MenuBarItem(item).invalidateProperties();
				MenuBarItem(item).invalidateSize();
			}
		}
		
		/**
		 * Utility function to build the list for menubar from the actionProvider.
		 * <p>Also sets the selection on the action so that the label/update/icon is the correct one</p>
		 */ 
		protected function getMenusList(selection:IList, parentId:String = null):ArrayCollection {
			if (actionProvider != null) {
				var listActions:ArrayCollection = new ArrayCollection();
				
				ActionUtil.processAndIterateActions(parentId, 
					actionProvider.getActions(selection),
					selection,
					null,
					this,
					function(action:IAction):void {
						listActions.addItem(action);
					}
				);
				
				// force the selection so we can have the correct labels
				for each (var action:IAction in listActions) {
					action.selection = selection;
				}
				
				return listActions;
			}
			
			return null;
		}
		
		/**
		 * Compose the childMenu (based on the action provider and the selected menuBarItem) and shows it
		 */
		protected function menu_showHandler(event:MenuEvent):void {
			// callLater because we need the handler to bring 
			// back control to the caller
			callLater(buildAndShowMenu);
		}
		
		/**
		 * Called when the current menu needs to hide.
		 */
		protected function menu_hideHandler(event:MenuEvent):void {
			hideMenu();
		}
		
		/**
		 * Builds the child menu based on the current selected menubar
		 */
		protected function buildAndShowMenu():void {
			if (menu != null) {
				// we have a menu displayed -> hide it
				hideMenu();
			}
			
			if (actionProvider != null) {
				// get the current selected action
				var item:IMenuBarItemRenderer = menuBarItems[selectedIndex];
				var menuBarAction:IAction = item.data as IAction;
				
				// Only a composed action can have children
				if (ActionUtil.isComposedAction(menuBarAction)) {
					// get the current selection
					var selection:IList = FlexUtilGlobals.getInstance().selectionManager.activeSelectionProvider.getSelection();
					
					// get the list of actions of the current menu
					menuDescriptor.selection = selection;
					var childMenuList:ArrayCollection = ArrayCollection(menuDescriptor.getChildren(menuBarAction));
					
					// create and show the menu
					menu = Menu.createMenu(this, childMenuList, false);
					menu.labelField = this.labelField;
					menu.iconField = this.iconField;
					
					var menuStyle:Object = getStyle("menuStyleName");
					if (menuStyle != null) {
						menu.styleName = menuStyle;
					}
					
					menu.addEventListener(MenuEvent.MENU_HIDE, childMenu_eventHandler);
					menu.addEventListener(MenuEvent.MENU_SHOW, childMenu_eventHandler);
					// on click execute the action
					menu.addEventListener(MenuEvent.ITEM_CLICK, childMenu_itemClickHandler);
					menu.addEventListener(KeyboardEvent.KEY_DOWN, childMenu_eventHandler);
					
					// set the menuDescriptor
					menuDescriptor.selection = selection;
					menu.dataDescriptor = menuDescriptor;
					
					// set the renderer
					menu.itemRenderer = menuItemRender;
					
					// get the VisibleApplicationRect so we can correctly display the menu
					// if it will try to get outside.
					var sm:ISystemManager = systemManager.topLevelSystemManager;
					var screen:Rectangle = sm.getVisibleApplicationRect(null, true);
					
					var pt:Point = new Point(0, 0);
					pt = (menuBarItems[selectedIndex] as DisplayObject).localToGlobal(pt);
					pt.y += menuBarItems[selectedIndex].height + 1;

					// Because of the rtl fix below, we need that menu to not have any animation
					if (layoutDirection == LayoutDirection.RTL) {
						menu.setStyle("openDuration", 0);
					}
					
					// show the menu so we can have its ExplicitOrMeasuredWidth and ExplicitOrMeasuredHeight
					// so we can check if it is inside or outside the visible screen, and to apply the 
					// correction for RTL
					menu.show(pt.x, pt.y);
					
					// if RTL make it shown from the right
					if (layoutDirection == LayoutDirection.RTL) {
						pt.x -= menu.getExplicitOrMeasuredWidth();
					}
					
					// check to see if we'll go offscreen
					if (pt.x + menu.getExplicitOrMeasuredWidth() > screen.x + screen.width) {
						pt.x = screen.width - menu.getExplicitOrMeasuredWidth();
					}
					pt.x = Math.max(pt.x, screen.x);
					
					if (pt.y + menu.getExplicitOrMeasuredHeight() > screen.height + screen.y) {
						pt.y -= menu.getExplicitOrMeasuredHeight();
					}
					pt.y = Math.max(pt.y, screen.y);
					
					// now move the menu to the correct position (if needed)
					if (menu.x != pt.x || menu.y != pt.y) {
						menu.move(pt.x, pt.y);
					}
					
					//tell the current barItem to be in down status and not hover
					menuBarItems[selectedIndex].menuBarItemState = "itemDownSkin";
				}
			}
		}
		
		/**
		 * Hides the currently shown menu (if any)
		 */
		protected function hideMenu():void {
			if (menu != null) {
				menu.hide();
				
				// also clean the selection from the submenus
				if (menu.dataDescriptor != null && menu.dataDescriptor is WebMenuDataDescriptor) {
					WebMenuDataDescriptor(menu.dataDescriptor).clearSelectionFromChildren();
				}
				
				menu = null;
			}
		}
		
		/**
		 * Process events sent by the childMenu:
		 * <ul>
		 * 	<li>MENU_HIDE - start a timeout to reset the look of the menuBar 
		 * 		and the selectedIndex (otherwise it will look selected forever)</li> 
		 * 	<li>MENU_SHOW - cancel the timeout if the user selected another 
		 * 		menu at a short time after the previouse was closed</li>
		 * 	<li>KEY_DOWN - need to be sent to the parent menuBar (to implement left 
		 * 		and right). Also on right, if we are on a selection that can be expanded, don't
		 * 		send anything to the parent - it needs to be interpreted by the menu and not menuBar
		 *  </li>
		 * </ul>
		 */
		protected function childMenu_eventHandler(event:Event):void {
			if (event is MenuEvent) {
				var menuEvent:MenuEvent = event as MenuEvent;
				if (menuEvent != null && menuEvent.menu != null && menuEvent.menu.parentMenu == null) {
					if (menuEvent.type == MenuEvent.MENU_HIDE) {
						// if the timeout is already in process, clean it
						if (resetMenuBarOnMenuClosingTimout != 0) {
							clearTimeout(resetMenuBarOnMenuClosingTimout);
						}
						
						// start the timeout to reset the look of the selected menuBar
						resetMenuBarOnMenuClosingTimout = setTimeout(function():void {
							if (selectedIndex != -1) {
								menuBarItems[selectedIndex].menuBarItemState = "itemUpSkin";
								selectedIndex = -1;
								
								// also clear the current menu
								hideMenu();
							}
						}, 50);
					} else if (menuEvent.type == MenuEvent.MENU_SHOW) {
						// we have another menu that just opened, immediatly after the first
						// one was closed -> stop the closing timeout
						if (resetMenuBarOnMenuClosingTimout != 0) {
							clearTimeout(resetMenuBarOnMenuClosingTimout);
						}
					}
				}
				
			} else if (event is KeyboardEvent) {
				var keyEvent:KeyboardEvent = event as KeyboardEvent;
				var keyCode:uint = keyEvent.keyCode;
				
				// take rtl into account
				if (layoutDirection == LayoutDirection.RTL) {
					if (keyCode == Keyboard.LEFT) {
						keyCode = Keyboard.RIGHT; 
					} else if (keyCode == Keyboard.RIGHT) {
						keyCode = Keyboard.LEFT;
					}
				}
				
				// dont notify parent if RIGHT and we are on an expandable menu
				// so that the menu has a chance to expand itself
				if (!(keyCode == Keyboard.RIGHT 
					&& ActionUtil.isComposedAction(IAction(Menu(event.target).selectedItem)))) {
					dispatchEvent(event);
				}
			}
		}
		
		/**
		 * Clicking on a menu (simple action -> no submenu) will execute
		 * the action.
		 */
		protected function childMenu_itemClickHandler(event:MenuEvent):void {
			var action:IAction = event.item as IAction;
			
			if (action != null) {
				action.run();
			}
		}
	}
}