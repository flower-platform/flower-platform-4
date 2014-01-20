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
package com.crispico.flower.util.layout.event {
	import flash.events.Event;
	
	/**
	 * Dispathed by <code>Workbench</code> each time the layout data structure changes.
	 * 
	 * @see Workbench
	 * @author Cristina
	 * 
	 */ 
	public class LayoutDataChangedEvent extends Event {
		
		public static const LAYOUT_DATA_CHANGED:String="layoutData_changed";
		
		public function LayoutDataChangedEvent() {
			super(LAYOUT_DATA_CHANGED);
		}

	}
}