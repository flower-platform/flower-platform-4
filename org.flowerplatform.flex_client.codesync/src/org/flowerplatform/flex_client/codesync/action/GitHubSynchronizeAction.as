package org.flowerplatform.flex_client.codesync.action {
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.mindmap.action.DiagramShellAwareActionBase;
	import org.flowerplatform.flex_client.core.mindmap.remote.Node;
	
	/**
	 * Temp. Should be replaced with a generic sync mechanism.
	 * 
	 * @author Mariana Gheorghe
	 */
	public class GitHubSynchronizeAction extends DiagramShellAwareActionBase {
		
		public function GitHubSynchronizeAction() {
			super();
			
			label = "GitHub Sync";
		}
		
		override public function get visible():Boolean {
			if (selection != null && selection.length == 1 && selection.getItemAt(0) is Node) {
				return true;
			}
			return false;
		}
		
		override public function run():void {
			var node:Node = Node(selection.getItemAt(0));
			CorePlugin.getInstance().serviceLocator.invoke("gitHubOperationsService.synchronize", [node.fullNodeId]);
		}
		
	}
}