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
/**
 * 
 * @author Vlad Bogdan Manica
 * 
 */
package org.flowerplatform.flex_client.team.git.action
{
	import mx.rpc.events.FaultEvent;
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flex_client.team.git.GitConstants;
	import org.flowerplatform.flex_client.team.git.ui.CreateBranchView;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;

	/**
	 * @author Vlad Bogdan Manica
	 */ 
	public class CheckoutAction extends ActionBase  {
		
		public static var ID:String = "org.flowerplatform.flex_client.team.git.action.CheckoutAction";
		
		public function CheckoutAction() {						
			super();				
			label = Resources.getMessage("flex_client.team.git.action.checkoutAction");
			icon = Resources.checkoutIcon;
			orderIndex = 240;
		}
		
		public function createNewBranch(node:Node):void {
			var view:CreateBranchView = new CreateBranchView();			
			view.node = node;
			
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
				.setViewContent(view)
				.setWidth(550)
				.setHeight(500)
				.setTitle(Resources.getMessage("flex_client.team.git.action.createBranch"))
				.setIcon(Resources.createBranchIcon)
				.show();
		}
		
		public function commitChanges():void {
			FlexUtilGlobals.getInstance().actionHelper.runAction(FlexUtilGlobals.getInstance().getActionInstanceFromRegistry(ShowGitStagingAction.ID), null, null);		
		}
		
		public function reset(node:Node, commitID:String):void {
			for each(var child:Node in node.parent.children) {
				if (child.properties[GitConstants.IS_CHECKEDOUT] == true){
					CorePlugin.getInstance().serviceLocator.invoke("GitService.reset", [child.nodeUri, "HARD", String(child.properties[GitConstants.COMMIT_ID])]);
					break;
				}
			}
			callGitServiceCheckout(node, commitID);			
		}		
		
		public function faultCallback(event:FaultEvent, node:Node, commitID:String):void {				
			if (event != null) {	
				var index:Number = event.fault.faultString.search("CheckoutConflictException");
				if (index != -1) {
					FlexUtilGlobals.getInstance().messageBoxFactory.createMessageBox()
						.setText(event.fault.faultString.substring(index))
						.setTitle(Resources.getMessage("flex_client.team.git.createSdiff.getInfo"))
						.setWidth(300)
						.setHeight(150)
						.addButton(Resources.getMessage("flex_client.team.git.action.commitChanges"), function():void {commitChanges();})
						.addButton(Resources.getMessage("flex_client.team.git.action.Reset"), function():void {reset(node, commitID);})
						.addButton(Resources.getMessage("flex_client.team.git.action.Cancel"), function():void {})
						.showMessageBox();	
				}
			}
		}	
		
		public function callGitServiceCheckout(node:Node, commitID:String):void {			
			CorePlugin.getInstance().serviceLocator.invoke("GitService.checkout", [node.nodeUri, commitID], null, function(event:FaultEvent):void {faultCallback(event, node, commitID)});
			
		}

		override public function run():void {
			var node:Node = Node(selection.getItemAt(0))		
			var name:String = node.properties[GitConstants.NAME];
			
			if (node.type == "gitLocalBranch" || node.type == "gitTag") {
				FlexUtilGlobals.getInstance().messageBoxFactory.createMessageBox()
					.setText(Resources.getMessage("flex_client.team.git.action.checkout.popup",[name]))
					.setTitle(Resources.getMessage("flex_client.team.git.createSdiff.getInfo"))
					.setWidth(300)
					.setHeight(125)
					.addButton(Resources.getMessage("flex_client.team.git.action.Yes"), function():void {callGitServiceCheckout(node, null);})
					.addButton(Resources.getMessage("flex_client.team.git.action.No"))
					.showMessageBox();
			} else if (node.type == "gitRemoteBranch") {
				FlexUtilGlobals.getInstance().messageBoxFactory.createMessageBox()
					.setText(Resources.getMessage("flex_client.team.git.action.checkout.remote"))
					.setTitle(Resources.getMessage("flex_client.team.git.createSdiff.getInfo"))
					.setWidth(350)
					.setHeight(200)
					.addButton(Resources.getMessage("flex_client.team.git.action.CreateNewBranch"), function():void {createNewBranch(node);})
					.addButton(Resources.getMessage("flex_client.team.git.action.CheckoutCommit"),  function():void {callGitServiceCheckout(node, null);})
					.addButton(Resources.getMessage("flex_client.team.git.action.Cancel"))
					.showMessageBox();			
			}			
		}
	}
}
