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
package org.flowerplatform.flex_client.core.node.event {
	import flash.events.Event;
	
	import org.flowerplatform.flex_client.core.editor.remote.Node;

	/**
	 * Dispatched when <code>Node</code>s are being removed.
	 *  
	 * @author Cristina Constantinescu
	 */
	public class NodeRemovedEvent extends Event {
		
		public static const NODE_REMOVED:String = "NodeRemovedEvent";
		
		public var node:Node;
		
		public function NodeRemovedEvent(node:Node) {
			super(NODE_REMOVED, false, false);
			this.node = node;
		}
		
	}
}