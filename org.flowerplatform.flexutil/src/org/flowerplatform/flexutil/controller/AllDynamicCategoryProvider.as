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