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
package com.crispico.flower.flexdiagram.contextmenu {
	import com.crispico.flower.flexdiagram.action.ActionContext;
	import com.crispico.flower.flexdiagram.action.AutoResizeContainer;
	import com.crispico.flower.flexdiagram.action.HRuleContextMenuEntry;
	import com.crispico.flower.flexdiagram.action.IAction;
	import com.crispico.flower.flexdiagram.action.IMenuEntrySortable;
	
	import flash.display.DisplayObject;
	import flash.display.DisplayObjectContainer;
	import flash.display.InteractiveObject;
	import flash.events.Event;
	import flash.events.MouseEvent;
	import flash.events.TimerEvent;
	import flash.filters.BlurFilter;
	import flash.geom.Point;
	import flash.geom.Rectangle;
	import flash.utils.Timer;
	
	import mx.collections.ArrayCollection;
	import mx.containers.Box;
	import mx.containers.HBox;
	import mx.controls.Button;
	import mx.controls.Label;
	import mx.core.Application;
	import mx.core.Container;
	import mx.core.UIComponent;
	import mx.core.mx_internal;
	import mx.effects.effectClasses.ParallelInstance;
	import mx.effects.effectClasses.ResizeInstance;
	import mx.events.EffectEvent;
	import mx.events.FlexEvent;
	import mx.events.ResizeEvent;
	
	/**
	 * This class represents the graphical component for the context menu.
	 * It may contain actions (<code>IAction</code> wrapped by <code>ActionEntry</code>)
	 * and/or submenus (<code>SubMenuEntry</code>). 
	 * 
	 * <p>
	 * The context menu needs to be populated with children (<code>ActionEntry</code> or 
	 * <code>SubMenuEntry</code> added with <code>addChild()</code>)
	 * during <code>fillContextMenu()</code> or <code>fillCreateContextMenu()</code>
	 * callbacks.
	 * 
	 * <p>
	 * When working with actions (<code>IAction</code> or <code>BaseAction</code>)
	 * the <code>addActionEntryIfVisible()</code> method may be used to add them
	 * to the menu.
	 * 
	 * @internal
	 * If the user wants to set a title for this menu, 
	 * then the title must be set before adding any children to this menu.
	 * 
	 * @see com.crispico.flower.flexdiagram.gantt.figure.AbstractGanttDiagramFigure#fillContextMenuFunction
	 * @see com.crispico.flower.flexdiagram.gantt.figure.AbstractGanttDiagramFigure#fillCreateContextMenuFunction
	 * @see com.crispico.flower.flexdiagram.action.IAction
	 * @see ActionEntry
	 * @see SubMenuEntry
	 * @see #addActionEntryIfVisible()
	 * @author Sorin
	 * @author Ioana Hagiescu
	 * 
	 */
	public class FlowerContextMenu extends AutoResizeContainer {
		
		/**
		 * Quick hack for a launch, because ContextMenuManager needs this,
		 * and it cannot acces VERSION.
		 * 
		 * @author Cristi
		 */ 
		public static function getFlexVersion():String {
			return mx_internal::VERSION;
		}
		
		/**
		 * @private
		 * 
		 */
		[Embed(source='/closeContextMenu.png')]
		public var closeIcon:Class;
				
		/**
		 * 
		 */
		public static const MINIMUM_WIDTH:int = 25;
		
		/**
		 * Stores the width of CM when minimized.
		 */ 
		public var minContextMenuWidth:Number = MINIMUM_WIDTH;
		
		/**
		 * 
		 */
		protected static const MOVE_EFFECT_DURATION:int = 100; //ms
				
		/**
		 * 
		 */
		protected static const TIMER_DURATION:int = 300; //ms		
		
		/**
		 * A sub Context Menu when showed it overlaps the parent Context Menu with
		 * a width given by this constant.
		 * 
		 */ 
		protected static const SUBMENU_INSIDE_PADDING:int = 5;
		
		/**
		 * The distance between the main element selected and the context menu; 
		 * if the main element selected is not visible, then it is the distance 
		 * between the top left corner of the screen and the top left corner of the menu.
		 * @private
		 * 
		 */
		public static const MAINMENU_PADDING:int = 10;
		
		/**
		 * The distance between the context menu and mouse position.
		 * If the mouse cursor overlaps the location where the main context menu will be positioned
		 * then it will be moved in the right/left with this padding.	
		 * @private
		 * @author Cristina
		 */ 
		protected static const MOUSE_MENU_PADDING:int = 5;
		
		/**
		 * 
		 */
		protected static const INACTIVE_TRANSPARENCY:Number = 0.8;

		/**
		 * The <code>SubMenuEntry</code> that is currently open.
		 * 
		 */
		protected var activeSubMenuEntry:SubMenuEntry;
		
		/**
		 * 
		 */
		protected var selectionProvider:ISelectionProvider;
		
		/**
		 * 
		 */
		protected var _parentContextMenu:FlowerContextMenu;
		
		/**
		 * 
		 */
		protected var titleArea:HBox;
		
		/**
		 * 
		 */
		protected var closeButton:Button;
		
		/**
		 * 
		 */
		protected var titleLabel:Label;		
		
		/**
		 * 
		 */
		protected var transparencyWhenActive:Number;
		
		/**
		 * 
		 */
		protected var _inactive:Boolean;
		
		protected var _beforeActionVisibilityEvaluatedFunction:Function;
		
		protected var _beforeActionExecutedFunction:Function;
		
		/**
		 * 
		 */
		protected var _afterActionExecutedFunction:Function;
		
		protected var _beforeCloseContextMenuFunction:Function;
		
		/**
		 * 
		 */
		protected var _closeAfterActionRun:Boolean;
		
		protected var _actionContext:ActionContext;
		
		protected var _iconsWidth:int = 16;
		
		protected var _noIconPaddingImage;
			
		/**
		 * Timer used for keeping visible a certain time the submenu, even if the mouse is not over it
		 * (it copy the windows start menu behaviour).
		 * @private
		 * 
		 */ 
		internal var timer:Timer;
		
		/**
		 * IMenuEntrySortable that specify as sortIndex DEFAULT_SORT_INDEX will be added at the bottom of the context menu.
		 * 
		 * @private
		 * 
		 */
		public static const DEFAULT_SORT_INDEX:int = int.MAX_VALUE;

		// TODO Sorin : owner direct pare a fi implicat in calcule, de scos in viitor
		/**
		 * @private
		 * 
		 */
		public function FlowerContextMenu(parentContainer:DisplayObjectContainer = null, parentContextMenu:FlowerContextMenu=null, owner:SubMenuEntry=null) {
			if (parentContainer == null)
				parentContainer = DisplayObjectContainer(Application.application);
			
			super(parentContainer, true);			
			this.owner = owner;
			this._parentContextMenu = parentContextMenu;

			timer = new Timer(TIMER_DURATION, 1);
			timer.addEventListener(TimerEvent.TIMER, timerHandler);
			
			if (isMainContextMenu()) {
				this.addEventListener(mx.events.ResizeEvent.RESIZE, resizeTitleToEntriesSize);
				initializeTitleArea();
				//Used to explicit set the width the title area to be same as the width of the menu entries
				//This was needed because when the title was big the CM was resizing its width to the width of the title
			}
			
			this.addEventListener(FlexEvent.ADD, emptyAddHandler);
			this.addEventListener(MouseEvent.CLICK, clickHandler);		
		}

		/** 
		 * This method is called when the context menu resizes to the width of the 
		 * menu entry with the maximum width. The explicit set of the width of the 
		 * title area is necessary because the width of the title area doesn't neeed
		 * to be larger then the maximum width of the context menu's menu entries. 
		 */ 
		protected function resizeTitleToEntriesSize(event:ResizeEvent):void {
			if (titleArea != null && this.measuredWidth != 0) {
				// We decrease the width by 2 because of the padding of the context menu
				// By this decrease we ensure that the title will not cause the increase of 
				// the CM width
				titleArea.maxWidth = this.measuredWidth - 2;
			}
		}
		
		/**
		 * Added for Flex 4 compatibility. For more details see http://forums.adobe.com/message/2882168 .
		 * 
		 * <p> 
		 * There was the case where on Flex 4, the add flexEvent was not triggered on the
		 * MenuEntryWithChildren, and as a result the arrow for the submenu was not added along 
		 * with a NPE for not finding the parent Context Menu.
		 * @private
		 * 
		 */ 
		protected function emptyAddHandler(event:Event):void {
		}
		
		public function get iconsWidth():int {
			return _iconsWidth;
		}
		
		public function set iconsWidth(iconsWidth:int):void {
			_iconsWidth = iconsWidth;
		}
		
		/**
		 * Before the initial logic:
		 * 
		 * <ul>
		 * 	<li>repositions the menu in the parent by calling <code>calculateMenuLocation()</code>
		 * 	<li>register/unregister listeners only if this is the main menu for :
		 * 		<ul>
		 * 			<li> <code>UpdateConnectionsEndsEvent</code>
		 * 			<li> <code>PropertyChangeEvent</code>
		 * 			<li> <code>ScrollEvent</code>
		 * 			<li> <code>MouseEvent</code>
		 * </ul>
		 * @private
		 * 
		 */
		public override function show(isVisible:Boolean, shouldRemoveChildren:Boolean=true):void {	
			if (isVisible) {
				width = minContextMenuWidth;				
				if (isMainContextMenu()) {
					// don't show the main menu initially (only when the children are added)
					visible = false;
					// Only the main Context Menu handles the mouse listener for outside the Context Menus  
					parentContainer.addEventListener(MouseEvent.MOUSE_DOWN, containerMouseDownHandler);
				} else {
					/**
					 * Unfortunatly when expanding a submenu, it's measured height value cannot be computed,
					 * so a call later is done to be notified about changes.
					 */
					this.callLater(updateLocationAsSubContextMenu);
				}				
			} else {
				
				// Only the main Context Menu handles the mouse listener for outside the Context Menus  
				if (isMainContextMenu()) {
					parentContainer.removeEventListener(MouseEvent.MOUSE_DOWN, containerMouseDownHandler);
				}
				if (!isMinimized() && width == measuredWidth) { // a show false when the menu is still rolled on
					addEventListener(MouseEvent.ROLL_OVER, rollOverHandler);
					removeEventListener(MouseEvent.ROLL_OUT, rollOutHandler);
				}								
				// Clear the state of inactive so next time the menus to appear with normal transparancy.
				// Needs to be done before clearing the active sub menu entry.
				inactive = false;
				// hide also the activeSubMenuEtry if it is not null, and set it to null
				if (activeSubMenuEntry != null) {
					activeSubMenuEntry.getSubMenu().show(false, false);
					// show false can happen also on a reset selection; need to reinitialize some values
					activeSubMenuEntry = null;		
				}	
			}
			super.show(isVisible, shouldRemoveChildren);
			// We need to set the NaN value otherwise the measuring does not perform well.
			if (isVisible && shouldRemoveChildren) {
				this.height = NaN;
			}
		}
		
		/**
		 * This method computes the location (x,y) of the sub Context Menu to be showed 
		 * and also assignes the direction of expanding based on the preference of the parent Context Menu 
		 * in the followin way:
		 * <ul>
		 * 	<li> if the parent prefers to expanded to the left, then this method will try to see if the this sub
		 * 		Context Menu in minimizes state has space to be expanded in left, otherwise it assign this the right direction;
		 * 	<li> if the parent preffers to expand in the right direction, this will try to see if this Context Menu has space
		 * 		enougth in the right direction, otherwise it assigns it the left direction.
		 * </ul>
		 * 
		 * <p/> Based on the direction x will be assign to this component.
		 * 
		 * <p/> The sub context Menu should stay at the same level as the sub menu entry so the computing of the y is done in the following way:
		 * <ul>
		 * 	<li> if the bottom y of this context menu is lower than the parent Container then it can stay near
		 * 	<li> otherwise to the top y of the menu entry will be substracted the height of the this context menu that goes out (in the bottom side of the container)   
		 * </ul>
		 * 
		 * <p/> The contract with the parent Context Menu is that the parent will always be visible on the parent container.
		 * @private
		 * 
		 */ 
		public function updateLocationAsSubContextMenu():void {
			if (parentSubMenuEntry == null) {
				return;
			}			
			
			// First compute the resize direction and then the x coordinate of this Sub Context Menu.
			// It takes in consideration the parent Context Menu wish to be resized.
			// The following computations are done relative to the parentContainer, because a Context Menu (parent) stays on the parent Container. 
			validateSize();	

			var subMenuPoint:Point = getSubMenuCoordonates();
			
			// If we wish to resize to left we must have space to add the minimum width to the left.
			if (parentContextMenu.isResizeToLeft) {
				this.isResizeToLeft = parentContextMenu.x - MINIMUM_WIDTH + SUBMENU_INSIDE_PADDING >= 0;
			} else { // else if the parent wishes to the right to resize, we see if the sub menu has space in the right.
				this.isResizeToLeft = parentContextMenu.x + parentContextMenu.width - SUBMENU_INSIDE_PADDING + this.measuredWidth >= parentContainer.width;
			}
			
			if (isResizeToLeft) {
				this.x = subMenuPoint.x - MINIMUM_WIDTH + SUBMENU_INSIDE_PADDING;
				
				// If the non expanded submenu it is placed on the left side of the parent menu
				// we prefer that the expand of the submenu to be done in the left side in order
				// not to overlap the main menu on the right.
				this.preferredResizeToRight = false;
			}  else {
				if (parentContextMenu.isMinimized()) {					
					this.x = subMenuPoint.x + MINIMUM_WIDTH - SUBMENU_INSIDE_PADDING;
				} else {
					this.x = subMenuPoint.x + parentSubMenuEntry.width - SUBMENU_INSIDE_PADDING;
				}
				
				// If the non expanded submenu it is placed on the right side of the parent menu
				// we prefer that the expand of the submenu to be done in the right side in order
				// not to overlap the main menu on the left.
				this.preferredResizeToRight = true;
			}
			
			/**
			 * Carpeala: The AutoResizeContainer keep the x coordinate before starting rolling events in
			 * the <code>originalX</code> field. It is used in the computation of the values to set to the move/resize
			 * effects. Because of the nature of the construction of the Context Menu Framework, the flow being :
			 * show, populate and update position, the AutoResizeContainer does not mantain the state of the originalX
			 * field at the update position phase. This value is set only when calling the <code>show</code> method.
			 * 
			 * <p/> There where more solutions to repair this:
			 * <ul>
			 * 	<li> resorting the order of the show, popuplate, and update position methods into 
			 * 		populate, update position, and show after, to let the AutoResizeContainer to store the correct value;
			 * 	<li> throw an event of x coordinate changing as a "position update" event and not a simple x position change
			 * 		(of a effect), this would be catched by the AutoResizeContainer
			 * 	<li> take the single place where the updating of the position of Context Menu is done and also update
			 * 		manually the <code> originalX</code> field; This solution was elected.   
			 */ 
			originalX = this.x;
					
			// This coordinate is the proposal where the sub Context Menu need to be place on the y axis. It is relative to the parent Container
			// summing the Y's to get the value.
			var proposedY:int = subMenuPoint.y;
			// if the final height is lower than the container height  
			if (proposedY + this.measuredHeight < parentContainer.height) {
				this.y = proposedY;
			} else { // otherwise we must lift this const menu up with the portion leaving out.
				// the final reaching height without the parent container height gives us the overlapping heigth
				var overlappingHeight:int = (proposedY + this.measuredHeight) - parentContainer.height; 
				this.y = proposedY - overlappingHeight;
			}
		}
		
		protected function getSubMenuCoordonates():Point {
			var parentObj:DisplayObjectContainer = parentSubMenuEntry.parent;
			var childObj:DisplayObjectContainer = parentSubMenuEntry;
			
			return parentSubMenuEntry.parent.localToGlobal(new Point(parentSubMenuEntry.x, parentSubMenuEntry.y));			
		}
		
		/**
		 * This method is intended to be used to compute and update the location of the main FlowerContextMenu.
		 *  
		 * <p> When the display area cand be smaller than the graphical selected element, it may be possible that when showing the main menu minimized, to show
		 * it over the mouse cursor, which delivers an undesired effect. As a solution if the <code>displayAreaSmaller</code> flag is true we check if the mouse
		 * cursor is near the display area, more exactly in the location where the mimized context would appear. If we find it there we expand the displayArea
		 * as if it would passed as argument in the first place. 
		 * 
  		 * <p/>This method will detect to throw the context menu into the corner in the following situations:
		 * <ul>
		 * 	<li> a display area was not provided (probably because the selection is not visible);
		 * 	<li> if the viewer area is not visible on the container area; 
		 * 	<li> if the display area is not visible on the visible viewer area;
		 * 	<li> on the horizontal axis there is not enougth space to add into the left or into the right the mimized context menu.
		 * </ul>
		 * <p/> When throwing in the corner one of the container area or the viewer area will be used depending on which is not visible.
		 * <p/> The computing of the x is done by first tring if has enougth space in the right, otherwise in the left.
		 * <p/> The computing of the y is done in the following mode:
		 * <ul>
		 * 	<li> if the display area is exceeds the top edge of the container area then then context menu will start from the top edge of container area;
		 * 	<li> if the context menu' height would exceed the bottom edge of the container area if it would start from display area then we lift it up
		 * 		so the bottom edge of the context menu to be the bottom edge of the container area;
		 * 	<li> otherwise the context menu will be positioned at the same level with the display area;
		 * </ul>
		 * <p/> The computing of x and y is done based on the container area or on the viewer area depending on the value of the useWholeScreen parameter suplied. 
		 *
		 * <p/> After computing, it will commit the location changes using <code>commitLocationAsMainContextMenu()</code> method. 
		 * @private
		 * 
		 */ 
		public static function updateMainContextMenuLocation(mainContextMenu:FlowerContextMenu, displayArea:Rectangle, containerArea:Rectangle, viewerArea:Rectangle, useWholeScreen:Boolean):void {
			var throwInCorner:Boolean = false;
			var whosCorner:Rectangle = null;
			var relativeContainerArea:Rectangle = null;

			// If the viewerArea is not visible in containerArea
			if (!viewerArea.intersects(containerArea)) {
				throwInCorner = true;
				whosCorner = containerArea;
			} else {
				// We chop of so that we have a visible viewer area
				viewerArea = viewerArea.intersection(containerArea);
				// We know that viewerArea is visible on the ContainerArea but what about display Area, is it visible?
				if (displayArea == null || !displayArea.intersects(viewerArea)) {
					throwInCorner = true;
					whosCorner = viewerArea;
				} else {
					// We chop of so we can have the visible displat area inside the viewerArea.
					displayArea = displayArea.intersection(viewerArea);
				}
			}
			
			// If the mouse cursor overlaps the location where the main context menu will be positioned 
			// then it will be moved in the right with padding or in the left with some padding (@see MOUSE_MENU_PADDING).			
			if (!throwInCorner) {
				var stageCursorPosition:Point = ContextMenuUtils.flex4CompatibleContainer_getCursorPosition(mainContextMenu.getParentContainer());
	
				// Check the right area
				// right of display area with [padding..min_widh+padding]  
				if (displayArea.x + displayArea.width + MAINMENU_PADDING <= stageCursorPosition.x &&
						stageCursorPosition.x <= displayArea.x + displayArea.width + MAINMENU_PADDING + MINIMUM_WIDTH) {
					displayArea.width = stageCursorPosition.x - MAINMENU_PADDING - displayArea.x + MOUSE_MENU_PADDING;
				}
				// Check the left area
				// [-min_width-padding..-padding] for left of display area
				if (stageCursorPosition.x <= displayArea.x - MAINMENU_PADDING &&
						displayArea.x - MAINMENU_PADDING - MINIMUM_WIDTH <= stageCursorPosition.x) {
					var delta:Number = displayArea.x - stageCursorPosition.x;	
					displayArea.x = displayArea.x - delta + MAINMENU_PADDING - MOUSE_MENU_PADDING;
					displayArea.width = displayArea.width + delta;	
				}
			}
			
			// Take the correct container on which to make computations about fitting the context menu.
			if (useWholeScreen) {
				relativeContainerArea = containerArea;
			} else {
				relativeContainerArea = viewerArea;
			}
			
			var computedIsResizeToLeft:Boolean;
			var computedX:Number;
			var computedY:Number;

			// We compute the X coordinate of the Context Menu, if it has place in the right, after in the left, and if not then we throw in the corner.
			if (!throwInCorner) {
				// By default we would like to place it to resize to the right.
				if (displayArea.x + displayArea.width + MAINMENU_PADDING + MINIMUM_WIDTH < relativeContainerArea.x + relativeContainerArea.width) {
					computedX = displayArea.x + displayArea.width + MAINMENU_PADDING;
					computedIsResizeToLeft = false;
				} else if (displayArea.x - MAINMENU_PADDING - MINIMUM_WIDTH >= relativeContainerArea.x) { // We fall in the case when we would like to resize to the left.
					computedX = displayArea.x - MAINMENU_PADDING - MINIMUM_WIDTH;
					computedIsResizeToLeft = true;
				} else {
					throwInCorner = true;
					whosCorner = viewerArea;
				}
			}
			
			// We need to do a validation of the size because flex framework did not had a chance to do it,
			// when removing children and adding other children only did an invalidation call but not validation.
			mainContextMenu.validateSize(true);

			// We compute the Y coordinate of the Context Menu
			if (!throwInCorner) {
				//	The top edge of the display area is upper than the top edge of the container area
				if (displayArea.y < relativeContainerArea.y) {
					computedY = relativeContainerArea.y;
					// If the bottom edge of the context menu is under the bottom edge of the container area, we must lift the context menu.
				} else if (displayArea.y + mainContextMenu.measuredHeight > relativeContainerArea.y + relativeContainerArea.height) {
					var overlappingHeight:int = (displayArea.y + mainContextMenu.measuredHeight) - (relativeContainerArea.y + relativeContainerArea.height); 
					computedY = displayArea.y - overlappingHeight;
				} else { // If both top edge of display area and the bottom edge of the context menu is inside the container area. 
					computedY = displayArea.y;
				}
			}
			
			// Commit the data if it should stay in the corner.
			if (throwInCorner) {
				// top right corner
				// an extra padding is added in case of a vertical scroll
				computedX = whosCorner.x + whosCorner.width - MINIMUM_WIDTH - 2 * MAINMENU_PADDING;
				computedY = whosCorner.y + MAINMENU_PADDING;
				computedIsResizeToLeft = false;
			}

			// Convert the global coordinates into the local ones. 			
			var relativeToContent:Point = ContextMenuUtils.flex4CompatibleContainer_globalToContent(mainContextMenu.getParentContainer(), new Point(computedX, computedY));
			// Commit the data to the context menu.
			mainContextMenu.commitLocationAsMainContextMenu(relativeToContent.x, relativeToContent.y, computedIsResizeToLeft);
		}
		
		/**
		 * This method is intended to be used when this context menu is used stand alone and not througth
		 * the context menu manager.
		 * 
		 * <p> It needs the area of the selection because it will automatically position it near the selection area
		 * using the parent container of the context menu. 
		 * @private
		 * 
		 */ 
		public function setLocation(displayAreaOfSelection:Rectangle, activeViewer:Container):void {
			var applicationContainerArea:Rectangle = new Rectangle(0, 0, getParentContainer().width, getParentContainer().height);
			
			var upperLeftGlobalActiveViewer:Point = activeViewer.localToGlobal(new Point(0, 0));
			var viewerArea:Rectangle = new Rectangle(upperLeftGlobalActiveViewer.x, upperLeftGlobalActiveViewer.y, activeViewer.width, activeViewer.height);
			FlowerContextMenu.updateMainContextMenuLocation(this, displayAreaOfSelection, applicationContainerArea, viewerArea,	false); 
		} 

		/**
		 * This method is intended to be called to move the coordinated of the main Context Menu.
		 * Along with moving it's coordinates it will command a moving of coordinates 
		 * 
		 * @private
		 * 
		 */ 
		public function commitLocationAsMainContextMenu(x:int, y:int, isResizeToLeft:Boolean):void {
			this.x = x;
			this.y = y;
			this.isResizeToLeft = isResizeToLeft;
			
			/**
			 * Carpeala: The AutoResizeContainer keep the x coordinate before starting rolling events in
			 * the <code>originalX</code> field. It is used in the computation of the values to set to the move/resize
			 * effects. Because of the nature of the construction of the Context Menu Framework, the flow being :
			 * show, populate and update position, the AutoResizeContainer does not mantain the state of the originalX
			 * field at the update position phase. This value is set only when calling the <code>show</code> method.
			 * 
			 * <p/> There where more solutions to repair this:
			 * <ul>
			 * 	<li> resorting the order of the show, popuplate, and update position methods into 
			 * 		populate, update position, and show after, to let the AutoResizeContainer to store the correct value;
			 * 	<li> throw an event of x coordinate changing as a "position update" event and not a simple x position change
			 * 		(of a effect), this would be catched by the AutoResizeContainer
			 * 	<li> take the single place where the updating of the position of Context Menu is done and also update
			 * 		manually the <code> originalX</code> field; This solution was elected.   
			 */ 
			this.originalX = x;
			
			// Recursively find the FlowerContextMenu under using the active sub Menu entry and command it a repositioning.
			var contextMenuToDescend:FlowerContextMenu = this;
			while (contextMenuToDescend != null && contextMenuToDescend.activeSubMenuEntry != null) {
				contextMenuToDescend = contextMenuToDescend.activeSubMenuEntry.getSubMenu();
				contextMenuToDescend.updateLocationAsSubContextMenu();
			}
		}
		
		/**
		 * This method recursively climbs the hierarchy until it finds the Main Context Menu 
		 * and commands it to be hidden.
		 * @private
		 * 
		 */ 
		public function closeMainContextMenu():void {
			//if (activeSubMenuEntry != null && activeSubMenuEntry.subMenuTimer.running) {				
			//	activeSubMenuEntry.subMenuTimer.reset();
			//}			
			if (isMainContextMenu()) {
				if (_beforeCloseContextMenuFunction) {
					_beforeCloseContextMenuFunction();
				}
				show(false);
			} else {
				parentContextMenu.closeMainContextMenu();
			}
		}
		
		/**
		 * 
		 * @private
		 */
		public function getActiveSubMenuEntry():SubMenuEntry {
			return this.activeSubMenuEntry;
		}
		
		/**
		 * @private
		 * 
		 */
		public function setActiveSubMenuEntry(activeSubMenuEntry:SubMenuEntry):void {
			this.activeSubMenuEntry = activeSubMenuEntry;
		}
		
		/**
		 * 
		 * @private
		 */
		protected function get parentContextMenu():FlowerContextMenu {
			return _parentContextMenu;	
		}
		
		/**
		 * 
		 * @private
		 */
		protected function get parentSubMenuEntry():SubMenuEntry {
			return parentContextMenu.activeSubMenuEntry;
		}
		
		/**
		 * 
		 * @private
		 */
		public function isMainContextMenu():Boolean {
			return parentContextMenu == null;
		}
		
		/**
		 * 
		 * @private
		 */
		public function setSelectionProvider(value:ISelectionProvider):void {
			this.selectionProvider = value;
		}

		/**
		 * Sets the title of the menu. This works only for the main
		 * context menu; sub context menus don't have a title. 
		 * 
		 */ 
		public function setTitle(value:String):void {
			if (isMainContextMenu()) {
				titleLabel.text = value;
			} else {
				throw "Setting the title is not allowed for a sub Context Menu.";
			}
		}

		/**
		 * If this Context Menu is the Main one then it will return information from the <code>selectionProvider</code> property,
		 * otherwise it will return the selection of the parent Context Menu.
		 * This method is used by the actions when they need to be run.
		 *
		 * @private
		 * 
		 */
		public function getSelection():ArrayCollection {
			if (isMainContextMenu()) {
				if (selectionProvider == null) {
					throw "No selection provider has been assigned to the Main Context Menu.";
				} else {
					return selectionProvider.getSelection();
				}
			} else {
				return parentContextMenu.getSelection();
			} 
		}
		
		/**
		 * Adds the given action (if visible) to the context menu 
		 * (based on the result of the <code>IAction.isVisible()</code>).
		 * Actually an <code>ActionEntry</code> that wraps the action is added.
		 *
		 * @internal
		 * If this context menu contains a function in #beforeActionVisibilityEvaluatedFunction property,
		 * it will be first called, before evaluating the visibility. Usuaslly the previously mentioned function
		 * has the role of setting some keys in the context of the action.
		 *    
		 * 
		 */ 
		public function addActionEntryIfVisible(action:IAction):void { 
			if (beforeActionVisibilityEvaluatedFunction != null) {
				beforeActionVisibilityEvaluatedFunction(action);
			}
			if (action.isVisible(getSelection())) {
				addChild(new ActionEntry(action));
			}
		}
		
		/**
		 * 
		 * @private
		 */
		public function getParentContainer():DisplayObjectContainer {
			return parentContainer;
		}
		
		/**
		 * This method is intended to be called when it is wished that the
		 * Context Menu component should be inactive but showed on the screen.
		 * 
		 * @private
		 */ 
		public function set inactive(value:Boolean):void {
			// Only if the value has changed
			if (_inactive == value)
				return;
				
			// Store the new value;
			_inactive = value;
			// Make the component enabled or not
			enabled = !_inactive;
			
			// If it has a sub child opened we must make it's children also transparent or not.
			if (activeSubMenuEntry != null) {
				activeSubMenuEntry.getSubMenu().inactive = value;
			}
		}
		
		/**
		 * 
		 * @private
		 */
		public function get inactive():Boolean {
			return _inactive;
		}

		/**
		 * Handler function called by <code>addActionEntryIfVisible</code> and it is intended
		 * to be called to assign the context to an action. Can be only assigned to the main context menu.
		 * @private
		 */ 		
		public function set beforeActionVisibilityEvaluatedFunction(value:Function):void {
			if (isMainContextMenu()) {
				_beforeActionVisibilityEvaluatedFunction = value;
			}
		}
		
		/**
		 * Return the main context menu handler function to be called before evaluating an action's visibility.
		 * @private  
		 */ 
		public function get beforeActionVisibilityEvaluatedFunction():Function {
			if (isMainContextMenu()) {
				return _beforeActionVisibilityEvaluatedFunction; 
			} else {
				return parentContextMenu.beforeActionVisibilityEvaluatedFunction;
			}
		}
		
		/**
		 * Handler function called by the menu entry for an action, in order to notify the interested 
		 * parts about the fact that execution of the action will start. 
		 * The purpose of setting this function for the moment is for the observer to know if it should fill a specific
		 * context or other details
		 * 
		 * <p/> This must be set only to the main Context Menu.
		 * 
		 * <p/> The signature function should be : 
		 * 	<pre> function handler(mainContextMenu:FlowerContextMenu, action:FlowerContextMenu):void </pre>
		 * 
		 * @see notifyBeforeActionExecuted() 
		 * 
		 * @private
		 */ 
		public function set beforeActionExecutedFunction(value:Function):void {
			_beforeActionExecutedFunction = value;
		}
		
		/**
		 * Handler function called by the menu entry for an action, in order to notify the interested 
		 * parts about the end of the execution of the action. The purpose of setting this function
		 * is for the observer to know if it should show the context menu again after execution.
		 * 
		 * <p/> This must be set only to the main Context Menu
		 * 
		 * <p/> The signature function should be : 
		 * 	<pre> function handler(mainContextMenu:FlowerContextMenu):void </pre>
		 * 
		 * @see notifyAfterActionExecuted() 
		 * 
		 * @private
		 */ 
		public function set afterActionExecutedFunction(value:Function):void {
			_afterActionExecutedFunction = value;	
		}
		
		/**
		 * Handler function called before closing the main context menu.
		 * <p/> This must be set only to the main Context Menu
		 * 
		 * <p/> The signature function should be : 
		 * 	<pre> function handler():void </pre>
		 * 
		 * @see closeMainContextMenu() 
		 * @private
		 */ 
		public function set beforeCloseContextMenuFunction(value:Function):void {
			_beforeCloseContextMenuFunction = value;	
		}
		
		/**
		 * Before executing an action, the Menu Entry calls this method to give a chance for the listeners to have their
		 * logic executed before running the action given as parameter.
		 * 
		 * <p/>This method will recursively search for the Main Context Menu because the contract is that only the Main Context Menu
		 * knows about envirioment details. 
		 *  
		 * <p/>This method is supposed to keep other behaviours added in the future, referring before execution of an action.
		 * 
		 * @private
		 */ 
		public function notifyBeforeActionExecuted(action:IAction):void {
			if (isMainContextMenu()) {
				if (_beforeActionExecutedFunction != null) {
					_beforeActionExecutedFunction(this);
				}
			} else {
				parentContextMenu.notifyBeforeActionExecuted(action);
			}
		}

		/**
		 * After executing an action, the Menu Entry calls this method to give a chance for the listeners to have their
		 * logic executed after running the action.
		 *
		 * <p/>This method will recursively search for the Main Context Menu because the contract is that only the Main Context Menu
		 * knows about envirioment details. 
		 *  
		 * <p/>This method is supposed to keep other behaviours added in the future, referring to after execution of an action.
		 * 
		 * @private
		 */ 
		public function notifyAfterActionExecuted():void {
			if (isMainContextMenu()) {
				if (_afterActionExecutedFunction != null) {
					_afterActionExecutedFunction(this);
				}
				if (_closeAfterActionRun) {
					ContextMenuManager.INSTANCE.setFocusOnContextMenuMainSelection();
					this.closeMainContextMenu();
				}
			} else {
				parentContextMenu.notifyAfterActionExecuted();
			}
		}
		
		/**
		 * 
		 * @private
		 */
		public function set closeAfterActionRun(value:Boolean):void {
			_closeAfterActionRun = value;	
		}
				
		/**
		 * @private
		 */ 
		public function get actionContext():ActionContext {
			return _actionContext;
		}	
		
		/**
		 * @private
		 */ 
		public function set actionContext(value:ActionContext):void {
			_actionContext = value;
		}
		
		/**
		 * Besides the inherited behaviour, this method add the title and the close button, when the first child is added.
		 * Atention: this method does not add the child at specified index. If the child is an IMenuEntrySortable
		 * it uses it's sort index to calculate the index at which the child should be added. 
		 * 
		 * NOTE: it has been rewritten, check the comments from inside the method!
		 * @private
		 * @author Florin
		 * @author Sorin 
		 * 
		 */ 
		public override function addChildAt(child:DisplayObject, index:int):DisplayObject {
			// The main menu contains a title area which is initialized 
			// when the first child is added so, before adding the real child
			// we add the title area and increase the index to be added under. 			
			if (isMainContextMenu() && numChildren == 0) {
				super.addChildAt(titleArea, 0);								
				index++;
			}
			
			// This method tries to arrange children based on the sortindex of the IMenuEntrySortable
			// interface if the child to be added implements it.

			// Filter old type of menu entries that do not implement the desired interface.
			if (!(child is IMenuEntrySortable)) {
				trace("FlowerContextMenu Warning : " + Object(child).label + " does not implement IMenuEntrySortable for obtaining a sort Index!");
				// If there are no children then the position to insert is write at the beggining. 
				if (isMainContextMenu() && numChildren == 0) { 
					index = 0;
				}
					
				// Knowing that the list of children has at the beggining non sortable menu entries,
				// and that at the end are sortable menu entries, ordered by their increasing index,
				// we try to find the last non sortable menu entry to insert after it.
				// We need this behaviour because then working with context menu you expect that
				// the order you make the operations of adding entries to be the same as the order 
				// of entries in the menu.
				for (var i:int = 0 ; i < numChildren; i++) 
					if (!(getChildAt(i) is IMenuEntrySortable)) {
						index = i + 1;
					}
				
				return super.addChildAt(child, index);				
			}
			
			var toBeInsertedSortableChild:IMenuEntrySortable = child as IMenuEntrySortable;
			if (toBeInsertedSortableChild.sortIndex == 0)
				trace("FlowerContextMenu Warning : the sort index for " + Object(child).label + " is 0, please assign a one!");  

			// We iterate from the end until we find a position where the sortindex of child to be added 
			// will be greater than the sortindex of the iterated child. 
			for (var i:int = numChildren - 1; i >=0 ; i--) {
				if (getChildAt(i) is IMenuEntrySortable) {
					var iteratedSortableChild:IMenuEntrySortable = getChildAt(i) as IMenuEntrySortable;
					if (iteratedSortableChild.sortIndex < toBeInsertedSortableChild.sortIndex ) {
						index = i + 1; // it will be inserted at the next index; 					
						break;	
					}
				} else {
					// Probably the sortindex of the child to be inserted is lower than all sortable children.
					index = i + 1;
					break;
				}
			}
			
			return super.addChildAt(child, index);
		}
		
		/**
		 * @private
		 */ 
		public function hasMenuEntries():Boolean {
			return this.numChildren > 1; // titleArea is considered the first child
		}
				
		/**
		 * This method initializes and adds the Title Area entry to this Context Menu,
		 * only if this Context Menu is the Main one. 
		 * @private
		 * 
		 */ 
		private function initializeTitleArea():void {
			// The title area entry
			titleArea = new HBox();
			titleArea.setStyle("backgroundColor", "0xDDEEEE");
			titleArea.setStyle("paddingRight", 2);	
			titleArea.setStyle("paddingLeft", 7);	
			titleArea.setStyle("verticalAlign", "middle");
			titleArea.percentWidth = 100;
			//titleArea.width = MINIMUM_WIDTH;
			titleArea.maxWidth = 0;
			
			// The component which holds the title
			titleLabel = new Label();
			titleLabel.setStyle("textAlign", "center");
			titleLabel.setStyle("fontSize", 9);	
			titleLabel.percentWidth = 94;	
			titleLabel.truncateToFit = true;
			titleLabel.minWidth = MINIMUM_WIDTH;
			
			// So that fade effect to work without embeding the font.
			titleLabel.filters = [new BlurFilter(0, 0, 0)];			
			// The container which holds the close button
			var buttonContainer:Box = new Box();
			buttonContainer.setStyle("horizontalAlign", "center");
			buttonContainer.setStyle("verticalAlign", "middle");			
			buttonContainer.percentHeight = 100;
						
			// The close button with it's mouse handler
			closeButton = new Button();
			closeButton.setStyle("icon", closeIcon);
			closeButton.height = 8;
			closeButton.width = 8;
			closeButton.addEventListener(MouseEvent.MOUSE_UP, closeButtonMouseUpHandler);
			closeButton.visible = true;
					
			// Insert first the close button inside the button container	
			buttonContainer.addChild(closeButton);

			// After add the title label and the button container to the title area entry				
			titleArea.addChild(buttonContainer);
			titleArea.addChild(titleLabel);							
		}
			
		/**
		 * @private
		 * 
		 */
		protected override function setStyles():void {
//			{
//				// CS-VC
//				Store;
//				BaseSequential;
//				BaseCirc;
//				StoreEntry;
//			}

			super.setStyles();
			
			setStyle("paddingBottom", 5);
			setStyle("borderStyle", "solid");
   			setStyle("borderColor", "black");
   			setStyle("backgroundColor", "#F1F3F8");
   			setStyle("verticalGap", 0);
   			
   			setStyle("disabledOverlayAlpha", INACTIVE_TRANSPARENCY);
   			
		}		
		
		/**
		 * When the close button on the main Context Menu is clicked this method is called.
		 * This listener will be added only if in this is the main context Menu, so no need
		 * for checking if we are in the main Context Menu state.
		 * @private
		 * 
		 */ 
		protected function closeButtonMouseUpHandler(event:MouseEvent):void {
			closeMainContextMenu();
		}

		/**
		 * Handler for the <code>MouseEvent.MOUSE_DOWN</code>
		 * @private
		 * 
		 */ 		
		protected function containerMouseDownHandler(event:MouseEvent):void {
			// TODO sorin : de stabilit daca atuinci cand se da click in afara defapt se face altceva
			// cu mouse-ul si defapt nu trebuie sa reactionam?
//			// If the clicked component does not have a parent a Context Menu (even itself)
//			// then it means that a click has been done outside Contxt Menu component.
//			if (parentOf(event.target, this.className) == null) {
//				closeMainContextMenu();
//			}
		}
		
		
		/**
		 * Besides the initial logic, submenu logic is implemented: if there 
		 * is a submenu open, and the mouse is over that submenu, then nothing happens.
		 * @private
		 * 
		 */
		public override function rollOutHandler(event:MouseEvent):void {			
			if (inactive) 
				return;
													
			if (event == null) {					
				super.rollOutHandler(event);				
			} else {																				
				if (activeSubMenuEntry != null && !(event.relatedObject != null && event.relatedObject.parent == this && event.relatedObject != activeSubMenuEntry.getSubMenu())) {
					removeEventListener(MouseEvent.ROLL_OUT, rollOutHandler);
					addEventListener(MouseEvent.ROLL_OVER, rollOverHandler);						
				}					
				if (!timer.running && ((activeSubMenuEntry && !activeSubMenuEntry.getSubMenu().timer.running && event.relatedObject != null && event.relatedObject.parent == this && event.relatedObject != activeSubMenuEntry.getSubMenu()) ||
					(owner != null && owner.parent && event.relatedObject != null && event.relatedObject != owner && event.relatedObject.parent == owner.parent))) {
					timer.start();
				}
				if (activeSubMenuEntry == null && shouldExpand) {				
					super.rollOutHandler(event);
				}				
			}
		}
		
		/**
		 * Besides the initial logic, submenu closing logic is implemented: if the 
		 * child that is under the mouse is not the activeSubMenuEntry, a "fake"
		 * rollOutEvent is sent to activeSubMenuEntry (forcing it to close). Otherwise
		 * nothing is done.
		 * @private
		 * 
		 */	
		 public override function rollOverHandler(event:MouseEvent):void {		 	
			if (inactive)
				return;	
			if (event == null) {
				super.rollOverHandler(null);
				return;
			}		
			if ((activeSubMenuEntry == null || event.target != activeSubMenuEntry) && !isFadingOff()) {
				if (activeSubMenuEntry != null) {	
					if (!shouldExpand) {					
						activeSubMenuEntry.rollOutHander(null);	
						rollOverHandler(null);
						return;				
					}					
					activeSubMenuEntry.dispatchEvent(new MouseEvent(MouseEvent.ROLL_OUT, true, false, null, null, InteractiveObject(event.target)));
					addEventListener(MouseEvent.ROLL_OUT, rollOutHandler);
					removeEventListener(MouseEvent.ROLL_OVER, rollOverHandler);					
				} else {
					if (titleLabel) {
						titleLabel.visible = true;																																										
					}						
					super.rollOverHandler(event);
				}		
			}		
			if (timer.running) {
				timer.reset();		
			}				
		}

		/**
		 * 
		 * When ending a resize effect we have the following behaviour:
		 * <ul>
		 * 	<li> after expanding we show the close button;
		 * 	<li> after collapsing we do a hideing of the title label.
		 * </ul>
		 * @private
		 * 
		 */ 
		protected override function effectEndHandler(event:EffectEvent):void {
			super.effectEndHandler(event);
			if (event.effectInstance is ResizeInstance || event.effectInstance is ParallelInstance) {
				if (width == originalWidth) {
					if (titleLabel != null) {
						titleLabel.visible = false;
					}
				}				
			}			
		}
		
		/**
		 * Handler for the <code>TimerEvent.TIMER</code>
		 * @private
		 * 
		 */ 
		protected function timerHandler(event:TimerEvent):void {	
			if (timer.currentCount >= 1) {								
				// if mouse outside any menu- show false for activeSubMenuEntry && roll out for the this
				// if mouse outside parentmenu, but in another parent - get that menu, and show false the activeSubMenuEntry of that menu
				// if mouse inside the parentmenu - show false for activeSubmenuEntry				
				var arr:Array = parentContainer.getObjectsUnderPoint(contentToGlobal(new Point(mouseX, mouseY)));
				for (var i:int = arr.length - 1; i >= 0; i--) {
					var obj:FlowerContextMenu = FlowerContextMenu(parentOf(arr[i]));
					if (obj != null) {
						if (obj.activeSubMenuEntry) {
							var subMenu:SubMenuEntry = SubMenuEntry(parentOf(arr[i], false));
							obj.activeSubMenuEntry.rollOutHander(null);
							if (!shouldExpand) {								
								obj.rollOverHandler(null);
							}
							if (subMenu != null) {
								subMenu.rollOverHander(null);
							}
						}											
						return;
					}					
				}				
				if (activeSubMenuEntry)
					activeSubMenuEntry.rollOutHander(null);
				if (width > MINIMUM_WIDTH) {					
					rollOutHandler(null);
				}
			} 
		}
		
		/**
		 * Returns any of the parents of the <code>obj</code>.
		 * If none found, returns null.
		 * Depending on <code>checkContextMenu<code>,
		 * the parent must be a <code>FlowerContextMenu</code> or a <code>MenuEntryWithChildren</code>.
		 * 
		 * We replaced the old parameter "className" with this one because in case we had an extended
		 * <code>MenuEntryWithChildren</code> (like <code>NavigateRelationMenuEntryWithChildren<code>)
		 * this method returned null and the submenu wasn't displayed at <code>rollOverHandler</code>.
		 * @private
		 * @author Cristina
		 * 
		 */ 
		protected static function parentOf(obj:Object, checkContextMenu:Boolean = true):UIComponent {
			var parent:DisplayObjectContainer = obj.parent;
			while (parent != null && parent is UIComponent && !(parent is (checkContextMenu ? FlowerContextMenu : SubMenuEntry)))
				parent = parent.parent;
			if (parent is UIComponent && parent is (checkContextMenu ? FlowerContextMenu : SubMenuEntry))
				return UIComponent(parent);
			else
				return null;	
		}

		/**
		 * The method expands the CM and removes the ROLL_OUT event 
		 * so that it cannot be closed when the mouse isn't under it.
		 * It is used when the user clicks on a submenu entry.
		 * @see MenuEntryWithChildren.clickHandler()
		 * @private
		 * @author Cristina  
		 * 
		 */ 
		public function changeStateToExpand():void {	
			removeEventListener(MouseEvent.ROLL_OUT, rollOutHandler);
			addEventListener(MouseEvent.ROLL_OVER, rollOverHandler, true);								
			super.notifyToExpand();		
		}
		
		/**
		 * Verifies if the mouse position is found in a rectagle 
		 * that is considered to be "near" the context menu.
		 * This rectangle's dimensions are : 
		 * <ul>
		 * 	<li> rectangle.height = CM.height
		 * 	<li> rectangle.width = MAINMENU_PADDING
		 * 	<li> If CM is found in left  : rectangle.x = CM.x + MAINMENU_PADDING
		 * 		 If CM is found in right : rectangle.x = CM.x - MAINMENU_PADDING 
		 * </ul>
		 * @private
		 * @author Cristina
		 */ 
		public function isCursorNear(event:MouseEvent):Boolean {
			// verifies Y position			
			var cursorNear:Boolean = y < event.stageY && y + height > event.stageY;					
			// verifies X position
			if (x < event.stageX) {
				cursorNear = cursorNear && (x + MAINMENU_PADDING > event.stageX); 
			} else {
				cursorNear = cursorNear && (x - MAINMENU_PADDING < event.stageX);
			}
			return cursorNear;			
		}
		
		protected function clickHandler(event:MouseEvent):void {			
			if (!isMinimized()) {
				return;
			}
			if (event.target is SubMenuEntry) {				
				var submenu:SubMenuEntry = SubMenuEntry(event.target);
				if (submenu == activeSubMenuEntry) {
					return;
				}
				shouldExpand = false;
				if (timer.running) {
					timer.reset();
				}				
				if (activeSubMenuEntry != null) {
					if (activeSubMenuEntry.getSubMenu().timer.running) {
						activeSubMenuEntry.getSubMenu().timer.reset();
					}
					activeSubMenuEntry.rollOutHander(null);					
				}								
				submenu.showSubMenu();				
			}						
		}
		
		public function mainContextMenuDispatchEvent (event:Event):void {
			if (isMainContextMenu()) {
				this.dispatchEvent(event);
			} else {
				parentContextMenu.mainContextMenuDispatchEvent(event);
			}
		}
		
		public function removeMultipleHRuleMenuEntries():void {
			var i:int = 0;
			while (i < numChildren) {				
				if (getChildAt(i) is HRuleContextMenuEntry) {
					if (i == 1) {
						removeChildAt(i);
					} else {
						var j:int = i + 1;
						while (j < numChildren && getChildAt(j) is HRuleContextMenuEntry) {
							removeChildAt(j);
						}
						if (i == numChildren - 1) {						
							removeChildAt(i);						
						}
						i++;
					}
				} else {
					i++;
				}				
			}
			if (numChildren == 1) {
				visible = false;
			}
		}
	}
}