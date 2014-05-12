/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
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
 * license-end
 */
package org.flowerplatform.flexutil.text {
	
	import mx.managers.IFocusManagerComponent;
	
	import spark.components.Label;
	
	/**
	 * Label can be used in Item Renderer component.
	 * 
	 * <p>
	 * Implements <code>IFocusManagerComponent</code>
	 * to dispatch focus on parent hierarchy.
	 * Needed to activate the view in workbench when the user
	 * selects an item from list.
	 * 
	 * @author Cristina Constantinescu
	 */ 
	public class FocusableLabel extends Label implements IFocusManagerComponent {	
	}
	
}