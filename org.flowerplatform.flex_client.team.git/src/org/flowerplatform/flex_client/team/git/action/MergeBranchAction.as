package org.flowerplatform.flex_client.team.git.action{
		
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flex_client.team.git.GitConstants;
	import org.flowerplatform.flex_client.team.git.ui.MergeBranchView;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	/**
	 * @author Tita Andreea
	 */
	
	public class MergeBranchAction extends ActionBase{
		
		public function MergeBranchAction(){
			super();
			icon = Resources.mergeBranch;
		}
		
		override public function  get visible():Boolean {
			if(selection != null && selection.length == 1 && selection.getItemAt(0) is Node) {
				var node:Node = Node(selection.getItemAt(0));
				if((node.getPropertyValue(GitConstants.IS_CHECKEDOUT) && (node.type == GitConstants.GIT_LOCAL_BRANCH_TYPE)) || node.type == GitConstants.GIT_REPO_TYPE) {
					label = Resources.getMessage("flex_client.team.git.action.mergeBranch.label");
					return true;
				}
				
				if((node.type == GitConstants.GIT_REMOTE_BRANCH_TYPE || node.type == GitConstants.GIT_LOCAL_BRANCH_TYPE) && !node.getPropertyValue(GitConstants.IS_CHECKEDOUT)) {
					label = Resources.getMessage("flex_client.team.git.action.mergeBranch");
					return true;
				}	
			}
			return false;
		}
		
		override public function run():void {
			var node:Node = Node(selection.getItemAt(0));
			if((node.getPropertyValue(GitConstants.IS_CHECKEDOUT) && node.type == GitConstants.GIT_LOCAL_BRANCH_TYPE) || node.type == GitConstants.GIT_REPO_TYPE ) {
				var viewMerge:MergeBranchView = new MergeBranchView();
				viewMerge.node = node;
				FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()			
					.setViewContent(viewMerge)
					.setWidth(500)
					.setHeight(550)
					.setIcon(icon)
					.setTitle(Resources.getMessage("flex_client.team.git.action.mergeBranch.label"))
					.show();
			}
			
			if( (node.type == GitConstants.GIT_REMOTE_BRANCH_TYPE || node.type == GitConstants.GIT_LOCAL_BRANCH_TYPE) && !node.getPropertyValue(GitConstants.IS_CHECKEDOUT)) {
				/* call merge action : squash = false, commit = true, fastForwardUpdate */ 
				CorePlugin.getInstance().serviceLocator.invoke("GitService.mergeBranch",[node.nodeUri, false, true, 0],
					function(result:String):void {
						FlexUtilGlobals.getInstance().messageBoxFactory.createMessageBox()
						.setText(result)
						.setTitle(Resources.getMessage('flex_client.team.git.mergeBranch.error'))
						.setWidth(300)
						.setHeight(200)
						.showMessageBox();
					});
			}
		}
	}
}