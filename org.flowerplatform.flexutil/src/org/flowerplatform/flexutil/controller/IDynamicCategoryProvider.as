package org.flowerplatform.flexutil.controller {
	import mx.collections.IList;
	
	/**
	 * Ported from the similar mechanism from Java.
	 * 
	 * @see java doc
	 * @author Cristina Constantinescu
	 */ 
	public interface IDynamicCategoryProvider {
		
		function getDynamicCategories(object:Object):IList;
		
	}
}