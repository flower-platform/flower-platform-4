package org.flowerplatform.flexutil.properties.property_line_renderer {
	
	import org.flowerplatform.flexutil.properties.remote.PropertyDescriptor;
	
	/**
 	*@author Diana Balutoiu 
	*@author Cristina Constantinescu
 	*/
	public interface IPropertyLineRenderer {
		
		function get propertyDescriptor():PropertyDescriptor;
		function set propertyDescriptor(value:PropertyDescriptor):void;
		
		function get nodeObject():Object;
		function set nodeObject(value:Object):void;
	}
}