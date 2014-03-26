package org.flowerplatform.flexutil.controller {
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	import mx.collections.IList;
	
	import org.apache.flex.collections.ArrayList;
	import org.flowerplatform.flexutil.Utils;
	
	/**
	 * Ported from the similar mechanism from Java.
	 * 
	 * @see java doc
	 * @author Cristina Constantinescu
	 */
	public class TypeDescriptor {
		
		public static const CATEGORY_PREFIX:String = "category.";
		public static const CATEGORY_RESOURCE_PREFIX:String = TypeDescriptor.CATEGORY_PREFIX + "resource.";
		
		private var _registry:TypeDescriptorRegistry;
		
		public function get registry():TypeDescriptorRegistry {
			return _registry;
		}
		
		private var _type:String;
		
		public function get type():String {
			return _type;
		}
		
		public function TypeDescriptor(registry:TypeDescriptorRegistry, type:String) {
			super();
			_registry = registry;
			_type = type;			
		}

		private var _categories:IList; /* List<String> */
		
		public function get categories():IList {
			if (_categories == null) {
				return new ArrayList();
			}
			return _categories;
		}

		public function addCategory(category:String):TypeDescriptor {
			if (!_registry.isConfigurable()) {
				throw new Error("Trying to add a new category to a non-configurable registry");
			}
			if (!Utils.beginsWith(category, CATEGORY_PREFIX)) {
				throw new Error("Category type should be prefixed with 'category.'");
			}
			if (_categories == null) {
				_categories = new ArrayList();
			}
			_categories.addItem(category);
			return this;
		}
		
		protected var singleControllers:Dictionary = new Dictionary(); /* Map<String, ControllerEntry<AbstractController>> */
		
		public function getSingleController(controllerType:String, object:Object):AbstractController {		
			return getCachedSingleController(controllerType, object, true);
		}
		
		protected function getCachedSingleController(controllerType:String, object:Object, includeDynamicCategoryProviders:Boolean):AbstractController {
			registry.configurable = false;
			
			var entry:ControllerEntry = getSingleControllerEntry(controllerType);
			if (entry.wasCached) {
				// categories were processed before; return the controller
				return entry.cachedValue as AbstractController;
			}
			
			// else => let's scan now the categories
			var allCategories:ArrayCollection = new ArrayCollection();
			allCategories.addAll(categories);
			if (includeDynamicCategoryProviders) {
				for (var i:int = 0; i < registry.getDynamicCategoryProviders().length; i++) {
					var categoryProvider:IDynamicCategoryProvider = IDynamicCategoryProvider(registry.getDynamicCategoryProviders().getItemAt(i));
					var dynamicCategories:IList = categoryProvider.getDynamicCategories(object);
					if (dynamicCategories != null) {
						allCategories.addAll(dynamicCategories);
					}
				}
			}
			
			// iterate categories to cache the controller
			for each (var category:String in allCategories) {
				var categoryDescriptor:TypeDescriptor = registry.getExpectedTypeDescriptor(category);
				if (categoryDescriptor == null) {
					// semi-error; a WARN is logged
					continue;
				}
				
				var categoryController:AbstractController = categoryDescriptor.getCachedSingleController(controllerType, object, false);
				if (categoryController != null) {
					// found a controller from a category
					// keep it if it has a lower order index than the existing one
					if (entry.cachedValue == null || AbstractController(entry.cachedValue).orderIndex > categoryController.orderIndex) {
						entry.cachedValue = categoryController;
					}
				}
			}
			
			if (entry.cachedValue is NullController) {
				// means we must ignore all registered controllers
				entry.cachedValue = null;
			}
			
			// finished scanning the categories
			entry.wasCached = true;
			
			return AbstractController(entry.cachedValue);
		}
		
		public function addSingleController(type:String, controller:AbstractController):TypeDescriptor {
			if (!registry.isConfigurable()) {
				throw new Error("Trying to add a new single controller to a non-configurable registry");
			}
			var entry:ControllerEntry = getSingleControllerEntry(type);
			entry.selfValue = controller;
			entry.cachedValue = controller;
			return this;
		}
	
		private function getSingleControllerEntry(type:String):ControllerEntry {
			var entry:ControllerEntry = singleControllers[type];
			if (entry == null) {
				entry = new ControllerEntry();
				singleControllers[type] = entry;
			}
			return entry;
		}
		
		protected var additiveControllers:Dictionary = new Dictionary(); /* Map<String, ControllerEntry<List<? extends AbstractController>>> */
		
		public function getAdditiveControllers(controllerType:String, object:Object):IList {
			return getCachedAdditiveControllers(controllerType, object, true);
		}
		
		protected function getCachedAdditiveControllers(controllerType:String, object:Object, includeDynamicCategoryProviders:Boolean):IList {
			registry.configurable = false;
			
			var entry:ControllerEntry = getAdditiveControllersEntry(controllerType);
			var controllers:ArrayCollection = entry.cachedValue as ArrayCollection;
			if (entry.wasCached) {
				// categories were processed before; return the controllers
				return controllers;
			}
			
			// else => let's scan now the categories
			var allCategories:ArrayCollection = new ArrayCollection();
			allCategories.addAll(categories);
			if (includeDynamicCategoryProviders) {
				for (var i:int = 0; i < registry.getDynamicCategoryProviders().length; i++) {
					var categoryProvider:IDynamicCategoryProvider = IDynamicCategoryProvider(registry.getDynamicCategoryProviders().getItemAt(i));
					var dynamicCategories:IList = categoryProvider.getDynamicCategories(object);
					if (dynamicCategories != null) {
						allCategories.addAll(dynamicCategories);
					}
				}
			}
			
			// iterate categories to cache the controllers
			for each (var category:String in allCategories) {
				var categoryDescriptor:TypeDescriptor = registry.getExpectedTypeDescriptor(category);
				if (categoryDescriptor == null) {
					// semi-error; a WARN is logged
					continue;
				}
				
				controllers.addAll(categoryDescriptor.getCachedAdditiveControllers(controllerType, object, false));
				entry.wasCached = true;
			}
			
			// finished scanning the categories
			entry.wasCached = true;
			
			controllers.source.sortOn("orderIndex", Array.NUMERIC);
			
			return controllers;
		}
				
		public function addAdditiveController(type:String, controller:AbstractController):TypeDescriptor {
			if (!registry.isConfigurable()) {
				throw new Error("Trying to add a new additive controller to a non-configurable registry");
			}
			var entry:ControllerEntry = getAdditiveControllersEntry(type);
			ArrayCollection(entry.selfValue).addItem(controller);
			ArrayCollection(entry.cachedValue).addItem(controller);
			return this;
		}
		
		private function getAdditiveControllersEntry(type:String):ControllerEntry {
			var entry:ControllerEntry = additiveControllers[type];
			if (entry == null) {
				entry = new ControllerEntry();
				entry.selfValue = new ArrayCollection();
				entry.cachedValue = new ArrayCollection();
				additiveControllers[type] = entry;
			}
			return entry;
		}
		
	}
}
