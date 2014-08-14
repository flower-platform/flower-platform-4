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
package org.flowerplatform.flexutil.plugin {
	import mx.collections.ArrayCollection;

	/**
	 * @author Cristi
	 */
	public class FlexPluginManager {
		
		public var flexPluginEntries:ArrayCollection = new ArrayCollection();
		
		public var currentLoadingSession:FlexPluginLoadingSession;
		
		protected function createLoadingSession():FlexPluginLoadingSession {
			return new FlexPluginLoadingSession(this);
		}
		
		public function loadPlugins(pluginSwcUrls:ArrayCollection, callbackFunction:Function = null, callbackObject:Object = null):void {
			if (currentLoadingSession != null) {
				throw new Error("A loading session is already in progress. Need to wait until it's finished");
			}
			
			if (pluginSwcUrls == null || pluginSwcUrls.length == 0) {
				throw new Error("A null or empty list of plugin urls was passed as param");
			}
			
			currentLoadingSession = createLoadingSession();
			currentLoadingSession.loadPlugins(pluginSwcUrls, callbackFunction, callbackObject);
		}
	
		/**
		 * Append "version=..." to list of url's parameters.
		 * @author Cristina Constantinescu
		 */ 
		public function appendVersionToUrl(url:String, version:String):String {
			if (url != null) {
				url += (url.split('?')[1] ? '&':'?') + "version=" + version;				
			}
			return url;
		}	
		
	}
}