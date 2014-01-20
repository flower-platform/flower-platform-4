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
	import com.crispico.flower.flexdiagram.action.IMenuEntrySortable;
	import com.crispico.flower.flexdiagram.util.common.FlowerLinkButton;
	
	import flash.display.DisplayObjectContainer;
	import flash.events.Event;
	import flash.events.MouseEvent;
	import flash.filters.BlurFilter;
	
	import mx.collections.ArrayCollection;
	import mx.controls.Image;
	import mx.core.UIComponent;
	import mx.core.mx_internal;
	import mx.effects.effectClasses.ResizeInstance;
	import mx.events.EffectEvent;
	import mx.events.FlexEvent;
	import mx.events.PropertyChangeEvent;
	
	use namespace mx_internal;
	/**
	 * Menu entry for a "sub-menu". It needs to be added in a parent
	 * context menu. This class wraps a <code>FlowerContextMenu</code>
	 * (obtained using <code>getSubMenu()</code>) that needs to be filled.
	 * 
	 * @internal
	 * Structure for a sub-menu in the <code>FlowerContextMenu</code>.
	 * 
	 * It is responsibile for showing / hiding the sub menu.
	 * 
	 * UPDATE :
	 * Functionality added when clicking a sub menu entry :
	 * <ul>
	 * 	<li> the parent menu is expanded
	 * 	<li> the corresponding sub menu is shown 
	 * 	<li> the parent menu doesn't close if the mouse isn't under it.
	 * </ul>
	 * 
	 * @see FlowerContextMenu
	 * @see #getSubMenu()
 	 * @author Cristina
	 * 
	 */
	public class SubMenuEntry extends FlowerLinkButton implements IMenuEntrySortable {
		
		/**
		 * 
		 * @private
		 */
		[Embed(source='/menuArrow.gif')]
		public var menuArrow:Class;
		
		[Embed(source='/small_arrow.gif')]
		public var smallMenuArrow:Class;
		
		/**
		 * 
		 */
		private var arrowIcon:Image;
		
		/**
		 * 
		 */
		private static const ARROW_SIZE:int = 13;
		
		/**
		 * 
		 */
		private var parentMenu:FlowerContextMenu;
		
		/**
		 * The sub - menu that is shown on mouseOver .
		 * 
		 */
		private var subMenu:FlowerContextMenu;

		// TODO sorin : should be rewriten this behaviour		
		/**
		 * If this magical flag is not maintained, when passing over multiple sub menu entries, at the last one,
		 * the showing would appear like a flickering. 
		 * 
		 */ 
		private var subMenuShowed:Boolean;
					
		/**
		 * The model behind the submenuEntry
		 * Gives the data necesary for setting the label, icon and the sortIndex of this visual component 
		 * @private
		 * @author Dana
		 */ 
		protected var _submenuEntryModel:SubMenuEntryModel;
		
		/**
		 * @return The wrapped <code>FlowerContextMenu</code> that needs to be filled
		 * with children (menu entries).
		 * 
		 * 
		 */
		public function getSubMenu():FlowerContextMenu {
			return this.subMenu;
		}	
		
		/**
		 * This is used to position the label for those entry that doesn't have
		 * an icon just like those that have one.
		 * 
		 * The constant 4 it is added because the entries that have an icon have the 
		 * padding left set to 2, because flex adds by default, for the buttons with icons, 
		 * a horizontal gap of 2 between the icon and the label and because adding the arrow
		 * for the submenu entries increases the icon width with 5..
		 *   
		 */ 
		protected function positionLabelWithHorizontalGap(event:Event):void {
			setStyle("paddingLeft", getParentContextMenu().iconsWidth + 9);
		}
		
		/**
		 * @internal
		 * (the sort index defaults to int.MAX_VALUE, i.e. FlowerContextMenu.DEAFAULT_SORT_INDEX; it is specified 
		 * this way due to compilation issues).
		 * 
		 * @param icon The icon (a <code>Class</code> for an embeded image).
		 * @param label The label of the submenu.
		 * @param parentContextMenu The parent menu (which can be the main context menu or another sub menu).
		 * @param sortIndex Used for ordering entries.
		 * 
		 */		 
		public function SubMenuEntry(subMenuEntryModel:SubMenuEntryModel, parentContextMenu:FlowerContextMenu) {
			
			_submenuEntryModel = subMenuEntryModel;
			// TODO sorin : this is not stored at this moment because there is a previous logic that depends 
			// to be null, until this menu entry is added.
			// TODO sorin : fix in future 
//			this.parentMenu = parentContextMenu;
			if (subMenuEntryModel.image != null) {				
				setStyle("iconURL", new ArrayCollection([smallMenuArrow, subMenuEntryModel.image]));
			} else {
				//delayed call because on positioning the label we need to have set the parent
				//in order to take the parent iconsWidth
				this.addEventListener(FlexEvent.ADD, positionLabelWithHorizontalGap);
			}
			
			setStyle("paddingLeft", 2);
			setStyle("paddingRight", 16); // Padding right for when this entry is the most longest(width) to have enought space to add the arrow.
			setStyle("textAlign", "left");
			setStyle("fontWeight", "normal");
			setStyle("textIndent", 10);
			setStyle("cornerRadius", 3);
			// So that fade effect to work without embeding the font.
			filters = [new BlurFilter(0, 0, 0)];
			
			this.label = subMenuEntryModel.label;
			percentWidth = 100;
			
			arrowIcon = new Image();
			arrowIcon.source = menuArrow;
			arrowIcon.setActualSize(ARROW_SIZE, ARROW_SIZE);	
			
			if (parentContextMenu is NoAutoResizeFlowerContextMenu)
				subMenu = new NoAutoResizeFlowerContextMenu(parentContextMenu.getParentContainer(), parentContextMenu, this);
			else	
				subMenu = new FlowerContextMenu(parentContextMenu.getParentContainer(), parentContextMenu, this);
			subMenu.iconsWidth = parentContextMenu.iconsWidth;
			subMenu.setStyle("paddingTop", 5);
			
			addEventListener(MouseEvent.ROLL_OVER, rollOverHander);
			addEventListener(MouseEvent.ROLL_OUT, rollOutHander);
			addEventListener(FlexEvent.ADD, addedStartHandler);
			addEventListener(FlexEvent.REMOVE, removedStartHandler);					
		}

		/**
		 * Overrided to bring in front the arrowIcon because, upper in the hierarchy,
		 * on every <code>updateDisplayList()</code> of the Flex <code>Button</code> class
		 * the three default children (the currentSkin, currentIcon and the textField) are bringed 
		 * in front of all other possible children. 
		 * 
		 * Because of this, it was a problem at roll over : the arrowIcon was being hidden by the
		 * rollOverSkin.   
		 */ 
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			
			if (arrowIcon.parent != null)
				setChildIndex(arrowIcon, numChildren - 1);
		}
		
		/**
		 * Listener of the <code>MouseEvent.ROLL_OVER</code> event.
		 * 
		 * Opens the submenu (if it is not already opened), and sets
		 * <code>activeSubMenuEntry</code> for the parent menu.
		 * 
		 * A timer is set(<code>openTimer</code>) 
		 * as the user can only over pass the mouse on this subMenuEntry to get to another entry
		 * 
		 * @private
		 */
		public function rollOverHander(event:Event):void {		
				
			// if it doesn't have yet a parent, don't show it
			// this happends when the parentMenu dispatches a EFFECT_END event, but the submenu was already removed from menu
				
			if (parentMenu == null) {				
				return;
			}															
			if (subMenu.timer.running) {
				subMenu.timer.reset();
			} else if (parentMenu.getActiveSubMenuEntry() == null && !parentMenu.isFadingOff()) {  	      												
				if (parentMenu.width < parentMenu.measuredWidth) {												
				//if (parentMenu.width)	
					parentMenu.addEventListener(EffectEvent.EFFECT_END, rollOverHander);													
				} else { // on effect_end						
					parentMenu.removeEventListener(EffectEvent.EFFECT_END, rollOverHander);													
					// effectEnd can be dispatched on a end of a reverse resize effect (so when the parent is minimized)
					// check if mouse still over the submenuentry or if a submenu entry was clicked
					if ((mouseX >=0 && mouseX <= width && mouseY >=0 && mouseY <= height && parentMenu.width == parentMenu.measuredWidth)) {
						parentMenu.setActiveSubMenuEntry(this);
						subMenu.show(true, false);
						subMenuShowed = true;										
					}			
				}
			}
		}

		/**
		 * This method is called as a result of a "normal" or "simulated" (by the parent menu) event distpatch.
		 * It hides the submenu, except if the mouse is "over" the submenu.
		 * 
		 * @private
		 */
		public function rollOutHander(event:MouseEvent):void {														
			if (event != null && (!(event.relatedObject == subMenu) && subMenuShowed)) {			
				if (!subMenu.timer.running) {										
					subMenu.timer.start();					
				} else {					
					parentMenu.timer.start();
				}						
			} else if (event == null) {									
				// the roll out event is catched and processed only if the subMenu is visible on screen(i.e. the viewer is set)
				parentMenu.setActiveSubMenuEntry(null);								
				subMenu.show(false);
				subMenuShowed = false;																													
			}		
		}
		
		/**
		 * This handler is used to wait for the parent to be set, 
		 * and the resize to big effect start for adding the arrow icon.
		 * 
		 * The parent is set only after the <code>SubMenuEntry</code>
		 * 
		 * For flex 4 compatibility see FlowerContextMenu.emptyAddHandler()  
		 * 
		 */ 
		private function addedStartHandler(event:Event):void {
			if (event is FlexEvent) {
				if (parentMenu == null) {
					parentMenu = getParentContextMenu();
					// this has been commented, i don't see their meaning
					//addEventListener(MouseEvent.ROLL_OVER, rollOverHander);
					//addEventListener(MouseEvent.ROLL_OUT, rollOutHander);
					//addEventListener(FlexEvent.ADD, addedStartHandler);					
					if (!(parentMenu is NoAutoResizeFlowerContextMenu)) { 
						//if the parent menu is a resizeable FlowerContextMenu 
						//we delay the adding of the arrow icon for the time
						//when the CM will be extended 
						parentMenu.addEventListener(EffectEvent.EFFECT_START, addedStartHandler);
					} else {
						//For now the width of this subMenuEntry is 0 so
						//we delay the adding of the arrow icon for the time
						//when the width of the this submenu entry will be set
						parentMenu.addEventListener(mx.events.ResizeEvent.RESIZE , delayedArrowAdd);
						}
				}	
			} else if (event is EffectEvent) {
				var effEvent:EffectEvent = EffectEvent(event);
				if (effEvent.effectInstance is ResizeInstance && ResizeInstance(effEvent.effectInstance).widthTo >= measuredWidth && arrowIcon.parent != this) {
					parentMenu.removeEventListener(EffectEvent.EFFECT_START, addedStartHandler);
					// if the parent has percentWidth set, the submenu width doesn't contain the parent menu min width
					if (parent is UIComponent && !isNaN(UIComponent(parent).percentWidth)) {
						arrowIcon.x = this.width + FlowerContextMenu.MINIMUM_WIDTH - ARROW_SIZE - 4;
					} else { 
						arrowIcon.x = this.width - ARROW_SIZE - 4;
					}
    				arrowIcon.y = 4; 
					arrowIcon.includeInLayout = false;
    				addChildAt(arrowIcon, this.numChildren);
				}
			}
		}		
		
		private function delayedArrowAdd(event:Event = null):void {
			this.removeEventListener(PropertyChangeEvent.PROPERTY_CHANGE, delayedArrowAdd);
			// if the parent has percentWidth set, the submenu width doesn't contain the parent menu min width
			if (parent is UIComponent && !isNaN(UIComponent(parent).percentWidth)) {
				arrowIcon.x = parentMenu.width + FlowerContextMenu.MINIMUM_WIDTH - ARROW_SIZE - 4;
			} else { 
				if (parentMenu != null)
					arrowIcon.x = parentMenu.width - ARROW_SIZE - 4;
			}
			arrowIcon.y = 4;    				
			addChild(arrowIcon);
		}
		
		/**
		 * Remove also the parent and its listeners.
		 * @author Cristina
		 * 
		 */ 
		private function removedStartHandler(event:Event):void {
			parentMenu.removeEventListener(EffectEvent.EFFECT_START, addedStartHandler);			
			parentMenu = null;						
		}		
		
		public function showSubMenu():void {
			parentMenu.setActiveSubMenuEntry(this);			
			subMenu.show(true, false);			
			subMenuShowed = true;																	
		}			
		
		/**
		 * @private
		 */ 
		public function get sortIndex():int {
			return _submenuEntryModel.sortIndex;
		}
		
		/**
		 * The method searches for the <code>FlowerContextMenu</code> parent by
		 * iterating through parents.
		 * <p>
		 * This is needed in order to work with different components on Context Menu. 
		 * A <code>SubMenuEntry</code> can be added not only in a <code>FlowerContextMenu</code> component.
		 * @author Cristina
		 */ 
		private function getParentContextMenu():FlowerContextMenu {
			var parentObj:DisplayObjectContainer = parent;
			while (parentObj != null) {
				if (parentObj is FlowerContextMenu) {
					return FlowerContextMenu(parentObj);
				}
				parentObj = parentObj.parent;
			}
			return null;
		}		
	}
}