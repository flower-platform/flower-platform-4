package org.flowerplatform.flexutil.controller {
	import flash.utils.getQualifiedClassName;
	
	import mx.utils.StringUtil;
	
	/**
	 * Ported from the similar mechanism from Java.
	 * 
	 * @see java doc
	 * @author Cristina Constantinescu
	 */ 
	public class AbstractController {
	
		public var orderIndex:int;
		
		public function AbstractController(orderIndex:int = 0) {
			this.orderIndex = orderIndex;
		}
		
		public function toString():String {
			var className:String = getQualifiedClassName(this);
			return StringUtil.substitute("{0} [orderIndex = {1}]", className.substr(className.indexOf("::") + 2), orderIndex);
		}
		
	}
}