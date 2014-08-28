/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * license-end
 */
package org.flowerplatform.flex_client.team.git.action {
	
	import mx.collections.IList;
	
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
	public class MergeAction extends ActionBase {
		
		public static var ID:String = "org.flowerplatform.flex_client.team.git.action.MergeAction";
		
		public var useNodeAsCommitId:Boolean;
		
		public function MergeAction(useNodeAsCommitId:Boolean=false) {
			super();
			this.useNodeAsCommitId = useNodeAsCommitId;
			icon = Resources.mergeBranch;
			orderIndex = 360;
		}
		
		override public function  get visible():Boolean {
			if (!useNodeAsCommitId) {
				var node:Node = Node(selection.getItemAt(0));
				if (node.type == GitConstants.GIT_REPO_TYPE && !node.getPropertyValue(GitConstants.IS_GIT_REPOSITORY)) {
					// not a git repository
					return false;
				}	
				var categories:IList = CorePlugin.getInstance().nodeTypeDescriptorRegistry.getOrCreateTypeDescriptor(node.type).categories;				
				if (categories.getItemIndex(GitConstants.GIT_REF_CATEGORY) >= 0 && !node.getPropertyValue(GitConstants.IS_CHECKEDOUT)) {
					// Merge...
					label = Resources.getMessage("flex_client.team.git.action.mergeBranch");
				} else {	
					// Merge
					label = Resources.getMessage("flex_client.team.git.action.mergeBranch.label");
				}				
			} else {
				label = Resources.getMessage("flex_client.team.git.action.mergeBranch");					
			}
			return true;
		}

		public function callMergeAction(nodeUri:String, squash:Boolean, commit:Boolean, fastForwardUpdate:int, idCommit:String):void {
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
				if ((node.type == GitConstants.GIT_REMOTE_BRANCH_TYPE || node.type == GitConstants.GIT_LOCAL_BRANCH_TYPE) && !node.getPropertyValue(GitConstants.IS_CHECKEDOUT)) {				
					callMergeAction(node.nodeUri, false, true, 0, null);
				} else {
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
			} else {
				callMergeAction(node.nodeUri, false, true, 0, node.getPropertyValue(GitHistoryConstants.ID));
			}
		}
	}
}
