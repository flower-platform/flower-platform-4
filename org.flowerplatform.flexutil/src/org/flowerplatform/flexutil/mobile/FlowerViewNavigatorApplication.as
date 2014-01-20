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