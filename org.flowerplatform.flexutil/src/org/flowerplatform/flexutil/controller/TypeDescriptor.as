package org.flowerplatform.flexutil.controller {
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	import mx.collections.IList;
	import mx.utils.ArrayUtil;
	
	import org.apache.flex.collections.ArrayList;
	import org.flowerplatform.flexutil.Pair;
	import org.flowerplatform.flexutil.Utils;
	
	import spark.supportClasses.INavigator;
	
	/**
	 * @see java doc
	 * @author Cristina Constantinescu
	 */
	public class TypeDescriptor {
		
		public static const CATEGORY_PREFIX:String = "category.";
		
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
		
		protected var singleControllers:Dictionary = new Dictionary(); /* Map<String, Pair<AbstractController, Boolean>> */
		
		public function getSingleController(controllerType:String, object:Object):AbstractController {		
			return getCachedSingleController(controllerType, object, true);
		}
		
		protected function getCachedSingleController(controllerType:String, object:Object, includeDynamicCategoryProviders:Boolean):AbstractController {
			registry.configurable = false;
			
			var pair:Pair = getSingleControllerPair(controllerType);
			if (pair.b) {
				// categories were processed before; return the controller
				return pair.a as AbstractController;
			}
			
			// else => let's scan now the categories
			var allCategories:ArrayCollection = new ArrayCollection();
			allCategories.addAll(categories);
			if (includeDynamicCategoryProviders) {
				for (var i:int = 0; i < registry.getDynamicCategoryProviders().length; i++) {
					var categoryProvider:IDynamicCategoryProvider = IDynamicCategoryProvider(registry.getDynamicCategoryProviders().getItemAt(i));
					allCategories.addAll(categoryProvider.getDynamicCategories(object));
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
					// found a controller from a category; cache it
					pair.a = categoryController;
					if (pair.b) {
						throw new Error("Object with type " + type + "registered multiple categories with controllers of type " + controllerType);
					}
					pair.b = true;
				}
			}
			
			// finished scanning the categories
			pair.b = true;
			
			return pair.a as AbstractController;
		}
		
		public function addSingleController(type:String, controller:AbstractController):TypeDescriptor {
			if (!registry.isConfigurable()) {
				throw new Error("Trying to add a new single controller to a non-configurable registry");
			}
			var pair:Pair = getSingleControllerPair(type);
			pair.a = controller;
			return this;
		}
		
		private function getSingleControllerPair(type:String):Pair {
			var pair:Pair = singleControllers[type];
			if (pair == null) {
				pair = new Pair(null, false);
				singleControllers[type] = pair;
			}
			return pair;
		}
		
		protected var additiveControllers:Dictionary = new Dictionary(); /* Map<String, Pair<List<? extends AbstractController>, Boolean>> */
		
		public function getAdditiveControllers(controllerType:String, object:Object):IList {
			return getCachedAdditiveControllers(controllerType, object, true);
		}
		
		protected function getCachedAdditiveControllers(controllerType:String, object:Object, includeDynamicCategoryProviders:Boolean):IList {
			registry.configurable = false;
			
			var pair:Pair = getAdditiveControllersPair(controllerType);
			var controllers:ArrayCollection = pair.a as ArrayCollection;
			if (pair.b) {
				// categories were processed before; return the controllers
				return controllers;
			}
			
			// else => let's scan now the categories
			var allCategories:ArrayCollection = new ArrayCollection();
			allCategories.addAll(categories);
			if (includeDynamicCategoryProviders) {
				for (var i:int = 0; i < registry.getDynamicCategoryProviders().length; i++) {
					var categoryProvider:IDynamicCategoryProvider = IDynamicCategoryProvider(registry.getDynamicCategoryProviders().getItemAt(i));
					allCategories.addAll(categoryProvider.getDynamicCategories(object));
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
				pair.b = true;
			}
			
			// finished scanning the categories
			pair.b = true;
			
			controllers.source.sortOn("orderIndex", Array.NUMERIC);
			
			return controllers;
		}
				
		public function addAdditiveController(type:String, controller:AbstractController):TypeDescriptor {
			if (!registry.isConfigurable()) {
				throw new Error("Trying to add a new additive controller to a non-configurable registry");
			}
			var pair:Pair = getAdditiveControllersPair(type);
			ArrayCollection(pair.a).addItem(controller);
			return this;
		}
		
		private function getAdditiveControllersPair(type:String):Pair {
			var pair:Pair = additiveControllers[type];
			if (pair == null) {
				pair = new Pair(new ArrayCollection(), false);
				additiveControllers[type] = pair;
			}
			return pair;
		}
		
	}
}