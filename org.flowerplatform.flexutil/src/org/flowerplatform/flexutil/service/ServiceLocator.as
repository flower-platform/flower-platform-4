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
package org.flowerplatform.flexutil.service {
	import flash.utils.Dictionary;
	
	import mx.messaging.ChannelSet;
	import mx.rpc.AbstractOperation;
	import mx.rpc.AsyncToken;
	import mx.rpc.Fault;
	import mx.rpc.remoting.RemoteObject;
	
	import org.flowerplatform.flexutil.FlexUtilAssets;
	import org.flowerplatform.flexutil.FlexUtilGlobals;

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
		
		public function faultHandler(fault:Fault, responder:ServiceResponder):void {
			if (responder.faultHandler != null) {
				responder.faultHandler(fault);
			} else {
				FlexUtilGlobals.getInstance().messageBoxFactory.createMessageBox()
					.setWidth(300)
					.setHeight(200)
					.setTitle(FlexUtilAssets.INSTANCE.getMessage("service.fault.title"))
					.setText(FlexUtilAssets.INSTANCE.getMessage("service.fault.message", [fault.faultString, fault.content]))
					.showMessageBox();
			}			
		}
		
		public function resultHandler(result:Object, responder:ServiceResponder):void {
			if (responder.resultHandler != null) {
				responder.resultHandler(result);
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
