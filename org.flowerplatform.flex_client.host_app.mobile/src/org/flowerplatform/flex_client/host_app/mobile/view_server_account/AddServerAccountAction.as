package org.flowerplatform.flex_client.host_app.mobile.view_server_account
{
	import org.flowerplatform.flex_client.host_app.mobile.view_edit_server_account.EditServerAccountView;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	/**
	 * @author Sebastian Solomon
	 */
	public class AddServerAccountAction extends ActionBase {
		public var serverAccountView:ServerAccountsView;
		
		public function AddServerAccountAction() {
			super();
			label = Resources.getMessage("mobile.add.server.account.label");
		}
		
		override public function run():void {
			var editAccountView:EditServerAccountView = new EditServerAccountView();
			editAccountView.setServerAccountView(serverAccountView);
			
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
				.setTitle(Resources.getMessage("mobile.edit.server.account.title"))
				.setViewContent(editAccountView)
				.show();			
		}
	}
}