package org.flowerplatform.flex_client.core.service {
	
	import com.crispico.flower.util.layout.Workbench;
	import com.crispico.flower.util.layout.WorkbenchViewHost;
	
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	import mx.messaging.ChannelSet;
	import mx.rpc.AbstractOperation;
	import mx.rpc.AsyncToken;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	
	import org.flowerplatform.flex_client.core.mindmap.MindMapEditorDiagramShell;
	import org.flowerplatform.flex_client.core.mindmap.MindMapEditorFrontend;
	import org.flowerplatform.flex_client.core.mindmap.remote.Node;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
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
	 * 	<li> <code>UPDATES</code> (optional) -> list of updates
	 * </ul>
	 * 
	 * @author Cristina Constantinescu
	 */ 
	public class UpdatesProcessingServiceLocator extends ServiceLocator {
		
		public static const MESSAGE_RESULT:String = "messageResult";
		public static const UPDATES:String = "updates";
		
		public static const LAST_UPDATE_TIMESTAMP:String = "timestampOfLastUpdate";
		public static const FULL_ROOT_NODE_ID:String = "fullRootNodeId";
		
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
			
			var diagramShell:MindMapEditorDiagramShell = getFirstMindMapEditorDiagramShell();
			if (diagramShell != null && diagramShell.rootModel != null) {
				var headers:Dictionary = new Dictionary();
				headers[LAST_UPDATE_TIMESTAMP] = diagramShell.updateProcessor.timestampOfLastRequest;				
				headers[FULL_ROOT_NODE_ID] = Node(diagramShell.getRoot(diagramShell.getNewDiagramShellContext())).fullNodeId;
				operation.messageHeaders = headers;
			}
			return operation;
		}
				
		override public function resultHandler(event:ResultEvent, responder:ServiceResponder):void {			
			var result:Object = event.result;
			
			// get message invocation result and send it to be processed by resultHandler
			var messageResult:Object = result[MESSAGE_RESULT];
			if (responder.resultHandler != null) {
				responder.resultHandler(messageResult);
			}
			
			if (result.hasOwnProperty(UPDATES)) { // updates exists, process them
				var updates:ArrayCollection = ArrayCollection(result[UPDATES]);
					
				var diagramShell:MindMapEditorDiagramShell = getFirstMindMapEditorDiagramShell();
				if (diagramShell != null) {
					diagramShell.updateProcessor.rootNodeUpdatesHandler(diagramShell.getNewDiagramShellContext(), updates);
				}
			}
		}
		
		// TODO CC: temporary code (when removing this code delete the flex_legacy dependency)
		private function getFirstMindMapEditorDiagramShell():MindMapEditorDiagramShell {
			if (Workbench(FlexUtilGlobals.getInstance().workbench).rootLayout == null) {
				return null;
			}
			var editors:ArrayCollection = new ArrayCollection();
			Workbench(FlexUtilGlobals.getInstance().workbench).getAllViewLayoutData(null, editors, true);
			for each (var editor:ViewLayoutData in editors) {
				var component:Object = FlexUtilGlobals.getInstance().workbench.getComponent(editor.viewId);
				if (component is WorkbenchViewHost) {
					component = WorkbenchViewHost(component).activeViewContent;
				}
				if (component is MindMapEditorFrontend) {
					return MindMapEditorDiagramShell(MindMapEditorFrontend(component).diagramShell);
				}
			}
			return null;
		}
		
	}
}