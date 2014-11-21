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
