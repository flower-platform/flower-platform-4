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
package com.crispico.flower.util {
	import mx.resources.ResourceManager;
	
	[ResourceBundle("com_crispico_flower_util")]
	/**
	 * @author Cristi
	 */
	public class UtilAssets {
		
		[Bindable]
		public static var INSTANCE:UtilAssets = new UtilAssets(); 
		
		/**
		 * Retrieves a message from the properties files. Parameters can be passed
		 * and the {?} place holders will be replaced with them.
		 * 
		 * Copied from MP.
		 */
		public function getMessage(messageId:String, params:Array=null):String {				
			return ResourceManager.getInstance().getString("com_crispico_flower_util", messageId, params);
		}
		
		[Embed(source="/view.png")]
		public var _viewIcon:Class;
		
		[Embed(source="/views.png")]
		public var _viewsIcon:Class;
		
		[Embed(source="/close_tab.png")]
		public var _closeTabIcon:Class;
		
		[Embed(source="/close_view.png")]
		public var _closeViewIcon:Class;
		
		[Embed(source="/close_all_views.png")]
		public var _closeAllViewIcon:Class;
		
		[Embed(source="/move_view.png")]
		public var _moveViewIcon:Class;
				
		[Embed(source="/tab_min.gif")]      
		public var tabMin:Class;
				
		[Embed(source="/tab_max.gif")]      
		public var tabMax:Class;
		
		[Embed(source="/tab_res.gif")]      
		public var tabRes:Class;
		
		[Embed(source="/maximize_view.png")]      
		public var _maximizeViewIcon:Class;
		
		[Embed(source="/restore_view.png")]      
		public var _restoreViewIcon:Class;
		
		[Embed(source="/minimize_view.png")]      
		public var _minimizeViewIcon:Class;
		
		[Embed(source="/dock.png")]      
		public var _dockIcon:Class;
		
		[Embed(source="/calendar.png")]
		public var _calendar:Class;
		
		[Embed(source="/info.png")]
		public var _infoIcon:Class;
	}
}