package org.flowerplatform.flex_client.core.users {
	import mx.controls.Alert;
	import mx.rpc.AsyncResponder;
	import mx.rpc.AsyncToken;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	
	import org.flowerplatform.flex_client.core.CoreConstants;
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class UserAuthenticationManager {
		
		public function getCurrentUser():void {
			CorePlugin.getInstance().serviceLocator.invoke("userService.getCurrentUser", null, function(user:Node):void {
				Alert.show(user == null ? "null" : user.nodeUri);
			});
		}
		
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
			// cookies will be set on the first iFrame that will open
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
			FlexUtilGlobals.getInstance().cookiesForJs[CoreConstants.JSESSIONID] = event.message.headers[CoreConstants.JSESSIONID];
		}
		
		protected function logoutFaultEventHandler(event:FaultEvent, token:Object):void {
			// show alert
		}
	}
}