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
		
		/**
		 * Retrieves a message from the properties files. Parameters can be passed
		 * and the {?} place holders will be replaced with them.
		 * 
		 * Copied from MP.
		 */
		public function getMessage(messageId:String, params:Array=null):String {				
			return ResourceManager.getInstance().getString("org_flowerplatform_flexutil", messageId, params);
		}
		
		//////////////////////////////////
		// Icons
		//////////////////////////////////
		
		// General
		
		[Embed(source="/info.png")]			
		public static const iconInfo:Class;
		
		[Embed(source="/exit.png")]
		public static const exitIcon:Class;
		
		[Embed(source="menu.png")]
		public static const menuIcon:Class;
		
		[Embed(source="/plus.gif")]			
		public static const iconCollapsed:Class;	
		
		[Embed(source="/minus.gif")]			
		public static const iconExpanded:Class;
		
		[Embed(source="resultset_next.png")]
		public static const rightArrowImage:Class;
		
		// Switch view(s)
		
		[Embed(source="switch_view.png")]
		public static const switch_view:Class;
		
		[Embed(source="switch_view1.png")]
		public static const switch_one_view:Class;
		
		[Embed(source="switch_view2.png")]
		public static const switch_two_views:Class;
				
		[Embed(source="logo_flower.png")]
		public static const logoFlower:Class;
	}
}