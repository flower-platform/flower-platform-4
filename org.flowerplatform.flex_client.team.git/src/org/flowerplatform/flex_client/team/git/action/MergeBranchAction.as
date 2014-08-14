package org.flowerplatform.flex_client.team.git.action {
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flex_client.team.git.GitConstants;
	import org.flowerplatform.flex_client.team.git.GitHistoryConstants;
	import org.flowerplatform.flex_client.team.git.ui.MergeBranchView;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	/**
	 * @author Tita Andreea
	 */
	public class MergeBranchAction extends ActionBase {
		
		public var useNodeAsCommitId:Boolean;
		
		public function MergeBranchAction(useNodeAsCommitId:Boolean=false) {
			super();
			this.useNodeAsCommitId = useNodeAsCommitId;
			icon = Resources.mergeBranch;
			
			if (useNodeAsCommitId) {
				setOrderIndex(int(Resources.getMessage('flex_client.team.git.action.history.sortIndex.Merge')));
			}
		}
		
		override public function  get visible():Boolean {
			if (selection != null && selection.length == 1 && selection.getItemAt(0) is Node) {
				if (!useNodeAsCommitId) {
					var node:Node = Node(selection.getItemAt(0));
					if (CorePlugin.getInstance().nodeTypeDescriptorRegistry.getOrCreateTypeDescriptor(node.type).categories.getItemIndex(GitConstants.GIT_REF_CATEGORY) >= 0 
						|| (node.type == GitConstants.GIT_REPO_TYPE && node.getPropertyValue(GitConstants.IS_GIT_REPOSITORY))) {
						
						if (node.getPropertyValue(GitConstants.IS_CHECKEDOUT) || node.type == GitConstants.GIT_REPO_TYPE) {
							label = Resources.getMessage("flex_client.team.git.action.mergeBranch.label");
						} else { 
							label = Resources.getMessage("flex_client.team.git.action.mergeBranch");
						}
						return true;
					}	
				} else {
					label = Resources.getMessage("flex_client.team.git.action.mergeBranch");
					return true;
				}
			}
			return false;
		}
		
		public function callMergeAction (nodeUri:String, squash:Boolean, commit:Boolean, fastForwardUpdate:int, idCommit:String):void {
			CorePlugin.getInstance().serviceLocator.invoke("GitService.mergeBranch",[nodeUri, squash, commit, fastForwardUpdate, idCommit],
				function(result:String):void {
					FlexUtilGlobals.getInstance().messageBoxFactory.createMessageBox()
					.setText(result)
					.setTitle(Resources.getMessage('info'))
					.setWidth(300)
					.setHeight(200)
					.showMessageBox();
				});
		}
		
		override public function run():void {
			var node:Node = Node(selection.getItemAt(0));
			if (!useNodeAsCommitId) {
				if (node.getPropertyValue(GitConstants.IS_CHECKEDOUT) || node.type == GitConstants.GIT_REPO_TYPE) {
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
				
				if ((node.type == GitConstants.GIT_REMOTE_BRANCH_TYPE || node.type == GitConstants.GIT_LOCAL_BRANCH_TYPE) && !node.getPropertyValue(GitConstants.IS_CHECKEDOUT)) {				
					callMergeAction(node.nodeUri, false, true, 0, null);
				}
			} else {
				callMergeAction(node.nodeUri, false, true, 0, node.getPropertyValue(GitHistoryConstants.ID));
			}
		}
	}
}
