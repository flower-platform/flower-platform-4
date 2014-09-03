package org.flowerplatform.flexutil.properties.property_renderer {
	import mx.controls.ColorPicker;
	import mx.events.ColorPickerEvent;
	
	import org.flowerplatform.flexutil.Utils;
	import org.flowerplatform.flexutil.properties.PropertiesHelper;
	import org.flowerplatform.flexutil.properties.property_line_renderer.PropertyLineRenderer;

	/**
	 * @author Diana Balutoiu
	 * @author Cristina Constantinescu
	 */
	public class ColorPickerPropertyRenderer extends ColorPicker implements IPropertyRenderer {
		
		private var _propertyLineRenderer:PropertyLineRenderer;
		
		public function ColorPickerPropertyRenderer() {
			super();
			
			addEventListener(ColorPickerEvent.CHANGE, colorChangeEventHandler);
			addEventListener(ColorPickerEvent.ENTER, colorChangeEventHandler);
		}
		
		public function isValidValue():Boolean {	
			return true;
		}
		
		public function set propertyLineRenderer(value:PropertyLineRenderer):void {
			_propertyLineRenderer = value;				
		}			
		
		public function get valueToCommit():Object {
			return Utils.convertColorToString(selectedColor);
		}
		
		public function valueChangedHandler():void {
			selectedColor = Utils.convertValueToColor(PropertiesHelper.getInstance().
				propertyModelAdapter.getPropertyValue(_propertyLineRenderer.nodeObject, _propertyLineRenderer.propertyDescriptor.name));
		}
		
		public function propertyDescriptorChangedHandler():void {
			enabled = !_propertyLineRenderer.propertyDescriptor.readOnly;			
		}
		
		private function colorChangeEventHandler(event:ColorPickerEvent):void {
			_propertyLineRenderer.commit();
		}
	}
}