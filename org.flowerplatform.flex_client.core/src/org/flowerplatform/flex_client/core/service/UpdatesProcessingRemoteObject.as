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