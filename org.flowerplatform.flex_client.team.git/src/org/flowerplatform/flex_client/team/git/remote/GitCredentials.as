/* license-start
 * 
 * Copyright (C) 2008 - 2014 Crispico Software, <http://www.crispico.com/>.
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
package org.flowerplatform.flex_client.team.git.remote{
	
	/**
	 * @author Andreea Tita
	 */
	
	[RemoteClass(alias="org.flowerplatform.team.git.remote.GitCredentials")]
	[Bindable]
	public class GitCredentials{
		
		private var _username:String;
		
		private var _password:String;
		
		public function set username(value:String):void {
			_username = value;
		}
		
		public function set password(value:String):void {
			_password = value;
		}
		
		public function get username():String {
			return _username;
		}
		
		public function get password():String {
			return _password;
		}
		
	}
}