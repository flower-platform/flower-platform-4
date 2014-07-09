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
package org.flowerplatform.flexutil.focusable_component {
	
	import mx.managers.IFocusManagerComponent;
	
	import spark.components.RichText;
	
	/** 
	 * Implements <code>IFocusManagerComponent</code>
	 * to dispatch focus on parent hierarchy.
	 * 
	 * @author Cristina Constantinescu
	 */ 
	public class FocusableRichText extends RichText implements IFocusManagerComponent {
		
	}
}