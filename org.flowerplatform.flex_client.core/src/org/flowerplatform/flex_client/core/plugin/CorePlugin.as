/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
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
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package org.flowerplatform.flex_client.core.plugin {
	import org.flowerplatform.flex_client.core.AbstractFlowerFlexPlugin;
	import org.flowerplatform.flexutil.Utils;
	
	/**
	 * @author Cristi
	 */
	public class CorePlugin extends AbstractFlowerFlexPlugin {
		
		protected static var INSTANCE:CorePlugin;
		
		public static function getInstance():CorePlugin {
			return INSTANCE;
		}
				
//		public static const VERSION:String = "2.0.0.M2_2013-06-04";
//				
//		/**
//		 * key = command name as String (e.g. "openResources")
//		 * value = parameters as String (e.g. text://file1,file2,file3)
//		 */ 
//		public var linkHandlers:Dictionary;
		
		override public function preStart():void {
			super.preStart();
			if (INSTANCE != null) {
				throw new Error("An instance of plugin " + Utils.getClassNameForObject(this, true) + " already exists; it should be a singleton!");
			}
			INSTANCE = this;
			
//			linkHandlers = new Dictionary();			
//			
//			if (ExternalInterface.available) {
//				// on mobile, it's not available
//				ExternalInterface.addCallback("handleLink", handleLink);
//			}
		}
		
//		/**
//		 * @author Cristina Constantinescu
//		 */
//		public function handleLink(queryString:String):void {			
//			var commands:Object = parseQueryStringParameters(queryString); // map command -> parameters
//			for (var object:String in commands) {	
//				var linkHandler:ILinkHandler = ILinkHandler(linkHandlers[object]);
//				if (linkHandler != null) {	
//					linkHandler.handleLink(object, commands[object]);					
//				}
//			}
//		}
//		
//		/**
//		 * @author Cristina Constantinescu
//		 */
//		public function handleLinkWithQueryStringDecoded(queryStringDecoded:Object):void {			
//			for (var object:String in queryStringDecoded) {	
//				var linkHandler:ILinkHandler = ILinkHandler(linkHandlers[object]);
//				if (linkHandler != null) {	
//					linkHandler.handleLink(object, queryStringDecoded[object]);					
//				}
//			}
//		}
//		
//		/**
//		 * @author Cristina Constantinescu
//		 */
//		public function parseQueryStringParameters(url:String):Object {
//			var query:String;
//			if (url.indexOf("?") != -1) { // no parameters passed in the url
//				query = url.substr(url.indexOf("?") + 1);
//			} else {
//				query = url;
//			}			
//			
//			var parameters:Object = new Object();
//			for each (var parameterWithValue:String in query.split("&")) { // spliting by group separator p1=v1&p2=v2
//				var parameter:String = null;
//				var value:String = null;
//				
//				var indexOfEqualSign:int = parameterWithValue.indexOf("=");
//				if (indexOfEqualSign < 0) { // No value, just key
//					parameter = parameterWithValue;
//					value = null;
//				} else {
//					parameter = parameterWithValue.substring(0, indexOfEqualSign);
//					value = parameterWithValue.substring(indexOfEqualSign + 1); // the rest represents the value, even though it contains an = character 
//				}
//				parameters[parameter] = value;
//			}
//			return parameters;
//		}
//			
//		public function getBrowserURLWithoutQuery():String {
//			if (ExternalInterface.available) {
//				var browserURL:String = ExternalInterface.call("getURL");
//				return browserURL.split("?")[0];
//			}
//			 return null;
//		}
	
	}
}
