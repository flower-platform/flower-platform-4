package org.flowerplatform.core.node.controller;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.util.controller.IDynamicCategoryProvider;
import org.flowerplatform.util.controller.TypeDescriptor;

/**
 * Provides the corresponding category based on {@link Node node}s resource type.
 * 
 * <p>
 * Resource must have the following format: %resource_type%://%other_info%. 
 * 
 * @author Cristina Constantinescu
 */
public class ResourceTypeDynamicCategoryProvider implements IDynamicCategoryProvider {

	public static final String CATEGORY_RESOURCE_PREFIX = TypeDescriptor.CATEGORY_PREFIX + "resource.";
	private static final Pattern RESOURCE_PATTERN = Pattern.compile("(\\w+)://?");
	
	@Override
	public List<String> getDynamicCategories(Object object) {
		if (object instanceof Node) {
			Node node = (Node) object;
			if (node.getResource() != null) {								
				Matcher matcher = RESOURCE_PATTERN.matcher(node.getResource());
				if (matcher.find()) {
					return Collections.singletonList(CATEGORY_RESOURCE_PREFIX + matcher.group(1));
				}
			}
		}
		return Collections.emptyList();
	}

}
