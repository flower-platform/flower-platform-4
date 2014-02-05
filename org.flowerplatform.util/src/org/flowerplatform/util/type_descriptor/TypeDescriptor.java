package org.flowerplatform.util.type_descriptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowerplatform.util.Pair;

/**
 * @author Cristian Spiescu
 * @author Cristina Constantinescu
 * @author Mariana Gheorghe
 */
public class TypeDescriptor {

	private List<String> categories;
	
	public List<String> getCategories() {
		if (categories == null) {
			return Collections.emptyList();
		}
		return categories;
	}
	
	public TypeDescriptor addCategory(String category) {
		if (categories == null) {
			categories = new ArrayList<String>();
		}
		categories.add(category);
		return this;
	}
	
	private Map<String, Pair<Object, Boolean>> controllers;
	
	private Map<String, Pair<Object, Boolean>> getControllers() {
		if (controllers == null) {
			controllers = new HashMap<String, Pair<Object,Boolean>>();
		}
		return controllers;
	}
	
	/**
	 * Returns the pair from the controllers map; perform lazy init
	 * if there is no pair registered.
	 */
	public Pair<Object, Boolean> getController(String type) {
		Pair<Object, Boolean> pair = getControllers().get(type);
		if (pair == null) {
			pair = new Pair<Object, Boolean>(null, false);
			getControllers().put(type, pair);
		}
		return pair;
	}
	
	/**
	 * Returns the pair from the controllers map; perform lazy init
	 * if there is no pair registered.
	 */
	public Pair<Object, Boolean> getControllers(String type) {
		Pair<Object, Boolean> pair = getControllers().get(type);
		if (pair == null) {
			pair = new Pair<Object, Boolean>(new ArrayList<Object>(), false);
			getControllers().put(type, pair);
		}
		return pair;
	}
	
	public TypeDescriptor addController(String type, OrderedElement controller) {
		Pair<Object, Boolean> pair = getController(type);
		pair.a = controller;
		return this;
	}
	
	public TypeDescriptor addControllerToList(String type, OrderedElement controller) {
		Pair<Object, Boolean> pair = getControllers(type);
		((List<OrderedElement>) pair.a).add(controller);
		return this;
	}

}
