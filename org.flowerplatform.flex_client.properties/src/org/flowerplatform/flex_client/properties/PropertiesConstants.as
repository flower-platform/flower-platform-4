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
package org.flowerplatform.flex_client.properties {
	import org.flowerplatform.flex_client.core.CoreConstants;
	import org.flowerplatform.flexutil.FlexUtilConstants;
	
	public class PropertiesConstants {
		
		//////////////////////////////////
		// Descriptors
		//////////////////////////////////
		public static const PREFERENCE_TYPE:String = "preference";
		public static const PREFERENCE_CATEGORY_TYPE:String = FlexUtilConstants.CATEGORY_PREFIX + PREFERENCE_TYPE;
		
		public static const PROPERTY_DESCRIPTOR:String = "propertyDescriptor";
		
		public static const PROPERTY_DESCRIPTOR_PROVIDER:String = "propertyDescriptorProvider";
		
		public static const INCLUDE_RAW_PROPERTY:String = "includeRawProperty";
		
		public static const PROPERTY_LINE_RENDERER_TYPE_DEFAULT:String = "Default";
		public static const PROPERTY_LINE_RENDERER_TYPE_CATEGORY:String = "Category";
		public static const PROPERTY_LINE_RENDERER_TYPE_STYLABLE:String = "Stylable";
		public static const PROPERTY_LINE_RENDERER_TYPE_PREFERENCE_DEFAULT:String = "PreferenceDefault";
		public static const PROPERTY_LINE_RENDERER_TYPE_PREFERENCE_GLOBAL:String = "PreferenceGlobal";
		public static const PROPERTY_LINE_RENDERER_TYPE_PREFERENCE_USER:String = "PreferenceUser";
		
		public static const PROPERTY_DESCRIPTOR_TYPE_STRING:String = "String";
		public static const PROPERTY_DESCRIPTOR_TYPE_BOOLEAN:String = "Boolean";
		public static const PROPERTY_DESCRIPTOR_TYPE_NUMBER:String = "Number";
		public static const PROPERTY_DESCRIPTOR_TYPE_NUMBER_STEPPER:String = "NumberStepper";
		public static const PROPERTY_DESCRIPTOR_TYPE_DROP_DOWN_LIST:String = "DropDownList";
		public static const PROPERTY_DESCRIPTOR_TYPE_COLOR_PICKER:String = "ColorPicker";
		public static const PROPERTY_DESCRIPTOR_TYPE_DATE:String = "Date";
		public static const PROPERTY_DESCRIPTOR_DEFAULT_CATEGORY:String = "";
		
		public static const DEFAULT_SUFFIX:String = ".default";
		public static const GLOBAL_SUFFIX:String = ".global";
		public static const USER_SUFFIX:String = ".user";
		
	}
}
