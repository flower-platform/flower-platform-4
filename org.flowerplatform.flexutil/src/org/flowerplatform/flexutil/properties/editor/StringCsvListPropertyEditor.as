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
package org.flowerplatform.flexutil.properties.editor {
	import mx.collections.ArrayList;
	import mx.collections.IList;
	
	import org.flowerplatform.flexutil.properties.PropertyEntry;

	/**
	 * @author Cristian Spiescu
	 */
	public class StringCsvListPropertyEditor extends StringPropertyEditor {
		
		public var separator:String = ",";
		
		override public function set propertyEntry(entry:PropertyEntry):void {
			super.propertyEntry = entry;
			// NOTE: I have seen that String(arrayCollection) gives us the expected result; however
			// there is no guarantee that it's the same for all IList instances; thus the implementation below
			var result:String = "";
			var list:IList = IList(entry.value);
			for (var i:int = 0; i < list.length; i++) {
				result += list.getItemAt(i);
				if (i < list.length - 1) {
					result += separator;
				}
			}
			
			text = result; 
		}
		
		override public function get valueToCommit():Object {
			return new ArrayList((super.valueToCommit as String).split(separator));
		}
		
	}
}