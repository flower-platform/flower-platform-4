package org.flowerplatform.flexutil.properties.property_renderer {
	import flash.events.FocusEvent;
	import flash.events.KeyboardEvent;
	import flash.ui.Keyboard;
	
	import spark.components.TextInput;
	
	import org.flowerplatform.flexutil.properties.PropertiesHelper;
	import org.flowerplatform.flexutil.properties.property_line_renderer.PropertyLineRenderer;
	
	
	/**
	 * @author Balutoiu Diana
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
			this.text = String(PropertiesHelper.getInstance().propertyModelAdapter
				.getPropertyValue(_propertyLineRenderer.nodeObject, _propertyLineRenderer.propertyDescriptor.name));
		}
		
		public function propertyDescriptorChangedHandler():void {
			enabled = !_propertyLineRenderer.propertyDescriptor.readOnly;
		}
	}
}