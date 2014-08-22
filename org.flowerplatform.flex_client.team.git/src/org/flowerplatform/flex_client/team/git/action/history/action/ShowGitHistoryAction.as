package org.flowerplatform.flex_client.team.git.action.history.action {
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flex_client.team.git.GitConstants;
	import org.flowerplatform.flex_client.team.git.action.history.GitHistoryViewProvider;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	/**
	 *	@author Vlad Bogdan Manica
	 */ 
	public class ShowGitHistoryAction extends ActionBase {
		
		public function ShowGitHistoryAction(){
			super();
			label = Resources.getMessage("gitHistory.action.show");
			icon = Resources.gitHistoryIcon;
			orderIndex = 1000;
		}
			
		override public function get visible():Boolean {
			if (selection.length == 1 && selection.getItemAt(0) is Node) {
				var node:Node = Node(selection.getItemAt(0));
				if (CorePlugin.getInstance().nodeTypeDescriptorRegistry.getOrCreateTypeDescriptor(node.type)
					.categories.getItemIndex(GitConstants.GIT_CATEGORY) >= 0) {
					return true;
				}
			}
			return false;
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