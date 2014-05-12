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
	import mx.binding.utils.BindingUtils;
	import mx.controls.ColorPicker;
	import mx.events.ColorPickerEvent;
	import mx.events.FlexEvent;
	
	import org.flowerplatform.flexutil.Utils;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class ColorPickerPropertyRenderer extends BasicPropertyRenderer {
		
		[Bindable]
		protected var colorPicker:ColorPicker;
		
		override protected function createChildren():void {
			
			colorPicker = new ColorPicker();
			colorPicker.percentWidth = 100;
			colorPicker.addEventListener(ColorPickerEvent.CHANGE, colorChangeEventHandler);
			colorPicker.addEventListener(ColorPickerEvent.ENTER, colorChangeEventHandler);
			
			colorPicker.addEventListener(FlexEvent.CREATION_COMPLETE, colorPicker_creationCompleteHandler);
			
			addElement(colorPicker);
			super.createChildren();
		}
		
		private function colorPicker_creationCompleteHandler(event:FlexEvent):void {		
			BindingUtils.bindSetter(valueChanged, data, "value");
		}
				
		protected function valueChanged(value:Object = null):void {
			if (data != null) {
				colorPicker.selectedColor = Utils.convertValueToColor(propertyDescriptor.value);
			}
		}
		
		private function colorChangeEventHandler(event:ColorPickerEvent):void {
			saveProperty();
		}
		
		override public function set data(value:Object):void {
			super.data = value;			
			colorPicker.enabled = !propertyDescriptor.readOnly;
		}
		
		override protected function getValue():Object {
			return Utils.convertColorToString(colorPicker.selectedColor);
		}
				
	}
}