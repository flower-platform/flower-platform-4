package org.flowerplatform.flexutil.properties.property_renderer {
	import org.flowerplatform.flexutil.properties.property_line_renderer.PropertyLineRenderer;
	
	/**
	 * @author Balutoiu Diana
	 * @author Cristina Constantinescu
	 */
	public interface IPropertyRenderer {
		
		function get valueToCommit():Object;
		
		function set propertyLineRenderer(value:PropertyLineRenderer):void;
		
		function isValidValue():Boolean;
		
		function valueChangedHandler():void;
		
		function propertyDescriptorChangedHandler():void;
	}
}