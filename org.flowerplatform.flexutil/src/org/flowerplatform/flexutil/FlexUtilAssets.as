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
package org.flowerplatform.flexutil {
	import mx.resources.ResourceManager;
	
	[ResourceBundle("org_flowerplatform_flexutil")]
	/**
	 * @author Cristi
	 */
	public class FlexUtilAssets {
				
		[Bindable]
		public static var INSTANCE:FlexUtilAssets = new FlexUtilAssets(); 
		
		[Embed(source="/info.png")]			
		public static const _iconInfo:Class;	
		
		/**
		 * Retrieves a message from the properties files. Parameters can be passed
		 * and the {?} place holders will be replaced with them.
		 * 
		 * Copied from MP.
		 */
		public function getMessage(messageId:String, params:Array=null):String {				
			return ResourceManager.getInstance().getString("org_flowerplatform_flexutil", messageId, params);
		}
		
	}
}