/* license-start
 * 
 * Copyright (C) 2008 - 2014 Crispico Software, <http://www.crispico.com/>.
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