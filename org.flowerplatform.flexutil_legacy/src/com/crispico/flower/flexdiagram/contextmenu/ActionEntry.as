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
	import com.crispico.flower.flexdiagram.action.IAction;
	import com.crispico.flower.flexdiagram.action.IMenuEntrySortable;
	import com.crispico.flower.flexdiagram.util.common.FlowerLinkButton;
	
	import flash.display.DisplayObjectContainer;
	import flash.events.Event;
	import flash.events.MouseEvent;
	import flash.filters.BlurFilter;
	
	import mx.collections.ArrayCollection;
	import mx.events.FlexEvent;
	import mx.states.SetStyle;

	/**
	 * Context menu entry that wraps an <code>IAction</code>. Instances of this
	 * class are added to <code>FlowerContextMenu</code>.
	 * 
	 * <p>
	 * It's recommended to use <code>FlowerContextMenu.addActionEntryIfVisible()</code>
	 * which creates an instance of this class based on the given <code>IAction</code>.
	 * 
	 * @see FlowerContextMenu
	 * @see com.crispico.flower.flexdiagram.action.IAction
	 * 
	 */
	public class ActionEntry extends FlowerLinkButton implements IMenuEntrySortable {
	
		/**
		 * 
		 */
		protected var action:IAction;		
		
		/**
		 * 
		 * @private
		 */
		public function ActionEntry(action:IAction) {
			super();
			this.action = action;
			
			label = action.label; 
			if (action.image is Class) {
				setStyle("icon", action.image); 
			} else {
				setStyle("iconURL", action.image);
			}
			
			if (action.image == null) {
				//delayed call because on positioning the label we need to have set the parent
				//in order to take the parent iconsWidth
				this.addEventListener(FlexEvent.ADD, positionLabelWithHorizontalGap);
			}
			
			
			setStyle("paddingLeft", 2);
			
			// By default if we have an link button with a icon and label Flex 
			// sets the horizontalGap at 2. But because the SubMenuEntry icon
			// are having a small arrow with the width 5, for the ActionEntry we encrise
			// the the paddingRightWith this value in order to keep the same layout as in the
			// case of a SubMenuEntry. 
			setStyle("horizontalGap", 7)
			setStyle("paddingRight", 16); // Padding right for when this entry is the most longest(width) to have enought space to add the arrow.
			setStyle("textAlign", "left");
			setStyle("fontWeight", "normal");
			setStyle("textIndent", 10);
			setStyle("cornerRadius", 3);
			
			percentWidth = 100;
			mouseChildren = false;	
			// So that fade effect to work without embeding the font.
			filters = [new BlurFilter(0, 0, 0)];

			addEventListener(MouseEvent.MOUSE_UP, mouseUpHandler);
		}
		
		/**
		 * This is used to position the label for those entry that doesn't have
		 * an icon just like those that have one.
		 * 
		 * The constant 4 it is added because the entries that have an icon have the 
		 * padding left set to 2, because flex adds by default, for the buttons with icons, 
		 * a horizontal gap of 2 between the icon and the label and because adding the arrow
		 * for the submenu entries increases the icon width with 5.
		 * 
		 *   
		 */ 
		protected function positionLabelWithHorizontalGap(event:Event):void {
			setStyle("paddingLeft", getParentContextMenu().iconsWidth + 9);
		}
		
		// TODO sorin :  arhitectura e cam imbarligata, ContextMenu tine niste handlere pe care ar trebui sa le execute in anumite momente, referitoare la actiune...
		
		/**
		 * 
		 * @private
		 */
		protected override function mouseUpHandler(event:MouseEvent):void {
			var parentMenu:FlowerContextMenu = getParentContextMenu();
			if (parentMenu is FlowerContextMenu && !FlowerContextMenu(parentMenu).isFadingOff()) {
				runAction();
			}
		}					
		/**
		 * 
		 * @private
		 */
		public function get sortIndex():int {
			return action.sortIndex;
		}
		
		/**
		 * The method searches for the <code>FlowerContextMenu</code> parent by
		 * iterating through parents.
		 * <p>
		 * This is needed in order to work with different components on Context Menu. 
		 * A <code>ActionEntry</code> can be added not only in a <code>FlowerContextMenu</code> component.
		 * @author Cristina
		 */ 
		protected function getParentContextMenu():FlowerContextMenu {
			var parentObj:DisplayObjectContainer = parent;
			while (!(parentObj is FlowerContextMenu)) {
				parentObj = parentObj.parent;
			}
			return FlowerContextMenu(parentObj);
		}
		
		/**
		 * Cases when this method is called :
		 * <ul>
		 * 	<li> when a mouse Up event is dispatched on this action entry.
		 * 	<li> when we want to execute directly the action inside this action entry.
		 * </ul> 
		 * @author Cristina
		 * @see SelectMoveResizeAction#mouseUp()
		 * @private
		 */ 
		public function runAction():void {
			// We keep a copy of the parent which is a flower context menu because, inside the action, when running,
			// it may dispatch notifications which are interpreted as refreshing the context menu, which determines the removing 
			// of the graphical parent of this Menu Entry, and it would result in a NPE.
			var flowerContextMenu:FlowerContextMenu = FlowerContextMenu(getParentContextMenu());
			// Make the main context Menu notify listeners the an action is about to be executed.
			flowerContextMenu.notifyBeforeActionExecuted(action);
			// Run the action on the provided selection.
			action.run(flowerContextMenu.getSelection());
			// Make the main Context Menu notify the listeners in order to know that a refreshing of context menu can be done if they wish to.
			flowerContextMenu.notifyAfterActionExecuted();
		}
	}

}