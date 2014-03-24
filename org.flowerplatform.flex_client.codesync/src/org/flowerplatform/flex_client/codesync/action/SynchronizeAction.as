package org.flowerplatform.flex_client.codesync.action {
	
	import org.flowerplatform.flex_client.codesync.CodeSyncPlugin;
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.action.DiagramShellAwareActionBase;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class SynchronizeAction extends DiagramShellAwareActionBase {
		
		public function SynchronizeAction() {
			super();
			label = CodeSyncPlugin.getInstance().getMessage("codesync.action.synchronize");
		}
		
		override public function get visible():Boolean {
			if (selection != null && selection.length == 1 && selection.getItemAt(0) is Node) {
				return true;
			}
			return false;
		}
		
		override public function run():void {
			var node:Node = Node(selection.getItemAt(0));
			CorePlugin.getInstance().serviceLocator.invoke("codeSyncOperationsService.synchronize", [node.resource]);
		}
		
	}
}