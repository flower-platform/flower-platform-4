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
package org.flowerplatform.flexdiagram.event {
	import flash.events.Event;
	
	/**
	 * Dispatched by <code>DiagramShell</code> to show creation options (e.g. a menu with actions).
	 * 
	 * @author Cristina Constantinescu
	 */ 
	public class ExecuteDragToCreateEvent extends Event	{
		
		public static const DRAG_TO_CREATE_EVENT:String = "dragToCreateEvent";
		
		public var context:Object;
		
		/**
		 * If true, the tool will not be deactivated after dispatching this event. 
		 */ 
		public var shouldFinishToolJobAfterExecution:Boolean;
		
		public function ExecuteDragToCreateEvent(context:Object = null, shouldFinishToolJobAfterExecution:Boolean = false)	{
			super(DRAG_TO_CREATE_EVENT);
			this.context = context;
			this.shouldFinishToolJobAfterExecution = shouldFinishToolJobAfterExecution;
		}
		
	}
}