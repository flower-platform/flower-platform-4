package org.flowerplatform.freeplane.controller;

import org.flowerplatform.core.node.controller.PropertySetter;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.freeplane.FreeplanePlugin;
import org.freeplane.features.attribute.Attribute;
import org.freeplane.features.attribute.NodeAttributeTableModel;
import org.freeplane.features.map.NodeModel;

/**
 * @author Cristina Constantinescu
 * @author Mariana Gheorghe
 */
public class FreeplanePropertySetter extends PropertySetter {

	@Override
	public void setProperty(Node node, String property, Object value) {
		NodeModel nodeModel = getNodeModel(node);
		switch (property) {
			case "body": 
			case "name":
				nodeModel.setText((String) value);
				break;
		}
		
		// persist the property value in the attributes table
		NodeAttributeTableModel attributeTable = NodeAttributeTableModel.getModel(nodeModel);
		boolean set = false;
		for (Attribute attribute : attributeTable.getAttributes()) {
			if (attribute.getName().equals(property)) {
				// there was already an attribute with this value; overwrite it
				attribute.setValue(value);
				set = true;
				break;
			}
		}
		if (!set) {
			// new attribute; add it
			attributeTable.getAttributes().add(new Attribute(property, value));
		}
		
		// set the property on the node instance too
		node.getOrCreateProperties().put(property, value);
	}

	@Override
	public void unsetProperty(Node node, String property) {
		NodeModel nodeModel = getNodeModel(node);
		
		// remove attribute from the attributes table
		NodeAttributeTableModel attributeTable = NodeAttributeTableModel.getModel(nodeModel);
		for (Attribute attribute : attributeTable.getAttributes()) {
			if (attribute.getName().equals(property)) {
				attributeTable.getAttributes().remove(attribute);
				break;
			}
		}
		
		// remove the property from the node instance too
		node.getOrCreateProperties().remove(property);
	}
	
	protected NodeModel getNodeModel(Node node) {
		return FreeplanePlugin.getInstance().getFreeplaneUtils().getNodeModel(node.getId());
	}

}
