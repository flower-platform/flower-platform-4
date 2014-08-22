package org.flowerplatform.flex_client.team.git.action {

	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flex_client.team.git.ui.LoginView;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	/**
	 * @author Andreea Tita
	 */
	public class LoginAction extends ActionBase {

		public static var ID:String = "org.flowerplatform.flex_client.team.git.action.LoginAction";

		public function LoginAction() {
			super();
			label = Resources.getMessage('flex_client.team.git.action.changeCredentials.label');
			icon = Resources.permission;
		}
	
		override public function run():void {
			var node:Node = Node(selection.getItemAt(0));
			var loginView:LoginView = new LoginView();
			loginView.node = node;
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
