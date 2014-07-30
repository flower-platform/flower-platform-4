package org.flowerplatform.flex_client.team.git.action{
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flex_client.team.git.GitConstants;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	public class RebaseAction extends ActionBase{
		
		public function RebaseAction() {
			super();
			label = Resources.getMessage('flex_client.team.git.action.reset');
			icon = Resources.resetIcon;
		}
		
		override public function get visible():Boolean {
			if (selection.length == 1 && selection.getItemAt(0) is Node) {
				var node:Node = Node(selection.getItemAt(0));
				if(node.type == GitConstants.GIT_LOCAL_BRANCHES_TYPE ||
					node.type == GitConstants.GIT_REMOTE_BRANCHES_TYPE ||
					node.type == GitConstants.GIT_TAGS_TYPES){
					return true;
				}
			}	
			return false;
			
			
		}
		
		override public function run():void {
			var node:Node = Node(selection.getItemAt(0));
			var resetView:ResetView = new ResetView();
			resetView.node = Node(selection.getItemAt(0));
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
				.setViewContent(resetView)
				.setWidth(500)
				.setHeight(530)
				.setTitle(label)
				.setIcon(icon)
				.show();
		}
	}
}
