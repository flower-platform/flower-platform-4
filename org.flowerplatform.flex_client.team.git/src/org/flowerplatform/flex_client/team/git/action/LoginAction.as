package org.flowerplatform.flex_client.team.git.action {
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flex_client.team.git.GitConstants;
	import org.flowerplatform.flex_client.team.git.ui.LoginView;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	/**
	 * @author Andreea Tita
	 */
	
	public class LoginAction extends ActionBase {
		public function LoginAction() {
			super();
			label = Resources.getMessage('flex_client.team.git.action.changeCredentials.label');
			icon = Resources.permission;
		}
		
		override public function get visible():Boolean {
			if (selection.length == 1 && selection.getItemAt(0) is Node) {
				var node:Node = Node(selection.getItemAt(0));
				return node.type == GitConstants.GIT_REMOTE_TYPE;
			}
			return false;
		}
		
		override public function run():void {
			var node:Node = Node(selection.getItemAt(0));
			var loginView:LoginView = new LoginView();
			loginView.repo = String(ArrayCollection(node.getPropertyValue(GitConstants.REMOTE_URIS)).getItemAt(0));
			CorePlugin.getInstance().serviceLocator.invoke("GitService.setCredentials",["git|" + loginView.repo, null]);
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
				.setViewContent(loginView)
				.setWidth(450)
				.setHeight(200)
				.setTitle(label)
				.setIcon(icon)
				.show();
		}
	}
}