package org.flowerplatform.flex_client.team.git.action
{
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flex_client.team.git.GitConstants;
	import org.flowerplatform.flex_client.team.git.ui.CloneRepoWizardView;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;

	public class CloneRepoAction extends ActionBase {
		
		public function CloneRepoAction() {
			super();
			label = Resources.getMessage("git.cloneRepo.title");
			icon = Resources.cloneRepoIcon;
//			orderIndex = 310;
		}
		
		override public function get visible():Boolean {
			
			if (selection != null && selection.length == 1 && selection.getItemAt(0) is Node) {
				var type:String = Node(selection.getItemAt(0)).type;
				if (type == GitConstants.GIT_REPO_TYPE) {
					return true;
				}
				return false;
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