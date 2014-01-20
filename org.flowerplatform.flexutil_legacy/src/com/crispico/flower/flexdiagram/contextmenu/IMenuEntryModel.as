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
	
	/**
	 * All the model classes that lay behind a graphical menu entry inside a FlowerContextMenu 
	 * have to implement this class.
	 * 
	 * @see IAction
	 * @see BaseAction
	 * @see SubMenuEntryModel
	 * @author Dana
	 */ 
	[RemoteClass(alias="com.crispico.flower.mp.contextmenu.IMenuEntryModel")]
	public interface IMenuEntryModel {
		/**
		 * The label of the menu entry. 
		 */
		function get label():String;
		
		/**
		 * The image of the menu entry (a <code>Class</code> of an embeded image).
		 */
		function get image():Object;
		
		/**
		 * Entries can specify a sort index at which should be added in the context menu. If
		 * they don't specify anything, they are added at the end of the context menu.
		 *  
		 * @internal
		 * By default, the sort index is ContextMenu.DEFAULT_SORT_INDEX, meaning they will be added at the bottom of the context menu.
		 */
		function get sortIndex():int; 
	}
}