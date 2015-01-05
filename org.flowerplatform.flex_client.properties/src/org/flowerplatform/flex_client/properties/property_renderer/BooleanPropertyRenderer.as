/* license-start
 * 
 * Copyright (C) 2008 - 2014 Crispico Software, <http://www.crispico.com/>.
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
	
	import flash.events.Event;
	import flash.events.MouseEvent;
	
	import org.flowerplatform.flex_client.properties.property_line_renderer.PropertyLineRenderer;
	
	import spark.components.CheckBox;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class BooleanPropertyRenderer extends CheckBox implements IPropertyRenderer {
		
		protected var _propertyLineRenderer:PropertyLineRenderer;
		
		public function BooleanPropertyRenderer() {
			super();			
			addEventListener(MouseEvent.CLICK, function(event:Event):void { _propertyLineRenderer.commit();});
		}
			
		public function isValidValue():Boolean {	
			return true;
		}
		
		public function set propertyLineRenderer(value:PropertyLineRenderer):void {
			_propertyLineRenderer = value;				
		}			
		
		public function get valueToCommit():Object {
			return selected;
		}
		
		public function valueChangedHandler():void {
			selected = _propertyLineRenderer.node.getPropertyValue(_propertyLineRenderer.propertyDescriptor.name);
		}
		
		public function propertyDescriptorChangedHandler():void {
			enabled = !_propertyLineRenderer.propertyDescriptor.readOnly;
		}
		
	}
}