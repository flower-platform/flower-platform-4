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
	
	import flash.events.FocusEvent;
	import flash.events.KeyboardEvent;
	import flash.ui.Keyboard;
	
	import org.flowerplatform.flex_client.properties.property_line_renderer.PropertyLineRenderer;
	
	import spark.components.TextInput;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class StringPropertyRenderer extends TextInput implements IPropertyRenderer {
		
		protected var _propertyLineRenderer:PropertyLineRenderer;
		
		public function StringPropertyRenderer() {
			super();
		
			addEventListener(KeyboardEvent.KEY_UP, 
				function(event:KeyboardEvent):void {
					if (event.keyCode == Keyboard.ENTER) {
						_propertyLineRenderer.commit();
					}
				}
			);
			addEventListener(FocusEvent.FOCUS_OUT, function(event:FocusEvent):void {_propertyLineRenderer.commit();});
		}
				
		public function isValidValue():Boolean {	
			return true;
		}
		
		public function set propertyLineRenderer(value:PropertyLineRenderer):void {
			_propertyLineRenderer = value;				
		}			
		
		public function get valueToCommit():Object {
			return this.text;
		}
		
		public function valueChangedHandler():void {
			this.text = _propertyLineRenderer.node.getPropertyValue(_propertyLineRenderer.propertyDescriptor.name);
		}
		
		public function propertyDescriptorChangedHandler():void {
			enabled = !_propertyLineRenderer.propertyDescriptor.readOnly;
		}
		
	}
}