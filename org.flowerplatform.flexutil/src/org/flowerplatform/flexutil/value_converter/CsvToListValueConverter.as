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