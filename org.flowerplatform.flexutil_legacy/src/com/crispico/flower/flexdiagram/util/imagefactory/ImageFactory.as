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
package  com.crispico.flower.flexdiagram.util.imagefactory {
	import flash.display.BitmapData;
	import flash.utils.Dictionary;
	import flash.utils.getQualifiedClassName;
	
	import mx.collections.ArrayCollection;
	import mx.core.Application;
	/**
	 * This class provides a fast way to retrieve an image or a composed image by using caching.
	 * The image can be retrived using <code>retrieveImage</code> method.
	 * 
	 * @author Florin Buzatu
	 * 
	 * 
	 */
	public class ImageFactory {
		
		/**
		 * Character used yo create a key. Is an invalid character for an url.
		 */
		private static var URL_SEPARATOR:String = "|";
		
		/**
		 * Bitmap data temporarily returned to client, until the requested image becomes available
		 * (downloading and composing images process is finished).
		 */
		private static var _unavailableBitmapData:BitmapData;
		
		[Embed(source="/defaultIcon.gif")]
		private static const UNAVAILABLE_CLASS:Class;
				
		/**
		 * 
		 */
		private var cache:Dictionary;

		/**
		 * The ImageFactory singleton.
		 */
		public static const INSTANCE:ImageFactory = new ImageFactory();
		
		/**
		 * This controls if to show an warning with the workaround when detecting 
		 * security problems due to the impossibility of downloading images locally.
		 * By default it is false and can be altered externally.
		 * 
		 */
		 
		public static var showSecurityWorkaroundMessage:Boolean = false;
		/**
		 * This keeps the workaround message to be shown to the user when security problems are detected. 
		 * This will be shown only if #showSecurityWorkaround flag is enabled.
		 * By default it is initialized by the <code>ImageFactory</code> but may be externally altered.
		 * 
		 */
		public static var securityWorkaroundMessage:String;
		
		/**
		 * 
		 */
		 function ImageFactory() {
			if (INSTANCE != null) 
				throw new Error("ImageFactory is a singleton and cannot be instantiated!");
			_unavailableBitmapData = new UNAVAILABLE_CLASS().bitmapData;
			cache = new Dictionary();
			initializeSecurityDetails();
		}
		
		/**
		 * The method initializes downloading process for the images found at <code>imageUrls</code>.
		 * The images are composed into a single image. Afterwards, the caller is notified by assigning the 
		 * bitmap data of the resulting image to <code>callbackObject[callbackProperty]</code>.
		 * 
		 * @param imageUrls - stores the requested urls for the icon and its decorators
		 * @param callbackObject - the object that needs the icon
		 * @param callbackPropriery - property of the callbackObject that is to be changed at update
		 * @param calledByUser - when called by user, the method sets the bitmap data to a default image, until
		 * the real bitmap data is loaded
		 * 
		 * 
		 */
		public function retrieveImage(imageUrls:ArrayCollection, callbackObject:Object, callbackProperty:String, calledByUser:Boolean=true):void {
			var key:String = getCacheKey(imageUrls);
			var imageCacheWaitingListEntry:ImageCacheWaitingListEntry = new ImageCacheWaitingListEntry(callbackObject, callbackProperty);
						
			var cacheEntry:ImageCacheEntry = cache[key] == null ? new ImageCacheEntry(imageUrls, imageCacheWaitingListEntry) : ImageCacheEntry(cache[key]); 			
						
			if (cache[key] == null) {
				// This means there is no entry in the cache for the required icon
				if (calledByUser)
					callbackObject[callbackProperty] = _unavailableBitmapData;
				cache[key] = cacheEntry;
			} else if (cacheEntry.data == null) {
				 /* This means there is an entry in the cache for the required icon,
				 but it's not loaded yet */
				if (calledByUser)
					callbackObject[callbackProperty] = _unavailableBitmapData;
				cacheEntry.addToWaitingList(imageCacheWaitingListEntry);
			} else {
				/* This means there is an entry in the cache with the required icon
				and it's ready to be loaded */
				callbackObject[callbackProperty] = cacheEntry.data;
			}			
		}
		
		public function get unavailableBitmapData():BitmapData {
			return _unavailableBitmapData;
		}
		 
		/**
		 * Returns the string created by concatenating all the strings (image and decorators urls)
		 * from the array collection.
		 */
		internal function getCacheKey(sourceImages:ArrayCollection):String {
			var key:String = "";
			for each (var object:Object in sourceImages) {
				if (object is Class) {
					key += getQualifiedClassName(Class(object));					
				} else { // String
					key += object;
				}
				key += URL_SEPARATOR;	
			}	 
			return key;
		}
		
		/**
		 * Returns the ImageCacheEntry for the url. Used by <code>ImageCacheEntry</code> of a composed image
		 * to retrieve from cache the entries for single images. 
		 */
		internal function getImageCacheEntry(object:Object): ImageCacheEntry {
			if (object is Class) {
				return cache[getQualifiedClassName(Class(object)) + URL_SEPARATOR];
			}
			return cache[object + URL_SEPARATOR]
		}
		
		/**
		 * By default <code>ImageFactory</code> initializes #showSecurityWorkaround and #securityWorkaroundMessage.
		 * These 2 are used by <code>ImageCacheEntry</code> when it detect security issues to know if to show and what message to show.
		 * 
		 */
		 internal function initializeSecurityDetails():void {
		 	showSecurityWorkaroundMessage = false;
		 	
		 	var appUrl:String = Application.application.url;
		 	var index:int = appUrl.lastIndexOf("/");
			if (index == -1) {
				return;
			}			
			var swfFolder:String = appUrl.substr(0, index);
		 	
		 	securityWorkaroundMessage = "A security issue prevent us to load images from local disk. " + 
               		   "Please edit the security settings to allow access to local disk, by adding " +
               		   swfFolder + " to \"Always trust files in these locations\"." + 
               		   "Security settings manager can be accessed at this url: " + 
               		   "http://www.macromedia.com/support/documentation/en/flashplayer/help/settings_manager04.html";
		 }
	}
}