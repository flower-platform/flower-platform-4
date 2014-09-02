package org.flowerplatform.flex_client.team.git.history.action
{
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flex_client.team.git.GitConstants;
	import org.flowerplatform.flex_client.team.git.action.CheckoutAction;
	import org.flowerplatform.flexutil.action.ActionBase;

	public class HistoryCheckout extends ActionBase 
	{
		public function HistoryCheckout()
		{
			super();
			label = Resources.getMessage("gitHistory.action.Checkout");
			icon = Resources.checkoutIcon;
			setOrderIndex(int(Resources.getMessage('flex_client.team.git.action.history.sortIndex.HistoryCheckout')));
		}
		
		override public function  get visible():Boolean {
			return (selection != null && selection.length == 1 && selection.getItemAt(0) is Node);
		}
		
		override public function run():void {	
			var node:Node = Node(selection.getItemAt(0));
			var myCheck:CheckoutAction = new CheckoutAction;
			myCheck.callGitServiceCheckout(node, node.getPropertyValue(GitConstants.ID));
		}
	}
}