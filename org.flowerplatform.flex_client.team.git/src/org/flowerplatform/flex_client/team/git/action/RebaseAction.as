package org.flowerplatform.flex_client.team.git.action{
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flex_client.team.git.GitConstants;
	import org.flowerplatform.flex_client.team.git.ui.RebaseView;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	public class RebaseAction extends ActionBase{
		
		public function RebaseAction() {
			super();
			label = Resources.getMessage('flex_client.team.git.action.rebase');
			icon = Resources.rebaseIcon;
		}
		
		override public function get visible():Boolean {
			if (selection.length == 1 && selection.getItemAt(0) is Node) {
				var node:Node = Node(selection.getItemAt(0));
				if (node.type == GitConstants.GIT_LOCAL_BRANCH_TYPE ||
					node.type == GitConstants.GIT_REMOTE_BRANCH_TYPE ){
					return true;
				}
			}	
			return false;
		}
		
		override public function run():void {
			var node:Node = Node(selection.getItemAt(0));
			var rebaseView:RebaseView = new RebaseView();
			rebaseView.node = Node(selection.getItemAt(0));
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
				.setViewContent(rebaseView)
				.setWidth(350)
				.setHeight(400)
				.setTitle(label)
				.setIcon(icon)
				.show();
		}
	}
}
