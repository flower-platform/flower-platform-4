package org.flowerplatform.flexutil.controller {
	import flash.utils.Dictionary;
	
	import mx.collections.IList;
	
	import org.apache.flex.collections.ArrayList;
	import org.flowerplatform.flexutil.Utils;
	
	/**
	 * @see java doc
	 * @author Cristina Constantinescu
	 */
	public class TypeDescriptorRegistry {
		
		internal var configurable:Boolean = true;
		
		public function isConfigurable():Boolean {
			return configurable;
		}
		
		private var typeDescriptors:Dictionary = new Dictionary(); /* Map<String, TypeDescriptor> */
		
		public function getOrCreateTypeDescriptor(type:String):TypeDescriptor {
			if (Utils.beginsWith(type, TypeDescriptor.CATEGORY_PREFIX)) {
				throw new Error("Please use getOrCreateCategoryTypeDescriptor()");
			}
			var result:TypeDescriptor = typeDescriptors[type];
			if (result == null) {
				result = new TypeDescriptor(this, type);
				typeDescriptors[type] = result;
			}
			return result;
		}
		
		public function getOrCreateCategoryTypeDescriptor(type:String):TypeDescriptor {
			if (!Utils.beginsWith(type, TypeDescriptor.CATEGORY_PREFIX)) {
				throw new Error("Category type should be prefixed with 'category.'");
			}
			
			var result:TypeDescriptor = typeDescriptors[type];
			if (result == null) {
				result = new CategoryTypeDescriptor(this, type);
				typeDescriptors[type] = result;
			}
			return result;
		}
		
		public function getExpectedTypeDescriptor(type:String):TypeDescriptor {
			var result:TypeDescriptor = typeDescriptors[type];
			if (result == null) {
				trace("Operation invoked for nodeType = {}, but there is no associated descriptor registered! Aborting operation.", type);
				return null;
			}
			return result;
		}
		
		private var dynamicCategoryProviders:IList; /* List<IDynamicCategoryProvider> */
		
		public function getDynamicCategoryProviders():IList {
			if (dynamicCategoryProviders == null) {
				dynamicCategoryProviders = new ArrayList();			
			}
			return dynamicCategoryProviders;
		}
		
		public function addDynamicCategoryProvider(provider:IDynamicCategoryProvider):void {
			getDynamicCategoryProviders().addItem(provider);
		}
		
		public function TypeDescriptorRegistry() {
			super();
			addDynamicCategoryProvider(new AllDynamicCategoryProvider());
		}
	
	}
}