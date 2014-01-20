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
package com.crispico.flower.flexdiagram.contextmenu
{
	import flash.display.DisplayObjectContainer;
	import flash.display.InteractiveObject;
	import flash.events.Event;
	import flash.events.MouseEvent;
	import flash.geom.Point;
	import flash.geom.Rectangle;

	public class NoAutoResizeFlowerContextMenu extends FlowerContextMenu
	{
		public function NoAutoResizeFlowerContextMenu(parentContainer:DisplayObjectContainer=null, parentContextMenu:FlowerContextMenu=null, owner:SubMenuEntry=null)
		{
			super(parentContainer, parentContextMenu, owner);
			
			//We don't need the click handler anymore because his role is limited to a resizeble flowerContextMenu. 
			this.removeEventListener(MouseEvent.CLICK, clickHandler);
			
			// the noAutoResizeFlowerContextMenu is never minimized
			this._isMinimized = false;
			
			// By default this context menu it is automatically close after the execution of
			// some of its actions
			this._closeAfterActionRun = true;
			
		}
		
		
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
				if (displayArea.x + displayArea.width + MAINMENU_PADDING + mainContextMenu.measuredWidth < relativeContainerArea.x + relativeContainerArea.width) {
					computedX = displayArea.x + displayArea.width + MAINMENU_PADDING;
					computedIsResizeToLeft = false;
				} else if (displayArea.x - MAINMENU_PADDING - mainContextMenu.measuredWidth >= relativeContainerArea.x) { // We fall in the case when we would like to resize to the left.
					computedX = displayArea.x - MAINMENU_PADDING - mainContextMenu.measuredWidth;
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
		
		public override function show(isVisible:Boolean, shouldRemoveChildren:Boolean=true):void {
			if (isVisible) {
				
				//width = minContextMenuWidth;				
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
			
				
				originalWidth = width;
				originalX = x;
				
				if (fadeEffect.isPlaying) {
					fadeEffect.stop();
				}
				if(shouldRemoveChildren) {
					removeAllChildren();
					// need to explicitely reset height to 0, as it remains at a previous value	
					height = 0;
					explicitHeight = NaN;
				}
				ContextMenuUtils.flex4CompatibleContainer_addChild(parentContainer, this);
				
				if (useFadeEffect) {
					fadeEffect.repeatDelay = 0;
					fadeEffect.alphaFrom = 0;
					fadeEffect.alphaTo = BACKGROUND_ALPHA;
					fadeEffect.play();
				}
			} else {
				
				// Only the main Context Menu handles the mouse listener for outside the Context Menus  
				if (isMainContextMenu()) {
					parentContainer.removeEventListener(MouseEvent.MOUSE_DOWN, containerMouseDownHandler);
				}
				
				addEventListener(MouseEvent.ROLL_OVER, rollOverHandler);
				removeEventListener(MouseEvent.ROLL_OUT, rollOutHandler);							
				// Clear the state of inactive so next time the menus to appear with normal transparancy.
				// Needs to be done before clearing the active sub menu entry.
				inactive = false;
				// hide also the activeSubMenuEtry if it is not null, and set it to null
				if (activeSubMenuEntry != null) {
					activeSubMenuEntry.getSubMenu().show(false, false);
					// show false can happen also on a reset selection; need to reinitialize some values
					activeSubMenuEntry = null;		
				}
				isResizeToLeft = !preferredResizeToRight;
				
				if (useFadeEffect) {
					if (fadeEffect.isPlaying) {
						fadeEffect.reverse();
						fadeEffect.repeatDelay = 1;
					}
					else {
						if (fadeEffect.alphaTo != 0 && fadeEffect.repeatDelay == 0) { 
							//no need to start again the fade "OFF" effect if it was already started
							fadeEffect.repeatDelay = 0;
							//fadeEffect.stop();
							fadeEffect.alphaFrom = BACKGROUND_ALPHA;
							fadeEffect.alphaTo = 0;							
							fadeEffect.play();
						}						
					}
				} else {
					ContextMenuUtils.flex4CompatibleContainer_removeChild(parentContainer, this);
				}	
			}
			// We need to set the NaN value otherwise the measuring does not perform well.
			if (isVisible && shouldRemoveChildren) {
				this.height = NaN;
			}
		}
		
		/**
		 * Because we don't need the resize efect of the context menu this function
		 * inherits only the behavior from flowerContextMenu without inheriting the one from AutoResizeContainer
		 * @private
		 * 
		 */
		public override function rollOutHandler(event:MouseEvent):void {			
			if (inactive) 
				return;
													
			if (event != null) {																					
				if (activeSubMenuEntry != null && !(event.relatedObject != null && event.relatedObject.parent == this && event.relatedObject != activeSubMenuEntry.getSubMenu())) {
					removeEventListener(MouseEvent.ROLL_OUT, rollOutHandler);
					addEventListener(MouseEvent.ROLL_OVER, rollOverHandler);						
				}					
				if (!timer.running && ((activeSubMenuEntry && !activeSubMenuEntry.getSubMenu().timer.running && event.relatedObject != null && event.relatedObject.parent == this && event.relatedObject != activeSubMenuEntry.getSubMenu()) ||
					(owner != null && owner.parent && event.relatedObject != null && event.relatedObject != owner && event.relatedObject.parent == owner.parent))) {
					timer.start();
				}				
			}
		}
		
		override public function updateLocationAsSubContextMenu():void {
			if (parentSubMenuEntry == null) {
				return;
			}			
			
			// First compute the resize direction and then the x coordinate of this Sub Context Menu.
			// It takes in consideration the parent Context Menu wish to be resized.
			// The following computations are done relative to the parentContainer, because a Context Menu (parent) stays on the parent Container. 
			validateSize();	
			
			var subMenuPoint:Point = getSubMenuCoordonates();
			
			// We try to position the submenu in the right of the main menu if it is possible
			this.isResizeToLeft = parentContextMenu.x + parentContextMenu.width - SUBMENU_INSIDE_PADDING + this.measuredWidth >= parentContainer.width;
			
			if (isResizeToLeft) {
				// If the subcontext menu doesn't fit on the right we try to possition it
				// in the left of the CM without overlapp the main menu
				this.x = subMenuPoint.x - this.measuredWidth + SUBMENU_INSIDE_PADDING;
				
				if (this.x < 0)
					// The subcontext menu doesn't fit in the left of the main menu without overlaping it.
					// We position the submenu in the left of the screen no matter if overlaps the main menu 
					this.x = 0;
			} else {
				this.x = subMenuPoint.x + parentSubMenuEntry.width - SUBMENU_INSIDE_PADDING;
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
			//originalX = this.x;
			
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
		
		/**
		 * Because we don't need the resize efect of the context menu this function
		 * inherits only the behavior from flowerContextMenu without inheriting the one from AutoResizeContainer
		 * @private
		 * 
		 */	
		 public override function rollOverHandler(event:MouseEvent):void {		 	
			if (inactive || event == null)
				return;		
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
		
		
		
	}
}