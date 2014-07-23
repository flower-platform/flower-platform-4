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
	import org.flowerplatform.flex_client.codesync.CodeSyncConstants;
	import org.flowerplatform.flex_client.core.CoreConstants;
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.team.git.ui.CreateBranchView;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	 
	public class CreateBranchAction extends ActionBase {
		
		protected var view:CreateBranchView;
		
		public function CreateBranchAction() {
			super();
		}
				
		override public function get visible():Boolean {
			if (selection.length == 1 && selection.getItemAt(0) is Node) {
				var node:Node = Node(selection.getItemAt(0));
				
				/* if local branch */
				if (node.properties.isLocalBranch) {
//					view.configureUpstreamCheckBox.selected = false;
//					view.configureUpstreamCheckBox.
					return true;		
				}
				
				/* if remote branch */
				if (node.properties.isRemoteBranch) {
					return true;
				}
				
				/* if tag */
				if (node.properties.isTag) {
					return true;
				}
				
				return CorePlugin.getInstance().nodeTypeDescriptorRegistry.getOrCreateTypeDescriptor(node.type)
					.categories.getItemIndex("") >= 0;
			}
			return false;
		}
		
		override public function run():void {
			var node:Node = Node(selection.getItemAt(0));
			var view:CreateBranchView = new CreateBranchView();
			view.repositoryNode = node;
			
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
				.setViewContent(view)
				.setWidth(550)
				.setHeight(500)
				.setTitle(label)
				.setIcon(icon)
				.show();
		}
	}
}