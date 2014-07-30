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
	import mx.rpc.events.FaultEvent;
	
	import org.flowerplatform.flex_client.codesync.CodeSyncConstants;
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flex_client.team.git.GitConstants;
	import org.flowerplatform.flexutil.FlexUtilAssets;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	/**
	 * @author Marius Iacob
	 */

	public class DeleteBranchAction extends ActionBase {
		
		public static const ID:String = "org.flowerplatform.flex_client.team.git.action.DeleteBranchAction";
		
		public function DeleteBranchAction()
		{
			super();
			label = Resources.getMessage("team.git.action.DeleteBranchAction");
			icon = Resources.deleteIcon;
			orderIndex = 40;
			
		}
		
		override public function get visible():Boolean {
			if (selection.length == 1 && selection.getItemAt(0) is Node) {
				var node:Node = Node(selection.getItemAt(0));
				if (node.type == GitConstants.GIT_LOCAL_BRANCH_TYPE || node.type == GitConstants.GIT_REMOTE_BRANCH_TYPE) {
					return true;
				}
				return CorePlugin.getInstance().nodeTypeDescriptorRegistry.getOrCreateTypeDescriptor(node.type)
					.categories.getItemIndex(CodeSyncConstants.CATEGORY_CODESYNC) >= 0;
			}
			return false;
		}
		
		public function FaultCallBack(event:FaultEvent):void {    
			if (event != null) {    
				var index:Number = event.fault.faultString.search(" Branch");
				FlexUtilGlobals.getInstance().messageBoxFactory.createMessageBox()
					.setText(event.fault.faultString.substr(index))
					.setTitle(Resources.getMessage("info"))
					.setWidth(300)
					.setHeight(200)
					.showMessageBox();
			} 
		}
		
		override public function run():void {
			var node:Node = Node(selection.getItemAt(0));
			
			FlexUtilGlobals.getInstance().messageBoxFactory.createMessageBox()
				.setText(Resources.getMessage("team.git.action.DeleteBranchDialog"))
				.setTitle(Resources.getMessage("info"))
				.setWidth(300)
				.setHeight(200)
				.addButton(FlexUtilAssets.INSTANCE.getMessage('dialog.yes'), function():void {CorePlugin.getInstance().serviceLocator.invoke("GitService.deleteBranch", [node.parent.nodeUri, node.nodeUri], null, FaultCallBack);})
				.addButton(FlexUtilAssets.INSTANCE.getMessage('dialog.no'))
				.showMessageBox();
		}
	}
}