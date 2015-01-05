/* license-start
 * 
 * Copyright (C) 2008 - 2014 Crispico Software, <http://www.crispico.com/>.
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
package org.flowerplatform.flexutil.selection {
	import flash.events.Event;
	
	import mx.collections.IList;
	
	
	/**
	 * @see SelectionManager
	 * @author Cristian Spiescu
	 */
	public class SelectionChangedEvent extends Event {
		
		public static const SELECTION_CHANGED:String = "selectionChanged";
		
		public var selectionProvider:ISelectionProvider;
		
		public var selection:IList;
		
		public var selectionForServer:IList;
		
		public function SelectionChangedEvent() {
			super(SELECTION_CHANGED);
		}
	}
}