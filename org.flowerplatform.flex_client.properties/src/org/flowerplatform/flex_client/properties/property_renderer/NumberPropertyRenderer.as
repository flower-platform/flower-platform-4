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
package org.flowerplatform.flex_client.properties.property_renderer {
	import flashx.textLayout.formats.TextAlign;
	
	import mx.events.FlexEvent;
	import mx.events.ValidationResultEvent;
	import mx.validators.ValidationResult;
	
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	
	import spark.validators.NumberValidator;
	
	/**
	 * @author Mariana Gheorghe
	 * @author Cristina Constantinescu
	 */
	public class NumberPropertyRenderer extends StringPropertyRenderer {
		
		private var numberValidator:NumberValidator;
		
		override protected function creationCompleteHandler(event:FlexEvent):void {
			super.creationCompleteHandler(event);
			
			numberValidator = new NumberValidator();
			
			propertyValue.restrict = "0-9\\-";
			propertyValue.setStyle("textAlign", TextAlign.RIGHT);
		}
		
		override protected function validPropertyValue():Boolean {
			var value:Object = getValue();
			if (value == null) {
				return true;
			}
			if (value != 0) {// done to alow '0', see: https://forums.adobe.com/thread/1038745
				var validationResultEvent:ValidationResultEvent = numberValidator.validate(value);			
				if (validationResultEvent.type == ValidationResultEvent.INVALID) {	
					// show validation error to client
					FlexUtilGlobals.getInstance().messageBoxFactory.createMessageBox()
						.setTitle(Resources.getMessage("properties.view"))
						.setText(ValidationResult(validationResultEvent.results[0]).errorMessage)
						.setWidth(300)
						.setHeight(200)					
						.showMessageBox();					
					return false;
				}
			}
			return true;
		}
		
		override protected function getValue():Object {
			var value:Object = super.getValue();
			if (value == "") {
				return null;
			}
			return new Number(value);
		}
	}
}