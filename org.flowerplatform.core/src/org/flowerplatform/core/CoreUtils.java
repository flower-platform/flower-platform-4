package org.flowerplatform.core;

import java.util.Map;

import org.flowerplatform.core.node.remote.Node;

/**
 * @author Mariana Gheorghe
 */
public class CoreUtils {

	public static boolean isSubscribable(Map<String, Object> properties) {
		Boolean isSubscribable = (Boolean) properties.get(CoreConstants.IS_SUBSCRIBABLE);
		if (isSubscribable == null) {
			return false;
		}
		return isSubscribable;
	}
	
	public static Node getResourceNode(Node node) {
		if (node.getResource() == null) {
			return null;
		} else if (CoreConstants.SELF_RESOURCE.equals(node.getResource())) {
			return node;
		}
		return new Node(node.getResource());
	}
	
}
