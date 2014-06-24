package org.flowerplatform.flex_client.properties.controllers {
	import mx.collections.IList;
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.properties.PropertiesConstants;
	import org.flowerplatform.flex_client.properties.remote.PropertyDescriptor;
	import org.flowerplatform.flexutil.controller.AbstractController;
	
	/**
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
		public function getPropertyDescriptor(context:Object, node:Node, property:String):PropertyDescriptor {
			var propertyDescriptor:PropertyDescriptor;
			var descriptors:IList = CorePlugin.getInstance().nodeTypeDescriptorRegistry.getExpectedTypeDescriptor(node.type).getAdditiveControllers(PropertiesConstants.PROPERTY_DESCRIPTOR, node);
			for (var i:int = 0; i < descriptors.length; i++) {
				propertyDescriptor = PropertyDescriptor(descriptors.getItemAt(i));
				if (propertyDescriptor.name == property) {
					return propertyDescriptor;
				}
			}			
			var index:int = property.lastIndexOf(".");
			if (index != -1) {						
				return getPropertyDescriptor(context, node, property.substring(0, index));				
			}
			return null;
		}
		
	}
}