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
package com.crispico.flower.flexdiagram.util.common {

	import mx.collections.ArrayCollection;
	import mx.controls.Button;
	import mx.controls.LinkButton;
	import mx.core.IFlexDisplayObject;
	import mx.core.mx_internal;
	
	use namespace mx_internal;
	
	[Style(name="iconURL", type="Object", inherit="no", states="up, over, down, disabled, selectedUp, selectedOver, selectedDown, selectedDisabled")]
	/**
	 * Using this extended class, a new property "iconURL" can be added to a link button.
	 * It is neccesary when we want to work with URL for icons instead of embedded icons.
	 * 
	 * @author Cristi
	 * @see FlowerLinkButton
	 */	
	public class FlowerButton extends Button {
		
		override mx_internal function viewIconForPhase(tempIconName:String):IFlexDisplayObject {
			// get button's icon URL
			var iconURLStyle:Object = getStyle("iconURL");
			
			// if the style exists
			if (iconURLStyle != null) {
				return ButtonUtils.viewIconForPhase(this, tempIconName);	
			} else {
				// if the iconURL style doesn't exist, try to get its embedded icon
				return super.viewIconForPhase(tempIconName);
			}				
		}
	}
	
}