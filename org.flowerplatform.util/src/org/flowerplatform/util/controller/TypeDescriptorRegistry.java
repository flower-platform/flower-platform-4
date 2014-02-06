package org.flowerplatform.util.controller;

import java.util.HashMap;
import java.util.Map;

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
 */
public class TypeDescriptorRegistry {

	private final static Logger logger = LoggerFactory.getLogger(TypeDescriptorRegistry.class);
	
	/**
	 * Package visibility, so that {@link TypeDescriptor} can change it.
	 * 
	 * @see TypeDescriptor#additiveControllers
	 */
	boolean configurable = true;

	/**
	 * @see TypeDescriptor#additiveControllers
	 */
	public boolean isConfigurable() {
		return configurable;
	}

	private Map<String, TypeDescriptor> typeDescriptors = new HashMap<String, TypeDescriptor>();
	
	public TypeDescriptor getOrCreateTypeDescriptor(String type) {
		if (type.startsWith(CategoryTypeDescriptor.CATEGORY_PREFIX)) {
			throw new IllegalArgumentException("Please use getOrCreateCategoryTypeDescriptor()");
		}
		TypeDescriptor result = typeDescriptors.get(type);
		if (result == null) {
			result = new TypeDescriptor(this, type);
			typeDescriptors.put(type, result);
		}
		return result;
	}
	
	/**
	 * Same as {@link #getOrCreateTypeDescriptor(String)}, but for categories. <code>type</code> should be
	 * prefixed with "category."; e.g. "category.codeSync", or "category.all".
	 * 
	 * @param type
	 * @return
	 */
	public TypeDescriptor getOrCreateCategoryTypeDescriptor(String type) {
		if (!type.startsWith(CategoryTypeDescriptor.CATEGORY_PREFIX)) {
			throw new IllegalArgumentException("Category type should be prefixed with 'category.'");
		}
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
			logger.warn("Operation invoked for nodeType = {}, but there is no associated descriptor registered! Aborting operation.", type);
			return null;
		}
		return result;
	}

}
