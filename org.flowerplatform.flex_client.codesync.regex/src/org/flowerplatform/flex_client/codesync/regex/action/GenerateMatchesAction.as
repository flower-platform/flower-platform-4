package org.flowerplatform.flex_client.codesync.regex.action {
	import org.flowerplatform.flex_client.codesync.regex.CodeSyncRegexConstants;
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexdiagram.mindmap.MindMapRootModelWrapper;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	public class GenerateMatchesAction extends ActionBase {
				
		public function GenerateMatchesAction() {
			super();
			preferShowOnActionBar = true;
			label = Resources.getMessage("regex.generate");
			icon = Resources.getResourceUrl("images/core/refresh_blue.png");
		}
		
		override public function get visible():Boolean {			
			if (selection == null || selection.length != 1) {
				return false;
			}
			var obj:Object = selection.getItemAt(0);
			if (obj is MindMapRootModelWrapper) {
				obj = MindMapRootModelWrapper(obj).model;
			}
			return obj is Node && 
				(Node(obj).type == CodeSyncRegexConstants.REGEX_CONFIG_TYPE 
					|| Node(obj).type == CodeSyncRegexConstants.REGEX_MACRO_TYPE 
					|| Node(obj).type == CodeSyncRegexConstants.REGEX_TYPE);
		}
		
		override public function run():void {
			var node:Node = Node(selection.getItemAt(0));									
			
			CorePlugin.getInstance().serviceLocator.invoke("codeSyncRegexService.generateMatches", [node.nodeUri]);
		}
				
	}
}