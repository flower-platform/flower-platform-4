package org.flowerplatform.freeplane.controller;

import org.flowerplatform.core.node.controller.PropertySetter;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.freeplane.FreeplanePlugin;
import org.freeplane.features.map.NodeModel;

/**
 * @author Cristina Constantinescu
 */
public class MindMapBasicPropertySetter extends PropertySetter {

	@Override
	public void setProperty(Node node, String property, Object value) {
		NodeModel nodeModel = getNodeModel(node);		
		if (property.equals("body")) {			
			nodeModel.setText((String) value);			
		}
	}

	@Override
	public void unsetProperty(Node node, String property) {
	}
	
	protected NodeModel getNodeModel(Node node) {
		return FreeplanePlugin.getInstance().getFreeplaneUtils().getNodeModel(node.getId());
	}

}