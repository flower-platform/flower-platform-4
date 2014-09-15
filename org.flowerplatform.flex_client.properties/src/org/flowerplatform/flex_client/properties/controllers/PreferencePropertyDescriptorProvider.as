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
	
	import mx.utils.ObjectUtil;
	
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.properties.PropertiesConstants;
	import org.flowerplatform.flex_client.properties.controllers.PropertyDescriptorProvider;
	import org.flowerplatform.flex_client.properties.remote.PropertyDescriptor;
	import org.flowerplatform.flexutil.Utils;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class PreferencePropertyDescriptorProvider extends PropertyDescriptorProvider {
		
		public function PreferencePropertyDescriptorProvider(orderIndex:int=0) {
			super(orderIndex);
		}
		
		/**
		 * @return A custom <code>PropertyDescriptor</code> for default/global/user properties (custom orderIndex & renderer).
		 */ 
		override public function getPropertyDescriptor(context:Object, node:Node, property:String):PropertyDescriptor {
			var pd:PropertyDescriptor = super.getPropertyDescriptor(context, node, property);
			
			if (pd != null) {
				pd = PropertyDescriptor(ObjectUtil.copy(pd));
				pd.name = property;
				if (Utils.endsWith(property, ".default")) {				
					pd.propertyLineRenderer = PropertiesConstants.PROPERTY_LINE_RENDERER_TYPE_PREFERENCE_DEFAULT;
					pd.orderIndex = 1;					
				} else if (Utils.endsWith(property, ".global")) {	
					pd.propertyLineRenderer = PropertiesConstants.PROPERTY_LINE_RENDERER_TYPE_PREFERENCE_GLOBAL;
					pd.orderIndex = 2;
					pd.readOnly = false;
				} else if (Utils.endsWith(property, ".user")) { 
					pd.propertyLineRenderer = PropertiesConstants.PROPERTY_LINE_RENDERER_TYPE_PREFERENCE_USER;
					pd.orderIndex = 3;
					pd.readOnly = false;
				}	
			}
			return pd;
		}
				
	}
}