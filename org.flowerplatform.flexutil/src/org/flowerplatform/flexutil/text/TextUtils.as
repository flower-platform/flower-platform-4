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
package org.flowerplatform.flexutil.text {
	import flash.events.FocusEvent;
	
	import mx.core.UIComponent;
	import mx.core.UITextField;
	import mx.utils.StringUtil;
	
	import spark.components.TextArea;
	import spark.components.TextInput;

	public class TextUtils {
		
		/**
		 * Given a text component, this method will add behavior to it so that when the component doesn't have focus
		 * it will show a special hint message for the user.
		 * <p>
		 * The component may be a TextArea, TextInput, UITextField. If the component is not recognized, the <code>textProperty</code>
		 * parameter is used to set and get the <code>hint</code>. It is though advised to update the implementation of this method.
		 * 
		 * @author Sorin
		 */ 
		public static function setTextComponentHint(uiComponent:UIComponent, hint:String, textProperty:String = null):void {
			if (textProperty == null) {
				if (uiComponent is TextArea || uiComponent is TextInput || uiComponent is UITextField) 
					textProperty = "text";
			}
			
			if (textProperty == null)
				throw "Impossible to add hint to " + uiComponent + " because textProperty was not passed or could not be determined";
			
			var focusFunction:Function = 
				function(event:FocusEvent):void {
					var currentText:String = uiComponent[textProperty] as String;
					if (event.type == FocusEvent.FOCUS_OUT && StringUtil.trim(currentText).length == 0) {
						uiComponent[textProperty] = hint;
						uiComponent.setStyle("color", 0x999999);
					} else if (event.type == FocusEvent.FOCUS_IN && currentText.replace(/\r|\n|\r\n/g, "") == hint.replace(/\r|\n|\r\n/g, "")) { // without \r or \n characters because the textarea component may choose to modify the hint string
						uiComponent[textProperty] = "";
						uiComponent.setStyle("color", 0x000000);
					}
				}
			
			focusFunction(new FocusEvent(FocusEvent.FOCUS_OUT)); // Run at least once to set the hint at the beginning
			uiComponent.addEventListener(FocusEvent.FOCUS_OUT, focusFunction);
			uiComponent.addEventListener(FocusEvent.FOCUS_IN, focusFunction);
		}
	}
}