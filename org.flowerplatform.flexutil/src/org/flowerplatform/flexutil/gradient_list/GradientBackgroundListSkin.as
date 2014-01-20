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
	import mx.core.ClassFactory;
	import mx.graphics.GradientEntry;
	import mx.graphics.LinearGradient;
	import mx.graphics.SolidColor;
	
	import spark.components.Scroller;
	import spark.components.supportClasses.GroupBase;
	import spark.skins.spark.ListSkin;
	 
	/**
	 * ListSkin for desktop, with gradient background (using <code>GradientListSkinLogic</code>)
	 * and custom vertical scrollbars.
	 * 
	 * @author Mariana
	 */ 
	public class GradientBackgroundListSkin extends ListSkin {
		
		override protected function initializationComplete():void {
			super.initializationComplete();
			GradientListSkinLogic.initializationComplete(hostComponent);
		}
		
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			GradientListSkinLogic.updateDisplayList(unscaledWidth, unscaledHeight, hostComponent, background);
			
			// the scroller layout takes care of the position of the viewport inside the scroller
			GroupBase(scroller.skin).layout = new TopAndBottomVerticalScrollerLayout();
			
			// set the skin factory for the vertical scrollbar
			if (scroller.verticalScrollBar) {
				var factory:ClassFactory = new ClassFactory(TopAndBottomVerticalScrollBarSkin);
				// this will set the width button to the width of the component
				factory.properties = { componentWidth : this.unscaledWidth };
				scroller.verticalScrollBar.setStyle("skinFactory", factory);
			}
		}
		
	}
}