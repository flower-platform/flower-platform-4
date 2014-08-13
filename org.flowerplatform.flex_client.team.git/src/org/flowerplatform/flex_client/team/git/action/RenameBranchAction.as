package org.flowerplatform.flex_client.team.git.action {
	
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flex_client.team.git.GitConstants;
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
			orderIndex = 310;
		}
		
		override public function get visible():Boolean {
			if (selection.length == 1 && selection.getItemAt(0) is Node) {
				var node:Node = Node(selection.getItemAt(0));
				return (node.type == GitConstants.GIT_LOCAL_BRANCH_TYPE || node.type == GitConstants.GIT_REMOTE_BRANCH_TYPE);
			}
			return false;
		}
		
		override public function run():void {
			var node:Node = Node(selection.getItemAt(0));
			var view:RenameBranchView = new RenameBranchView();
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