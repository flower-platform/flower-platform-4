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
package org.flowerplatform.flexutil.value_converter {
	import org.flowerplatform.flexutil.FlexUtilConstants;
	import org.flowerplatform.flexutil.controller.AbstractController;
	import org.flowerplatform.flexutil.controller.TypeDescriptorRegistry;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class AbstractValueConverter extends AbstractController {
		public function AbstractValueConverter() {
			super(0);
		}
		
		public function convertValue(value:Object, extraInfo:Object):Object {
			throw new Error("This method must be implemented");
		}
		
		public static function registerValueConverters(typeDescriptorRegistry:TypeDescriptorRegistry):void {
			typeDescriptorRegistry.getOrCreateTypeDescriptor(FlexUtilConstants.NOTYPE_VALUE_CONVERTERS)		
				.addSingleController(FlexUtilConstants.VALUE_CONVERTER_STRING_HEX_TO_UINT, new StringHexToUintValueConverter())
				.addSingleController(FlexUtilConstants.VALUE_CONVERTER_CSV_TO_LIST, new CsvToListValueConverter())
				.addSingleController(FlexUtilConstants.VALUE_CONVERTER_LIST_TO_LIST, new ListToListValueConverter());
		}
	}
}