package org.flowerplatform.util.controller;

import java.util.List;

/**
 * Provides a list of categories for an {@link Object object}.
 * 
 * <p>
 * This provider isn't controller type related, so it is added in a different list, kept in {@link TypeDescriptorRegistry}
 * (not in {@link TypeDescriptor#getCategories()}).
 * <p>
 * When calling {@link TypeDescriptor#getAdditiveControllers(String, Object)} or {@link TypeDescriptor#getSingleController(String, Object)},
 * the controllers associated to each category from {@link #getDynamicCategories(Object)} are added to method returning list.
 * 
 * <p>
 * Examples:
 * <ul>
 * 	<li> {@link AllDynamicCategoryProvider} provides "category.all"
 *  <li> provider that returns, depending on some object's attribute, the corresponding category 
 *  	 (object is File -> "category.file"; object is MindMapNode -> "category.mm")
 * </ul>
 * 
 * @see TypeDescriptorRegistry#getDynamicCategoryProviders()
 * 
 * @author Cristina Constantinescu
 */
public interface IDynamicCategoryProvider {

	List<String> getDynamicCategories(Object object);
	
}
