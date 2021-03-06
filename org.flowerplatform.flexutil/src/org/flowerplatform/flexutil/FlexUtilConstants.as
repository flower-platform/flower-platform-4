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
package org.flowerplatform.flexutil {
	
	
	public class FlexUtilConstants {
		
		// TypeDescriptorRegistry
		public static const CATEGORY_PREFIX:String = "category.";
		public static const CATEGORY_ALL:String = CATEGORY_PREFIX + "all";
		public static const NOTYPE_PREFIX:String = "noType.";
		public static const NOTYPE_VALUE_CONVERTERS:String = NOTYPE_PREFIX + "valueConverters";
		
		public static const EXTRA_INFO_VALUE_CONVERTER:String = "valueConverter";
		public static const EXTRA_INFO_CSV_TO_LIST_PREFIX:String = "csvToListPrefix";
		public static const EXTRA_INFO_CSV_TO_LIST_SUFFIX:String = "csvToListSuffix";
		
		public static const VALUE_CONVERTER_STRING_HEX_TO_UINT:String = "stringHexToUint";
		public static const VALUE_CONVERTER_CSV_TO_LIST:String = "csvToList";
		public static const VALUE_CONVERTER_LIST_TO_LIST:String = "listToList";
		
		// Properties
		public static const EVENT_COMMIT_PROPERTY:String = "CommitProperty";
		public static const PROPERTIES_CONTEXT_INCLUDE_PROPERTIES_WITHOUT_DESCRIPTOR:String = "includePropertiesWithoutDescriptor";
		public static const PROPERTIES_CONTEXT_SHOW_REMOVE_PROPERTY:String = "removeProperty";
		public static const PROPERTIES_CONTEXT_SHOW_ADD_PROPERTY:String = "addProperty";
		public static const PROPERTIES_CONTEXT_IS_CREATE_MODE:String = "createMode";
		
		public static const FEATURE_PROPERTY_COMMIT_CONTROLLER:String = "PropertyCommitController";
		public static const FEATURE_PROPERTY_DESCRIPTORS:String = "PropertyDescriptors";
		public static const FEATURE_PROPERTY_GROUP_DESCRIPTORS:String = "PropertyGroupsDescriptors";
		
		public static const NOTYPE_PROPERTY_EDITORS:String = NOTYPE_PREFIX + "propertyEditors";
		public static const NOTYPE_CLASSES_TO_PROPERTY_EDITORS:String = NOTYPE_PREFIX + "classesToPropertyEditors";
		public static const PROPERTY_EDITOR_TYPE_STRING:String = "String";
		public static const PROPERTY_EDITOR_TYPE_STRING_CSV_LIST:String = "StringCsvList";
		public static const PROPERTY_EDITOR_TYPE_BOOLEAN:String = "Boolean";
		
		// other
		
		public static const CONTROL:String = "CONTROL";
		public static const COMMAND:String = "COMMAND";
		public static const SHIFT:String = "SHIFT";
		public static const ALTERNATE:String = "ALTERNATE";
		
		public static const EMBED_IN_FLEX_APP:String = "embedInFlexApp";
		public static const EMBED_IN_FLEX_APP_WEB:String = "web";
		public static const EMBED_IN_FLEX_APP_MOBILE:String = "mobile";
		
	}
}
