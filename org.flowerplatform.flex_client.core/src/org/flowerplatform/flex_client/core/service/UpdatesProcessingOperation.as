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
package org.flowerplatform.flex_client.core.service {
	import flash.utils.Dictionary;
	
	import mx.core.mx_internal;
	import mx.messaging.messages.IMessage;
	import mx.rpc.AbstractService;
	import mx.rpc.AsyncToken;
	import mx.rpc.remoting.Operation;
	
	use namespace mx_internal;
	
	/**
	 * Besides super functionality, adds additional headers before invoking the message.
	 * 
	 * @see UpdatesProcessingServiceLocator
	 * @author Cristina Constantinescu
	 */ 
	public class UpdatesProcessingOperation extends Operation {
		
		public var messageHeaders:Dictionary;
		
		public function UpdatesProcessingOperation(remoteObject:AbstractService = null, name:String = null) {
			super(remoteObject, name);
		}
		
		override mx_internal function invoke(message:IMessage, token:AsyncToken = null):AsyncToken {
			if (messageHeaders != null) {
				for (var key:Object in messageHeaders) {
					message.headers[key] = messageHeaders[key];
				}						
			}
			return super.mx_internal::invoke(message, token);
		}
		
	}
}