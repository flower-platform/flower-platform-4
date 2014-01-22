package org.flowerplatform.flex_client.core.service {
	import flash.utils.Dictionary;
	
	import mx.controls.Alert;
	import mx.messaging.ChannelSet;
	import mx.messaging.channels.AMFChannel;
	import mx.rpc.AbstractOperation;
	import mx.rpc.AsyncToken;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.remoting.RemoteObject;
	
	import org.flowerplatform.flexutil.FlexUtilGlobals;

	public class ServiceLocator {
		
		protected var channelSet:ChannelSet;
		
		protected var remoteObjects:Dictionary = new Dictionary();
		
		public function ServiceLocator() {
			channelSet = new ChannelSet();
			channelSet.addChannel(new AMFChannel(null, FlexUtilGlobals.getInstance().rootUrl + 'messagebroker/remoting-amf'));
		}
		
		public function addService(serviceId:String):void {
			var remoteObject:RemoteObject = new RemoteObject(serviceId);
			remoteObject.channelSet = channelSet;
			remoteObjects[serviceId] = remoteObject;
		}
		
		public function getRemoteObject(serviceId:String):RemoteObject {
			var remoteObject:RemoteObject = RemoteObject(remoteObjects[serviceId]);
			if (remoteObject == null) {
				throw new Error("Service not registered with id = " + serviceId);
			}
			return remoteObject;
		}
		
		public function faultHandler(event:FaultEvent):void {
			Alert.show("Error while sending request to server: " + event.fault.faultString + "\n" + event.fault.content);
		}
		
		public function invoke(serviceId:String, methodName:String, parameters:Array = null, resultCallback:Function = null, faultCallback:Function = null):void {
			var operation:AbstractOperation = getRemoteObject(serviceId).getOperation(methodName);
			var token:AsyncToken = operation.send.apply(operation, parameters);
			token.addResponder(new ServiceResponder(this, resultCallback, faultCallback));
		}
	}
}