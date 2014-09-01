package org.flowerplatform.flexutil.properties.property_renderer {
	import flash.events.Event;
	import flash.events.MouseEvent;
	
	import spark.components.CheckBox;
	
	import org.flowerplatform.flexutil.properties.property_line_renderer.PropertyLineRenderer;

	/**
	 * @author Balutoiu Diana
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
			//TODO: needs node property
		}
		
		public function propertyDescriptorChangedHandler():void {
			enabled = !_propertyLineRenderer.propertyDescriptor.readOnly;
		}
	}
}