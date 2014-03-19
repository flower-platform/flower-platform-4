package org.flowerplatform.flex_client.host_app.mobile.view_server_account
{
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	public class AddServerAccountAction extends ActionBase {
		public function AddServerAccountAction() {
			super();
			label = "Add Server Account";
		}
		
		override public function run():void {
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()				
				.setViewIdInWorkbench("editServerAccount")
				.setTitle("Edit Server Account")
				.show();
		}
	}
}