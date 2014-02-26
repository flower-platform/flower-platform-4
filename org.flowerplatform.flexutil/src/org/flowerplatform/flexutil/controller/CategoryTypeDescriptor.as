package org.flowerplatform.flexutil.controller {
	
	/**
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