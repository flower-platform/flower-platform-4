package org.flowerplatform.flex_client.team.git.action {
	import org.flowerplatform.flex_client.codesync.CodeSyncConstants;
	import org.flowerplatform.flex_client.core.CoreConstants;
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flex_client.team.git.ui.RenameBranchView;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	/**
	 * @author Tita Andreea
	 */
	
	public class RenameBranchAction extends ActionBase {
		
		public function RenameBranchAction() {
			super();
			icon = Resources.renameBranch;
			label = Resources.getMessage("flex_client.team.git.action.renameBranch");
			
		}
		
		
		override public function get visible():Boolean {
			if (selection.length == 1 && selection.getItemAt(0) is Node) {
				var node:Node = Node(selection.getItemAt(0));
				/* you must know the type of nodes */
				if (node.type == CoreConstants.FILE_SYSTEM_NODE_TYPE) {
					return true;
				}
				//return CorePlugin.getInstance().nodeTypeDescriptorRegistry.getOrCreateTypeDescriptor(node.type)
				//	.categories.getItemIndex(CodeSyncConstants.CATEGORY_CODESYNC) >= 0;
			}
			return false;
		}
		
		
		override public function run():void {
			var node:Node = Node(selection.getItemAt(0));
			var view:RenameBranchView = new RenameBranchView();
			var index:int = node.nodeUri.indexOf("|");
			
			if (index < 0) {
				index = node.nodeUri.length;
			}
			view.pathNode = node.nodeUri.substring(node.nodeUri.indexOf(":") + 1, index);
			view.node = node;
			
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
				.setViewContent(view)
				.setWidth(450)
				.setHeight(200)
				.setTitle(label)
				.setIcon(icon)
				.show();	
		}
	}
}