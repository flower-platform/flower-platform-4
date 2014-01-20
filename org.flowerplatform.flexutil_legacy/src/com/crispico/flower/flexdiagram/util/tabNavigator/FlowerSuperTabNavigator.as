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
	import flexlib.containers.SuperTabNavigator;
	import flexlib.controls.SuperTabBar;
		
	/**
	 * This graphical component knows how to show tabs with icons that were set using their URLs.
	 * The URLs can be set to each child, when it is added to tabNavigator, by using the "iconURL" style.
	 * If a child has set this style and also the embed icon, the style has bigger priority.
	 * 
	 * @author Cristina
	 * 
	 */
	public class FlowerSuperTabNavigator extends SuperTabNavigator {

	    /**	 
	     * The tab navigator will work with a <code>CustomSuperTabBar</code> 
	     * instead of a <code>SuperTabBar</code>
	     * 
	     */
	    override protected function createTabBar():SuperTabBar {
	      return new FlowerSuperTabBar();
	    }
	}
	
}