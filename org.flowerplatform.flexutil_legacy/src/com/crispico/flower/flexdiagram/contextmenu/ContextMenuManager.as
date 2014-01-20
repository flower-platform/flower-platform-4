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
package  com.crispico.flower.flexdiagram.contextmenu {
	import com.crispico.flower.flexdiagram.action.IActionProvider2;
	
	import flash.display.DisplayObject;
	import flash.display.Stage;
	import flash.events.ContextMenuEvent;
	import flash.events.Event;
	import flash.events.MouseEvent;
	import flash.events.TimerEvent;
	import flash.geom.Point;
	import flash.geom.Rectangle;
	import flash.ui.ContextMenu;
	import flash.ui.ContextMenuItem;
	import flash.utils.Dictionary;
	import flash.utils.Timer;
	
	import mx.automation.IAutomationObject;
	import mx.collections.ArrayCollection;
	import mx.controls.ColorPicker;
	import mx.core.UIComponent;
	import mx.managers.IFocusManagerComponent;
	
	/**
	 * This class represents the Context Menu framework and can be used to : 
	 * <ul>
	 * 	<li> register client viewers and unregister them;
	 * 	<li> notify it about client changes regarding refresh needed, repositioning needed or disabling/enabling needed;
	 * </ul>
	 * 
	 * This works in conjunction with <code>IContextMenuLogicProvider</code> and with <code>IActionProvider</code> in the following way:
	 * <ul>
	 * 	<li> when going with the mouse over a registerd client viewer it asks the provider where is the location
	 * 		to show the context menu, if it can show it, and asks for the content of the context menu;
	 * 	<li> when going with the mouse out of an registered client viewers it closes it's context menu associated.
	 * </ul>
	 * @author Sorin
	 * 
	 */
	public class ContextMenuManager {
		
		public static const INSTANCE:ContextMenuManager = new ContextMenuManager();
		
		private static const SHOW_CONTEXT_MENU_NOLABEL_ENTRY:String = "<no label>";
		
		private static const RIGHT_CLICK_CM:String = "rightClickCM";
		
		private static const MOUSE_OVER_SELECTION_CM:String = "leftClickCM";
		
		/**
		 * Default close menu delay.
		 * Used by <code>closeMenuTimer</code> if the client hasn't <code>closeMenuDelay</code> set.
		 * @author Cristina		 
		 */ 
		private static const CLOSE_MENU_DELAY_DEFAULT:int = 900; //ms				
		
		/**
		 * Map from a client notifier to a ClientNotifierData.
		 * 
		 */ 
		public var notifierToData:Dictionary = new Dictionary();
		
		/**
		 * Map from a viewer to a notifier, needed to determine from target the notifier.
		 */
		public var viewerToNotifier:Dictionary = new Dictionary();
		
		/**
		 * This field holds the active Viewer registered if the mouse's cursor is over one.
		 */ 		
		private var activeViewer:UIComponent;
		
		/**
		 * This field holds the active Context Menu (if one is showed) and it is associated to the active Client viewer.
		 * This is updated when :
		 * <ul>
		 * 	<li> hiding or showing as a result of changing the context menu disabledness state for a client viewer;
		 * 	<li> hiding or showing as a result of moving the mouse's cursor out of or in a the client viewer;
		 * </ul>
		 */
		private var activeContextMenu:FlowerContextMenu;
		
		
		/**
		 * This indicates the type of the active Context Menu.
		 * It can have one of the values: RIGHT_CLICK_CM or MOUSE_OVER_SELECTION_CM
		 * Based on how this context menu was showed (on right click or on mouse move over selection)
		 */ 
		
		private var activeContextMenuType:String;
		
		/**
		 * Variable used in case of a activeViewer with a rightClickEnabled.
		 * It stores the position of the CM relative to the content of the activeViewer.
		 * Its value is update every time a new right click CM apears (i.e. on a right click)
		 * We need to keep this position relative to content of the activeViewer in order to
		 * update the position of the CM when a scroll of a zoom happens.
		 * 
		 * 
		 * @see #updatePosition() 
		 */  
		private var rightClickContentPosition:Point;

		/**
		 * Used by to cache the target by the mouse on stage listener in order to optimize
		 * the processing for finding a registered client viewer.
		 */ 
		private var cachedTarget:Object;
		
		private var cachedContextMenus:ArrayCollection;
		
		private var cachedNoResizeContextMenus:ArrayCollection;
		
		private var newContextMenuCounter:Number = 0;
		
		private var newNoResizeContextMenuCounter:Number = 0;
		
		/**
		 * Stores the last mouse event.
		 * Used to know where the Flower CM must appear when selecting 
		 * the corresponding menu item from Flash CM.
		 * 
		 * @author Cristina
		 */ 	
		private var lastMouseEvent:MouseEvent;
				
		/**
		 * Timer used for keeping visible a certain time the main context menu, even if the mouse is not over it.
		 */ 		 
		private var closeMenuTimer:Timer;			
		
		/**
		 * @see getter
		 */ 
		private var _closeMenuDelay:Number = CLOSE_MENU_DELAY_DEFAULT;
		
		/**
		 * @see getter
		 */ 
		private var _expandMenuDelay:Number;
		
		/**
		 * @see getter
		 */ 
		private var _accelerateExpandMenuDelay:Number;
		
		/**
		 * Variable that indicates if the last mouse click event
		 * happend on the active viewer was a right click one.
		 * 
		 * <p>
		 * It is used for a client that it is managed with a 
		 * context menu enabled on right click logic.
		 */   
		private var _rightClickEventHappend:Boolean = false;
		
		/**
		 * If we have a CM with a mouse over selection logic of apeareance, the CM manager
		 * triggers the closing of the CM on mouse move or mouse over viewer.
		 * If this variable is setted to false the closing of the CM is disabled
		 * by disabling the actions from the <code>mouseStageHandler</code>
		 * 
		 * For now the default value it is modified only if the activeContextMenu tells so to
		 * the CM manager by throwing the "enableAuthomaticClosingMenu" or the "disableAuthomaticClosingMenu"
		 */ 
		public var authomaticClosingMenuDisabled:Boolean = false;
		
		/**
		 * Responsible for:
		 * <ul>
		 * 	<li> creating the list of cached context menus
		 * </ul>
		 * 
		 */
		public function ContextMenuManager() {
			if (INSTANCE != null)
				throw "This Singleton class cannot be instanciated.";			
			
			// Two context menu's are instanciated in order to switch them for having an fade out effect and a fade in effect on the screen in the same time.
			var first:FlowerContextMenu = new FlowerContextMenu();
			first.afterActionExecutedFunction = afterActionExecutedHandler;						
			var second:FlowerContextMenu = new FlowerContextMenu();
			second.afterActionExecutedFunction = afterActionExecutedHandler;
						
			this.cachedContextMenus = new ArrayCollection([first, second]);
			
			var firstNoAutoResize:FlowerContextMenu = new NoAutoResizeFlowerContextMenu();
			first.afterActionExecutedFunction = afterActionExecutedHandler;						
			var secondNoAutoResize:FlowerContextMenu = new NoAutoResizeFlowerContextMenu();
			second.afterActionExecutedFunction = afterActionExecutedHandler;
				
			this.cachedNoResizeContextMenus = new ArrayCollection([firstNoAutoResize, secondNoAutoResize]);
			
			closeMenuTimer = new Timer(CLOSE_MENU_DELAY_DEFAULT, 1);
			closeMenuTimer.addEventListener(TimerEvent.TIMER_COMPLETE, closeMenuTimerHandler);				
		}
		
		/**
		 * Delay used before closing the main Flower CM.
		 * @author Cristina
		 */		 
		public function get closeMenuDelay():Number {
			return _closeMenuDelay;
		}		
		
		public function set closeMenuDelay(value:Number):void {
			_closeMenuDelay = value;	
			closeMenuTimer.delay = value;
		}
		
		/**
		 * Delay used before expanding the main Flower CM.
		 * @author Cristina
		 */	
		public function get expandMenuDelay():Number {
			return _expandMenuDelay;
		}
		
		public function set expandMenuDelay(value:Number):void {
			_expandMenuDelay = value;
			for each (var contextMenu:FlowerContextMenu in cachedContextMenus) {
				contextMenu.expandDelay = value;
			}		
		}
		
		/**
		 * Delay used to accelerate the expanding of Flower CM.
		 * The acceleration is done when the mouse is moving in a small area (square with the side length 5 pixels).
		 * @author Cristina
		 */	
		public function get accelerateExpandMenuDelay():Number {
			return _accelerateExpandMenuDelay;
		}
		
		public function set accelerateExpandMenuDelay(value:Number):void {
			_accelerateExpandMenuDelay = value;
			for each (var contextMenu:FlowerContextMenu in cachedContextMenus) {
				contextMenu.accelerateExpandDelay = value;
			}
		}
		
		/**
		 * This method registers a client viewer, so that the CM framework can detect mouse cursor's out/over the viewer area and the client to be able
		 * to notify about specific changes so that the CM framework to update the associated Context Menu.
		 * 
		 * <p> This method is responsable for updating the map and adding mouse event listeners to the client viewer.
		 * 
		 * <p> Parameters needed in order to register a client:
		 * <ul>
		 * 	<li> viewer - the graphical container when the mouse cursor is over, the associated context menu is shown;
		 * 	<li> useWholeScreen - if the positioning of the main context menu can exceed the bounds of the viewer; default is true
		 *  <li> logicprovider - provider for the display area of the graphical selected elements and provider for the selection needed by the context menu when
		 * 			executing an action of a menu entry; by default it is considered that the viewer provides the logicprovider;
		 * 	<li> actionprovider - provider for the method that fills the context menu with menu entries for the actions that are available; by default
		 * 			it is considered that the viewer is the provider of the actions
		 * 	<li> beforeFillContextMenuFunction - a function that is called before this framework calls the actual fillContextMenu function of the actionprovider.
		 * 	<li> beforeShowContextMenuFromFlashFunction - a function that is called before this framework shows the Flash Context Menu.	
		 * 		<blockquote>
		 * 			<code>beforeShowContextMenuFromFlashFunction(point:Point):Boolean</code>
		 *		</blockquote>
		 * 		Returns <code>true</code> if the entry must be added to Flash Context Menu, otherwise returns <code>false</code>.	
		 * <li> notifier - the object that can be passed as argument when notifing about refreshing, position, enabling changed; by default it is considered
		 * 			the graphical area (viewer);
		 * <li> flashHandler - provider for the method that shows the Flower CM when selecting the corresponding menu item from Flash CM;
		 * <li> minContextMenuWidth - represents the minimum width of CM when it is minimized. By default it is 25 (FlowercontextMenu.MINIMUM_WIDTH).
		 * <li> initialContextMenuEnabled - indicates whether or not the CM should be enabled at the time when the client is registered.  
		 * 
		 */
		public function registerClient(viewer:UIComponent, useWholeScreen:Boolean=true, logicProvider:IContextMenuLogicProvider=null, actionProviver:IActionProvider2=null, beforeFillContextMenuFunction:Function = null, beforeShowContextMenuOnFlashFunction:Function=null, contextMenuEntryLabelFunction:Function=null, notifier:Object=null, rightClickEnabled:Boolean = false, minContextMenuWidth:Number=25):ClientNotifierData {
			
			// If the logic provider is not given (null) then the viewer itself it is to be considered that it is the logic provider
			if (logicProvider == null) {
				logicProvider = IContextMenuLogicProvider(viewer);
			}
			// if the action provider is not given (null) then the viewer itself it is considererd to be the action provider
			if (actionProviver == null) {
				actionProviver = IActionProvider2(viewer);
			} 
			// if the notifier is not given (null) then the viewer itself will be accepted as notifier
			if (notifier == null) {
				notifier = viewer;
			}
						
			notifierToData[notifier] = new ClientNotifierData(viewer, actionProviver, beforeFillContextMenuFunction, 
											beforeShowContextMenuOnFlashFunction, contextMenuEntryLabelFunction, 
											logicProvider, useWholeScreen, minContextMenuWidth, rightClickEnabled);
			viewerToNotifier[viewer] = notifier;
			
			// When registering a client viewer, it may be possible that it is not added to the stage at this moment
			// so if it is added we add our mouse listeners to the stage, and if not we add a listeners for when it 
			// will be added to the stage in order to execute later out adding of the listeners to the stage.	
			if (viewer.stage != null) {
				delayedListenersAdder(viewer);
			} else {
				viewer.addEventListener(Event.ADDED_TO_STAGE, viewerAddedToStageHandler);
			}
			
			return notifierToData[notifier];	
		}
		
		protected function rightClickLitenersAdder(viewer:UIComponent):void {
			//Because the right click it could be also handled by the client
			//for adding/removing elements from selection we need the right click
			//handler to be called after the client handlers in order to have access 
			//to the latest selection in the <code>mouseClickHandler()</code> 
			//viewer.stage.addEventListener("rightClick", mouseClickHandler, false, Number.MIN_VALUE);
			
			//any client that register to the viewer it have to set a positive 
			//priority for its right click handlers that manage the selection
			viewer.stage.addEventListener("rightClick", mouseClickHandler, false, -1);
			
			// The event listener was changed from CLICK to MOUSE_DOWN because the click event
			// could not be catched when a superTab component was clicked causing the folowing problem:
			// When a RightClick CM was showed and we click to switch tabs the context menu didn't disappear
			viewer.stage.addEventListener(MouseEvent.MOUSE_DOWN, mouseClickHandler, false, 10000);
		}
		
		protected function rightClickLitenersRemover(viewer:UIComponent):void {
			viewer.stage.removeEventListener("rightClick", mouseClickHandler);
			viewer.stage.removeEventListener(MouseEvent.MOUSE_DOWN, mouseClickHandler);
		}
		
		//Called when changing the rightClickEnabled of a viewer
		public function changeListeners(viewer:UIComponent):void {
			//if now is disabled we need to remove 
			//the listeners for rightClick CM logic
			if (ClientNotifierData(notifierToData[viewerToNotifier[viewer]]).rightClickEnabled) {
				if (viewer.stage != null) 
					rightClickLitenersAdder(viewer);
				else 
					viewer.addEventListener(Event.ADDED_TO_STAGE, rightClickLitenersAdder);
			} else {
				if (viewer.stage != null) 
					rightClickLitenersRemover(viewer);
				else 
					viewer.addEventListener(Event.ADDED_TO_STAGE, rightClickLitenersRemover);
			}
		}
		
		/**
		 * Used when a component is not attached on the entire existance of the application.
		 * At least one of the viewer or the client notifier must be given to unregister.
		 * 
		 */
		public function unregisterClient(viewer:UIComponent, notifier:Object):void {
			if (viewer == null && notifier == null)  {
				throw "Incorrect call to unregister a client";
			}
			
			if (notifier == null) {
				notifier = viewerToNotifier[viewer];
			}
			
			var notifierData:ClientNotifierData =  ClientNotifierData(notifierToData[notifier]);
				
			if (viewer == null) {	
				viewer = notifierData.viewer;
			}
			
			// LU: obtain reference to the viewer's stage to unregister listeners if necessary.
			// There is a special case when the component is no longer on stage and this deactivation happens.
			// see Bug #7187 on Gantt4Flex
			var stage:Stage = viewer.stage;
			
			if (stage == null) {
				stage = notifierData.stage; 
			}		
		
			// If the client to unregister is the active one we need to deactivate the viewer
			// and to close it's context menu if opened.
			if (viewer == activeViewer) {
				activeViewer = null;
				if (activeContextMenu != null) {
					removeActiveContextMenu();
				}
			}
			
			delete notifierToData[notifier];
			delete viewerToNotifier[viewer];
			
			var hasClients:Boolean = false;
			for (var c:* in viewerToNotifier) {
				hasClients = true;
				break;	
			}
			
			if (!hasClients) {
				stage.removeEventListener(MouseEvent.MOUSE_MOVE, mouseStageHandler);
				stage.removeEventListener(MouseEvent.MOUSE_OVER, mouseStageHandler);
			}			
		}

		/**
		 * This handler is called when a client viewer has been added to the stage 
		 * and it will call the method reposnsible for adding mouse listeners to the stage.
		 * 
		 * <p/> It closes the state, more exactly it removes itself as a listener.
		 * 
		 */ 
		private function viewerAddedToStageHandler(event:Event):void {
			var viewer:UIComponent = UIComponent(event.target);
			// Even if does not have this listener it wont crack.
			viewer.removeEventListener(Event.ADDED_TO_STAGE, viewerAddedToStageHandler);
			// Adds the listeners to the stage of the viewer.
			delayedListenersAdder(viewer);
		}
		
		/**
		 * This method will be called only with a viewer that has been added to the stage
		 * in order to add mouse listeners to it.
		 * The manager needs to listen for distinct events depending on the logic for 
		 * showing the CM (right click logic/mouse over selection logic) 
		 */ 
		private function delayedListenersAdder(viewer:UIComponent):void {		
			var clientNotifierData:ClientNotifierData = ClientNotifierData(notifierToData[viewerToNotifier[viewer]]);
			// In the Flash player versions > 11.2, if there is no listener added for rightClick event, 
			// after a rightClick event was triggered, every mouse event will have the buttonDown property
			// setted to true even if this is not actually true. 
			// Because of this we reggister a emptyRightClickEventListener to prevent this behaviour.
			
			//Application.application.stage.addEventListener("rightClick", emptyRightClickHandler, false, -1);
			viewer.stage.addEventListener("rightClick", emptyRightClickHandler, false, -1);
			
			if (clientNotifierData.rightClickEnabled) {//CM appears on right click 
				rightClickLitenersAdder(viewer );
			}
			 
			// Even this will be called multiple times only a listener registration will be created.
			// So in unregisterClient, we remove this listener only if there are no more clients.
			// (as described http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/events/EventDispatcher.html#addEventListener%28%29)
			viewer.stage.addEventListener(MouseEvent.MOUSE_MOVE, mouseStageHandler);
			viewer.stage.addEventListener(MouseEvent.MOUSE_OVER, mouseStageHandler);	
			// init custom Flash CM
			initializeFlashMenuForViewer(viewer);		
		}
		
		private function emptyRightClickHandler(event:Event):void {
		}
		
		/**
		 * This method listenes for mouse click events happened on the stage of every registred viewer. 
		 * It handles the event only if the active viewer it is managed with a right click CM logic or if it is null.
		 * It is not applied in the case of a active viewer with a mouse over selection CM logic. For this cases 
		 * it exists equivalent handler for mouse move and mouse over events.
		 * 
		 * It processes them in order to detect the registered client viewer under the mouse coordinates.
		 * It uses an optimization based on target under the mouse. If the mouse cursor is
		 * over our active context menu we do not change the active viewer because it is
		 * in a neutral zone, this being needed in order not to close the context menu 
		 * if some part of it is outside the viewer.
		 * 
		 * <p/> After detecting the new client viewer the behaviour is the following:
		 * <ul>
		 * 	<li> if the old active Viewer is not null we must hide it's active context menu;
		 * 	<li> if the new active Viewer is not null, if it has a right click CM logic and 
		 * the mouse event was a right click one we must refresh it in order to show it's menu;
		 * </ul> 
		 */ 
		private function mouseClickHandler(event:MouseEvent):void {
			if (activeViewer == null || activeViewer != null && ClientNotifierData(notifierToData[viewerToNotifier[activeViewer]]).rightClickEnabled) {
				// saves the mouse event for later use
				lastMouseEvent = event;
				
				// Variable that holds the found registered client viewer under the mouse coordinates.
				var newActiveViewer:UIComponent = null;
				var contextMenuUnder:Boolean = false;
				var component:DisplayObject = DisplayObject(event.target);
				
				var decisionObject:Object = getContextMenuOrViewerUnder(event.target);
				if (decisionObject != null && decisionObject is FlowerContextMenu)
					contextMenuUnder = true;
				else 
					newActiveViewer = UIComponent(decisionObject);	 			
					
				// Store the new target in order not to make processing next time.
				cachedTarget = event.target;
				
				// If we click outside the current context menu we close it.
				if (!contextMenuUnder) {
					// If the old active viewer was a client viewer we must hide it's context menu.
					if (activeViewer != null) {
						// Only if it's associated context menu it is showed (and not said by the logic provider to be closed).
						if (activeContextMenu != null && activeContextMenuType == RIGHT_CLICK_CM) {
							removeActiveContextMenu();
						}
					}
						
					if (newActiveViewer != activeViewer) {
						activeViewer = newActiveViewer;
					}
					
					//we show the new acrtiveContextMenu
					if (event.type == "rightClick" && newActiveViewer != null) {
						rightClickContentPosition = activeViewer.globalToContent(new Point(event.stageX, event.stageY));
						//rightClickCMPositionRelativeToContent = activeViewer.localToContent(new Point(event.stageX, event.stageY));
						refresh(viewerToNotifier[activeViewer], RIGHT_CLICK_CM);
					}				
				}
			}
		}
		
		/**
		 * Initializes the Flash CM and adds a show context menu entry that 
		 * displayes the Flower CM when it's selected.
		 * <p>
		 * By default, the contextMenu property is null.		
		 * Defining a customized Flex Context Menu for a given element 
		 * just means creating an instance of ContextMenu and assigning it to the contextMenu property of the element.
		 * @author Cristina
		 */ 
		private function initializeFlashMenuForViewer(viewer:UIComponent):void {			
			var clientNotifierData:ClientNotifierData = ClientNotifierData(notifierToData[viewerToNotifier[viewer]]);	
			// adds the entry only if it has the function implemented
			if (clientNotifierData.beforeShowContextMenuFromFlashFunction == null ||
				!clientNotifierData.beforeShowContextMenuFromFlashFunction(null)) {
				return;
			}
			// create custom context menu for viewer
			if (viewer.contextMenu == null) {
				viewer.contextMenu = new ContextMenu();
			}	
			var flashContextMenu:ContextMenu = viewer.contextMenu;		
			flashContextMenu.hideBuiltInItems();		
					
			// get entry's label
			var entryLabel:String = SHOW_CONTEXT_MENU_NOLABEL_ENTRY;
			if (clientNotifierData.contextMenuEntryLabelFunction != null) {
				entryLabel = clientNotifierData.contextMenuEntryLabelFunction();
			}
			// create entry	and add handler	
			var showContextMenuItem:ContextMenuItem = new ContextMenuItem(entryLabel);
			flashContextMenu.customItems.push(showContextMenuItem);
			showContextMenuItem.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, contextMenuEntryHandler);
		}
		
		/**
		 * Is invoked when the show context menu entry is selected.
		 * It calls the method provided by IFlashContextMenuHandler.
		 * @author Cristina
		 */ 
		private function contextMenuEntryHandler(event:ContextMenuEvent):void {
			if (activeViewer == null)
				return;
			// a callLater is needed because mouse coordonates aren't yet updated
			activeViewer.callLater(showContextMenu);			
		}		
		
		/**
		 * @see #contextMenuEntryHandler(ContextMenuEvent)
		 * @author Cristina
		 */ 
		private function showContextMenu():void {
			// executes custom behavior before showing CM
			var clientNotifierData:ClientNotifierData = ClientNotifierData(notifierToData[viewerToNotifier[activeViewer]]);		
			clientNotifierData.beforeShowContextMenuFromFlashFunction(new Point(lastMouseEvent.stageX, lastMouseEvent.stageY));
			// refresh CM
			refresh(activeViewer);
		}
		
		/**
		 * Returns the viewer under the mouse coordinates.
		 * If there is a context menu under the mouse returns it.
		 * Returns null if  no viewer under the mouse. 
		 */  
		private function getContextMenuOrViewerUnder(objectToLookUnderIt:Object):Object {
			var viewerUnder:UIComponent = null;
			var contextMenuUnder:FlowerContextMenu = null;
			var component:DisplayObject = objectToLookUnderIt is DisplayObject ? DisplayObject(objectToLookUnderIt) : null;
			// Climb the component while a registered client viewer has not been found. 
			while (component != null) {
				if (component is FlowerContextMenu) {
					contextMenuUnder = FlowerContextMenu(component); 
					break;
				}
				if (viewerToNotifier[component] != null) {
					viewerUnder = UIComponent(component);
					break;
				}
				
				// differences between Flex 3 to 4
				if (parseInt(FlowerContextMenu.getFlexVersion().charAt(0)) == 3) {
					// flex 3
					component = component.parent;
				} else {
					// flex 4
					// when an action entry has a ColorPicker child and we the event.target is 
					// the swatchPanel (the panel with the color to chooose) of the color picker
					// if we go up on the parent hierarchy, we don't find the action entry it was normal
					if (component is IAutomationObject && IAutomationObject(component)["automationOwner"] is ColorPicker)
						component = IAutomationObject(component)["automationOwner"];
					else
						component = component.parent;				
				}
			}
			if (contextMenuUnder != null) 
				return contextMenuUnder;
			else 
				return viewerUnder;	 	
		}
			
				
		/**
		 * This method listenes for mouse events happened on the stage of every registred viewer 
		 * only if the active viewer it is managed with a mouse over selection CM logic or if it is null.
		 * It is not applied in the case of a right click CM logic. For this cases it exists equivalent handlers
		 * for mouse click events.
		 * 
		 * It processes them in order to detect the registered client viewer under the mouse coordinates.
		 * It uses an optimization based on target under the mouse. If the mouse cursor is
		 * over our active context menu we do not change the active viewer because it is
		 * in a neutral zone, this being needed in order not to close the context menu 
		 * if some part of it is outside the viewer.
		 * 
		 * <p/> After detecting the new client viewer the behaviour is the following:
		 * <ul>
		 * 	<li> if the old active Viewer is not null we must hide it's active context menu;
		 * 	<li> if the new active Viewer is not null and if it has a mouse over selection CM logic
		 *  we must refresh it in order to show it's menu;
		 * </ul> 
		 * 
		 * UPDATE :
		 * A new functionality was added :
		 * When the mouse is not over an acceptable area, then the CM will be closed using a delay.
		 * The acceptable area contains :
		 * <ul>
		 * 	<li> a selected object
		 * 	<li> the active context menu
		 * 	<li> the distance between the menu and the selected object.
		 * </ul>
		 * @author Cristina
		 * 
		 * 
		 */ 
		private function mouseStageHandler(event:MouseEvent):void {
			if (authomaticClosingMenuDisabled)
				return;
			
			//applied only for the viewers with a left click CM
			// or for viewers with right click CM that have some special elements that needs to be managed with
			// the left click logic
			if (activeViewer == null || !ClientNotifierData(notifierToData[viewerToNotifier[activeViewer]]).rightClickEnabled
				|| ClientNotifierData(notifierToData[viewerToNotifier[activeViewer]]).rightClickEnabled
				   && ClientNotifierData(notifierToData[viewerToNotifier[activeViewer]]).isOverSpecialSelectedElementFunction != null
				   && activeContextMenuType != RIGHT_CLICK_CM) {
				// saves the mouse event for later use
				lastMouseEvent = event;
				
				// Variable that holds the found registered client viewer under the mouse coordinates.
				var newActiveViewer:UIComponent = null;
				var contextMenuUnder:Boolean = false;
				var component:DisplayObject = DisplayObject(event.target);
				
				var decisionObject:Object = getContextMenuOrViewerUnder(event.target);
				if (decisionObject != null && decisionObject is FlowerContextMenu)
					contextMenuUnder = true;
				else 
					newActiveViewer = UIComponent(decisionObject);	
				// we don't want close the menu if we are over it
				if (waitingForClosingMenu && contextMenuUnder) {
					waitingForClosingMenu = false;			
				}
				
				if (activeViewer != null && newActiveViewer == activeViewer ) {	
					// Applied only for the viewers with a mouse over selection CM logic
					// An activity diagram exists in (FD-CM) Context Menu for the following behavior			
					var clientNotifierData:ClientNotifierData = ClientNotifierData(notifierToData[viewerToNotifier[activeViewer]]);								
					
					var menuShouldBeOpened:Boolean;
					if (clientNotifierData.rightClickEnabled)
						menuShouldBeOpened = contextMenuUnder || // mouse is under CM
							activeContextMenu != null && activeContextMenu.isCursorNear(event) || // mouse is near CM
							clientNotifierData != null && clientNotifierData.isOverSpecialSelectedElementFunction(event.stageX, event.stageY); // mouse is over in object
					else
						menuShouldBeOpened = contextMenuUnder || // mouse is under CM
	    					activeContextMenu != null && activeContextMenu.isCursorNear(event) || // mouse is near CM
	   						clientNotifierData != null && clientNotifierData.logicProvider.isOverSelection(event); // mouse is over in object
					
					if (activeContextMenu == null && menuShouldBeOpened) { 
						// the CM is closed and the mouse is moved in CM acceptable area, so show it 
						if (clientNotifierData.rightClickEnabled)
							refresh(viewerToNotifier[activeViewer], MOUSE_OVER_SELECTION_CM);
						else
							refresh(viewerToNotifier[activeViewer], MOUSE_OVER_SELECTION_CM);	
					  			  
					} if (activeContextMenu != null && menuShouldBeOpened && waitingForClosingMenu) { 
						// the CM is waiting for closing, but the mouse is moved again in CM acceptable area, stops the waiting
					   waitingForClosingMenu = false;
					} if (!menuShouldBeOpened && !waitingForClosingMenu) { 
						// the CM must be closed and the waiting wasn't started, start waiting
					   waitingForClosingMenu = true;
					}
				}
							
				/*if (cachedTarget == event.target == UIComponent(cachedTarget).focusManager.getFocus())
					return; */		
					
				// Store the new target and the new focus in order not to make processing next time.
				cachedTarget = event.target;
				
				// The active viewer must be changed.
				// We change the active viewer only if the last activeViewer has lost focus
				if (newActiveViewer != activeViewer && !contextMenuUnder && !activeViewerHasFocus()) {								
					// If the old active viewer was a client viewer we must hide it's context menu.
					if (activeViewer != null) {
						// Only if it's associated context menu it is showed (and not said by the logic provider to be closed).
						if (activeContextMenu != null) {
							removeActiveContextMenu();
						}
					}
					activeViewer = newActiveViewer;	
					
					// If the new active viewer is a client viewer and has a mouse over selection CM logic we must show it's context menu.
					if (activeViewer != null && !ClientNotifierData(notifierToData[viewerToNotifier[activeViewer]]).rightClickEnabled) {
						// Based on the new active viewer a refresh for it's Context Menu is commanded.
						refresh(viewerToNotifier[activeViewer], MOUSE_OVER_SELECTION_CM);
					}				
				}	
			}			
		}
		
		/**
		 * @author Cristi
		 */
		private function needRefreshMouseOverSelectionCM ():Boolean {
			if (activeViewer == null)
				return false;
			var clientData:ClientNotifierData = ClientNotifierData(notifierToData[viewerToNotifier[activeViewer]]);
			if (!clientData.rightClickEnabled)
				return true;
			else {
				if (clientData.isOverSpecialSelectedElementFunction == null)
					return false;
				else {
					if (activeViewer.stage == null) {
						/**
						 * This happens in the following scenario: open a diagram, right click within it, close it.
						 * 
						 * @autor Cristi
						 */
						return false;
					}
					return clientData.isOverSpecialSelectedElementFunction(activeViewer.stage.mouseX, activeViewer.stage.mouseY);
				}
			}
		}
		
		private function activeViewerHasFocus():Boolean {
			if (activeViewer == null)
				return false;
			
			/* A component could have the focus manager null if it is removed from stage.
			   In that case we consider that this component doesn't have the focus in order
			   to alow the mouseStageHandler to change the activeViewer because this handler doesn't
			   change the active viewer unless the old one has lost focus */
			if (activeViewer.focusManager == null)
				return false;
			
			var currentFocus:IFocusManagerComponent = activeViewer.focusManager.getFocus();
			
			if (currentFocus == null) 
				return false;
			
			var posibleViewerOrCMFromCurrentFocus:Object = getContextMenuOrViewerUnder(currentFocus);
			
			// When an action from the CM is selected, focus changes 
			// on the corresponding action entry
			// In this case we consider that the activeViewer still has the focus
			if (posibleViewerOrCMFromCurrentFocus == activeViewer || posibleViewerOrCMFromCurrentFocus is FlowerContextMenu)
				return true;
		
			return false;
		}

		/**
		 * Used after executing a context menu action.
		 * 
		 * @author Cristina 
		 */
		public function setFocusOnContextMenuMainSelection():void {
			if (activeViewer == null) {
				return;
			}
			ClientNotifierData(notifierToData[viewerToNotifier[activeViewer]]).logicProvider.setFocusOnMainSelectedObject();			
		}
		
		/**
		 * Handler for the <code>TimerEvent.TIMER_COMPLETE</code>.
		 * It closes the active context menu.
		 * @author Cristina
		 */		
		private function closeMenuTimerHandler(event:TimerEvent):void {
			if (activeContextMenu != null) {
				removeActiveContextMenu();
			}			
		}
		
		/**
		 * Thie method is intended to be called in following ways:
		 * <ul>
		 * 	<li> by the client viewer as a result of changing the data that fills the entries and the title in the context menu;
		 *  <li> as a result of changing the enabledness of the client which updates the state of showen/hidden of the context menu;
		 * 	<li> as the result of interaction, when moving the mouse's cursor over a viewer;
		 * </ul>
		 * 
		 * The behaviour is the following in case if the notifier is the same with the active viewer:
		 * <ul>
		 *  <li> if the refresh was called by the client the manager checks if the active client needs
		 *  a right click update of the CM and, if it doesn't, the refresh will only make the old CM disappear
		 * 	<p> In all other cases: 
		 * 	<li> hide the old context menu(if it exists one showed)
		 * 	<li> if the notifier wants the context menu to be enabled it show's a new context menu
		 * 	<li> fill the new context menu with data
		 * 	<li> position the new context menu
		 * </ul>
		 * 
		 * The parameter CMType will be not null only if the refresh it is called by CMManager.
		 * If the method is called by the client, he doesn't know what type of CM he will have, because
		 * this it is determined by CMManager.  So this parameter could be used to determine if the refresh
		 * was called by the CMManager listeners or it was called by the client.  
		 * 
		 */
		public function refresh(notifier:Object, CMType:String = null, refreshCausedByRightClickSelectionChanged:Boolean = false):void {	
			var clientNotifierData:ClientNotifierData = ClientNotifierData(notifierToData[notifier]);
			// The refreshing will only work if the viewer of the notifier is the one active and if the notifier is registered.	
			if (clientNotifierData == null || activeViewer != clientNotifierData.viewer) 
				return;
			
			// If the selection changed by right click on a element from the client
			// we cancel the refresh because the refresh for the new selection was handled
			// by the CMManager listener for right click event
			if (refreshCausedByRightClickSelectionChanged)
				return;
			
			// When refreshing the Context Menu we must ensure that the old one hides with effect. 
			if (activeContextMenu != null) {	
				removeActiveContextMenu();
			}
			
			// If the refresh was called by the client and we don't need refreshMouseOverSelectionCM
			// the menu isn't showed anymore
			if (CMType == null && !needRefreshMouseOverSelectionCM())
				return;
			
			if (waitingForClosingMenu) {
				waitingForClosingMenu = false;
			}
			
			// If a client viewer wishes it's associatedContext Menu to be disabled we stop and do not show the menu.
			if (!clientNotifierData.contextMenuEnabled) 
				return;
				
			//Obtain a clean one to commit information into it.
			//if (clientNotifierData.rightClickEnabled && lastMouseEvent.type == "rightClick") { 
			if (CMType != null && CMType == RIGHT_CLICK_CM ) { 
				activeContextMenu = getCleanNoResizeContextMenu();
				activeContextMenuType = RIGHT_CLICK_CM;
			} else {
				activeContextMenu = getCleanContextMenu();
				activeContextMenuType = MOUSE_OVER_SELECTION_CM;
				activeContextMenu.addEventListener("disableAuthomaticClosingMenu", disableAuthomaticClosingMenu);
				activeContextMenu.addEventListener("enableAuthomaticClosingMenu", enableAuthomaticClosingMenu);
			}
			activeContextMenu.minContextMenuWidth = clientNotifierData.minContextMenuWidth;	
			activeContextMenu.iconsWidth = clientNotifierData.menuIconsWidth;
			activeContextMenu.show(true);

			// Update the clean context Menu with menu entries and with title.
			activeContextMenu.setSelectionProvider(clientNotifierData.logicProvider);
			// Give a chance to the the observer to know that we are about to the fill context menu function
			// so that it can first assign some logic to the Context Menu like a function that is called
			// every time when adding of an action is occured. 
			if (clientNotifierData.beforeFillContextMenuFunction != null) {
				clientNotifierData.beforeFillContextMenuFunction(activeContextMenu);
			}
			clientNotifierData.actionProvider.fillContextMenu(activeContextMenu);
			
			// Updating the position after Context Menu has been populated in the same method 
			// will not trigger the showing to the screen and moving it.
			updatePosition(notifier, CMType);
		}
		
		/**
		 * This method is intended to be called in 2 ways:
		 * <ul>
		 * 	<li> by the user as a result of changing the display area of the selection, scrolling, or zooming;
		 * 	<li> by the refresh method in the stage for setting the coordonates of the context menu to appear at;
		 * </ul>
		 * 
		 * 
		 * 
		 * <p/> It will call the Context menu's logic for updating the position for the main one, taking into account
		 * the value provider by the client like the display area and the useWholeArea.
		 * 
		 * <p/> It will only update the active context menu only if the notifier corresponds to it.
		 * 
		 */
		public function updatePosition(notifier:Object, CMType:String = null):void {
			var clientNotifierData:ClientNotifierData = ClientNotifierData(notifierToData[notifier]);
			// The updating of the position will only work if the viewer of the notifier is the one active and if the notifier is registered. 			
			if (clientNotifierData == null || activeViewer != clientNotifierData.viewer) 
				return; 
			
			if (CMType == null && !needRefreshMouseOverSelectionCM())
				return;
			// It may be the case when the context menu has been disabled for a notifier but the notifier wants to update the
			// position as a result of coordinate changes. (e.g when drag and drop tool is activated the context menu is set
			// as disabled but still refreshing of the children as a result of adding a new node, will call this method). 
			if (activeContextMenu == null)
				return;
			// If there is no child, no menu entry added then the context menu is invisible and we do not need to update the position
			// for it because it would be a waste of time. This can happen if in the refresh stage, there where no available actions.
			if (activeContextMenu.numChildren == 0) 
				return;
							
			var applicationContainerArea:Rectangle = new Rectangle(0, 0, activeContextMenu.getParentContainer().width, activeContextMenu.getParentContainer().height);
			
			var upperLeftGlobalActiveViewer:Point = activeViewer.localToGlobal(new Point(0, 0));
			var viewerArea:Rectangle = new Rectangle(upperLeftGlobalActiveViewer.x, upperLeftGlobalActiveViewer.y, activeViewer.width, activeViewer.height);
	
			if(CMType != null && CMType == RIGHT_CLICK_CM) {
				// Position the main context at the point, relative to the content 
				// of the activeViewer, where was registred the last rightClick event
				
				//construct an artificial very small areaOfSelection round the last position of the right click
				var rightClickGlobalPosition:Point = activeViewer.contentToGlobal(rightClickContentPosition);
				var areaOfSelection:Rectangle = new Rectangle(rightClickGlobalPosition.x, rightClickGlobalPosition.y, 1, 1);
				NoAutoResizeFlowerContextMenu.updateMainContextMenuLocation(activeContextMenu, 
						areaOfSelection, applicationContainerArea, viewerArea, 
						clientNotifierData.useWholeScreen);
				//activeContextMenu.setLocation(areaOfSelection, Container(activeViewer));		
				
			} else { //position the main context relative to the displayAreaOfSelection provided by the client
				FlowerContextMenu.updateMainContextMenuLocation(activeContextMenu, 
						clientNotifierData.logicProvider.displayAreaOfSelection, applicationContainerArea, viewerArea, 
						clientNotifierData.useWholeScreen); 
			}
		}

		/**
		 * This method is intended to be called by clients in order to change if their associated Context Menu should be visible or not when
		 * the client is active. When called, it will store the value and it will call a refreshing. If the client is not visible then
		 * the refresh method will not process the new value.
		 * <p/> It should be called when the viewer wishes to disable the Context Menu durring specific behaviour.
		 * 
		 */ 		
		public function updateContextMenuEnabled(notifier:Object, value:Boolean):void {
			var clientNotifierData:ClientNotifierData = ClientNotifierData(notifierToData[notifier]);
			// Only if the notifier is registered.
			if (clientNotifierData == null)
				return;
			// If the new value for context menu enabled is the same one with the stored one, then it is not needed anymore to do a refresh.
			if (clientNotifierData.contextMenuEnabled == value) 
				return;
			clientNotifierData.contextMenuEnabled = value;
			
			refresh(notifier);
		}
		
		/**
		 * In order to let a FlowerContextMenu finish it's effects, and still to show a new Context Menu with it's effect
		 * we use more ContextMenus. This method is intended to be called each time we will to fill a new
		 * Context Menu with data.
		 * 
		 * <p/> This method's implementation uses the <code>newContextMenuCounter</code> property
		 * and the list of precreated <code>cachedContextMenus</code>returning them in circular way.
		 */
		private function getCleanContextMenu():FlowerContextMenu {
			var index:Number = newContextMenuCounter % cachedContextMenus.length; // looping circular using modulo
			newContextMenuCounter++; // increments the value to return a new one;
			return FlowerContextMenu(cachedContextMenus.getItemAt(index));
		}
		
		/**
		 * Same as <code>getCleanContextMenu()</code> only that 
		 * it returns a special kind of CM : a no auto resizeable one
		 */
		private function getCleanNoResizeContextMenu():NoAutoResizeFlowerContextMenu {
			var index:Number = newNoResizeContextMenuCounter % cachedNoResizeContextMenus.length; // looping circular using modulo
			newNoResizeContextMenuCounter++; // increments the value to return a new one;
			return NoAutoResizeFlowerContextMenu(cachedNoResizeContextMenus.getItemAt(index));
		}
		
		/**
		 * This function will be called by a Context Menu component when it detects that a menu entry with an action was executed.
		 * After the closing of the Contex Menu component that executed an action, a refreshing for the associated viewer is done.
		 * The refreshing will be done only if the the viewer of the closed Context Menu is still active.
		 * 
		 */ 		
		private function afterActionExecutedHandler(closedContextMenu:FlowerContextMenu):void {
			// Because it may be the case when we can execute an action, the user can move the mouse, so the client viewer is a different one,
			// we will only perform a refreshing only the context menu showed is the same one with the one closed. If we would not have done
			// this we could have refreshed the context menu of an other client. 
			if (activeContextMenu == closedContextMenu) {
				refresh(viewerToNotifier[activeViewer]);
			}
		}
		
		/**
		 * Returns the current state of the <code>closeMenuTimer</code>.
		 * States :
		 * <ul>
		 * 	<li> <code>true</code> - the timer is running
		 * 	<li> <code>false</code> - othewise
		 * </ul>
		 * @author Cristina
		 */ 
		private function get waitingForClosingMenu():Boolean {
			return closeMenuTimer.running;
		}
		
		/**
		 * Changes the state of the <code>closeMenuTimer</code> based on the value given as parameter.
		 * If <code>true</code>, the timer is started, otherwise it stops the timer.
		 * When the timer's delay passes, the state changes automaticaly in <code>false</code>. 
		 * @see #mouseStageHandler(MouseEvent)
		 * @see #refresh(Object)
		 * @author Cristina
		 */ 
		private function set waitingForClosingMenu(value:Boolean):void {
			if (value) {
				closeMenuTimer.start();
			} else {
				closeMenuTimer.reset();
			}
		}
		
		protected function removeActiveContextMenu ():void {
			activeContextMenu.closeMainContextMenu();
			
			/* 
			Because the authomaticClosingMenuDisabled property could be setted 
			by the activeCM and is applied to it we reset the value of this property
			in order for the future active CM to set there own values
			*/
			if (authomaticClosingMenuDisabled)
				authomaticClosingMenuDisabled = false;
			
			if (activeContextMenuType == MOUSE_OVER_SELECTION_CM) {
				activeContextMenu.removeEventListener("disableAuthomaticClosingMenu", disableAuthomaticClosingMenu);
				activeContextMenu.removeEventListener("enableAuthomaticClosingMenu", enableAuthomaticClosingMenu);
			}
		
			activeContextMenu = null;
			activeContextMenuType = null;
		}
		
		protected function enableAuthomaticClosingMenu(event:Event):void {
			authomaticClosingMenuDisabled = false;
		}
		
		protected function disableAuthomaticClosingMenu(event:Event):void {
			authomaticClosingMenuDisabled = true;
		}
	}
	
}