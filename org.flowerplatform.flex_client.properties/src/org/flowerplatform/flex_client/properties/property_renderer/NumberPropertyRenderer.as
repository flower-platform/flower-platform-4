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
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
*
* Contributors:
* Crispico - Initial API and implementation
*
* license-end
*/
package org.flowerplatform.flex_client.properties.property_renderer {
	import flash.events.Event;
	
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
		
		public function NumberPropertyRenderer() {
			super();
			
			addEventListener(FlexEvent.CREATION_COMPLETE, 
				function(event:FlexEvent):void {
					numberValidator = new NumberValidator();				
					restrict = "0-9\\-";
					setStyle("textAlign", TextAlign.RIGHT);
				}
			);
		}
				
		override public function isValidValue():Boolean {	
			var value:Object = valueToCommit;
			if (value == null) {
				return true;
			}
			if (value != 0) { // done to alow '0', see: https://forums.adobe.com/thread/1038745
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
				
		override public function get valueToCommit():Object {
			var value:Object = super.valueToCommit;
			if (value == "") {
				return null;
			}
			return new Number(value);
		}
		
	}
}