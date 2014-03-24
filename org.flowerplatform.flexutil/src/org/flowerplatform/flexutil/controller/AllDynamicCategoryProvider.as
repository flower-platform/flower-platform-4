package org.flowerplatform.flexutil.controller {
	import mx.collections.IList;
	
	import org.apache.flex.collections.ArrayList;
	
	/**
	 * Ported from the similar mechanism from Java.
	 * 
	 * @see java doc
	 * @author Cristina Constantinescu
	 */  
	public class AllDynamicCategoryProvider implements IDynamicCategoryProvider	{
				
		public static const CATEGORY_ALL:String = TypeDescriptor.CATEGORY_PREFIX + "all";
		
		public function getDynamicCategories(object:Object):IList {		
			return new ArrayList([CATEGORY_ALL]);
		}
		
	}
}