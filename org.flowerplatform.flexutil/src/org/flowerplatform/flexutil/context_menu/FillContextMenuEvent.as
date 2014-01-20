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
package org.flowerplatform.flexutil.context_menu {
	
	import flash.events.Event;
	
	import mx.collections.IList;
	
	import org.flowerplatform.flexutil.action.IAction;
	
	public class FillContextMenuEvent extends Event {
		
		public static const FILL_CONTEXT_MENU:String = "fillContextMenu";
		
		public var allActions:Vector.<IAction>;
		
		public var rootActionsAlreadyCalculated:IList;
		
		public var selection:IList;
		
		public var context:Object;
		
		public function FillContextMenuEvent() {
			super(FILL_CONTEXT_MENU);
		}
	}
}