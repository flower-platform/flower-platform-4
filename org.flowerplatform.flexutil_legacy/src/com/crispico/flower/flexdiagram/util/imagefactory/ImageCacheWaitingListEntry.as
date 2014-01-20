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
	
	/**
	 * This class groups the following information:
	 * <ul>
	 * 	<li>callbackObject - the object that requested an image</li>
	 * 	<li>
	 * 	callbackProperty - property of the callbackObject that will be set 
	 * 	to the bitmap data of the image, as soon as the image is available
	 * 	</li>
	 * </ul>
	 * 
	 * @author Marius Arhire
	 * @author Florin Buzatu
	 * 
	 * 
	 */
	internal class ImageCacheWaitingListEntry {
		
		internal var callbackObject:Object;
		
		internal var callbackProperty:String;
		
		function ImageCacheWaitingListEntry(callbackObject:Object, callbackProperty:String) {
			this.callbackObject = callbackObject;
			this.callbackProperty = callbackProperty;
		}
	} 
}