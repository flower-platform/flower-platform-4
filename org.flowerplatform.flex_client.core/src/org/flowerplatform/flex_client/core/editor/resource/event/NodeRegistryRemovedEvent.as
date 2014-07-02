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
package org.flowerplatform.flex_client.core.editor.resource.event {
	
	import flash.events.Event;
	
	/**
	 * Dispatched by <code>NodeRegistry</code> when it is removed/unlinked from resourceNodeIds to nodeRegistries map.
	 * 
	 * <p>
	 * Listeners can add additional behavior like closing the editor corresponding to a node registry.
	 * 
	 * @author Cristina Constantinescu
	 */ 
	public class NodeRegistryRemovedEvent extends Event {
		
		public static const REMOVED:String = "NodeRegistryRemovedEvent";
		
		public function NodeRegistryRemovedEvent() {
			super(REMOVED, false, false);
		}
		
	}
}