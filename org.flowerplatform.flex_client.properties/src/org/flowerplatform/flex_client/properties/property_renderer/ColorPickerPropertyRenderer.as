package org.flowerplatform.flex_client.properties.property_renderer {
	import mx.binding.utils.BindingUtils;
	import mx.controls.ColorPicker;
	import mx.events.ColorPickerEvent;
	import mx.events.FlexEvent;
	
	import org.flowerplatform.flex_client.properties.remote.PropertyDescriptor;
	import org.flowerplatform.flexutil.Utils;
	
	import spark.events.ColorChangeEvent;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class ColorPickerPropertyRenderer extends BasicPropertyRenderer {
		
		[Bindable]
		protected var colorPicker:ColorPicker;
		
		override protected function createChildren():void {
			super.createChildren();
			
			colorPicker = new ColorPicker();
			colorPicker.percentWidth = 100;
			colorPicker.addEventListener(ColorPickerEvent.CHANGE, colorChangeEventHandler);
			colorPicker.addEventListener(ColorPickerEvent.ENTER, colorChangeEventHandler);
			
			colorPicker.addEventListener(FlexEvent.CREATION_COMPLETE, colorPicker_creationCompleteHandler);
			
			addElement(colorPicker);
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