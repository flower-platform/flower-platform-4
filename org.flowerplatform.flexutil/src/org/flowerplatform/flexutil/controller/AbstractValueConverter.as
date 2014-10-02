package org.flowerplatform.flexutil.controller {
	
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
	}
}