package org.flowerplatform.core.node.controller;

import java.util.Collections;
import java.util.List;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.CoreUtils;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.resource.in_memory.ResourceNodeInfo;
import org.flowerplatform.util.controller.IDynamicCategoryProvider;
import org.flowerplatform.util.controller.TypeDescriptor;

/**
 * Provides a dynamic category based on a {@link Node}'s resource type.
 * For a given node, find the resource category from its resource node, i.e. {@link ResourceNodeInfo#getResourceCategory()}.
 * 
 * <p>
 * This way we can easily implement plugins that are "persistence providers" for a certain type of resource. E.g. the
 * FreePlane plugin would register various controllers ({@link ChildrenProvider}, {@link PropertiesProvider}, etc.) directly
 * on the category "category.resource.mm". Each node for a "mm" resource will belong to this category, no matter its node type
 * or the static categories assigned to the node type.
 * 
 * @author Cristina Constantinescu
 * @author Cristian Spiescu
 * @author Mariana Gheorghe
 */
public class ResourceTypeDynamicCategoryProvider implements IDynamicCategoryProvider {

	public static final String CATEGORY_RESOURCE_PREFIX = TypeDescriptor.CATEGORY_PREFIX + "resource.";
	
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
