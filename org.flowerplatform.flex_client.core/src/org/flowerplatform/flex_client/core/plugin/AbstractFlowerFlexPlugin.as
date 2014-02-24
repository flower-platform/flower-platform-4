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
	import mx.resources.ResourceManager;
	
	import org.flowerplatform.flexutil.plugin.AbstractFlexPlugin;
	import org.flowerplatform.flexutil.resources.ResourcesUtils;
	
	/**
	 * @author Cristian Spiescu
	 * @uthor Cristina Constantinescu
	 */
	public class AbstractFlowerFlexPlugin extends AbstractFlexPlugin {
		
		public static const MESSAGES_FILE:String = "messages.properties";
		
		public static const IMAGE_COMPOSER_PREFIX:String = "servlet/image-composer/";
		
		protected var _resourcesUrl:String;
		
		protected var _composedImagesUrl:String;
		
		/**
		 * Name of the java plugin project.
		 * If not <code>null</code>, <code>resourceUrl</code> points to this url.
		 */ 
		protected var correspondingJavaPlugin:String;

		public function get resourcesUrl():String {
			if (_resourcesUrl == null) {				
				const regex:RegExp = new RegExp("((.*?\/)+)(.*?\/)swc\/");
				var groups:Array = regex.exec(flexPluginDescriptor.url);
				if (groups == null || groups.length != 4) {
					throw new Error("Error getting the bundle name from the url: " + flexPluginDescriptor.url + "; tried to apply regex: " + regex);
				}
				_resourcesUrl = groups[1] + (correspondingJavaPlugin != null ? correspondingJavaPlugin + "/": groups[3]);
			}
			return _resourcesUrl;
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
		 * Returns the request string for the image composed from the URLs. 
		 * E.g. <tt>servlet/image-composer/url1|url2|url3</tt>
		 * 
		 * <p>
		 * Checks if the first URL already contains the image-composer prefix; 
		 * this way it can be used to append images to the same string.
		 * 
		 * @author Mariana Gheorghe
		 */
		public function getImageComposerUrl(... urls:Array):String {
			if (urls.length == 0) {
				return null;
			}
			var composedUrl:String = "";
			for each (var url:String in urls) {
				if (url != null) {
					composedUrl += (composedUrl.length > 0 ? "|" : "") + url;
				}
			}
			if (composedUrl.length == 0) {
				return null;
			}
			if (composedUrl.indexOf(IMAGE_COMPOSER_PREFIX) < 0) {
				composedUrl = IMAGE_COMPOSER_PREFIX + composedUrl;
			}
			return composedUrl;
		}
	
	}
}