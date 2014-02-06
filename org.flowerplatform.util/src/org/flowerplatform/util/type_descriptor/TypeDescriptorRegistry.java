package org.flowerplatform.util.type_descriptor;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowerplatform.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Cristian Spiescu
 * @author Mariana Gheorghe
 */
public class TypeDescriptorRegistry {

	private final static Logger logger = LoggerFactory.getLogger(TypeDescriptorRegistry.class);

	private Map<String, TypeDescriptor> nodeTypeDescriptors = new HashMap<String, TypeDescriptor>();
	
	public TypeDescriptor getOrCreateNodeTypeDescriptor(String type) {
		TypeDescriptor result = nodeTypeDescriptors.get(type);
		if (result == null) {
			result = new TypeDescriptor();
			nodeTypeDescriptors.put(type, result);
		}
		return result;
	}
	
	public TypeDescriptor getExpectedNodeTypeDescriptor(String type) {
		TypeDescriptor result = nodeTypeDescriptors.get(type);
		if (result == null) {
			logger.warn("Operation invoked for nodeType = {}, but there is no associated descriptor registered! Aborting operation.", type);
			return null;
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public <T extends OrderedElement> T getController(TypeDescriptor descriptor, String controllerType) {
		Pair<Object, Boolean> pair = descriptor.getController(controllerType);
		if (pair.b) {
			// categories were processed before; return the controller
			return (T) pair.a;
		}
		
		// iterate categories to cache the controller
		for (String category : descriptor.getCategories()) {
			TypeDescriptor categoryDescriptor = getExpectedNodeTypeDescriptor(category);
			if (categoryDescriptor == null) {
				// semi-error; a WARN is logged
				continue;
			}
			Pair<Object, Boolean> categoryPair = categoryDescriptor.getController(controllerType);
			if (categoryPair.a != null) {
				pair.a = categoryPair.a;
				if (pair.b) {
					throw new RuntimeException(String.format(
							"Node with type %s registered multiple categories with controllers of type %s", null, controllerType));
				}
				pair.b = true;
			}
		}
		
		// a controller was found
		if (pair.a != null) {
			pair.b = true;
		}
		// nobody registered a controller for this type
		if (!pair.b) {
			throw new RuntimeException("No controller of type " + controllerType);
		}
		return (T) pair.a;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends OrderedElement> List<T> getControllers(TypeDescriptor descriptor, String controllerType) {
		Pair<Object, Boolean> pair = descriptor.getControllers(controllerType);
		List<T> controllers = (List<T>) pair.a;
		if (pair.b) {
			// categories were processed before; return the controllers
			return controllers;
		}
		
		// iterate categories to cache the controllers
		if (descriptor.getCategories().size() == 0) {
			pair.b = true;
		} else {
			for (String category : descriptor.getCategories()) {
				TypeDescriptor categoryDescriptor = getExpectedNodeTypeDescriptor(category);
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
