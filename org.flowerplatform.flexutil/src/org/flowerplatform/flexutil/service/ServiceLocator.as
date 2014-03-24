package org.flowerplatform.flexutil.service {
	import flash.utils.Dictionary;
	
	import mx.controls.Alert;
	import mx.messaging.ChannelSet;
	import mx.rpc.AbstractOperation;
	import mx.rpc.AsyncToken;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;

	/**
	 * @author Cristian Spiescu
 	 * @author Cristina Constantinescu
	 */
	public class ServiceLocator {
		
		protected var channelSet:ChannelSet;
		
		protected var remoteObjects:Dictionary = new Dictionary();
		
		public function ServiceLocator(channelSet:ChannelSet) {
			this.channelSet = channelSet;			
		}
		
		public function addService(serviceId:String):void {
			var remoteObject:RemoteObject = createRemoteObject(serviceId);
			remoteObject.channelSet = channelSet;
			remoteObjects[serviceId] = remoteObject;
		}
		
		protected function createRemoteObject(serviceId:String):RemoteObject {
			return new RemoteObject(serviceId);
		}
		
		public function getRemoteObject(serviceId:String):RemoteObject {
			var remoteObject:RemoteObject = RemoteObject(remoteObjects[serviceId]);
			if (remoteObject == null) {
				throw new Error("Service not registered with id = " + serviceId);
			}
			return remoteObject;
		}
		
		public function faultHandler(event:FaultEvent, responder:ServiceResponder):void {
			if (responder.faultHandler != null) {
				responder.faultHandler(event);
			} else {
				Alert.show("Error while sending request to server: " + event.fault.faultString + "\n" + event.fault.content);
			}			
		}
		
		public function resultHandler(event:ResultEvent, responder:ServiceResponder):void {
			if (responder.resultHandler != null) {
				responder.resultHandler(event);
			}
		}
			
		/**
		 * @author Cristian Spiescu
		 * @author Cristina Constantinescu
		 */
		public function invoke(serviceIdAndMethodName:String, parameters:Array = null, resultCallback:Function = null, faultCallback:Function = null):void {
			if (serviceIdAndMethodName.indexOf(".") == -1) {
				throw new Error("serviceIdAndMethodName MUST have the following format: <service_id>.<method_name>!");
			}
			var tokens:Array = serviceIdAndMethodName.split(".");
			
			var operation:AbstractOperation = getOperation(tokens[0], tokens[1]);
			
			var token:AsyncToken = operation.send.apply(operation, parameters);
			token.addResponder(new ServiceResponder(this, resultCallback, faultCallback));
		}
		
		protected function getOperation(serviceId:String, name:String):AbstractOperation {
			return getRemoteObject(serviceId).getOperation(name);
		}
		
	}
}