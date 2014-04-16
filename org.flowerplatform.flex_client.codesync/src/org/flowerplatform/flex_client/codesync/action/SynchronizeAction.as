package org.flowerplatform.flex_client.codesync.action {
	
	import mx.collections.IList;
	
	import org.flowerplatform.flex_client.codesync.CodeSyncConstants;
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.action.DiagramShellAwareActionBase;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.resources.Resources;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class SynchronizeAction extends DiagramShellAwareActionBase {
		
		public function SynchronizeAction() {
			super();
			label = Resources.getMessage("codesync.action.synchronize");
			icon = Resources.synchronizeIcon;
			orderIndex = 400;
		}
		
		override public function get visible():Boolean {
			if (selection != null && selection.length == 1 && selection.getItemAt(0) is Node) { 
				var categorysList:IList = CorePlugin.getInstance().nodeTypeDescriptorRegistry.getExpectedTypeDescriptor(Node(selection.getItemAt(0)).type).categories;
				for (var i:int=0; i < categorysList.length; i++) {
					if (categorysList.getItemAt(i) == CodeSyncConstants.CATEGORY_CODESYNC) {
						return true;
					}
				}
			}
			return false;
		}
		
		override public function run():void {
			var node:Node = Node(selection.getItemAt(0));
			CorePlugin.getInstance().serviceLocator.invoke("codeSyncOperationsService.synchronize", [node.resource]);
		}
		
	}
}