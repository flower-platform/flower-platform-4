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
package org.flowerplatform.flexutil.gradient_list {
	
	import spark.components.Scroller;
	import spark.components.supportClasses.ScrollerLayout;
	import spark.components.supportClasses.Skin;
	import spark.core.IViewport;
	
	/**
	 * Custom layout for vertical scrolling. This is meant to be used together with the
	 * <code>CustomVScrollBarSkin</code>. 
	 * 
	 * @author Mariana
	 */ 
	public class TopAndBottomVerticalScrollerLayout extends ScrollerLayout {
		
		/**
		 * Position the viewport inside the scroller. 
		 * 
		 * <p>
		 * If the vertical scrollbars are visible, make sure the scroll buttons don't cover up
		 * the viewport.
		 */ 
		override public function updateDisplayList(w:Number, h:Number):void {
			super.updateDisplayList(w, h);
			
			var g:Skin = target as Skin;
			var scroller:Scroller = (g && ("hostComponent" in g)) ? Object(g).hostComponent as Scroller : null;
		
			if (scroller) {
				var viewport:IViewport = scroller.viewport;
				if (viewport && scroller.verticalScrollBar && scroller.verticalScrollBar.visible) {
					// move the viewport just under the up scroll button
					viewport.setLayoutBoundsPosition(0, TopAndBottomVerticalScrollBarSkin.BUTTON_SIZE);
					// set the height of the viewport so it doesn't get covered up by the down button
					viewport.setLayoutBoundsSize(viewport.width, viewport.height - 2 * TopAndBottomVerticalScrollBarSkin.BUTTON_SIZE);
				}
			}
		}
		
	}
}