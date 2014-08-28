package org.flowerplatform.flex_client.team.git.history.action {
	
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flex_client.team.git.GitConstants;
	import org.flowerplatform.flex_client.team.git.history.GitHistoryViewProvider;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	/**
	 *	@author Vlad Bogdan Manica
	 */ 
	public class ShowGitHistoryAction extends ActionBase {
		
		public static const ID:String = "org.flowerplatform.flex_client.team.git.history.action.ShowGitHistoryAction";
		
		public function ShowGitHistoryAction(){
			super();
			label = Resources.getMessage("gitHistory.action.show");
			icon = Resources.gitHistoryIcon;
			orderIndex = 500;
		}
		
		override public function  get visible():Boolean {
			var node:Node = Node(selection.getItemAt(0));
			if (node.type == GitConstants.GIT_REPO_TYPE && !node.getPropertyValue(GitConstants.IS_GIT_REPOSITORY)) {
				// not a git repository
				return false;
			}	
			return true;
		}
		
		override public function run():void {
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
				.setViewIdInWorkbench(GitHistoryViewProvider.ID)
				.setWidth(975)
				.setHeight(550)
				.show();
		}
		
	}
}