/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
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
package org.flowerplatform.flexutil.controller {
	import mx.utils.StringUtil;
	
	import org.flowerplatform.flexutil.Utils;
	
	/**
	 * Ported from the similar mechanism from Java.
	 * 
	 * @see java doc
	 * @author Cristina Constantinescu
	 */ 
	public class AbstractController {
	
		private var _orderIndex:int;
		
		public function get orderIndex():int {
			return _orderIndex;
		}
		
		public function set orderIndex(value:int):void {
			_orderIndex = value;
		}
		
		public function AbstractController(orderIndex:int = 0) {
			this.orderIndex = orderIndex;
		}
		
		public function toString():String {
			return StringUtil.substitute("{0} [orderIndex = {1}]", Utils.getClassNameForObject(this, false), orderIndex);
		}
		
	}
}
