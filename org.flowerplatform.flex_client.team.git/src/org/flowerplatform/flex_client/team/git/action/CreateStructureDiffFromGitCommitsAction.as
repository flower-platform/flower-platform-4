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
	import mx.controls.Alert;
	
	import org.flowerplatform.flex_client.codesync.CodeSyncConstants;
	import org.flowerplatform.flex_client.core.CoreConstants;
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flex_client.team.git.CreateStructureDiffFromGitCommitsDialog;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	/**
	 * @author Valentina-Camelia Bojan
	 */ 
	public class CreateStructureDiffFromGitCommitsAction extends ActionBase  {
		
		public function CreateStructureDiffFromGitCommitsAction() {
			super();
			
			label = Resources.getMessage("flex_client.team.git.action.createSdiffFromGitCommits");
		}
		
		override public function get visible():Boolean {
			if (selection.length == 1 && selection.getItemAt(0) is Node) {
				var node:Node = Node(selection.getItemAt(0));
				if (node.type == CodeSyncConstants.CODESYNC ||					
							node.type == CoreConstants.CODE_TYPE ||
							node.type == CoreConstants.FILE_SYSTEM_NODE_TYPE ||
							node.type == CoreConstants.FILE_NODE_TYPE) {
					return true;
				}
				return CorePlugin.getInstance().nodeTypeDescriptorRegistry.getOrCreateTypeDescriptor(node.type)
					.categories.getItemIndex(CodeSyncConstants.CATEGORY_CODESYNC) >= 0;
			}
			return false;
		}
		
		override public function run():void {
			var node:Node = Node(selection.getItemAt(0));
			var dialog:CreateStructureDiffFromGitCommitsDialog = new CreateStructureDiffFromGitCommitsDialog();
			var index:int = node.nodeUri.indexOf("|");
			if (index < 0) {
				index = node.nodeUri.length;
			}
			dialog.repo = node.nodeUri.substring(node.nodeUri.indexOf(":") + 1, index);
			
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
				.setViewContent(dialog)
				.setWidth(550)
				.setHeight(500)
				.setTitle(label)
				.setIcon(icon)
				.show();
		}
	}
}