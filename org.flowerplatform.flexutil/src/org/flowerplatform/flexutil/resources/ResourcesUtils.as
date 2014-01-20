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
package org.flowerplatform.flexutil.resources {
	
	import flash.events.Event;
	import flash.events.IEventDispatcher;
	import flash.events.IOErrorEvent;
	import flash.events.SecurityErrorEvent;
	import flash.events.TextEvent;
	import flash.net.URLLoader;
	import flash.net.URLRequest;
	
	import mx.controls.Alert;
	import mx.resources.IResourceManager;
	import mx.resources.ResourceBundle;
	import mx.resources.ResourceManager;
	
	import org.flowerplatform.flexutil.FlexUtilGlobals;

	/**
	 * @author Cristi
	 */
	public class ResourcesUtils	{
		
		public static function registerMessageBundle(locale:String, messageBundle:String, url:String, object:Object = null):void {
			var loadedHandler:Function = function(event:Event):void {
				var urlLoader:URLLoader = URLLoader(event.target);
				var source:String = urlLoader.data as String;
				
				var resourceManager:IResourceManager = ResourceManager.getInstance();
				var bundle:ResourceBundle = new ResourceBundle(locale, messageBundle); 
				new PropertiesParser().parseProperties(source, bundle.content);
				resourceManager.addResourceBundle(bundle, false); 
				resourceManager.update();
				
				if (object != null && object is IEventDispatcher) {
					IEventDispatcher(object).dispatchEvent(new ResourceUpdatedEvent());
				}
			}
			
			var errorHandler:Function = function (event:TextEvent):void {
				Alert.show("Error loading message bundle: " + messageBundle + ", url: " + url + ", message: " + event.text);
			}
			
			var urlLoader:URLLoader = new URLLoader(new URLRequest(FlexUtilGlobals.getInstance().createAbsoluteUrl(url)));
			urlLoader.addEventListener(Event.COMPLETE, loadedHandler);
			urlLoader.addEventListener(IOErrorEvent.IO_ERROR, errorHandler);
			urlLoader.addEventListener(SecurityErrorEvent.SECURITY_ERROR, errorHandler);
		}
	}
}