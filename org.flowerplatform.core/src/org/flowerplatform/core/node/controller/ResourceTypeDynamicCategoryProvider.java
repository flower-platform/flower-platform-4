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
package org.flowerplatform.core.node.controller;

import java.util.Collections;
import java.util.List;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.CoreUtils;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.resource.in_memory.ResourceNodeInfo;
import org.flowerplatform.util.controller.IDynamicCategoryProvider;

/**
 * Provides a dynamic category based on a {@link Node}'s resource type.
 * For a given node, find the resource category from its resource node, i.e. {@link ResourceNodeInfo#getResourceCategory()}.
 * 
 * <p>
 * This way we can easily implement plugins that are "persistence providers" for a certain type of resource. E.g. the
 * FreePlane plugin would register various controllers ({@link IChildrenProvider}, {@link IPropertiesProvider}, etc.) directly
 * on the category "category.resource.mm". Each node for a "mm" resource will belong to this category, no matter its node type
 * or the static categories assigned to the node type.
 * 
 * @author Cristina Constantinescu
 * @author Cristian Spiescu
 * @author Mariana Gheorghe
 */
public class ResourceTypeDynamicCategoryProvider implements IDynamicCategoryProvider {

	@Override
	public List<String> getDynamicCategories(Object object) {
		if (object instanceof Node) {
			Node node = (Node) object;
			Node resourceNode = CoreUtils.getResourceNode(node);
			if (resourceNode == null) {
				return Collections.emptyList();
			}
			String resourceCategory = CorePlugin.getInstance().getResourceService().getResourceCategory(resourceNode.getFullNodeId());
			if (resourceCategory != null) {
				return Collections.singletonList(resourceCategory);
			}
		}
		return Collections.emptyList();
	}

}
