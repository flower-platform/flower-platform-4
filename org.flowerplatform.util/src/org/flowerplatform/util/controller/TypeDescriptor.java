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
package org.flowerplatform.util.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowerplatform.util.UtilConstants;

/**
 * A type descriptor specifies (for a node type):
 * <ul>
 * 	<li>what are the categories this type belong to, cf. {@link #getCategories()}, {@link #addCategory(String)}</li>
 * 	<li>what are the single controllers, cf. {@link #getSingleController(TypeDescriptor, String)} {@link #addSingleController(String, AbstractController)}</li>
 * 	<li>what are the additive controllers, cf. {@link #getAdditiveControllers(String)} {@link #addAdditiveController(String, AbstractController)}</li>
 * </ul>
 * 
 * Meaning of a category: let's use an example (with additive controllers).
 * <ul>
 * 	<li>For node type "repository", <code>RepoChilrenProvider</code> is registered.</li>
 * 	<li>For category type "category.fileSystemNode", <code>FileSystemChildrenProvider</code> is registered.</li>
 * 	<li>For category type "category.saveableNode", <code>SaveableNodeChildrenProvider</code> is registered</li>
 * <li>For category type "category.all", <code>AllChildrenProvider</code> is registered</li>
 * </ul>
 * 
 * For the type descriptor corresponding to type "repository", we specify that it belongs to categories "category.fileSystemNode" and 
 * "category.saveableNode". In this case, when we request controllers (cf. {@link #getAdditiveControllers(String)})
 * we'll get <code>RepoChilrenProvider</code>, <code>FileSystemChildrenProvider</code>, <code>SaveableNodeChildrenProvider</code> and 
 * <code>AllChildrenProvider</code> (because "category.all" is a dynamic category, to which belong all types; other dynamic categories
 * can be added cf. {@link IDynamicCategoryProvider}). 
 * 
 * <p>
 * The mechanism is similar for single controllers. However, in this case only one controller is possible for a node type (that can be defined at a category type
 * level as well, not necessarily at node type level).
 * 
 * @see AbstractController
 * @see TypeDescriptorRegistry
 * 
 * @author Cristian Spiescu
 * @author Mariana Gheorghe
 * @author Cristina Constantinescu
 */
public class TypeDescriptor {

	private TypeDescriptorRegistry registry;
	
	public TypeDescriptorRegistry getRegistry() {
		return registry;
	}

	private String type;
	
	public String getType() {
		return type;
	}

	public TypeDescriptor(TypeDescriptorRegistry registry, String type) {
		super();
		this.registry = registry;
		this.type = type;
	}

	/**
	 * @see #getCategories()
	 */
	private List<String> categories;
	
	/**
	 * @return The categories to which this node belongs to.
	 */
	public List<String> getCategories() {
		if (categories == null) {
			return Collections.emptyList();
		}
		return categories;
	}
	
	/**
	 * Adds a new category. 
	 * 
	 * <p>
	 * May throw {@link IllegalStateException}, cf. {@link TypeDescriptorRegistry#configurable}. 
	 * 
	 * @return <code>this</code>, cf. builder pattern.
	 */
	public TypeDescriptor addCategory(String category) {
		if (!getRegistry().isConfigurable()) {
			throw new IllegalStateException("Trying to add a new category to a non-configurable registry");
		}
		if (!category.startsWith(UtilConstants.CATEGORY_PREFIX)) {
			throw new IllegalArgumentException("Category type should be prefixed with 'category.'");
		}
		if (categories == null) {
			categories = new ArrayList<String>();
		}
		categories.add(category);
		return this;
	}

	/**
	 * Same mechanism as for {@link #additiveControllers}.
	 * 
	 * @see #additiveControllers
	 */
	protected Map<String, ControllerEntry<AbstractController>> singleControllers = new HashMap<String, ControllerEntry<AbstractController>>();

	/**
	 * @return For a given controller type, the single controller 
	 * (collected from this node type, as well as its categories OR from object's dynamic category providers). 
	 */
	public <T extends AbstractController> T getSingleController(String controllerType, Object object) {		
		return getCachedSingleController(controllerType, object, true, true);
	}
	
	/**
	 * @return For a given controller type, the single controller (collected from this node type, as well as its categories). 
	 */
	@SuppressWarnings("unchecked")
	protected <T extends AbstractController> T getCachedSingleController(String controllerType, Object object, boolean includeDynamicCategoryProviders, boolean keepCached) {
		getRegistry().configurable = false;
		
		ControllerEntry<AbstractController> entry = getSingleControllerEntry(controllerType);
		if (entry.wasCached()) {
			// categories were processed before; return the controller
			return (T) entry.getCachedValue();
		}
		
		// else => let's scan now the categories
		T controller = (T) entry.getSelfValue();
		
		List<String> categories =  new ArrayList<String>();
		categories.addAll(getCategories());
		if (includeDynamicCategoryProviders) {
			for (IDynamicCategoryProvider categoryProvider : getRegistry().getDynamicCategoryProviders()) {
				categories.addAll(categoryProvider.getDynamicCategories(object));
			}
		}
		
		// iterate categories to cache the controller
		for (String category : categories) {
			TypeDescriptor categoryDescriptor = getRegistry().getExpectedTypeDescriptor(category);
			if (categoryDescriptor == null) {
				// semi-error; a WARN is logged
				continue;
			}
			
			T categoryController = categoryDescriptor.getCachedSingleController(controllerType, object, false, keepCached);
			if (categoryController != null) {
				// found a controller from a category
				// keep it if it has a lower order index than the existing one
				if (controller == null || controller.getOrderIndex() > categoryController.getOrderIndex()) {
					controller = categoryController;
				}
			}
		}
		
		if (controller instanceof NullController) {
			// means we must ignore all registered controllers
			controller = null;
		}
		
		// finished scanning the categories
		if (keepCached) {
			entry.setCached(true);
			entry.setCachedValue(controller);
		}
		
		return controller;
	}
		
