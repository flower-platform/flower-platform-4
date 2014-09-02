package org.flowerplatform.flexutil.properties.controllers
{
	import mx.collections.IList;
	
	import org.flowerplatform.flexutil.controller.AbstractController;
	import org.flowerplatform.flexutil.properties.PropertiesConstants;
	import org.flowerplatform.flexutil.properties.PropertiesHelper;
	import org.flowerplatform.flexutil.properties.remote.PropertyDescriptor;

	/**
	 * @author Balutoiu Diana
	 * @author Cristina Constantinescu
	 */ 
	public class PropertyDescriptorProvider extends AbstractController {
		
		public function PropertyDescriptorProvider(orderIndex:int=0) {
			super(orderIndex);
		}
		
		/**
		 * @return The <code>PropertyDescriptor</code> found in registry for given node and property. <p>
		 * 
		 * 			If no descriptor found, verifies if property has "x.y" format and tries to get descriptor from "x" property. 
		 * 			(e.g. preferences have value.default, value.global, value.user, but only value has a property descriptor registered).<p>
		 * 
		 * 			Otherwise returns <code>null</code>.
		 */ 
		public function getPropertyDescriptor(context:Object, nodeObject:Object, property:String):PropertyDescriptor {
			var propertyDescriptor:PropertyDescriptor;
			var descriptors:IList = PropertiesHelper.getInstance().nodeTypeDescriptorRegistry
				.getExpectedTypeDescriptor(PropertiesHelper.getInstance().composedTypeProvider.getType(nodeObject))
				.getAdditiveControllers(PropertiesConstants.PROPERTY_DESCRIPTOR, nodeObject);
			for (var i:int = 0; i < descriptors.length; i++) {
				propertyDescriptor = PropertyDescriptor(descriptors.getItemAt(i));
				if (propertyDescriptor.name == property) {
					return propertyDescriptor;
				}
			}			
			var index:int = property.lastIndexOf(".");
			if (index != -1) {						
				return getPropertyDescriptor(context, nodeObject, property.substring(0, index));				
			}
			return null;
		}
	}
}