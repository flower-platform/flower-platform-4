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
	import mx.collections.ArrayList;
	
	import org.flowerplatform.flexutil.FlexUtilConstants;
	import org.flowerplatform.flexutil.controller.AbstractValueConverter;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class CsvToListValueConverter extends AbstractValueConverter	{
		
		override public function convertValue(value:Object, extraInfo:Object):Object {
			var result:ArrayList = new ArrayList();
			if (value == null || value == "") {
				return result;
			}
			if (extraInfo != null) {
				var prefix:String = extraInfo[FlexUtilConstants.EXTRA_INFO_CSV_TO_LIST_PREFIX];
				var suffix:String = extraInfo[FlexUtilConstants.EXTRA_INFO_CSV_TO_LIST_SUFFIX];
			}
			if (prefix == null) {
				prefix = "";
			}
			if (suffix == null) {
				suffix = "";
			}
			var spl:Array = String(value).split(",");
			for (var i:int = 0; i < spl.length; i++) {
				result.addItem(prefix + spl[i] + suffix);
			}
			return result;
		}
		
	}
}