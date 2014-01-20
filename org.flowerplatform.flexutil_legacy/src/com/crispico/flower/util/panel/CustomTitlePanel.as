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
package com.crispico.flower.util.panel
{
	import com.crispico.flower.flexdiagram.util.common.BitmapContainer;
	import com.crispico.flower.util.spinner.ModalSpinner;
	import com.crispico.flower.util.spinner.ModalSpinnerSupport;
	
	import flash.display.DisplayObject;
	
	import mx.containers.TitleWindow;
	import mx.core.IFlexDisplayObject;
	import mx.core.mx_internal;
	
	use namespace mx_internal;
	
	/**
	 * Panel that allows the use of custom icons, mainly
	 * meant to be used with images retrieved from <code>ImageFactory</code>.	
	 * 
	 * @author Cristina 
	 * 
	 */  
	public class CustomTitlePanel extends TitleWindow implements ModalSpinnerSupport {
		
		private var _modalSpinner:ModalSpinner;
		
		/**
		 * The icon URL displayed in the title bar.
		 */ 
		public var titleIconURL:String;
		
		/**
		 * If <code>titleIconURL</code> is set, replaces the image class
		 * with a <code>BitmapContainer</code> object.
		 */ 
		override protected function commitProperties():void {			
			var newIcon:IFlexDisplayObject;
			if (titleIconURL != null) {			
				newIcon = new BitmapContainer(titleIconURL);
				titleIcon = null;
			}			
			super.commitProperties();
			
			if (newIcon != null) {
				mx_internal::titleIconObject = newIcon;
				titleBar.addChild(DisplayObject(mx_internal::titleIconObject));  				
				if (initialized)
                	layoutChrome(unscaledWidth, unscaledHeight);                    
	 		}
		}
		
		public function get modalSpinner():ModalSpinner	{
			return _modalSpinner;
		}
		
		public function set modalSpinner(value:ModalSpinner):void {
			_modalSpinner = value;
		}
		
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			if (modalSpinner != null) {
				// reposition the modal spinner so that we leave the title bar uncovered
				modalSpinner.y = titleBar.height;
				modalSpinner.setActualSize(unscaledWidth, unscaledHeight - titleBar.height);
			}
		}

	}
}