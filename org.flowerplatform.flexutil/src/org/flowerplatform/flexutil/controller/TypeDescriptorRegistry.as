package org.flowerplatform.flexutil.controller {
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	import mx.collections.IList;
	import mx.core.mx_internal;
	
	import org.apache.flex.collections.ArrayList;
	import org.flowerplatform.flexutil.FlexUtilConstants;
	import org.flowerplatform.flexutil.Utils;
	
	use namespace mx_internal;
	
	/**
	 * Ported from the similar mechanism from Java.
	 * 
	 * @see java doc
	 * @author Cristina Constantinescu
	 */
	public class TypeDescriptorRegistry {
		
		internal var configurable:Boolean = true;
		
		public function isConfigurable():Boolean {
			return configurable;
		}
		
		mx_internal var typeDescriptors:Dictionary = new Dictionary(); /* Map<String, TypeDescriptor> */
		
		public function getOrCreateTypeDescriptor(type:String):TypeDescriptor {
			if (Utils.beginsWith(type, FlexUtilConstants.CATEGORY_PREFIX)) {
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
			if (!Utils.beginsWith(type, FlexUtilConstants.CATEGORY_PREFIX)) {
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
		
		public function addTypeDescriptorsRemote(typeDescriptorRemoteList:ArrayCollection): void {
			for (var i:int = 0; i < typeDescriptorRemoteList.length; i++) {
				var remote:TypeDescriptorRemote = TypeDescriptorRemote(typeDescriptorRemoteList.getItemAt(i));
				
				// create new type descriptor with remote type
				var descriptor:TypeDescriptor = null;
				if (Utils.beginsWith(remote.type, FlexUtilConstants.CATEGORY_PREFIX)) {
					descriptor = this.getOrCreateCategoryTypeDescriptor(remote.type);
				} else {
					descriptor = this.getOrCreateTypeDescriptor(remote.type);
				}
				
				// add static categories
				for each (var category:String in remote.categories) {
					descriptor.addCategory(category);
				}
				
				// add single controllers
				for (var singleControllerType:String in remote.singleControllers) {
					descriptor.addSingleController(singleControllerType, remote.singleControllers[singleControllerType]);
				}
				
				// add additive controllers
				for (var additiveControllerType:String in remote.additiveControllers) {
					for each (var additiveController:AbstractController in remote.additiveControllers[additiveControllerType]) {
						descriptor.addAdditiveController(additiveControllerType, additiveController);
					}
				}
			}
		}
		
		public function TypeDescriptorRegistry() {
			super();
			addDynamicCategoryProvider(new AllDynamicCategoryProvider());
		}
	
	}
}