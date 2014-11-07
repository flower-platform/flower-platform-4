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
package org.flowerplatform.flexutil.controller {
	import flash.events.IEventDispatcher;
	
	import org.flowerplatform.flexutil.FlexUtilConstants;
	import org.flowerplatform.flexutil.value_converter.AbstractValueConverter;
	
	/**
	 * Some Flex logic expect some predefined properties (e.g. <code>BaseRenderer</code>), that may not
	 * be exactly the same in the model.
	 * 
	 * <p>
	 * This class does this translation, using a "mapping" found in the <code>TypeDescriptorRegistry</code>,
	 * which usually comes from the server at init. E.g. for "fontFamily" we actually use "font.NAME".  
	 * 
	 * @author Cristian Spiescu
	 */
	public class ValuesProvider extends AbstractController {
		
		public var featurePrefix:String;
		
		public function ValuesProvider(featurePrefix:String = "", orderIndex:int = 0) {
			super(orderIndex);
			this.featurePrefix = featurePrefix;
		}
		
		public function getFeature(typeDescriptorRegistry:TypeDescriptorRegistry, object:IEventDispatcher, key:String):String {
			return featurePrefix + key;
		}
		
		protected function getDescriptor(typeDescriptorRegistry:TypeDescriptorRegistry, object:IEventDispatcher, key:String):GenericDescriptor {
			var feature:String = getFeature(typeDescriptorRegistry, object, key);
			return GenericDescriptor(typeDescriptorRegistry.getExpectedTypeDescriptor(typeDescriptorRegistry.typeProvider.getType(object)).getSingleController(feature, object));
		}
		
		public function getPropertyName(typeDescriptorRegistry:TypeDescriptorRegistry, object:IEventDispatcher, key:String, descriptor:GenericDescriptor = null):String {
			if (descriptor == null) {
				descriptor = getDescriptor(typeDescriptorRegistry, object, key);
			}
			if (descriptor == null) {
				return null;
			} else {
				return descriptor.value as String;				
			}
		}
		
		public function getValue(typeDescriptorRegistry:TypeDescriptorRegistry, object:IEventDispatcher, key:String):Object {
			var descriptor:GenericDescriptor = getDescriptor(typeDescriptorRegistry, object, key);
			if (descriptor == null) {
				return null;
			}
			var actualPropertyName:String = getPropertyName(typeDescriptorRegistry, object, key, descriptor);
			var value:Object = getValueFromActualPropertyName(object, actualPropertyName);
			var converterKey:String = descriptor.getExtraInfoProperty(FlexUtilConstants.EXTRA_INFO_VALUE_CONVERTER) as String;
			if (converterKey != null) {
				var converter:AbstractValueConverter = AbstractValueConverter(typeDescriptorRegistry.getExpectedTypeDescriptor(FlexUtilConstants.NOTYPE_VALUE_CONVERTERS).getSingleController(converterKey, null));
				value = converter.convertValue(value, descriptor.extraInfo);
			}
			return value;
		}
		
		public function getValueFromActualPropertyName(object:IEventDispatcher, actualPropertyName:String):Object {
			return getActualObject(object)[actualPropertyName];
		}
		
		/**
		 * Needed to listen for <code>PropertyChangeEvent</code>s.
		 */
		public function getActualObject(object:IEventDispatcher):IEventDispatcher {
			return object;
		}
		
	}
}