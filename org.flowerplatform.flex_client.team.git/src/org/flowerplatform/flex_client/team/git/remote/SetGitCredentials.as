package org.flowerplatform.flex_client.team.git.remote{
	
	[RemoteClass(alias="org.flowerplatform.team.git.remote.SetGitCredentials")]
	[Bindable]
	public class SetGitCredentials{
		
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