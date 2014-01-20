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
package com.crispico.flower.flexdiagram.util.tabNavigator {
	
	import com.crispico.flower.flexdiagram.util.common.ButtonUtils;
	
	import flexlib.controls.SuperTabBar;
	
	import mx.controls.Button;
	import mx.core.ClassFactory;
	import mx.core.IFlexDisplayObject;
	import mx.core.mx_internal;
	
	use namespace mx_internal;
	
	/**
	 * Besides its super functionality, the class overrides methods 
	 * that can be used to add a new style "iconURL" to a child (tab)
	 * in order to work also with an URL for the child's icon.
	 * 
	 * @author Cristina
	 * 
	 */
	public class FlowerSuperTabBar extends SuperTabBar {
		
		/**
		 * Represents the pattern that will be used to identify the URL of an icon
		 */ 
		private static const PATTERN_URL:String = "&url=";
		
		/**
		 * Sets the class factory to <code>CustomSuperTab</code>. 
		 */ 
		public function FlowerSuperTabBar() {
			super();
			navItemFactory = new ClassFactory(FlowerSuperTab);
		}
		
		/**
		 * The method was overrided so that after it creates the item,
		 * adds the new property by splitting the label, if it contains 2 pieces of information.
		 * @param label - a string formed in the following way :
		 * label = item's label + PATTERN_URL + icon's URL 
		 */
		override protected function createNavItem(
										label:String,
										icon:Class = null):IFlexDisplayObject {
			// creates the item with the standard properties
			var newButton:Button = Button(super.createNavItem(label, icon));
			// searches for the pattern	
			var indexURL:int = label.search(PATTERN_URL);
			// if the pattern exists, then the item must have an "iconURL" set
			if (indexURL != -1) {
				// get the correct label and set the URL style
				newButton.label = label.substr(0, indexURL);
				newButton.setStyle(ButtonUtils.iconURLStyleName, label.substr(indexURL+PATTERN_URL.length));
			}
			return newButton;
		}
		
		/**
		 * This override was neccesary because in <code>createNavItem</code> we don't have acces to
		 * the object in order to get its "iconURL" style.
		 * The method is used only when a <code>createNavItem</code> is called.
		 */
		override public function itemToLabel(data:Object):String {
			// get the item's label
			var label:String = super.itemToLabel(data);
			// if the item has "iconURL" set, add it to the label
			var iconURLstyle:String = data.getStyle(ButtonUtils.iconURLStyleName);
			if (iconURLstyle != null) {
				label += PATTERN_URL + iconURLstyle;
			}
			// return the new label
			// the label will be split in <code>createNavItem</code>
			return label;			
		}
		
		/**
		 * Don't hilite the selection if -1.
		 * It crashes otherwise. 
		 */ 
		override protected function hiliteSelectedNavItem(index:int):void {
			if (index == -1) {
				return;
			}
			if (index >= numChildren) {				
				index = numChildren - 1;
			}
			super.hiliteSelectedNavItem(index);
		}
	}
	
}