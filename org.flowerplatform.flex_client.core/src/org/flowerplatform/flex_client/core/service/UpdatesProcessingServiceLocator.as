/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
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
	
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	import mx.messaging.ChannelSet;
	import mx.rpc.AbstractOperation;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	
	import org.flowerplatform.flex_client.core.CoreConstants;
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.node.IServiceInvocator;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.service.ServiceLocator;
	import org.flowerplatform.flexutil.service.ServiceResponder;
	import org.flowerplatform.flexutil.view_content_host.IViewContent;

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
	public class UpdatesProcessingServiceLocator extends ServiceLocator implements IServiceInvocator {
		
		private var communicationErrorViewContent:IViewContent;
		
		public function UpdatesProcessingServiceLocator(channelSet:ChannelSet) {
			super(channelSet);
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
			
			var headers:Dictionary = new Dictionary();
			var resourceSets:Array = CorePlugin.getInstance().nodeRegistryManager.getResourceSets();
			if (resourceSets.length > 0) {
				// a sorted list is sent to improve search performance on server
				headers[CoreConstants.RESOURCE_SETS] = new ArrayCollection(resourceSets.sort());
				headers[CoreConstants.RESOURCE_URIS] = new ArrayCollection(CorePlugin.getInstance().nodeRegistryManager.getResourceUris().sort());
			}
			headers[CoreConstants.LAST_UPDATE_TIMESTAMP] = CorePlugin.getInstance().lastUpdateTimestampOfServer;
			operation.messageHeaders = headers;
			
			return operation;
		}
		
		override public function resultHandler(event:ResultEvent, responder:ServiceResponder):void {			
			var result:Object = event.result;
			
			if (result.hasOwnProperty(CoreConstants.LAST_UPDATE_TIMESTAMP)) {
				CorePlugin.getInstance().lastUpdateTimestampOfServer = result[CoreConstants.LAST_UPDATE_TIMESTAMP];
				CorePlugin.getInstance().lastUpdateTimestampOfClient = new Date().time;
				
				CorePlugin.getInstance().debug_forceUpdateAction.updateLabel();
			}
			
			if (result.hasOwnProperty(CoreConstants.UPDATES)) { // updates exists, process them
				CorePlugin.getInstance().nodeRegistryManager.processUpdates(result[CoreConstants.UPDATES]);
			}
			
			if (result.hasOwnProperty(CoreConstants.RESOURCE_NODE_IDS_NOT_FOUND)) {
				CorePlugin.getInstance().nodeRegistryManager.unlinkResourceNodesForcefully(result[CoreConstants.RESOURCE_NODE_IDS_NOT_FOUND]);
			}
			
			CorePlugin.getInstance().updateTimer.restart();
			if (communicationErrorViewContent != null) {
				FlexUtilGlobals.getInstance().popupHandlerFactory.removePopup(communicationErrorViewContent);
				communicationErrorViewContent = null;
			}
			
			// get message invocation result and send it to be processed by resultHandler
			var messageResult:Object = result[CoreConstants.MESSAGE_RESULT];
			if (responder.resultHandler != null) {
				responder.resultHandler(messageResult);
			}
		}
		
		override public function faultHandler(event:FaultEvent, responder:ServiceResponder):void {
			if (event.fault.faultCode == "Channel.Call.Failed" /*|| event.fault.faultCode == "Client.Error.MessageSend")*/) {
				if (communicationErrorViewContent == null) {
					communicationErrorViewContent = new ReconnectingViewContent();
					FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
						.setViewContent(communicationErrorViewContent)
						.showModalOverAllApplication();
				}
			} else if (event.fault.faultCode == "Client.Error.MessageSend") {
				if (communicationErrorViewContent == null) {
					if (FlexUtilGlobals.getInstance().clientCommunicationErrorViewContent == null) {
						communicationErrorViewContent = new ReconnectingViewContent();
					} else {
						communicationErrorViewContent = new (FlexUtilGlobals.getInstance().clientCommunicationErrorViewContent)();
					}
					FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
						.setViewContent(communicationErrorViewContent)
						.showModalOverAllApplication();
				}
			}else {
				super.faultHandler(event, responder);
			}
		}

	}
}
