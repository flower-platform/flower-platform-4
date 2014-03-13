package org.flowerplatform.freeplane.controller;

import java.util.regex.Matcher;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.controller.ResourceTypeDynamicCategoryProvider;
import org.flowerplatform.core.node.controller.RootNodeProvider;
import org.flowerplatform.core.node.remote.Node;

/**
 * @author Cristina Constantinescu
 * @author Mariana Gheorghe
 */
public class MindMapRootNodeProvider extends RootNodeProvider {

	@Override
	public Node getRootNode(Node node) {
		if (CorePlugin.RESOURCE_TYPE.equals(node.getType())) {
			return node;
		}
		
		// the resource root node's id is resource||path-to-resource
		
		// get the path from the resource of this node
		Matcher matcher = ResourceTypeDynamicCategoryProvider.RESOURCE_PATTERN.matcher(node.getResource());
		String path = null;
		if (matcher.find()) {
			path = matcher.group(2);
		}
		return new Node(CorePlugin.RESOURCE_TYPE, null, path, null);
	}

}
