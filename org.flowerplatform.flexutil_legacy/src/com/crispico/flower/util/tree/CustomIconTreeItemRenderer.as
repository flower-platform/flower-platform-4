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
package com.crispico.flower.util.tree {
	import com.crispico.flower.util.tree.checkboxtree.CheckBoxTreeItemRenderer;
	
	import flash.display.DisplayObject;
	
	import mx.controls.treeClasses.TreeListData;
	import mx.core.IFlexDisplayObject;

	
	/**
	 * Abstract renderer that allows the use of custom icons, mainly
	 * meant to be used with images retrieved from <code>ImageFactory</code>.
	 * 
	 * <p>
	 * Subclasses need to override <code>createIcon()</code> method.
	 * 
	 * @author Cristi 
	 */ 
	public class CustomIconTreeItemRenderer extends CheckBoxTreeItemRenderer {
		
		/**
		 * Cancels the original icon related logic (where icon = Class provided
		 * by tree) and uses our icon
		 * instead.
		 */ 
		override protected function commitProperties():void {
			var newIcon:IFlexDisplayObject = createIcon();
			if (newIcon != null) {
				TreeListData(listData).icon = null;
			}
			super.commitProperties();
			if (newIcon != null) {
				icon = newIcon;
				addChild(DisplayObject(icon));
			}
		} 

		/**
		 * Needs to be overidden and return a new icon instance
		 * (probably based on the model data).
		 */ 
		protected function createIcon():IFlexDisplayObject {
			return null;
		}

	}
	
}