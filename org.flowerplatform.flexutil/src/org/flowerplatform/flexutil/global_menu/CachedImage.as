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
package org.flowerplatform.flexutil.global_menu {
	
	import flash.display.LoaderInfo;
	import flash.events.Event;
	
	import mx.controls.Image;
	import mx.core.mx_internal;
	
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	
	/**
	 * An <code>mx.controls.Image</code> that uses the global content cache. 
	 * 
	 * @author Mariana Gheorghe
	 */
	public class CachedImage extends Image {
		
		override public function load(url:Object=null):void {
			// get the content from the cache
			var cachedInfo:LoaderInfo = LoaderInfo(FlexUtilGlobals.getInstance().imageContentCache.getCacheEntry(url));
			if (cachedInfo == null) {
				// this is the first time this content was requested; load it
				cachedInfo = LoaderInfo(FlexUtilGlobals.getInstance().imageContentCache.load(url).content);
			}
			
			// set the content holder for this image
			if (mx_internal::contentHolder == null) {
				mx_internal::contentHolder = cachedInfo.loader;
				addChild(cachedInfo.loader);
			}
			
			if (cachedInfo.content == null) {
				// content is not yet loaded; listen for the COMPLETE event
				cachedInfo.addEventListener(Event.COMPLETE, completed);
			} else {
				// content was loaded beforehand; display it
				contentLoaded();
				invalidateDisplayList();
			}
		}
		
		protected function completed(event:Event):void {
			// simply pass the event to the handler
			var content:LoaderInfo = LoaderInfo(event.target);
			content.removeEventListener(Event.COMPLETE, completed);	
			mx_internal::contentLoaderInfo_completeEventHandler(event);
		}
		
	}
}