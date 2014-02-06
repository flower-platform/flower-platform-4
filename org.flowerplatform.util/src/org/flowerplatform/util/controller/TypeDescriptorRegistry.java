package org.flowerplatform.util.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowerplatform.util.Pair;
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
	boolean configurable;

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

	@SuppressWarnings("unchecked")
	public <T extends AbstractController> T getController(TypeDescriptor descriptor, String controllerType) {
		Pair<Object, Boolean> pair = descriptor.getController(controllerType);
		if (pair.b) {
			// categories were processed before; return the controller
			return (T) pair.a;
		}
		
		// else => let's scan now the categories
		
		// iterate categories to cache the controller
		for (String category : descriptor.getCategories()) {
			TypeDescriptor categoryDescriptor = getExpectedTypeDescriptor(category);
			if (categoryDescriptor == null) {
				// semi-error; a WARN is logged
				continue;
			}
			Pair<Object, Boolean> categoryPair = categoryDescriptor.getController(controllerType);
			// TODO as face testul pe .b
			if (pair.b) {
				throw new RuntimeException(String.format(
						"Node with type %s registered multiple categories with controllers of type %s", null, controllerType));
			}
			pair.a = categoryPair.a;
			pair.b = true;
		}
		
		// nobody registered a controller for this type
		if (!pair.b) {
			throw new RuntimeException("No controller of type " + controllerType);
			// TODO nu as arunca exceptie
		}
		return (T) pair.a;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends AbstractController> List<T> getControllers(TypeDescriptor descriptor, String controllerType) {
		Pair<Object, Boolean> pair = descriptor.getControllers(controllerType);
		List<T> controllers = (List<T>) pair.a;
		if (pair.b) {
			// categories were processed before; return the controllers
			return controllers;
		}
		
		// else => let's scan now the categories
		
		// iterate categories to cache the controllers
		if (descriptor.getCategories().size() == 0) {
			pair.b = true;
		} else {
			for (String category : descriptor.getCategories()) {
				TypeDescriptor categoryDescriptor = getExpectedTypeDescriptor(category);
				if (categoryDescriptor == null) {
					// semi-error; a WARN is logged
					continue;
				}
				
				Pair<Object, Boolean> categoryPair = categoryDescriptor.getControllers(controllerType);
				if (categoryPair.a != null) {
					controllers.addAll((List<T>) categoryPair.a);
					pair.b = true;
				}
			}
		}
		
		// order the controllers before returning
		Collections.sort(controllers);
		return controllers;
	}
}
