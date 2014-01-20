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
package org.flowerplatform.flexutil.mobile.gradient_list {
		
	
	import org.flowerplatform.flexutil.gradient_list.GradientListSkinLogic;
	
	import spark.skins.mobile.ListSkin;
	 
	/**
	 * ListSkin for mobile, with gradient background (using <code>GradientListSkinLogic</code>).
	 * 
	 * @author Mariana  
	 */ 
	public class GradientBackgroundMobileListSkin extends ListSkin {
		
		override protected function initializationComplete():void {
			super.initializationComplete();
			GradientListSkinLogic.initializationComplete(hostComponent);
		}
		
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			GradientListSkinLogic.updateDisplayList(unscaledWidth, unscaledHeight, hostComponent);
		}	
		
	}
}