package org.flowerplatform.flex_client.team.git.action {
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flex_client.team.git.GitConstants;
	import org.flowerplatform.flex_client.team.git.ui.component.PushView;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	/**
	 * @author Andreea Tita
	 */
	public class PushAction extends ActionBase {
		
		public function PushAction() {
			super();
			icon = Resources.push;
		}
		
		override public function  get visible():Boolean {
			if (selection != null && selection.length == 1 && selection.getItemAt(0) is Node) {
				var node:Node = Node(selection.getItemAt(0));
				if (node.type == GitConstants.GIT_REPO_TYPE) {
					label = Resources.getMessage("flex_client.team.git.action.Push.pushOnGit");
				}
				if (node.type == GitConstants.GIT_REMOTE_TYPE) {
					label = Resources.getMessage("flex_client.team.git.action.Push.pushOnRemote");
				}
				return true;
			}
			return false;
		}
		
		override public function run():void {
			var node:Node = Node(selection.getItemAt(0));
			// git category
			if (node.type == GitConstants.GIT_REPO_TYPE){
				var viewPush:PushView = new PushView();
				viewPush.node = node;
				FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()			
					.setViewContent(viewPush)
					.setWidth(500)
					.setHeight(550)
					.setIcon(icon)
					.setTitle(Resources.getMessage("flex_client.team.git.action.Push.pushOnGit"))
					.show();
			} else if (node.type == GitConstants.GIT_REMOTE_TYPE) {
				// remote category
				CorePlugin.getInstance().serviceLocator.invoke("GitService.push", [node.nodeUri, null], 
					function(result:String):void {
						FlexUtilGlobals.getInstance().messageBoxFactory.createMessageBox()
							.setText(result)
							.setTitle(Resources.getMessage('flex_client.team.git.ui.Push.pushResult'))
							.setWidth(300)
							.setHeight(200)
							.showMessageBox();
					});
			}
		}
	}
}