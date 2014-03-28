package org.flowerplatform.flex_client.host_app.mobile.view_edit_server_account
{
	import flash.net.SharedObject;
	
	import mx.collections.ArrayCollection;
	import mx.events.CloseEvent;
	
	import spark.components.Alert;
	
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;

	/**
	 * @author Sebastian Solomon
	 */
	public class RemoveServerAccountAction extends ActionBase {
		private var indexToDelete:int;
		private var editAccountView:EditServerAccountView;
		
		public function RemoveServerAccountAction() {
			super();
			label = "Remove Server Account";
		}
		
		public function setIndexToDelete(index:int):void {
			indexToDelete = index;
		}
		
		public function setEditAccountView(value:EditServerAccountView):void {
			editAccountView = value;
		}
		
		public function seteditAccountView(editServerAccountView:EditServerAccountView):void {
			editAccountView = editServerAccountView;
		}
		
		override public function run():void {
			var dialog_obj:Object = Alert.show("Are you shure you want to delte account", "Delete account", Alert.YES | Alert.NO, null, confirmAccountDeleteHandler, null, Alert.NO);
		}
		
		private function confirmAccountDeleteHandler(event:CloseEvent):void {
			if (event.detail == Alert.YES) { // yes button
				if (indexToDelete >= 0) {
					var sharedObject:SharedObject = SharedObject.getLocal("flower-platform");
					var arrayCollection:ArrayCollection = sharedObject.data.serverAccounts;
					
					arrayCollection.removeItemAt(indexToDelete);
					sharedObject.data.serverAccounts = arrayCollection;
					sharedObject.flush(10000);
					editAccountView.getServerAccountView().refresh();
					FlexUtilGlobals.getInstance().popupHandlerFactory.removePopup(editAccountView);
				}
			}
		}
			
	}
}