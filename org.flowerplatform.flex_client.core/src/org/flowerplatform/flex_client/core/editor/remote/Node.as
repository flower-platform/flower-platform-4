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
package org.flowerplatform.flex_client.core.editor.remote {
	import flash.utils.IDataInput;
	import flash.utils.IDataOutput;
	import flash.utils.IExternalizable;
	
	import mx.collections.ArrayCollection;
	import mx.utils.ObjectProxy;
	import mx.utils.object_proxy;
	
	use namespace object_proxy;
	
	/**
	 * Server -> client only. On the server side, the nodes are note linked together
	 * (parent/children). But here, on the client, these links are recalculated and 
	 * maintained by <code>NodeUpdateProcessor</code>.
	 * 
	 * <p>
	 * Implements IExternalizable to wrap properties in an ObjectProxy 
	 * (this way events will be dispatched individually for each property changed).
	 * 
	 * @author Cristina Constantinescu
	 */
	[Bindable]
	[RemoteClass(alias="org.flowerplatform.core.node.remote.Node")]
	public class Node implements IExternalizable {
		
		public var type:String;				
		public var nodeUri:String;
		
		private var _properties:Object;
		
		[Transient]
		public var parent:Node;
		
		[Transient]
		public var children:ArrayCollection;
		
		public function Node(nodeUri:String = null) {
			this.nodeUri = nodeUri;
			_properties = new ObjectProxy(new Object());
		}
					
		public function get properties():Object {
			return _properties;
		}
		
		public function get fragment():String {
			var index:int = nodeUri.lastIndexOf("#");
			if (index < 0) {
				return null;
			}
			return nodeUri.substring(index + 1);
		}
		
		public function get schemeSpecificPart():String {
			var index:int = nodeUri.indexOf(":");
			if (index < 0) {
				throw new Error("Invalid URI: " + nodeUri);
			}
			var ssp:String = nodeUri.substring(index + 1);
			index = ssp.lastIndexOf("#");
			if (index < 0) {
				return ssp;
			}
			return ssp.substring(0, index);
		}
		
		public function getPropertyValue(property:String):* {
			var propertyObj:Object = getPropertyValueOrWrapper(property);
			if (propertyObj is PropertyWrapper) {
				return PropertyWrapper(propertyObj).value;
			}
			return propertyObj;
		}
		
		public function getPropertyValueOrWrapper(property:String):* {
			return properties[property];		
		}
		
		public function readExternal(input:IDataInput):void { 
			type = input.readObject() as String; 
			nodeUri = input.readObject() as String;
			_properties = new ObjectProxy(input.readObject());
		} 
		
		public function writeExternal(output:IDataOutput):void { 
			output.writeObject(type); 
			output.writeObject(nodeUri); 
			output.writeObject(ObjectProxy(properties).object); 
		} 
		
		public function toString():String {
			return nodeUri;
		}
		
	}
}
