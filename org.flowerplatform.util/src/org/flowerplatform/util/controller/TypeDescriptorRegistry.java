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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.flowerplatform.util.UtilConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This registry contains the mapping: "node type" -> {@link TypeDescriptor} (which holds mainly the controllers
 * associated to "node type".
 * 
 * @see AbstractController
 * @see TypeDescriptor
 * 
 * @author Cristian Spiescu
 * @author Mariana Gheorghe
 * @author Cristina Constantinescu
 */
public class TypeDescriptorRegistry {

	private static final Logger LOGGER = LoggerFactory.getLogger(TypeDescriptorRegistry.class);
	
	/**
	 * Package visibility, so that {@link TypeDescriptor} can change it.
	 * 
	 * @see TypeDescriptor#additiveControllers
	 */
	boolean configurable = true;

	private ITypeProvider typeProvider;
	
	/**
	 * @see TypeDescriptor#additiveControllers
	 */
	public boolean isConfigurable() {
		return configurable;
	}

	private Map<String, TypeDescriptor> typeDescriptors = new HashMap<String, TypeDescriptor>();
	
	public ITypeProvider getTypeProvider() {
		return typeProvider;
	}

	public void setTypeProvider(ITypeProvider typeProvider) {
		this.typeProvider = typeProvider;
	}

	/**
	 *@author see class
	 **/
	public TypeDescriptor getOrCreateTypeDescriptor(String type) {
		if (type.startsWith(UtilConstants.CATEGORY_PREFIX)) {
			throw new IllegalArgumentException("Please use getOrCreateCategoryTypeDescriptor()");
		}
		return getOrCreateTypeDescriptorInternal(type);
	}
	
	/**
	 * Same as {@link #getOrCreateTypeDescriptor(String)}, but for categories. <code>type</code> should be
	 * prefixed with "category."; e.g. "category.codeSync", or "category.all".
	 * 
	 * @param type
	 * @return
	 */
	public TypeDescriptor getOrCreateCategoryTypeDescriptor(String type) {
		if (!type.startsWith(UtilConstants.CATEGORY_PREFIX)) {
			throw new IllegalArgumentException("Category type should be prefixed with 'category.'");
		}
		return getOrCreateTypeDescriptorInternal(type);
	}
	
	/**
	 * @author see class 
	 */
	public TypeDescriptor getOrCreateTypeDescriptorInternal(String type) {
		TypeDescriptor result = typeDescriptors.get(type);
		if (result == null) {
			result = new CategoryTypeDescriptor(this, type);
			typeDescriptors.put(type, result);
		}
		return result;
	}
	
	/**
	 * @return The corresponding type descriptor, if exists. <code>null</code> otherwise, in which case, we
	 * 	also log a warning. 
	 */
	public TypeDescriptor getExpectedTypeDescriptor(String type) {
		TypeDescriptor result = typeDescriptors.get(type);
		if (result == null) {
			LOGGER.warn("Operation invoked for nodeType = {}, but there is no associated descriptor registered! Aborting operation.", type);
			return null;
		}
		return result;
	}
		
	private List<IDynamicCategoryProvider> dynamicCategoryProviders;
	/**
	 *@author see class
	 **/
	public List<IDynamicCategoryProvider> getDynamicCategoryProviders() {
		if (dynamicCategoryProviders == null) {
			dynamicCategoryProviders = new ArrayList<IDynamicCategoryProvider>();			
		}
		return dynamicCategoryProviders;
	}
	/**
	 *@author see class
	 **/
	public void addDynamicCategoryProvider(IDynamicCategoryProvider provider) {
		getDynamicCategoryProviders().add(provider);
	}
	/**
	 *@author see class
	 **/
	public TypeDescriptorRegistry() {
		super();
		addDynamicCategoryProvider(new AllDynamicCategoryProvider());
	}
	
	/**
	 * @return All the registered types.
	 */
	public List<String> getRegisteredTypes() {
		List<String> types = new ArrayList<String>();
		types.addAll(typeDescriptors.keySet());
		return types;
	}
	
	/**
	 * Converts the registered {@link TypeDescriptor}s to {@link TypeDescriptorRemote}s that will be sent to the client.
	 * 
	 * @author Mariana Gheorghe
	 */
	public List<TypeDescriptorRemote> getTypeDescriptorsRemote() {
		List<TypeDescriptorRemote> remotes = new ArrayList<TypeDescriptorRemote>();
		for (TypeDescriptor descriptor : typeDescriptors.values()) {
			// create the new remote type descriptor with the type and static categories
			TypeDescriptorRemote remote = new TypeDescriptorRemote(descriptor.getType(), descriptor.getCategories());
			
			// filter the single controllers map
			for (Entry<String, ControllerEntry<IController>> entry : descriptor.singleControllers.entrySet()) {
				if (entry.getValue().getSelfValue() instanceof IDescriptor) {
					remote.getSingleControllers().put(entry.getKey(), (IDescriptor) entry.getValue().getSelfValue());
				}
			}
			
			// filter the additive controlers map
			for (Entry<String, ControllerEntry<List<? extends IController>>> entry : descriptor.additiveControllers.entrySet()) {
				List<IDescriptor> additiveControllers = new ArrayList<IDescriptor>();
				for (IController abstractController : entry.getValue().getSelfValue()) {
					if (abstractController instanceof IDescriptor) {
						additiveControllers.add((IDescriptor) abstractController);
					}
				}
				if (additiveControllers.size() > 0) {
					remote.getAdditiveControllers().put(entry.getKey(), additiveControllers);
				}
			}
			remotes.add(remote);
		}
		return remotes;
	}
	
	/**
	 * @author Cristina Constantinescu
	 * @author Cristian Spiescu
	 */
	public IController getSingleController(String feature, Object model) {
		String type = typeProvider.getType(model);
		TypeDescriptor typeDescriptor = getExpectedTypeDescriptor(type);
		if (typeDescriptor == null) {
			// this happens when we don't have anything registered for this type
			// we create an empty one to allow getting controllers from dynamic categories.
			typeDescriptor = getOrCreateTypeDescriptor(type);			
		}
		return typeDescriptor.getSingleController(feature, model);		
	}

	/**
	 * @author Cristina Constantinescu
	 * @author Cristian Spiescu
	 */
	public List<? extends IController> getAdditiveControllers(String feature, Object model) {
		String type = typeProvider.getType(model);
		TypeDescriptor typeDescriptor = getExpectedTypeDescriptor(type);
		if (typeDescriptor == null) {
			// this happens when we don't have anything registered for this type
			// we create an empty one to allow getting controllers from dynamic categories.
			typeDescriptor = getOrCreateTypeDescriptor(type);			
		}
		return typeDescriptor.getAdditiveControllers(feature, model);
	}

}
