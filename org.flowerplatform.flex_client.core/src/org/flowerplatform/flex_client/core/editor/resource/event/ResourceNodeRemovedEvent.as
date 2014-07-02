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
	 * Dispatched by <code>NodeRegistry</code> when a resource node is removed/unlinked from resourceNodeIds to nodeRegistries map.
	 * 
	 * <p>
	 * Listeners can add additional behavior like collapsing a node in case of a mind map structure.
	 * 
	 * @author Cristina Constantinescu
	 */ 
	public class ResourceNodeRemovedEvent extends Event {
		
		public static const REMOVED:String = "ResourceNodeRemoved";
		
		public var resourceNodeId:String;
		
		public function ResourceNodeRemovedEvent(resourceNodeId:String) {
			super(REMOVED, false, false);
			this.resourceNodeId = resourceNodeId;
		}
		
	}
}