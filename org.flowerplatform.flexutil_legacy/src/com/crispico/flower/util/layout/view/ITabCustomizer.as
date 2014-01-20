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
package com.crispico.flower.util.layout.view {
	import com.crispico.flower.flexdiagram.contextmenu.FlowerContextMenu;
	
	/**
	 * Interface that needs to be implemented in order to provide additional support for a view tab. <br>
	 * Objects that implements this interface must provide functionality for the following methods:
	 * <ul>
	 * 	<li> fillContextMenu() - adds to context menu additional actions. 	
	 * </ul>
	 * 
	 * @author Cristina	 * 
	 * 
	 */
	public interface ITabCustomizer {
		
		/**
		 * 
		 */
		 function fillContextMenu(contextMenu:FlowerContextMenu):void;
	}
}