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
	
	 
	public class CheckoutAction extends ActionBase  {
		
		public static var ID:String = "org.flowerplatform.flex_client.team.git.action.CheckoutAction";
		
		public function CheckoutAction() {						
			super();				
			label = Resources.getMessage("flex_client.team.git.action.checkoutAction");
			icon = Resources.getResourceUrl("/images/mindmap/icons/checkout.gif");
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
				.addButton("Cancel", function():void {})
				.showMessageBox();	
		}	
		
		public function FaultCallback(event:FaultEvent):void {				
			if (event != null) {				
				var index:Number = event.fault.faultString.search("CheckoutConflictException");
				if (index != -1)
					uncommitedChanges(event.fault.faultString.substring(index));
			}
		}	
		
		public function callGitServiceCheckout(nodeUri:String):void {			
			CorePlugin.getInstance().serviceLocator.invoke("GitService.checkout",[nodeUri],null,FaultCallback);						
		}
		
		override public function run():void {
			var node:Node = Node(selection.getItemAt(0))		
			var Name:String = node.properties[GitConstants.NAME];
			
			if (node.type == "gitLocalBranch") {
				FlexUtilGlobals.getInstance().messageBoxFactory.createMessageBox()
					.setText(Resources.getMessage("checkout.popup",[Name]))
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
					.addButton("Create new branch", function():void {createNewBranch(node);})
					.addButton("Checkout Commit",  function():void {callGitServiceCheckout(node.nodeUri);})
					.addButton("Cancel", function():void {})
					.showMessageBox();			
			}
			else if (node.type == "gitTag") {
				FlexUtilGlobals.getInstance().messageBoxFactory.createMessageBox()
					.setText(Resources.getMessage("checkout.popup",[Name]))
					.setTitle(Resources.getMessage("info"))
					.setWidth(300)
					.setHeight(125)
					.addButton("Yes", function():void {callGitServiceCheckout(node.nodeUri);})
					.addButton("No")
					.showMessageBox();	
			}
		}
}
}