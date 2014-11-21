/* license-start
 * 
 * Copyright (C) 2008 - 2014 Crispico Software, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * license-end
 */
package org.flowerplatform.flexutil.controller {
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	import mx.collections.ArrayList;
	import mx.collections.IList;
	import mx.core.mx_internal;
	
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
		
		public var typeProvider:ITypeProvider;
		
		private var _masterRegistry:TypeDescriptorRegistry;
		
		public function isConfigurable():Boolean {
			return configurable;
		}

		public function set masterRegistry(registry:TypeDescriptorRegistry):void {
			_masterRegistry = registry;
			typeProvider = _masterRegistry.typeProvider;
		}
		
		public function get masterRegistry():TypeDescriptorRegistry {
			return _masterRegistry;
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
				// no descriptor found => check in master
				if (masterRegistry != null) {
					result = masterRegistry.getExpectedTypeDescriptor(type);
				}
				if (result == null) {
					trace("Operation invoked for nodeType = {}, but there is no associated descriptor registered! Aborting operation.", type);
				}
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
		
		public function getSingleController(feature:String, model:Object):AbstractController {
			return getExpectedTypeDescriptor(typeProvider.getType(model)).getSingleController(feature, model);
		}
	
		public function getAdditiveControllers(feature:String, model:Object):IList {
			return getExpectedTypeDescriptor(typeProvider.getType(model)).getAdditiveControllers(feature, model);
		}
		
		/**
		 * Add the remote descriptors received from the server.
		 * 
		 * @see <code>getTypeDescriptorsRemote()</code> from Java.
		 */
		public function addTypeDescriptorsRemote(remotes:ArrayCollection):void {
			for (var i:int = 0; i < remotes.length; i++) {
				var remote:TypeDescriptorRemote = TypeDescriptorRemote(remotes.getItemAt(i));
				
				// create new type descriptor with remote type
				var descriptor:TypeDescriptor = null;
				if (Utils.beginsWith(remote.type, FlexUtilConstants.CATEGORY_PREFIX)) {
					descriptor = getOrCreateCategoryTypeDescriptor(remote.type);
				} else {
					descriptor = getOrCreateTypeDescriptor(remote.type);
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
	}
}
