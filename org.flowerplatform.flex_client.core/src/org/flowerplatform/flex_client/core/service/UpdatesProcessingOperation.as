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