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
package org.flowerplatform.flex_client.host_app.mobile.text {
	
	import flash.events.FocusEvent;
	
	import mx.events.FlexEvent;
	
	import spark.skins.mobile.TextInputSkin;
	
	/**
	 * @author Mariana
	 */
	public class ValidatingTextInputSkin extends TextInputSkin {
		
		override protected function createChildren():void {
			super.createChildren();
			
			// trigger validation when the text input loses focus
			textDisplay.addEventListener(FocusEvent.FOCUS_OUT, function(evt:FocusEvent):void {
				textDisplay.dispatchEvent(new FlexEvent(FlexEvent.VALUE_COMMIT));
			});
		}
		
		/**
		 * Copied from super.measure(), except for the part where the text is set, to avoid
		 * an initial validation when the application starts.
		 */
		override protected function measure():void {
			var paddingLeft:Number = getStyle("paddingLeft");
			var paddingRight:Number = getStyle("paddingRight");
			var paddingTop:Number = getStyle("paddingTop");
			var paddingBottom:Number = getStyle("paddingBottom");
			var textHeight:Number = getStyle("fontSize") as Number;
			
			// width is based on maxChars (if set)
			if (hostComponent && hostComponent.maxChars)
			{
				// Grab the fontSize and subtract 2 as the pixel value for each character.
				// This is just an approximation, but it appears to be a reasonable one
				// for most input and most font.
				var characterWidth:int = Math.max(1, (getStyle("fontSize") - 2));
				measuredWidth =  (characterWidth * hostComponent.maxChars) + 
					paddingLeft + paddingRight;
			}
			
			measuredHeight = paddingTop + textHeight + paddingBottom;
		}
		
	}
}