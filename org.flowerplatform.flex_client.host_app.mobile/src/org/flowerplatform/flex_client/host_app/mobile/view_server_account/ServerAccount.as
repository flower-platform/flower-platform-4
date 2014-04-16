package org.flowerplatform.flex_client.host_app.mobile.view_server_account {

	/**
	 * @author Sebastian Solomon
	 */
	[Bindable]
	public class ServerAccount {
			public var friendlyName:String;
			
			public var host:String;
			
			public var user:String;
			
			public var password:String;
			
			public var isDefault:Boolean;
			
		public function setData(data:Object):void {
			friendlyName = data.friendlyName;
			host = data.host;
			user = data.user;
			password = data.password;
			isDefault = (data.isDefault == null) ? false : data.isDefault; 
		}
		
	}
	
}