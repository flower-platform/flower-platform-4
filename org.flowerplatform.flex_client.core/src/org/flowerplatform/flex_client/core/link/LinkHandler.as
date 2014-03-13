package org.flowerplatform.flex_client.core.link {
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.mindmap.remote.Node;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	
	public class LinkHandler implements ILinkHandler {
		
		public static const OPEN_RESOURCES:String = "openResources";
		public static const SELECT_RESOURCE_AT_INDEX:String = "selectResourceAtIndex";
		
		public var viewId:String;
		
		public function LinkHandler(viewId:String) {
			this.viewId = viewId;
		}
		
		public function handleLink(command:String, parameters:String):void {
			if (command == OPEN_RESOURCES) {
				// parameters format = file1,file2,file3|selectResourceAtIndex=1
				var files:String = parameters;
				var index:String;
				
				if (parameters.lastIndexOf("|") != -1) { // index exists
					files = parameters.split("|")[0];
					index = parameters.split("|")[1];	
					if (index.match(SELECT_RESOURCE_AT_INDEX + "=[0-9]")) {
						index = index.substring(index.lastIndexOf("=") + 1);
					}					
				}
				
				for each (var file:String in files.split(",")) {
					var view:ViewLayoutData = new ViewLayoutData();
					var root:Node = new Node();
					root.type = CorePlugin.RESOURCE_TYPE;
					root.idWithinResource = file;
					view.customData = root.fullNodeId;
					view.isEditor = true;
					view.viewId = viewId;
					FlexUtilGlobals.getInstance().workbench.addEditorView(view, true);
				}
			}
		}
	}
}