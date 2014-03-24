package org.flowerplatform.core;

import static org.flowerplatform.core.CorePlugin.SELF_RESOURCE;
import static org.flowerplatform.core.NodePropertiesConstants.IS_SUBSCRIBABLE;

import java.util.Map;

import org.flowerplatform.core.node.remote.Node;

/**
 * @author Mariana Gheorghe
 */
public class CoreUtils {

	public static boolean isSubscribable(Map<String, Object> properties) {
		Boolean isSubscribable = (Boolean) properties.get(IS_SUBSCRIBABLE);
		if (isSubscribable == null) {
			return false;
		}
		return isSubscribable;
	}
	
	public static Node getRootNode(Node node) {
		if (node.getResource() == null) {
			return null;
		} else if (SELF_RESOURCE.equals(node.getResource())) {
			return node;
		}
		return new Node(node.getResource());
	}
	
}
