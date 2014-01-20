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
package org.flowerplatform.flexutil.mobile.view_content_host {
	import mx.core.FlexGlobals;
	import mx.validators.EmailValidator;
	
	import org.flowerplatform.flexutil.action.ActionBase;
	
	import spark.components.View;
	import spark.components.ViewMenuItem;
	
	public class OpenMenuAction extends ActionBase {
		
		protected var view:MobileViewHostBase;
		public var viewMenuItems:Vector.<ViewMenuItem>;
		
		[Embed(source="menu.png")]
		public static const menuIcon:Class;
		
		public function OpenMenuAction(view:MobileViewHostBase) {
			this.view = view;
			icon = menuIcon;
		}
		
		override public function get enabled():Boolean {
			return viewMenuItems != null && viewMenuItems.length > 0;
		}
		
		override public function run():void {
			FlexGlobals.topLevelApplication.viewMenuOpen = true;
		}
		
	}
}