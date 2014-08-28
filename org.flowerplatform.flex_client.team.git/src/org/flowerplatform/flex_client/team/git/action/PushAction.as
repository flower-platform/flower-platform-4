package org.flowerplatform.flex_client.team.git.action {
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flex_client.team.git.GitConstants;
	import org.flowerplatform.flex_client.team.git.ui.PushView;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	/**
	 * @author Andreea Tita
	 */
	public class PushAction extends ActionBase {
		
		public static var ID:String = "org.flowerplatform.flex_client.team.git.action.PushAction";
		
		public function PushAction() {
			super();
			icon = Resources.push;
			orderIndex = 255;
		}
		
		override public function  get visible():Boolean {
			var node:Node = Node(selection.getItemAt(0));
			if (node.type == GitConstants.GIT_REPO_TYPE && !node.getPropertyValue(GitConstants.IS_GIT_REPOSITORY)) {
				// not a git repository
				return false;
			}	
			if (node.type != GitConstants.GIT_REMOTE_TYPE) {
				label = Resources.getMessage("flex_client.team.git.action.Push.pushOnGit");
				return true;
			} else {
				label = Resources.getMessage("flex_client.team.git.action.Push.pushOnRemote");
				return true;
			}			
			return false;
		}
		
		override public function run():void {
			var node:Node = Node(selection.getItemAt(0));
			if (node.type == GitConstants.GIT_REMOTE_TYPE) {
				// remote category
				CorePlugin.getInstance().serviceLocator.invoke("GitService.push", [node.nodeUri, null, null], 
					function(result:String):void {
						FlexUtilGlobals.getInstance().messageBoxFactory.createMessageBox()
						.setText(result)
						.setTitle(Resources.getMessage('flex_client.team.git.ui.Push.pushResult'))
						.setWidth(300)
						.setHeight(200)
						.showMessageBox();
					});
			} else {
				var viewPush:PushView = new PushView();
				viewPush.node = node;
				FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()			
					.setViewContent(viewPush)
					.setWidth(500)
					.setHeight(550)
					.setIcon(icon)
					.setTitle(Resources.getMessage("flex_client.team.git.action.Push.pushOnGit"))
					.show();
			}
		}
		
	}
}
