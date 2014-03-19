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
	import mx.events.FlexEvent;
	
	/**
	 * @author Sebastian Solomon
	 */
	public class DatePropertyRenderer extends StringPropertyRenderer {
		
		override protected function getValue():Object {
			return data.value;	
		}
		
		override protected function creationCompleteHandler(event:FlexEvent):void {
			if (data.value is Date) {
				var date:Date = data.value as Date;
				var month:String = addLeadingZero(date.monthUTC + 1);
				var day:String = addLeadingZero(date.dateUTC);
				var hours:String = addLeadingZero(date.hours);
				var minutes:String = addLeadingZero(date.minutes);
				var seconds:String = addLeadingZero(date.seconds);
				// YYYY-MM-DD hh:mm:ss
				propertyValue.text = date.fullYear + "-" + month + "-" + day + " " + 
						hours + ":" + minutes + ":" + seconds; 
			}
		}
		
		protected function addLeadingZero(number:Number):String {
			return (number >= 10 ? '' : '0') + number;
		}
		
	}
}
