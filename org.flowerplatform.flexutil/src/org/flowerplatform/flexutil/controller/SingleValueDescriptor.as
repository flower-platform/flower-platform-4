package org.flowerplatform.flexutil.controller {
	
	/**
	 * @author Cristian Spiescu
	 */
	public class SingleValueDescriptor extends AbstractController {

		public var value:Object;
		
		public function SingleValueDescriptor(value:Object=null, orderIndex:int=0) {
			super(orderIndex);
			this.value = value;
		}
		
		override public function toString():String {
			return super.toString() + "[value = " + value + "]";
		}		
	}
}