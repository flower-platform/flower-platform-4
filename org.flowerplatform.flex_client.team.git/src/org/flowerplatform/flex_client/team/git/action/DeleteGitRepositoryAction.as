/* license-start
 * 
 * Copyright (C) 2008 - 2014 Crispico Software, <http://www.crispico.com/>.
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
	
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flex_client.team.git.GitConstants;
	import org.flowerplatform.flex_client.team.git.ui.DeleteGitRepositoryView;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	/**
	 * @author Catalin Burcea
	 */
	public class DeleteGitRepositoryAction extends ActionBase {
		
		public static var ID:String = "org.flowerplatform.flex_client.team.git.action.DeleteGitRepositoryAction";
		 
		public function DeleteGitRepositoryAction() {
			super();
			label = Resources.getMessage("flex_client.team.git.action.deleteGitRepository");
			icon = Resources.deleteIcon;
			orderIndex = 515;
		}
		
		override public function get visible():Boolean {
			if (selection.length == 1 && selection.getItemAt(0) is Node) {
				var node:Node = Node(selection.getItemAt(0));				
				return node.type == GitConstants.GIT_REPO_TYPE && node.getPropertyValue(GitConstants.IS_GIT_REPOSITORY);
			}
			return false;
		}
		
		override public function run():void {
			var node:Node = Node(selection.getItemAt(0));
			var deleteGitRepoView:DeleteGitRepositoryView = new DeleteGitRepositoryView();
			deleteGitRepoView.repoNode = node;
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
				.setViewContent(deleteGitRepoView)
				.setWidth(450)
				.setHeight(150)
				.setTitle(label)
				.setIcon(icon)
				.show();
		}
	}
	
}