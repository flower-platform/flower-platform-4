package org.flowerplatform.freeplane.style.controller;

import static org.flowerplatform.core.NodePropertiesConstants.HAS_CHILDREN;
import static org.flowerplatform.core.NodePropertiesConstants.TEXT;
import static org.flowerplatform.mindmap.MindMapNodePropertiesConstants.MIN_WIDTH;
import static org.flowerplatform.mindmap.MindMapNodePropertiesConstants.MAX_WIDTH;

import org.flowerplatform.core.node.controller.PropertiesProvider;
import org.flowerplatform.core.node.remote.Node;

/**
 * @author Sebastian Solomon
 */
public class StylePropertiesProvider extends PropertiesProvider {

	@Override
	public void populateWithProperties(Node node) {
		node.getProperties().put(TEXT, "styles");
		node.getProperties().put(HAS_CHILDREN, true);
		node.getProperties().put("styleName", "In progress (Yellow)");
		node.getProperties().put(MIN_WIDTH, node.getPropertyValue(MIN_WIDTH));
		node.getProperties().put(MAX_WIDTH, node.getPropertyValue(MAX_WIDTH));
	}

}
