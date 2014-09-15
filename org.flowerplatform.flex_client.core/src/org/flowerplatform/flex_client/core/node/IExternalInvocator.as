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
package org.flowerplatform.flex_client.core.node {
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.flex_client.core.editor.remote.FullNodeIdWithChildren;
	import org.flowerplatform.flex_client.core.node.remote.ServiceContext;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public interface IExternalInvocator {
		
		function getNewListInstance():ArrayCollection;
		
		function getNewFullNodeIdWithChildrenInstance():FullNodeIdWithChildren;
		
		function getServiceContextInstance():ServiceContext;
		
		function showMessageBox(titleKeyMessage:String, textKeyMessage:String, textParams:Array):void;
		
		function createUpdateEvent(source:Object, property:String, oldValue:Object, newValue:Object):Object;
		
		function addEventListener(source:Object, eventType:String, handler:Function):void;
		
		function removeEventListener(source:Object, eventType:String, handler:Function):void;
		
	}
}