	/**
	 * Adds a new single controller. 
	 * 
	 * <p>
	 * May throw {@link IllegalStateException}, cf. {@link TypeDescriptorRegistry#configurable}. 
	 * 
	 * @return <code>this</code>, cf. builder pattern.
	 */
	public TypeDescriptor addSingleController(String type, AbstractController controller) {
		if (!getRegistry().isConfigurable()) {
			throw new IllegalStateException("Trying to add a new single controller to a non-configurable registry");
		}
		ControllerEntry<AbstractController> entry = getSingleControllerEntry(type);
		entry.setSelfValue(controller);
		return this;
	}
	
	private ControllerEntry<AbstractController> getSingleControllerEntry(String type) {
		ControllerEntry<AbstractController> entry = singleControllers.get(type);
		if (entry == null) {
			entry = new ControllerEntry<AbstractController>();
			singleControllers.put(type, entry);
		}
		return entry;
	}
	
	/**
	 * Key = controller type. Value = the actual controller + a <code>boolean</code> that indicates
	 * if the categories where scanned for controllers or not yet.
	 * 
	 * <p>
	 * So, for a given controller type, when the <code>boolean</code> is <code>false</code> => contains the controller(s) only for this type. 
	 * When it's <code>true</code> => it contains the controller(s) for this type, and for the categories this type belongs to.
	 * 
	 * <p>
	 * The categories are scanned lazily, on first access. This caching mechanism exist, in order to improve read performance. I.e. avoid
	 * scanning the categories at each call.
	 * 
	 * <p>
	 * This method sets {@link TypeDescriptorRegistry#configurable} to <code>true</code>. I.e. from now on, no more configuration is possible any
	 * more. 
	 * 
	 * @see #singleControllers
	 */
	protected Map<String, ControllerEntry<List<? extends AbstractController>>> additiveControllers = new HashMap<String, ControllerEntry<List<? extends AbstractController>>>();

	public <T extends AbstractController> List<T> getAdditiveControllers(String controllerType, Object object) {
		return  getCachedAdditiveControllers(controllerType, object, true, true);
	}
		
	/**
	 * @return For a given controller type, the additive controllers 
	 * (collected from this node type, as well as its categories AND from object's dynamic category providers). 
	 */
	@SuppressWarnings("unchecked")
	protected <T extends AbstractController> List<T> getCachedAdditiveControllers(String controllerType, Object object, boolean includeDynamicCategoryProviders, boolean keepCached) {
		getRegistry().configurable = false;
		
		ControllerEntry<List<? extends AbstractController>> entry = getAdditiveControllersEntry(controllerType);
		if (entry.wasCached()) {
			// categories were processed before; return the controllers
			return (List<T>) entry.getCachedValue();
		}
		
		// else => let's scan now the categories
		
		List<T> controllers = new ArrayList<T>();
		controllers.addAll((Collection<? extends T>) entry.getSelfValue());
		
		List<String> categories =  new ArrayList<String>();
		categories.addAll(getCategories());
		if (includeDynamicCategoryProviders) {
			for (IDynamicCategoryProvider categoryProvider : getRegistry().getDynamicCategoryProviders()) {
				categories.addAll(categoryProvider.getDynamicCategories(object));
			}
		}
		// iterate categories to cache the controllers
		for (String category : categories) {
			TypeDescriptor categoryDescriptor = getRegistry().getExpectedTypeDescriptor(category);
			if (categoryDescriptor == null) {
				// semi-error; a WARN is logged
				continue;
			}
			
			controllers.addAll((Collection<? extends T>) categoryDescriptor.getCachedAdditiveControllers(controllerType, object, false, keepCached));
		}
		
		// finished scanning the categories
		if (keepCached) {
			entry.setCached(true);
			entry.setCachedValue(controllers);
		}
		
		Collections.sort(controllers);
		return controllers;
	}

	/**
	 * Adds a new additive controller. 
	 * 
	 * <p>
	 * May throw {@link IllegalStateException}, cf. {@link TypeDescriptorRegistry#configurable}. 
	 * 
	 * @return <code>this</code>, cf. builder pattern.
	 */
	@SuppressWarnings("unchecked")
	public TypeDescriptor addAdditiveController(String type, AbstractController controller) {
		if (!getRegistry().isConfigurable()) {
			throw new IllegalStateException("Trying to add a new additive controller to a non-configurable registry");
		}
		ControllerEntry<List<? extends AbstractController>> entry = getAdditiveControllersEntry(type);
		((List<AbstractController>) entry.getSelfValue()).add(controller);
		return this;
	}
	
	private ControllerEntry<List<? extends AbstractController>> getAdditiveControllersEntry(String type) {
		ControllerEntry<List<? extends AbstractController>> entry = additiveControllers.get(type);
		if (entry == null) {
			entry = new ControllerEntry<List<? extends AbstractController>>();
			entry.setSelfValue(new ArrayList<AbstractController>());
			additiveControllers.put(type, entry);
		}
		return entry;
	}

	@Override
	public String toString() {
		return String.format("%s [type = %s]", this.getClass().getSimpleName(), type);
	}
			
}