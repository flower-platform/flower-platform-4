/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
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
package org.flowerplatform.flexutil.mobile {
	import flash.display.Graphics;
	import flash.display.Sprite;
	
	import mx.core.mx_internal;
	
	import spark.components.ViewNavigatorApplication;
	
	use namespace mx_internal;
	
	/**
	 * The mouse shield when the menu is open has customizable color and alpha.
	 * 
	 * @author Cristian Spiescu
	 */
	public class FlowerViewNavigatorApplication extends ViewNavigatorApplication {
		
		private var mouseShild1:Sprite;
		
		public var mouseShieldColor:uint = 0;
		
		public var mouseShieldAlpha:Number = 0.5;
		
		/**
		 * Copied from the superclass, because mouseShild is private.
		 */
		override mx_internal function attachMouseShield():void
		{
			if (skin)
			{
				mouseShild1 = new Sprite();
				
				var g:Graphics = mouseShild1.graphics;
				g.beginFill(mouseShieldColor,mouseShieldAlpha);
				g.drawRect(0,0,getLayoutBoundsWidth(), getLayoutBoundsHeight());
				g.endFill();
				
				skin.addChild(mouseShild1);
			}
		}
		
		override mx_internal function removeMouseShield():void
		{
			if (mouseShild1 && skin)
			{
				skin.removeChild(mouseShild1);
				mouseShild1 = null;
			}
		}			
	}
}