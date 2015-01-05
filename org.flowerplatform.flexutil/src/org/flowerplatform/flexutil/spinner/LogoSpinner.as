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
package org.flowerplatform.flexutil.spinner {
	import mx.controls.Image;
	
	import org.flowerplatform.flexutil.FlexUtilGlobals;

	/**
	 * Spinner with logo.
	 * @author Cristina Constantinescu
	 */ 
	public class LogoSpinner extends ModalSpinner {
	
		private var logoIcon:Object;
		
		public function LogoSpinner(logoIcon:Object) {
			this.logoIcon = logoIcon;
		}
					
		override protected function createChildren():void {
			super.createChildren();
			
			spinner.size = 46;
			
			var icon:Image = new Image();
			if (logoIcon is String)
				icon.source = FlexUtilGlobals.getInstance().adjustImageBeforeDisplaying(logoIcon);
			else
				icon.source = logoIcon;
			addChildAt(icon, 0);			
		}
		
	}
}
