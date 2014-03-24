package org.flowerplatform.flex_client.core.link {
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.action.OpenAction;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	
	public class LinkHandler implements ILinkHandler {
		
		public static const OPEN_RESOURCES:String = "openResources";
		public static const SELECT_RESOURCE_AT_INDEX:String = "selectResourceAtIndex";
		
		public static const RESOURCES_SEPARATOR:String = ",";
		
		public var viewId:String;
		
		public function LinkHandler(viewId:String) {
			this.viewId = viewId;
		}
		
		public function handleLink(command:String, parameters:String):void {
			if (command == OPEN_RESOURCES) {
				// parameters format = file1,file2,file3|selectResourceAtIndex=1
				var files:String = parameters;
				
				// TODO MG: deactivated because this is a full node id = type|resource|id
//				var index:String;
//				if (parameters.lastIndexOf("|") != -1) { // index exists
//					files = parameters.split("|")[0];
//					index = parameters.split("|")[1];	
//					if (index.match(SELECT_RESOURCE_AT_INDEX + "=[0-9]")) {
//						index = index.substring(index.lastIndexOf("=") + 1);
//					}					
//				}
				
				for each (var file:String in files.split(RESOURCES_SEPARATOR)) {
					CorePlugin.getInstance().serviceLocator.invoke("nodeService.getNode", [file], 
						function(node:Node):void {
							new OpenAction().open(node);
						});
				}
			}
		}
	}
}