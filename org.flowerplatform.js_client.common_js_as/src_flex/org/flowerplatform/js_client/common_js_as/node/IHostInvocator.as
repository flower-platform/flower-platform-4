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
package org.flowerplatform.js_client.common_js_as.node {
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.flex_client.core.editor.remote.FullNodeIdWithChildren;
	
	/**
	 * Implemented within the host system. Some of the methods are factory methods, i.e.
	 * the host implementation creates instances that will be managed by the JS code. And
	 * other methods perform various tasks within the host system.
	 *
	 * @author Cristina Constantinescu
	 */
	public interface IHostInvocator {
		
		function createFullNodeIdWithChildrenInstance():FullNodeIdWithChildren;
		
		function createMapInstance():Object;
		function addInMap(map:Object, key:String, value:Object):void;
		
		function showMessageBox(titleKeyMessage:String, textKeyMessage:String, textParams:Array):void;
			
		function createListInstance():ArrayCollection;
		function contains(list:ArrayCollection, item:Object):Boolean;
		function getLength(list:ArrayCollection):int;		
		function addItem(list:ArrayCollection, item:Object):void;		
		function addItemAt(list:ArrayCollection, item:Object, index:int):void;		
		function getItemAt(list:ArrayCollection, index:int):Object;		
		function getItemIndex(list:ArrayCollection, item:Object):int;		
		function removeItemAt(list:ArrayCollection, index:int):Object;
		
	}
}
