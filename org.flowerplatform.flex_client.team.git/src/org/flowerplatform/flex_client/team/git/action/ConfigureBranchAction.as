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
		
		public static const ID:String = "org.flowerplatform.flex_client.team.git.action.ConfigureBranchAction";
		
		public function ConfigureBranchAction() {
			super();
			label = Resources.getMessage('flex_client.team.git.action.configureBranch');
			icon = Resources.configBrenchIcon;

		}
		
//		override public function get visible():Boolean {
//			if (selection.length == 1 && selection.getItemAt(0) is Node) {
//				var node:Node = Node(selection.getItemAt(0));
//				if(node.type == GitConstants.GIT_LOCAL_BRANCH_TYPE || 
//				   node.type == GitConstants.GIT_REMOTE_BRANCH_TYPE){
//					return true;
//				}
//			}
//			
//			return false;
//
//		}
		
		override public function run():void {
			var node:Node = Node(selection.getItemAt(0));
			var configBranchView:ConfigureBranchView = new ConfigureBranchView();			
			configBranchView.node = node;
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
				.setViewContent(configBranchView)
				.setWidth(500)
				.setHeight(200)
				.setTitle(label)
				.setIcon(icon)
				.show();
		}
		
		
		
		
		
	}
}