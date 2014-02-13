package org.flowerplatform.flex_client.core.service {
	import com.crispico.flower.util.layout.Workbench;
	import com.crispico.flower.util.layout.WorkbenchViewHost;
	
	import mx.collections.ArrayCollection;
	import mx.core.UIComponent;
	import mx.rpc.IResponder;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	
	import org.flowerplatform.flex_client.core.mindmap.MindMapEditorDiagramShell;
	import org.flowerplatform.flex_client.core.mindmap.MindMapEditorFrontend;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	
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
			/* this result contains 2 objects:
			* 1. messageInvocationResult:Object -> the actual result of method invocation
			* 2. updates:ArrayCollection -> all updates performed in method's execution body
			*/
			var result:Object = data.result;
			
			var messageInvocationResult:Object = result.messageInvocationResult;
			if (resultHandler != null) {
				resultHandler(messageInvocationResult);
			}
			
			if (result.hasOwnProperty("updates")) {
				var updates:ArrayCollection = ArrayCollection(result.updates);
				
				// TODO CC: temporary code (when removing this code delete the flex_legacy dependency)
				var editors:ArrayCollection = new ArrayCollection();
				Workbench(FlexUtilGlobals.getInstance().workbench).getAllViewLayoutData(null, editors, true);
				for each (var editor:ViewLayoutData in editors) {
					var component:Object = FlexUtilGlobals.getInstance().workbench.getComponent(editor.viewId);
					if (component is WorkbenchViewHost) {
						component = WorkbenchViewHost(component).activeViewContent;
					}
					if (component is MindMapEditorFrontend) {
						MindMapEditorDiagramShell(MindMapEditorFrontend(component).diagramShell).updateProcessor.rootNodeUpdatesHandler(updates);
					}
				}
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