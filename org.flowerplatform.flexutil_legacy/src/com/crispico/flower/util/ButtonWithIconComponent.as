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
	import mx.controls.Button;
	import mx.core.IFlexDisplayObject;
	import mx.core.UIComponent;
	import mx.core.mx_internal;
	
	use namespace mx_internal;
	
	/**
	 * Button that hosts any type of UIComponent as icon.
	 * 
	 * @author Cristi
	 */
	public class ButtonWithIconComponent extends Button	{
		
		public var iconComponent:UIComponent;
		
		override mx_internal function viewIconForPhase(tempIconName:String):IFlexDisplayObject {
			if (currentIcon == null && iconComponent != null) {
				currentIcon = iconComponent;
				addChild(iconComponent);
			}
			return currentIcon;
		}
	}
}