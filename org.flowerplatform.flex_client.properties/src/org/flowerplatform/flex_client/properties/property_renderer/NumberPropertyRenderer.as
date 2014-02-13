package org.flowerplatform.flex_client.properties.property_renderer {
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class NumberPropertyRenderer extends StringPropertyRenderer {
		
		override protected function getValue():Object {
			var value:Object = super.getValue();
			return new Number(value);
		}
	}
}