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
package org.flowerplatform.flex_client.core.editor.update.event {
	import flash.events.Event;
	
	import mx.collections.ArrayList;
	import mx.collections.IList;
	
	import org.flowerplatform.flex_client.core.editor.remote.Node;

	/**
	 * Dispatched when a <code>Node</code> has been
	 * updated (from Java). 
	 * 
	 * <p>
	 * The difference between this event and PropertyChangeEvent 
	 * is that this event is dispatched only once per object, regardless of
	 * the fact that the copied values are new or not. PropertyChangeEvent
	 * is dispatched once per property (and not per object) only if the new
	 * value is different from the old one.
	 * 
	 * @author Cristian Spiescu
	 * @author Cristina Constantinescu
	 * 
	 */
	public class NodeUpdatedEvent extends Event {
		
		public static const NODE_UPDATED:String = "NodeUpdatedEvent";
		
		public var node:Node;
		
		public var allPropertiesUpdated:Boolean;
		public var updatedProperties:IList;
		public var removedProperties:IList;
		
		public function NodeUpdatedEvent(node:Node, updatedProperties:IList = null, removedProperties:IList = null, allPropertiesUpdated:Boolean = false) {
			super(NODE_UPDATED, false, false);
			this.node = node;
			this.updatedProperties = updatedProperties;
			this.removedProperties = removedProperties;
			this.allPropertiesUpdated = allPropertiesUpdated;
		}
		
		public function addUpdatedProperty(property:String):void {
			if (updatedProperties == null) {
				updatedProperties = new ArrayList();
			}
			updatedProperties.addItem(property);
		}
		
		public function addRemovedProperty(property:String):void {
			if (removedProperties == null) {
				removedProperties = new ArrayList();
			}
			removedProperties.addItem(property);
		}
		
	}
}