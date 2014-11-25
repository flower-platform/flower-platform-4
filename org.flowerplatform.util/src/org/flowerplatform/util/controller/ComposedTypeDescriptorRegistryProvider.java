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
package org.flowerplatform.util.controller;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mariana Gheorghe
 */
public class ComposedTypeDescriptorRegistryProvider implements ITypeDescriptorRegistryProvider {

	private List<ITypeDescriptorRegistryProvider> providers = new ArrayList<ITypeDescriptorRegistryProvider>();

	private TypeDescriptorRegistry masterRegistry;
	
	public ComposedTypeDescriptorRegistryProvider(TypeDescriptorRegistry masterRegistry) {
		this.masterRegistry = masterRegistry;
	}
	
	/**
	 * Add the provider to the list of providers.
	 */
	public void addProvider(ITypeDescriptorRegistryProvider provider) {
		providers.add(provider);
	}
	
	@Override
	public TypeDescriptorRegistry getTypeDescriptorRegistry(Object model) {
		for (ITypeDescriptorRegistryProvider provider : providers) {
			TypeDescriptorRegistry registry = provider.getTypeDescriptorRegistry(model);
			if (registry != null) {
				registry.setMasterRegistry(masterRegistry);
				return registry;
			}
		}
		return masterRegistry;
	}

}
