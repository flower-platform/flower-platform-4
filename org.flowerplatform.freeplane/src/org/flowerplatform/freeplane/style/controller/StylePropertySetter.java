package org.flowerplatform.freeplane.style.controller;

import static org.flowerplatform.mindmap.MindMapNodePropertiesConstants.MIN_WIDTH;
import static org.flowerplatform.mindmap.MindMapNodePropertiesConstants.MAX_WIDTH;

import org.flowerplatform.core.node.controller.PropertySetter;
import org.flowerplatform.core.node.controller.PropertyValueWrapper;
import org.flowerplatform.core.node.remote.Node;

public class StylePropertySetter extends PropertySetter {

	@Override
	public void setProperty(Node node, String property,
			PropertyValueWrapper value) {
		
		node.getOrPopulateProperties().put(MIN_WIDTH, value.getPropertyValue());
		node.getOrPopulateProperties().put(MAX_WIDTH, value.getPropertyValue());

	}

	@Override
	public void unsetProperty(Node node, String property) {
		// TODO Auto-generated method stub

	}

}
