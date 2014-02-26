package org.flowerplatform.flexutil.controller {
	import mx.collections.IList;
	
	/**
	 * @see java doc
	 * @author Cristina Constantinescu
	 */ 
	public interface IDynamicCategoryProvider {
		
		function getDynamicCategories(object:Object):IList;
		
	}
}