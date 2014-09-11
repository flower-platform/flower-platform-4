package org.flowerplatform.flex_client.core.user {
	import mx.rpc.AsyncResponder;
	import mx.rpc.AsyncToken;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	
	import org.flowerplatform.flex_client.core.CoreConstants;
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class UserAuthenticationManager {
		
		public var currentUser:String;
		
		public function login(username:String, password:String, loginSuccessHandler:Function = null):void {
			var token:AsyncToken = CorePlugin.getInstance().channelSet.login(username, password); 
			token.addResponder(new AsyncResponder(function(event:ResultEvent, token:Object = null):void {
				loginResultEventHandler(event, token);
				if (loginSuccessHandler != null) {
					loginSuccessHandler();
				}
			}, loginFaultEventHandler));
		}
		
		protected function loginResultEventHandler(event:ResultEvent, token:Object = null):void {
			currentUser = event.message.headers[CoreConstants.CURRENT_USER];
			// cookies will be set on the first iFrame that will open
			FlexUtilGlobals.getInstance().cookiesForJs[CoreConstants.CURRENT_USER] = currentUser;
			FlexUtilGlobals.getInstance().cookiesForJs[CoreConstants.JSESSIONID] = event.message.headers[CoreConstants.JSESSIONID];
		}
		
		protected function loginFaultEventHandler(event:FaultEvent, token:Object = null):void {
			// show alert
		}
	
		public function logout():void {
			var token:AsyncToken = CorePlugin.getInstance().channelSet.logout();
			token.addResponder(new AsyncResponder(logoutResultEventHandler, loginFaultEventHandler));
		}
		
		protected function logoutResultEventHandler(event:ResultEvent, token:Object = null):void {
			currentUser = null;
			// unset currentUser cookie
			FlexUtilGlobals.getInstance().cookiesForJs[CoreConstants.CURRENT_USER] = null;
			FlexUtilGlobals.getInstance().cookiesForJs[CoreConstants.JSESSIONID] = event.message.headers[CoreConstants.JSESSIONID];
		}
		
		protected function logoutFaultEventHandler(event:FaultEvent, token:Object):void {
			// show alert
		}
	}
}