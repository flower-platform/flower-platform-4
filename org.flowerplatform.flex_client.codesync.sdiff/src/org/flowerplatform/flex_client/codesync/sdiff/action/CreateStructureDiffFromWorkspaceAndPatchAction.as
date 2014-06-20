package org.flowerplatform.flex_client.codesync.sdiff.action {
	
	import org.flowerplatform.flex_client.codesync.CodeSyncConstants;
	import org.flowerplatform.flex_client.codesync.sdiff.CreateStructureDiffDialog;
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	/**
	 * @author Mariana Gheorghe
	 */ 
	public class CreateStructureDiffFromWorkspaceAndPatchAction extends ActionBase {
		
		public function CreateStructureDiffFromWorkspaceAndPatchAction() {
			super();
			
			label = "Create Structure Diff from Workspace and Patch";
		}
		
		override public function get visible():Boolean {
			if (selection.length == 1 && selection.getItemAt(0) is Node) {
				var node:Node = Node(selection.getItemAt(0));
				if (node.type == CodeSyncConstants.CODESYNC) {
					return true;
				}
				return CorePlugin.getInstance().nodeTypeDescriptorRegistry.getOrCreateTypeDescriptor(node.type)
					.categories.getItemIndex(CodeSyncConstants.CATEGORY_CODESYNC) >= 0;
			}
			return false;
		}
		
		override public function run():void {
			var node:Node = Node(selection.getItemAt(0));
			var dialog:CreateStructureDiffDialog = new CreateStructureDiffDialog();
			var index:int = node.nodeUri.indexOf("|");
			if (index < 0) {
				index = node.nodeUri.length;
			}
			dialog.repo = node.nodeUri.substring(node.nodeUri.indexOf(":") + 1, index);
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
				.setViewContent(dialog)
				.setWidth(800)
				.setHeight(500)
				.setTitle(label)
				.setIcon(icon)
				.show();
		}
		
	}
}