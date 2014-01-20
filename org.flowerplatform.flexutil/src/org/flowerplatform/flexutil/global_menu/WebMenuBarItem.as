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
package org.flowerplatform.flexutil.global_menu {
	import flash.display.DisplayObject;
	
	import mx.controls.Image;
	import mx.controls.menuClasses.MenuBarItem;
	
	import org.flowerplatform.flexutil.action.IAction;
	
	/**
	 * <code>MenuBarItem</code> renderer that adds the possibility to show an image from
	 * a URL.
	 * 
	 * @author Mariana
	 * @author Mircea Negreanu
	 */ 
	public class WebMenuBarItem extends MenuBarItem {
		
		/**
		 * @author Mariana
		 * @author Mricea Negreanu
		 */
		override protected function commitProperties():void	{
			super.commitProperties();
			
			if (icon == null) { 
				icon = createIconFromUrl();
				if (icon != null)
					addChild(DisplayObject(icon));
			}
		}
		
		/**
		 * @author Mariana
		 * @author Mircea Negreanu
		 */
		private function createIconFromUrl(img:Image = null):Image {
			if (data is IAction && IAction(data).icon is String) {
				if (img == null) {
					img = new Image();
				}
				img.source = IAction(data).icon;
				img.maxWidth = 16;
				img.maxHeight = 16;
				return img;
			}
			return null;
		}
		
	}
}