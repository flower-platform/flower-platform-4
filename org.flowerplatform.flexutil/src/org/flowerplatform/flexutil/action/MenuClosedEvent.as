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
package org.flowerplatform.flexutil.action {
	import flash.events.Event;
	
	/**
	 * Dispatched when the menu is closed, 
	 * by the menu system specific to the platform (web: ContextMenu; mobile: MobileHostBase). 
	 * 
	 * The event is dispatched on the top level app.
	 * 
	 * @author Cristina Constantinescu
	 */ 
	public class MenuClosedEvent extends Event {
		
		public static const MENU_CLOSED:String = "MENU_CLOSED";
				
		public function MenuClosedEvent() {
			super(MENU_CLOSED);
		}
	}
}