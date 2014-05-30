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
package org.flowerplatform.flex_client.core.editor.remote {
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.flex_client.core.node.event.NodeUpdatedEvent;
	import org.flowerplatform.flexutil.Utils;
	
	/**
	 * Server -> client only. On the server side, the nodes are note linked together
	 * (parent/children). But here, on the client, these links are recalculated and 
	 * maintained by <code>NodeUpdateProcessor</code>.
	 * 
	 * @author Cristina Constantinescu
	 */
	[Bindable]
	[RemoteClass(alias="org.flowerplatform.core.node.remote.Node")]
	public class Node {
		
		private static const FULL_NODE_ID_SEPARATOR:String = "|";				
		
		private static const FULL_NODE_ID_PATTERN:RegExp = new RegExp("\\((.*?)\\|(\\(?.*\\)?)\\|(.*)\\)");
		
		private var cachedFullNodeId:String;
		
		private var _type:String;		
		private var _resource:String;		
		private var _idWithinResource:String;		
				
		private var _properties:Object;
		
		[Transient]
		public var parent:Node;
		
		[Transient]
		public var children:ArrayCollection;
		
		public function Node(fullNodeId:String = null) {
			if (fullNodeId != null) {
				// this case happens when a Node is created from client
				// so instantiate & populate required attributes
				
				var result:Object = FULL_NODE_ID_PATTERN.exec(fullNodeId);
				type = result[1];
				resource = result[2].length == 0 ? null : result[2];
				idWithinResource = result[3].length == 0 ? null : result[3];
				cachedFullNodeId = fullNodeId;
				
				properties = new Object();
			}
		}
		
		public function get type():String {
			return _type;
		}

		public function set type(value:String):void {
			_type = value;
			cachedFullNodeId = null;
		}

		public function get resource():String {
			return _resource;
		}
		
		public function set resource(value:String):void {
			_resource = value;
			cachedFullNodeId = null;
		}
		
		public function get idWithinResource():String {
			return _idWithinResource;
		}
		
		public function set idWithinResource(value:String):void {
			_idWithinResource = value;
			cachedFullNodeId = null;
		}
				
		public function get properties():Object	{
			return _properties;
		}
		
		public function set properties(value:Object):void {			
			_properties = value;
			
			this.dispatchEvent(new NodeUpdatedEvent(this, null, null, true));			
		}
		
		public function get fullNodeId():String {
			if (cachedFullNodeId == null) {
				cachedFullNodeId = "(" + Utils.defaultIfNull(type) + FULL_NODE_ID_SEPARATOR + Utils.defaultIfNull(resource) + FULL_NODE_ID_SEPARATOR + Utils.defaultIfNull(idWithinResource) + ")";
			}
			return cachedFullNodeId;
		}
		
		public function toString():String {
			return fullNodeId;
		}
		
	}
}
