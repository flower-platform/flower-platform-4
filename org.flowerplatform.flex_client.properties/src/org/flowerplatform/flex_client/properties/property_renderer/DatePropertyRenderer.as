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
	import mx.binding.utils.BindingUtils;
	import mx.events.FlexEvent;
	
	import spark.formatters.DateTimeFormatter;
	
	/**
	 * @author Sebastian Solomon
	 * @author Cristina Constantinescu
	 */
	public class DatePropertyRenderer extends StringPropertyRenderer {
		
		override protected function getValue():Object {
			return data.value;	
		}
		
		override protected function creationCompleteHandler(event:FlexEvent):void {
			BindingUtils.bindSetter(updateDate, data, "value");
		}
		
		private function updateDate(newValue:Object):void {				
			if (newValue is Date) {
				var dtf:DateTimeFormatter = new DateTimeFormatter();
				dtf.dateTimePattern = "yyyy-MM-dd HH:mm:ss";
				propertyValue.text = dtf.format(newValue);
			}
			
		}
		
	}
}
