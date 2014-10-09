package org.flowerplatform.flexutil.value_converter {
	import org.flowerplatform.flexutil.controller.AbstractValueConverter;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class StringHexToUintValueConverter extends AbstractValueConverter	{
		
		override public function convertValue(value:Object, extraInfo:Object):Object {
			if (value == null) {
				return null;
			}
			return uint("0x" + String(value).substr(1));
		}
		
	}
}