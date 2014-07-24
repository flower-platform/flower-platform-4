
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
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flex_client.team.git.GitConstants;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;

	public class CheckoutAction extends ActionBase  {
		
		public function CheckoutAction() {
			label = Resources.getMessage("flex_client.team.git.action.checkoutAction");
			//icon = Resources.getResourceUrl("/mindmap/icons/checkout.gif");
			super();				
		}
		
		public function createNewBranch():void {
			//TODO call createNewBranch and Checkout!
		}	
		public function commitChanges():void {	
			//TODO call commit
		}
		public function reset():void {
			//TODO call reset
		}
		
		public function uncommitedChanges(text:String):void {			
			FlexUtilGlobals.getInstance().messageBoxFactory.createMessageBox()
				.setText(text)
				.setTitle(Resources.getMessage("info"))
				.setWidth(300)
				.setHeight(150)
				.addButton("Commit Changes", function():void {commitChanges();})
				.addButton("Reset", function():void {reset();})
				.addButton("Cancel2", function():void {})
				.showMessageBox();	
		}	
		
		public function callGitServiceCheckout(nodeUri:String):void {			
			CorePlugin.getInstance().serviceLocator.invoke("GitService.checkoutBranch",[nodeUri],null,FaultCallback);						
		}

		public function FaultCallback(event:FaultEvent):void {				
			if (event != null) {				
				var index:Number = event.fault.faultString.search("CheckoutConflictException");
				if (index != -1)
					uncommitedChanges(event.fault.faultString.substring(index));
			}
		}	
		
		override public function get visible():Boolean {
			if (selection.length == 1 && selection.getItemAt(0) is Node) {
				var node:Node = Node(selection.getItemAt(0));
				if (node.type == GitConstants.GIT_LOCAL_BRANCH_TYPE ||	node.type == GitConstants.GIT_REMOTE_BRANCH_TYPE) {
					return true;
				}
			}
			return false;
		}
				
		override public function run():void {
			var node:Node = Node(selection.getItemAt(0))		
			var branchName:String = node.properties[GitConstants.NAME];
			
			if (node.type == "gitLocalBranch") {
				FlexUtilGlobals.getInstance().messageBoxFactory.createMessageBox()
					.setText(Resources.getMessage("checkout.popup",[branchName])) //TODO change BranchName
					.setTitle(Resources.getMessage("info"))
					.setWidth(300)
					.setHeight(125)
					.addButton("Yes", function():void {callGitServiceCheckout(node.nodeUri);})
					.addButton("No")
					.showMessageBox();
			}
			else if (node.type == "gitRemoteBranch") {
				FlexUtilGlobals.getInstance().messageBoxFactory.createMessageBox()
					.setText(Resources.getMessage("checkout.remote"))
					.setTitle(Resources.getMessage("info"))
					.setWidth(350)
					.setHeight(200)
					.addButton("Create new branch", function():void {createNewBranch();})
					.addButton("Checkout Commit",  function():void {callGitServiceCheckout(node.nodeUri);})
					.addButton("Cancel3", function():void {})
					.showMessageBox();			
			}		
	}
}
}