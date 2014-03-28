package org.flowerplatform.flex_client.host_app.mobile.view_server_account {

	/**
	 * @author Sebastian Solomon
	 */
	public class ServerAccount {
			public var friendlyName:String;
			
			public var host:String;
			
			public var user:String;
			
			public var password:String;
			
			public var isDefault:Boolean;
			
//		public function ServerAccount() {
//			friendlyName = "friendlyName";
//			host = "host";
//			user = "user";
//			password = "password";
//		}
		
		public function setData(data:Object):void {
			friendlyName = data.friendlyName;
			host = data.host;
			user = data.user;
			password = data.password;
			isDefault = (data.isDefault == null) ? false : data.isDefault; 
		}
		
	}
	
}