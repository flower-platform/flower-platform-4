/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
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
		
		public function TypeDescriptorRegistry() {
			super();
			addDynamicCategoryProvider(new AllDynamicCategoryProvider());
		}
	
	}
}