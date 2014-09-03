package org.flowerplatform.flexutil.properties.property_renderer {
	import flash.events.Event;
	
	import spark.components.NumericStepper;
	
	import flashx.textLayout.formats.TextAlign;
	
	import org.flowerplatform.flexutil.properties.PropertiesHelper;
	import org.flowerplatform.flexutil.properties.property_line_renderer.PropertyLineRenderer;

	/**
	 * @author Balutoiu Diana
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
			this.value = Number(PropertiesHelper.getInstance().propertyModelAdapter
				.getPropertyValue(_propertyLineRenderer.nodeObject, _propertyLineRenderer.propertyDescriptor.name));
		}
		
		public function propertyDescriptorChangedHandler():void {
			enabled = !_propertyLineRenderer.propertyDescriptor.readOnly;			
		}
	}
}