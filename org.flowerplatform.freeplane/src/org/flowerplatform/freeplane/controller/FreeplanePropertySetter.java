package org.flowerplatform.freeplane.controller;

import org.flowerplatform.core.node.controller.PropertyValueWrapper;
import org.flowerplatform.core.node.remote.Node;
import org.freeplane.features.attribute.Attribute;
import org.freeplane.features.attribute.NodeAttributeTableModel;
import org.freeplane.features.map.NodeModel;

/**
 * @author Cristina Constantinescu
 * @author Mariana Gheorghe
 */
public class FreeplanePropertySetter extends MindMapBasicPropertySetter {

	@Override
	public void setProperty(Node node, String property, PropertyValueWrapper wrapper) {
		super.setProperty(node, property, wrapper);
		NodeModel nodeModel = getNodeModel(node);
		if (property.equals("name")) {			
			nodeModel.setText((String) wrapper.getPropertyValue());				
		}
		
		// persist the property value in the attributes table
		NodeAttributeTableModel attributeTable = (NodeAttributeTableModel) nodeModel.getExtension(NodeAttributeTableModel.class);		
		if (attributeTable == null) {
			attributeTable = new NodeAttributeTableModel(nodeModel);
			nodeModel.addExtension(attributeTable);
		}		
		
		boolean set = false;
		for (Attribute attribute : attributeTable.getAttributes()) {
			if (attribute.getName().equals(property)) {
				// there was already an attribute with this value; overwrite it
				attribute.setValue(wrapper.getPropertyValue());
				set = true;
				break;
			}
		}
		if (!set) {
			// new attribute; add it
			attributeTable.getAttributes().add(new Attribute(property, wrapper.getPropertyValue()));
		}
		
		// set the property on the node instance too
		node.getOrPopulateProperties().put(property, wrapper.getPropertyValue());
	}

	@Override
	public void unsetProperty(Node node, String property) {
		NodeModel nodeModel = getNodeModel(node);
		
		// remove attribute from the attributes table
		NodeAttributeTableModel attributeTable = (NodeAttributeTableModel) nodeModel.getExtension(NodeAttributeTableModel.class);		
		for (Attribute attribute : attributeTable.getAttributes()) {
			if (attribute.getName().equals(property)) {
				attributeTable.getAttributes().remove(attribute);
				break;
			}
		}
		
		// remove the property from the node instance too
		node.getOrPopulateProperties().remove(property);
	}

}
