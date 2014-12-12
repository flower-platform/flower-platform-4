package org.flowerplatform.flexutil.value_converter {
	import mx.collections.IList;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class ListToListValueConverter extends CsvToListValueConverter {
		
		override protected function isEmpty(value:Object):Boolean {
			return IList(value).length == 0;
		}
		
		override protected function split(value:Object):Array {
			return IList(value).toArray();
		}
		
	}
}