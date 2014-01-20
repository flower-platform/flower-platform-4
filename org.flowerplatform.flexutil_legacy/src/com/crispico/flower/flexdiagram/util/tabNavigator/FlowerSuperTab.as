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
package  com.crispico.flower.flexdiagram.util.tabNavigator {
	
	import com.crispico.flower.flexdiagram.util.common.ButtonUtils;
	
	import flash.display.DisplayObject;
	
	import flexlib.controls.tabBarClasses.SuperTab;
	
	import mx.controls.Button;
	import mx.core.IFlexDisplayObject;
	import mx.core.mx_internal;
	
	use namespace mx_internal;
	
	/**
	 * Besides knowing how to retrive an image from a Class, this graphical component has the
	 * following functionality : setting the "iconURL" style, knows how to show the image
	 * based on its URL.
	 * 
	 * @author Cristina
	 * 
	 */
	public class FlowerSuperTab extends SuperTab {		
		
		[Embed(source="/closeContextMenu.png")]      
		private var closeButtonIcon:Class;
		
		public function FlowerSuperTab() {
				
		}
		
		/**
		 * 
		 */
		override mx_internal function viewIconForPhase(tempIconName:String):IFlexDisplayObject {
			return ButtonUtils.viewIconForPhase(this, tempIconName);
		}
		
		override public function addChild(child:DisplayObject):DisplayObject {			
			if (child is Button) {
				var closeButton:Button = child as Button;			
				closeButton.setStyle("icon", closeButtonIcon);
			}
			return super.addChild(child);
		}
	}
	
}