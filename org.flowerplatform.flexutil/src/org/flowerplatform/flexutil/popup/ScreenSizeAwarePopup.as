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
package org.flowerplatform.flexutil.popup {
	
	import mx.core.FlexGlobals;
	import mx.events.FlexEvent;
	
	import spark.components.BorderContainer;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class ScreenSizeAwarePopup extends BorderContainer { 
		
		public function ScreenSizeAwarePopup() {
			super();
			addEventListener(FlexEvent.UPDATE_COMPLETE, adjustPositionIfNeededHandler);
		}
		
		protected function adjustPositionIfNeededHandler(event:FlexEvent):void {
			var appWidth:Number = FlexGlobals.topLevelApplication.width;
			var appHeight:Number = FlexGlobals.topLevelApplication.height;
			if (x + width > appWidth) {
				// exits the visible area / horizontal axis
				adjustHorizontalPosition(appWidth);
			}
			if (y + height > appHeight) {
				// exits the visible area / vertical axis
				adjustVerticalPosition(appHeight);
			}
		}
		
		protected function adjustHorizontalPosition(appWidth:Number):void {
			x = Math.max(0, appWidth - width);
		}
		
		protected function adjustVerticalPosition(appHeight:Number):void {
			if (y - height > 0) {
				// if the whole component can be translated before the cursor, do it!
				y -= height + verticalOffset;
			} else {
				// try at least to make it not exit the screen
				y = Math.max(0, appHeight - height);
			}
		}
		
		protected function get verticalOffset():Number {
			return 0;
		}
	}
}