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
package org.flowerplatform.flex_client.core.service {
	
	import mx.core.mx_internal;
	import mx.rpc.AbstractOperation;
	import mx.rpc.remoting.RemoteObject;
	
	/**
	 * Use <code>UpdatesProcessingOperation</code> as class instance for operation.
	 * 
	 * @see UpdatesProcessingServiceLocator
	 * @author Cristina Constantinescu
	 */
	public class UpdatesProcessingRemoteObject extends RemoteObject {
		
		public function UpdatesProcessingRemoteObject(destination:String = null) {
			super(destination);
		}
		
		override public function getOperation(name:String):AbstractOperation {			
			var op:UpdatesProcessingOperation = new UpdatesProcessingOperation(this, name);
			super.mx_internal::_operations[name] = op;
			op.mx_internal::asyncRequest = super.mx_internal::asyncRequest;
			
			return op;
		}
		
	}
}