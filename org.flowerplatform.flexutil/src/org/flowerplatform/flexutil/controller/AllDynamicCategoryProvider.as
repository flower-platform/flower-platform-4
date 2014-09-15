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
package org.flowerplatform.flexutil.controller {
	import mx.collections.IList;
	
	import org.apache.flex.collections.ArrayList;
	import org.flowerplatform.flexutil.FlexUtilConstants;
	
	/**
	 * Ported from the similar mechanism from Java.
	 * 
	 * @see java doc
	 * @author Cristina Constantinescu
	 */  
	public class AllDynamicCategoryProvider implements IDynamicCategoryProvider	{
				
		public function getDynamicCategories(object:Object):IList {		
			return new ArrayList([FlexUtilConstants.CATEGORY_ALL]);
		}
		
	}
}