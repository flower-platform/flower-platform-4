package org.flowerplatform.flex_client.team.git.action {
	
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flex_client.team.git.ui.DeleteGitRepositoryView;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;

	public class DeleteGitRepositoryAction extends ActionBase {
		 
		public function DeleteGitRepositoryAction() {
			super();
			label = Resources.getMessage("flex_client.team.git.action.deleteGitRepository");
			icon = Resources.getResourceUrl("/images/mindmap/icons/button_cancel.png");
		}
		
		override public function get visible():Boolean {
			return true;
		}
		
		override public function run():void {
			var node:Node = Node(selection.getItemAt(0));
			var deleteGitRepoView:DeleteGitRepositoryView = new DeleteGitRepositoryView();
			var index:int = node.nodeUri.indexOf("|");
			if (index < 0) {
				index = node.nodeUri.length;
			}
			deleteGitRepoView.repo = node.nodeUri.substring(node.nodeUri.indexOf(":") + 1, index);
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
				.setViewContent(deleteGitRepoView)
				.setWidth(650)
				.setHeight(300)
				.setTitle(label)
				.setIcon(icon)
				.show();
		}
		
	}
}