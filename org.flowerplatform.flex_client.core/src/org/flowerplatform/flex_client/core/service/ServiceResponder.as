package org.flowerplatform.flex_client.core.service {
	import mx.rpc.IResponder;
	import mx.rpc.events.FaultEvent;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class ServiceResponder implements IResponder {
		
		protected var resultHandler:Function;
		
		protected var faultHandler:Function;
		
		protected var serviceLocator:ServiceLocator;
		
		public function ServiceResponder(serviceLocator:ServiceLocator, resultHandler:Function, faultHandler:Function) {
			this.serviceLocator = serviceLocator;
			this.resultHandler = resultHandler;
			this.faultHandler = faultHandler;
		}
		
		public function result(data:Object):void {
			if (resultHandler != null) {
				resultHandler(data);
			}
		}
		
		public function fault(info:Object):void {
			if (faultHandler != null) {
				faultHandler(info);
			} else {
				serviceLocator.faultHandler(FaultEvent(info));
			}
		}
	}
}