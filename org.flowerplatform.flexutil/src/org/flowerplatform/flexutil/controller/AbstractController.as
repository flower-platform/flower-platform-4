package org.flowerplatform.flexutil.controller {
	
	/**
	 * @see java doc
	 * @author Cristina Constantinescu
	 */ 
	public class AbstractController {
	
		public var orderIndex:int;
		
		public function AbstractController(orderIndex:int = 0) {
			this.orderIndex = orderIndex;
		}
		
	}
}