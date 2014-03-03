package org.flowerplatform.flex_client.core.service {
	
	import mx.collections.ArrayCollection;
	import mx.messaging.ChannelSet;
	import mx.rpc.events.ResultEvent;
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.mindmap.MindMapEditorDiagramShell;
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
		
		public function UpdatesProcessingServiceLocator(channelSet:ChannelSet) {
			super(channelSet);
		}
		
		override public function resultHandler(event:ResultEvent, responder:ServiceResponder):void {
			var result:Object = event.result;
			
			// get message invocation result and send it to be processed by resultHandler
			var messageResult:Object = result[MESSAGE_RESULT];
			if (responder.resultHandler != null) {
				responder.resultHandler(messageResult);
			}
			
			if (result.hasOwnProperty(UPDATES)) { // updates exists, process them
				var rootNodeIdToUpdates:Object = result[UPDATES];
				for (var rootNodeId:String in rootNodeIdToUpdates) {
					var updates:ArrayCollection = rootNodeIdToUpdates[rootNodeId];
					var diagramShell:MindMapEditorDiagramShell = CorePlugin.getInstance().fullRootNodeIdToDiagramShell[rootNodeId];
					if (diagramShell != null) {
						diagramShell.updateProcessor.rootNodeUpdatesHandler(updates);
					}
				}
			}
		}
	
	}
}
