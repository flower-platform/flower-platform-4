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
	import flash.display.DisplayObject;
	import flash.events.Event;
	
	import mx.core.UIComponent;
	
	/**
	 * Dispatched by <code>Workbench</code> each time the active view changes.
	 * 
	 * @see ActiveViewList
	 * 
	 * @author Cristina
	 */
	public class ActiveViewChangedEvent extends Event {
	
		public static const ACTIVE_VIEW_CHANGED:String = "ActiveViewChangedEvent";
		
		public var newView:UIComponent;
		
		public var oldView:UIComponent;
		
		public function ActiveViewChangedEvent(newView:UIComponent, oldView:UIComponent):void {
			super(ACTIVE_VIEW_CHANGED);
			this.oldView = oldView;
			this.newView = newView;
		}

	}
}