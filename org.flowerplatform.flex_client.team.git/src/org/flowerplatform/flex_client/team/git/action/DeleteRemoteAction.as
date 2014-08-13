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
	import org.flowerplatform.flex_client.core.CoreConstants;
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flex_client.team.git.GitConstants;
	import org.flowerplatform.flexutil.FlexUtilAssets;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	/**
	 * @author Cristina Brinza
	 */
	
	public class DeleteRemoteAction extends ActionBase {
		
		public function DeleteRemoteAction() {
			super();
			
			icon = Resources.deleteRemote;
			label = Resources.getMessage("flex_client.team.git.action.DeleteRemoteAction.deleteRemote");
		}
		
		override public function get visible():Boolean {
			if (selection.length == 1 && selection.getItemAt(0) is Node) {
				var node:Node = Node(selection.getItemAt(0));
				
				return (node.type == GitConstants.GIT_REMOTE_TYPE);
			}
			
			return false;
		}
		
		override public function run():void {
			var node:Node = Node(selection.getItemAt(0));
			
			FlexUtilGlobals.getInstance().messageBoxFactory.createMessageBox()
				.setText(Resources.getMessage("flex_client.team.git.action.DeleteRemoteAction.confirmDelete", [node.getPropertyValue(GitConstants.NAME)]))
				.setTitle(Resources.getMessage("info"))
				.setWidth(300)
				.setHeight(200)
				.addButton(FlexUtilAssets.INSTANCE.getMessage('dialog.yes'), 
					function():void {
						CorePlugin.getInstance().serviceLocator.invoke("GitService.deleteRemote", [node.nodeUri, node.parent.nodeUri]);
					})
				.addButton(FlexUtilAssets.INSTANCE.getMessage('dialog.no'))
				.showMessageBox();
		}
	}
}