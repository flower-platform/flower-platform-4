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
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flex_client.team.git.GitConstants;
	import org.flowerplatform.flex_client.team.git.ui.CreateBranchView;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	 
	/**
	 * @author Cristina Brinza
	 */	
	public class CreateBranchAction extends ActionBase {
		
		public static const ID:String = "org.flowerplatform.flex_client.team.git.action.CreateBranchAction";
		
		private var useNodeAsCommitId:Boolean;
		
		public function CreateBranchAction(useNodeAsCommitId:Boolean = false) {
			super();
			
			this.useNodeAsCommitId = useNodeAsCommitId;
			
			label = Resources.getMessage("flex_client.team.git.action.createBranch");
			icon = Resources.createBranchIcon;
			orderIndex = 300;
		}
				
		override public function get visible():Boolean {
			if (selection != null && selection.length == 1 && selection.getItemAt(0) is Node) {
				var node:Node = Node(selection.getItemAt(0));

				if (!useNodeAsCommitId) {
					return CorePlugin.getInstance().nodeTypeDescriptorRegistry.getOrCreateTypeDescriptor(node.type)
						.categories.getItemIndex(GitConstants.GIT_REF_CATEGORY) >= 0;
				} else {
					return true;
				}
			}
			return false;
		}

		override public function run():void {
			var node:Node = Node(selection.getItemAt(0));
			var view:CreateBranchView = new CreateBranchView();
			
			view.node = node;
			view.useNodeAsCommitId = useNodeAsCommitId;
			
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
				.setViewContent(view)
				.setWidth(450)
				.setHeight(350)
				.setTitle(label)
				.setIcon(icon)
				.show();
		}
	}
}
