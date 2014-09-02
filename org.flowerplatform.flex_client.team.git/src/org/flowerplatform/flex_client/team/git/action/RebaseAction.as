package org.flowerplatform.flex_client.team.git.action{
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flex_client.team.git.GitConstants;
	import org.flowerplatform.flex_client.team.git.ui.RebaseView;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	/**
	 * @author Cojocea Marius Eduard
	 */	
	public class RebaseAction extends ActionBase{
		
		public static var ID:String = "org.flowerplatform.flex_client.team.git.action.RebaseAction";
		
		public function RebaseAction() {
			super();
			label = Resources.getMessage('flex_client.team.git.action.rebase');
			icon = Resources.rebaseIcon;
			orderIndex = 380;
		}
		
		override public function get visible():Boolean {
			var node:Node = Node(selection.getItemAt(0));
			if (node.type == GitConstants.GIT_REPO_TYPE && !node.getPropertyValue(GitConstants.IS_GIT_REPOSITORY)) { 
				// not a git repository
				return false;
			}
			return true;			
		}
		
		override public function run():void {
			var node:Node = Node(selection.getItemAt(0));
			var rebaseView:RebaseView = new RebaseView();
			rebaseView.node = Node(selection.getItemAt(0));
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
				.setViewContent(rebaseView)
				.setWidth(500)
				.setHeight(530)
				.setTitle(label)
				.setIcon(icon)
				.show();
		}
	}
}
