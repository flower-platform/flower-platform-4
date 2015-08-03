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
package org.flowerplatform.jsutil.service {
	
	import com.crispico.xopsOld.XopsPlugin;
	
	import flash.utils.Dictionary;
	
	import mx.collections.IList;
	import mx.messaging.ChannelSet;
	import mx.rpc.AbstractOperation;
	import mx.rpc.remoting.RemoteObject;
	
	import org.flowerplatform.flexutil.service.ServiceLocator;
	import org.flowerplatform.flexutil.service.ServiceResponder;

	/**
	 * Custom behavior to get updates registered after each message invocation.
	 * 
	 * <p>
	 * Before sending message to server, adds additional headers like timestamp of last update, full resourceNode id.
	 * These headers will be used on server to get a list of latest updates.
	 * <p>
	 * The server result contains:
	 * <ul>
	 * 	<li> <code>MESSAGE_RESULT</code> -> the message invocation result; it will be handled by <code>response.resultHandler</code>
	 * 	<li> <code>UPDATES</code> (optional) -> map of full resourceNode id to a list of updates that will be applied to the corresponding processor
	 * </ul>
	 * 
	 * @author Cristina Constantinescu
	 * @author Mariana Gheorghe
	 */ 
	public class UpdatesProcessingServiceLocator extends ServiceLocator {
		
		public function UpdatesProcessingServiceLocator(channelSet:ChannelSet) {
			super(channelSet);
		}

		public static function getDiffUpdatesRequest():Object {
			var diffUpdateInfo:Object = new Object();
			var channelsData:* = XopsPlugin.instance.entityRegistryManager.getNotificationChannelsData();
			diffUpdateInfo.notificationChannels = channelsData;
			
			return diffUpdateInfo;
		}
		
		public static function processDiffUpdates(updates:Object):void {
			for (var channel:String in updates) {
				var channelUpdates:IList = updates[channel];
				for (var i:int = 0; i < channelUpdates.length; i++) {
					var update:* = channelUpdates.getItemAt(i);
					XopsPlugin.instance.entityRegistryManager.processDiffUpdate(channel, update);
				}	
			}
		}
		
		/**
		 * Uses <code>UpdatesProcessingRemoteObject</code> to instantiate the remoteObject.
		 */
		override protected function createRemoteObject(serviceId:String):RemoteObject {
			return new UpdatesProcessingRemoteObject(serviceId);
		}
		
		/**
		 * Adds specific headers to operation.
		 */	
		override protected function getOperation(serviceId:String, name:String):AbstractOperation {
			var operation:UpdatesProcessingOperation = UpdatesProcessingOperation(super.getOperation(serviceId, name));
			
			var diffUpdatesRequest:Object = getDiffUpdatesRequest();
			
			var headers:Dictionary = new Dictionary();
			headers["diffUpdatesRequest"] = new DiffUpdatesRequest(XopsPlugin.instance.entityRegistryManager.getNotificationChannelsData() as Array);
			operation.messageHeaders = headers;
			
			return operation;
		}
		
		override public function resultHandler(result:Object, responder:ServiceResponder):void {
			if (result.hasOwnProperty("updates")) { // updates exists, process them
				processDiffUpdates(result.updates);
			}
			
			// get message invocation result and send it to be processed by resultHandler
			var messageResult:Object = result["originalResult"];
			if (responder.resultHandler != null) {
				responder.resultHandler(messageResult);
			}
		}
		
//		override public function faultHandler(fault:Fault, responder:ServiceResponder):void {
//			if (fault.faultCode == "Channel.Call.Failed" /*|| fault.faultCode == "Client.Error.MessageSend")*/) {
//				if (communicationErrorViewContent == null) {
//					communicationErrorViewContent = new ReconnectingViewContent();
//					FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
//						.setViewContent(communicationErrorViewContent)
//						.showModalOverAllApplication();
//				}
//			} else if (fault.faultCode == "Client.Error.MessageSend") {
//				if (communicationErrorViewContent == null) {
//					if (FlexUtilGlobals.getInstance().clientCommunicationErrorViewContent == null) {
//						communicationErrorViewContent = new ReconnectingViewContent();
//					} else {
//						communicationErrorViewContent = new (FlexUtilGlobals.getInstance().clientCommunicationErrorViewContent)();
//					}
//					FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
//						.setViewContent(communicationErrorViewContent)
//						.showModalOverAllApplication();
//				}
//			}else {
//				super.faultHandler(fault, responder);
//			}
//		}
	
	}
}
