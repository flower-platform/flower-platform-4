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
package org.flowerplatform.flex_client.properties.property_renderer {
	
	import flash.events.Event;
	
	import flashx.textLayout.formats.TextAlign;
	
	import org.flowerplatform.flex_client.properties.property_line_renderer.PropertyLineRenderer;
	
	import spark.components.NumericStepper;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class NumericStepperPropertyRenderer extends NumericStepper implements IPropertyRenderer {
		
		protected var _propertyLineRenderer:PropertyLineRenderer;
				
		public function NumericStepperPropertyRenderer() {
			super();
			
			maximum = int.MAX_VALUE;
			minimum = int.MIN_VALUE;			
			setStyle("textAlign", TextAlign.RIGHT);
			
			addEventListener(Event.CHANGE, function(event:Event):void {_propertyLineRenderer.commit();});			
		}
		
		public function isValidValue():Boolean {	
			return true;
		}
		
		public function set propertyLineRenderer(value:PropertyLineRenderer):void {
			_propertyLineRenderer = value;				
		}			
		
		public function get valueToCommit():Object {			
			if (isNaN(value)) {
				return null;
			}
			return value;
		}
		
		public function valueChangedHandler():void {
			this.value = _propertyLineRenderer.node.getPropertyValue(_propertyLineRenderer.propertyDescriptor.name);			
		}
		
		public function propertyDescriptorChangedHandler():void {
			enabled = !_propertyLineRenderer.propertyDescriptor.readOnly;			
		}
		
	}
}