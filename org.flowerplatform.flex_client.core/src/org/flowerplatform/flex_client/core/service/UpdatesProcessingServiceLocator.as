package org.flowerplatform.flex_client.core.service {
	
	import flash.events.IEventDispatcher;
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	import mx.messaging.ChannelSet;
	import mx.rpc.AbstractOperation;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.EditorFrontend;
	import org.flowerplatform.flex_client.core.mindmap.remote.Node;
	import org.flowerplatform.flex_client.core.mindmap.remote.update.ChildrenUpdate;
	import org.flowerplatform.flex_client.core.mindmap.remote.update.Update;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.service.ServiceLocator;
	import org.flowerplatform.flexutil.service.ServiceResponder;

	/**
	 * Custom behavior to get updates registered after each message invocation.
	 * 
	 * <p>
	 * Before sending message to server, adds additional headers like timestamp of last update, full rootNode id.
	 * These headers will be used on server to get a list of latest updates.
	 * <p>
	 * The server result contains:
	 * <ul>
	 * 	<li> <code>MESSAGE_RESULT</code> -> the message invocation result; it will be handled by <code>response.resultHandler</code>
	 * 	<li> <code>UPDATES</code> (optional) -> map of full rootNode id to a list of updates that will be applied to the corresponding processor
	 * </ul>
	 * 
	 * @author Cristina Constantinescu
	 * @author Mariana Gheorghe
	 */ 
	public class UpdatesProcessingServiceLocator extends ServiceLocator {
		
		public static const MESSAGE_RESULT:String = "messageResult";
		public static const UPDATES:String = "updates";
		
		public static const LAST_UPDATE_TIMESTAMP:String = "timestampOfLastUpdate";
		public static const ROOT_NODE_IDS:String = "rootNodeIds";
		public static const ROOT_NODE_IDS_NOT_FOUND:String = "rootNodeIdsNotFound";
		
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
			headers[LAST_UPDATE_TIMESTAMP] = CorePlugin.getInstance().rootNodeIdToEditors.lastUpdateTimestamp;
			headers[ROOT_NODE_IDS] = CorePlugin.getInstance().rootNodeIdToEditors.getRootNodeIds();
			operation.messageHeaders = headers;
			
			return operation;
		}
		
		override public function resultHandler(event:ResultEvent, responder:ServiceResponder):void {			
			var result:Object = event.result;
			
			// get message invocation result and send it to be processed by resultHandler
			var messageResult:Object = result[MESSAGE_RESULT];
			if (responder.resultHandler != null) {
				responder.resultHandler(messageResult);
			}
			
			if (result.hasOwnProperty(LAST_UPDATE_TIMESTAMP)) {
				CorePlugin.getInstance().rootNodeIdToEditors.lastUpdateTimestamp = result[LAST_UPDATE_TIMESTAMP];
			}
			
			if (result.hasOwnProperty(UPDATES)) { // updates exists, process them
				sendUpdatesToOpenEditors(result[UPDATES]);
			}
			
			if (result.hasOwnProperty(ROOT_NODE_IDS_NOT_FOUND)) {
				closeObsoleteEditos(result[ROOT_NODE_IDS_NOT_FOUND]);
			}
			
			CorePlugin.getInstance().updateTimer.restart();
		}
		
		private function sendUpdatesToOpenEditors(rootNodeIdToUpdates:Object):void {
			for (var rootNodeId:String in rootNodeIdToUpdates) {
				var updates:ArrayCollection = rootNodeIdToUpdates[rootNodeId];
				var editors:ArrayCollection = CorePlugin.getInstance().rootNodeIdToEditors.getEditors(rootNodeId);
				for each (var editor:EditorFrontend in editors) {
					updates = processUpdates(updates);
					editor.updateProcessor.rootNodeUpdatesHandler(editor.getContext(), updates);
				}
			}
		}
		
		/**
		 * Children that come with the <code>ChildrenUpdate</code>s must be cloned, so that a different instance
		 * is applied to each editor. Otherwise, the same node will be added to all the node registries, and then 
		 * any <code>NodeUpdatedEvent</code> on this node will be caught by all the renderers from all open editors.
		 */
		private function processUpdates(updates:ArrayCollection):ArrayCollection {
			for each (var update:Update in updates) {
				if (update is ChildrenUpdate) {
					var childrenUpdate:ChildrenUpdate = ChildrenUpdate(update);
					var node:Node = childrenUpdate.targetNode;
					childrenUpdate.targetNode = new Node(node.fullNodeId);
					childrenUpdate.targetNode.properties = node.properties;
				}
			}
			return updates;
		}
	
		private function closeObsoleteEditos(rootNodeIdsNotFound:ArrayCollection):void {
			var idsList:String = "";
			for each (var rootNodeId:String in rootNodeIdsNotFound) {
				var editors:ArrayCollection = CorePlugin.getInstance().rootNodeIdToEditors.getEditors(rootNodeId);
				for each (var editor:EditorFrontend in editors) {
					FlexUtilGlobals.getInstance().workbench.closeView(IEventDispatcher(editor.viewHost));
				}
				idsList += "\n* " + rootNodeId;
			}
			FlexUtilGlobals.getInstance().messageBoxFactory.createMessageBox()
			.setText(CorePlugin.getInstance().getMessage("editor.error.subscribe.message", [idsList]))
			.setTitle(CorePlugin.getInstance().getMessage("editor.error.subscribe.title"))
			.setWidth(300)
			.setHeight(200)
			.showMessageBox();
		}
		
	}
}
