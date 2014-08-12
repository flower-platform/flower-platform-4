package org.flowerplatform.flex_client.team.git.action
{
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flex_client.team.git.GitConstants;
	import org.flowerplatform.flex_client.team.git.ui.CloneRepoWizardView;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	/**
	 *@author Alina Bratu
	 */
	public class CloneRepoAction extends ActionBase {
		
		public function CloneRepoAction() {
			super();
			label = Resources.getMessage("git.cloneRepo.title");
			icon = Resources.cloneRepoIcon;
		}
		
		override public function get visible():Boolean {
			if (selection != null && selection.length == 1 && selection.getItemAt(0) is Node) {
				var node:Node = Node(selection.getItemAt(0));
				return (node.type == GitConstants.GIT_REPO_TYPE && !node.getPropertyValue(GitConstants.IS_GIT_REPOSITORY));
			}
			return false;
		}
		
		override public function run():void {
			var node:Node = Node(selection.getItemAt(0));	
			
			var view:CloneRepoWizardView = new CloneRepoWizardView();
			view.nodeUri = node.nodeUri;
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()				
				.setViewContent(view)
				.setWidth(500)
				.setHeight(400)
				.setTitle(label)	
				.setIcon(icon)
				.show();			
		}
	}
}