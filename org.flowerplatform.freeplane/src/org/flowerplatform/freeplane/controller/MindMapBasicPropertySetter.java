package org.flowerplatform.freeplane.controller;

import org.flowerplatform.core.NodePropertiesConstants;
import org.flowerplatform.core.node.controller.PropertySetter;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.freeplane.FreeplanePlugin;
import org.freeplane.features.attribute.Attribute;
import org.freeplane.features.attribute.NodeAttributeTableModel;
import org.freeplane.features.map.NodeModel;
import org.freeplane.features.nodestyle.NodeSizeModel;

/**
 * @author Cristina Constantinescu
 */
public class MindMapBasicPropertySetter extends PropertySetter {

	@Override
	public void setProperty(Node node, String property, Object value) {		
		NodeModel nodeModel = getNodeModel(node);		
		switch (property) {
			case NodePropertiesConstants.TEXT:
				nodeModel.setText((String) value);	
				return;
			case "min_width":
				Integer newMinWidth;
				try {
					newMinWidth = Integer.valueOf((String) value);
				} catch (Exception e) {
					newMinWidth = NodeSizeModel.NOT_SET;
				}
				NodeSizeModel.createNodeSizeModel(nodeModel).setMinNodeWidth(newMinWidth);
				return;		
			case "max_width":	
				Integer newMaxWidth;
				try {
					newMaxWidth = Integer.valueOf((String) value);
				} catch (Exception e) {
					newMaxWidth = NodeSizeModel.NOT_SET;
				}
				NodeSizeModel.createNodeSizeModel(nodeModel).setMaxNodeWidth(newMaxWidth);								
				return;			
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
		node.getOrPopulateProperties().put(property, value);
	}
	
	protected NodeModel getNodeModel(Node node) {
		return FreeplanePlugin.getInstance().getFreeplaneUtils().getNodeModel(node.getIdWithinResource());
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
