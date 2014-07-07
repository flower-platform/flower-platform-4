/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * license-end
 */
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