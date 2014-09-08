/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
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
	
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.properties.PropertiesConstants;
	import org.flowerplatform.flex_client.properties.PropertiesPlugin;
	import org.flowerplatform.flex_client.properties.remote.PropertyDescriptor;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexutil.Utils;

	/**
	 * @author Cristina Constantinescu
	 */ 
	public class RawPropertyDescriptorProvider extends PropertyDescriptorProvider {
		
		public function RawPropertyDescriptorProvider(orderIndex:int=0) {
			super(orderIndex);
		}
		
		/**
		 * @return A dummy <code>PropertyDescriptor</code> for raw properties ("raw" = property without descriptor). 
		 * 			<p>
		 * 			Gets type based on property value class registered in <Code>PropertiesPlugin.getInstance().propertyValueClassToPropertyDescriptorType</code>.
		 * 			If no type associated, string type is used.
		 */ 
		override public function getPropertyDescriptor(context:Object, node:Node, property:String):PropertyDescriptor {
			if (context.hasOwnProperty(PropertiesConstants.INCLUDE_RAW_PROPERTY) && !Boolean(context[PropertiesConstants.INCLUDE_RAW_PROPERTY]).valueOf()) {
				return null;
			}
			var type:String;
						
			var propertyValue:Object = node.getPropertyValue(property);
			if (propertyValue != null) {
				type = PropertiesPlugin.getInstance().propertyValueClassToPropertyDescriptorType[Utils.getClass(propertyValue)];
			}
			if (type == null) {
				type = PropertiesConstants.PROPERTY_DESCRIPTOR_TYPE_STRING;
			}						
			
			var pd:PropertyDescriptor = new PropertyDescriptor();
			pd.type = type;
			pd.name = property;		
			pd.category = Resources.getMessage("raw.properties");		
			pd.orderIndex = (property == "nodeUri" ? int.MAX_VALUE - 1 : int.MAX_VALUE);
			return pd;			
		}		
		
	}
}