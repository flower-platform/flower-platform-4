package org.flowerplatform.flex_client.team.git.action {
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flex_client.team.git.ui.ConfigureBranchView;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;

	/**
 	* @author Diana Balutoiu 
 	*/
	public class ConfigureBranchAction extends ActionBase {
		
		public function ConfigureBranchAction() {
			super();
			label = Resources.getMessage("flex_client.team.git.action.configureBranch");

		}
		
		override public function get visible():Boolean {
			if (selection.length == 1 && selection.getItemAt(0) is Node) {
				var node:Node = Node(selection.getItemAt(0));
				//TODO: If the node type is gitBranch => visibility true (from GitConstants)
			}
			
			return false;
		}
		
		override public function run():void {
			var node:Node = Node(selection.getItemAt(0));
			var configBranchView:ConfigureBranchView = new ConfigureBranchView();
			
			//TODO: Get the name of the branch and repo from node for View
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
				.setViewContent(configBranchView)
				.setWidth(500)
				.setHeight(300)
				.setTitle(label)
				//.setIcon(icon)
				.show();
		}
		
		
		
		
		
	}
}