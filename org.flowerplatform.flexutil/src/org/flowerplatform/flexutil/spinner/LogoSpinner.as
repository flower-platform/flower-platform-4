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

	/**
	 * Spinner with logo.
	 * @author Cristina Constantinescu
	 */ 
	public class LogoSpinner extends ModalSpinner {
	
		private var logoIcon:Class;
		
		public function LogoSpinner(logoIcon:Class) {
			this.logoIcon = logoIcon;
		}
					
		override protected function createChildren():void {
			super.createChildren();
			
			spinner.size = 46;
			
			var icon:Image = new Image();
			icon.source = logoIcon;
			addChildAt(icon, 0);			
		}
		
	}
}