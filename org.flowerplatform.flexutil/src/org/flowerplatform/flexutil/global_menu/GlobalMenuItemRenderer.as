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
	
	import flash.display.DisplayObject;
	
	import mx.controls.menuClasses.MenuItemRenderer;
	import mx.core.IFlexDisplayObject;
	
	import org.flowerplatform.flexutil.action.IAction;
	
	/**
	 * Renderer implementation that uses <code>icon</code> attribute from an IAction node to
	 * retrieve an image from the URL.
	 * <p>
	 * Used by application <code>WebMenuBar</code> to set icons on pop-up submenus based on their url.
	 * 
	 * @author Cristina
	 * @author Mircea Negreanu
	 */ 
	public class GlobalMenuItemRenderer extends MenuItemRenderer {
		
		private var cachedImage:CachedImage;
		
		/**
		 * @author Cristina
		 * @author Mircea Negreanu
		 */
		override protected function commitProperties():void {
			super.commitProperties();
			if (icon == null) {
				var newIcon:IFlexDisplayObject = createIcon();
				if (newIcon != null) {
					icon = newIcon;
					addChild(DisplayObject(icon));
				}
			}	
		}
		
		/**
		 * @author Cristina
		 * @author Mircea Negreanu
		 */
		protected function createIcon():IFlexDisplayObject {			
			if (data is IAction && IAction(data).icon is String) {
				if (cachedImage == null) {
					cachedImage = new CachedImage();
					cachedImage.source = IAction(data).icon;
					cachedImage.maxWidth = 16;
					cachedImage.maxHeight = 16;
				}
				return cachedImage;
			} else {
				return null;
			}
		} 
		
	}
}