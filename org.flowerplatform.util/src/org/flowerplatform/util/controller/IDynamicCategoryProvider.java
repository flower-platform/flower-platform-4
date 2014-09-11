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

import java.util.List;

/**
 * {@link TypeDescriptor} relies on implementations of this interface (registered cf. {@link TypeDescriptorRegistry#getDynamicCategoryProviders()}, 
 * {@link TypeDescriptorRegistry#addDynamicCategoryProvider(IDynamicCategoryProvider)}) to get a list of dynamic categories for an {@link Object object} type. 
 * 
 * <p>
 * A static category is a category which is specified per node/object type (cf. {@link TypeDescriptor#getCategories()}). <br>
 * A dynamic category is a category which is generated programmatically (by implementations of this interface) for an object <b>type</b> (not for an object, cf. remark below).
 * 
 * <p>
 * Examples:
 * <ul>
 * 	<li>{@link AllDynamicCategoryProvider} provides "category.all" for all node types.</li>
 *  <li>a provider that returns, depending on some object's constant/immutable attribute/property, the corresponding category 
 *  	 (object is File -> "category.file"; object is MindMapNode -> "category.mm")</li>
 * </ul>
 * 
 * <b>REMARKS:</b>
 * <ul>
 * 	<li>This is not a {@link AbstractController controller}.</li>
 * 	<li>The "dynamicity" is per node type, not per node. E.g. The first time when a controller is retrieved for "node1" of type "myNodeType",
 *  {@link IDynamicCategoryProvider}s are interrogated. The second time when a controller is retrieved for another node ("node2") of same type
 *  ("myNodeType"), {@link IDynamicCategoryProvider}s are no longer interrogated, because the first value(s) that were returned are already cached.
 *  We have this behavior considering performance. In the future, if "dynamicity" at node level is needed, we may find a solution.
 * 
 * @see TypeDescriptor
 * @see TypeDescriptorRegistry#getDynamicCategoryProviders()
 * 
 * @author Cristina Constantinescu
 * @author Cristian Spiescu
 */
public interface IDynamicCategoryProvider {

	/**
	 *@author see class
	 **/
	List<String> getDynamicCategories(Object object);
	
}