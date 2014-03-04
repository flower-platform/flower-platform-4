package org.flowerplatform.flexutil.controller {
	
	/**
	 * Ported from the similar mechanism from Java.
	 * 
	 * @see java doc
	 * @author Cristina Constantinescu
	 */ 
	public class CategoryTypeDescriptor extends TypeDescriptor {
		
		public function CategoryTypeDescriptor(registry:TypeDescriptorRegistry, type:String) {
			super(registry, type);
		}
		
		override public function addCategory(category:String):TypeDescriptor {
			throw new Error("Categories cannot belong to other categories");
		}
		
	}
}