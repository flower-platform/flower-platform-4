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
package org.flowerplatform.flex_client.core {
	import mx.resources.ResourceManager;
	
	import org.flowerplatform.flexutil.plugin.AbstractFlexPlugin;
	import org.flowerplatform.flexutil.resources.ResourcesUtils;
	
	/**
	 * @author Cristi
	 */
	public class AbstractFlowerFlexPlugin extends AbstractFlexPlugin {
		
		public static const MESSAGES_FILE:String = "messages.properties";
		
		protected var _resourcesUrl:String;
		
		protected var _composedImagesUrl:String;

		public function get resourcesUrl():String {
			if (_resourcesUrl == null) {
				const regex:RegExp = new RegExp("(.*?/)swc/");
				var groups:Array = regex.exec(flexPluginDescriptor.url);
				if (groups == null || groups.length != 2) {
					throw new Error("Error getting the bundle name from the url: " + flexPluginDescriptor.url + "; tried to apply regex: " + regex);
				} 
				var package_:String = groups[1];
				_resourcesUrl = groups[1];
			}
			return _resourcesUrl;
		}
		
		/**
		 * @author Mariana
		 */
		public function get composedImagesUrl():String {
			if (_composedImagesUrl == null) {
				var index:int = resourcesUrl.indexOf("public-resources");
				_composedImagesUrl = resourcesUrl.substr(0, index) + "image-composer/";
//				_composedImagesUrl = resourcesUrl.replace("public-resources", "image-composer");				
			}
			return _composedImagesUrl;
		}

		override public function start():void {
			super.start();
			registerMessageBundle();
			registerClassAliases();
		}
		
		protected function registerMessageBundle():void {
			ResourcesUtils.registerMessageBundle("en_US", resourcesUrl, getResourceUrl(MESSAGES_FILE));
		}
		
		protected function registerClassAliases():void {
			
		}
		
		/**
		 * Retrieves a message from the properties files. Parameters can be passed
		 * and the {?} place holders will be replaced with them.
		 */
		public function getMessage(messageId:String, params:Array=null):String {				
			return ResourceManager.getInstance().getString(resourcesUrl, messageId, params);
		}
		
		public function getResourceUrl(resource:String):String {
			return resourcesUrl + resource;
		}

		/**
		 * @author Mariana
		 */
		public function getComposedImageUrl(images:String):String {
			return composedImagesUrl + images;  
		}
		
	}
}