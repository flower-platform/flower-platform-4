
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
	
	import org.flowerplatform.flex_client.core.CoreConstants;
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;

	public class CheckoutAction extends ActionBase  {
		
		public function CheckoutAction() {
			super();				
			label = Resources.getMessage("flex_client.team.git.action.checkoutAction");
		}
		
		public function createNewBranch():void {
			//TODO call createNewBranch
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
				.addButton(label = "Commit Changes", function():void {commitChanges();})
				.addButton(label = "Reset", function():void {reset();})
				.addButton(label = "Cancel")
				.showMessageBox();	
		}	
		
		public function callGitServiceCheckout(branchName:String, repo:String):void {			
			CorePlugin.getInstance().serviceLocator.invoke("GitService.checkoutBranch",[branchName, repo],null,FaultCallback);						
		}

		public function FaultCallback(event:FaultEvent):void {				
			if (event != null) {				
				if (event.fault.faultString.substr(28,25) == "CheckoutConflictException")
					uncommitedChanges(event.fault.faultString.substring(28));
			}
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
			var repo:String;
			var node:Node = Node(selection.getItemAt(0));
			var index:int = node.nodeUri.indexOf("|");
			if (index < 0) {
				index = node.nodeUri.length;
			}
			repo = node.nodeUri.substring(node.nodeUri.indexOf(":") + 1, index);
			
			////////TOTO Test if Branch is Local then createMessageBox()////////			
			//If branchName is local create MessageBox
			FlexUtilGlobals.getInstance().messageBoxFactory.createMessageBox()
				.setText(Resources.getMessage("checkout.remote") + "<BranchName> ?") //TODO change BranchName
				.setTitle(Resources.getMessage("info"))
				.setWidth(300)
				.setHeight(100)
				.addButton(label = "Yes", function():void { callGitServiceCheckout("refs/remotes/origin/bugy", repo);})
				.addButton(label = "No")
				.showMessageBox();			
			//TOTO Else if Branch is NOT Local
			FlexUtilGlobals.getInstance().messageBoxFactory.createMessageBox()
				.setText(Resources.getMessage("checkout.remote") + "<BranchName> ?") //TODO change BranchName
				.setTitle(Resources.getMessage("info"))
				.setWidth(300)
				.setHeight(100)
				.addButton(label = "Create new branch", function():void {createNewBranch();})
				.addButton(label = "Checkout Commit",  function():void { callGitServiceCheckout("refs/remotes/origin/bugy", repo);})
				.addButton(label = "Cancel")
				.showMessageBox();		
			
		}		
	}
}