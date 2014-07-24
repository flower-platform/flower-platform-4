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

package org.flowerplatform.flex_client.team.git.action
{
	import mx.collections.ArrayCollection;
	import mx.collections.ArrayList;
	
	import org.flowerplatform.flex_client.core.CoreConstants;
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flex_client.team.git.GitStagingDialog;
	import org.flowerplatform.flex_client.team.git.GitStagingProperties;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;

	/**
	 * @author Marius Iacob
	 */
	
	public class GitStagingAction extends ActionBase
	{
		public var repoPath:String;
		public function GitStagingAction()
		{
			super();
			label = Resources.getMessage("team.git.action.GitStagingAction");
			icon = Resources.gitStagingIcon;
		}
		
		override public function get visible():Boolean {
			if (selection.length == 1 && selection.getItemAt(0) is Node) {
				var node:Node = Node(selection.getItemAt(0));
				if (node.type == CoreConstants.FILE_SYSTEM_NODE_TYPE) {
					return true;
				}
			}
			return false;
		}
		
		override public function run():void {
			var node:Node = Node(selection.getItemAt(0));
			var dialog:GitStagingDialog = new GitStagingDialog();
			var index:int = node.nodeUri.indexOf("|");
			if (index < 0) {
				index = node.nodeUri.length;
			}
			repoPath = node.nodeUri.substring(node.nodeUri.indexOf(":") + 1, index);
			dialog.repo = repoPath;
			var unstagedData:ArrayCollection = new ArrayCollection();
			CorePlugin.getInstance().serviceLocator.invoke("GitService.unstagedList", [repoPath], function(data:ArrayCollection):void {unstagedData = data} );
			dialog.unstagedData = unstagedData;
			
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()				
				.setViewIdInWorkbench(GitStagingProperties.ID)
				.setWidth(550)
				.setHeight(500)
				.show();
		}
		
//		public function getUnstagedData():ArrayCollection {
//			var unstagedData:ArrayCollection = new ArrayCollection();
//			CorePlugin.getInstance().serviceLocator.invoke("GitService.unstagedList", [repoPath], function(data:ArrayCollection):void {unstagedData = data} );
//			return unstagedData;
//		}
	}
}