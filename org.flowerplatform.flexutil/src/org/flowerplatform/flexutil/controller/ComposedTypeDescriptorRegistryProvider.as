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
	import mx.collections.ArrayCollection;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class ComposedTypeDescriptorRegistryProvider implements ITypeDescriptorRegistryProvider {
		
		private var providers:ArrayCollection = new ArrayCollection();
		
		private var masterRegistry:TypeDescriptorRegistry;
		
		public function ComposedTypeDescriptorRegistryProvider(masterRegistry:TypeDescriptorRegistry) {
			this.masterRegistry = masterRegistry;
		}
		
		public function addProvider(provider:ITypeDescriptorRegistryProvider):void {
			providers.addItem(provider);
		}
		
		public function getTypeDescriptorRegistry(model:Object):TypeDescriptorRegistry {
			for each (var provider:ITypeDescriptorRegistryProvider in providers) {
				var registry:TypeDescriptorRegistry = provider.getTypeDescriptorRegistry(model);
				if (registry != null) {
					registry.masterRegistry = masterRegistry;
					return registry;
				}
			}
			return masterRegistry;
		}
	}
}