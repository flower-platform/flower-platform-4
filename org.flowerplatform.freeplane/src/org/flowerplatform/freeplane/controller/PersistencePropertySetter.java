package org.flowerplatform.freeplane.controller;

import org.flowerplatform.core.NodePropertiesConstants;
import org.flowerplatform.core.node.controller.PropertySetter;
import org.flowerplatform.core.node.controller.PropertyValueWrapper;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.mindmap.MindMapPlugin;
import org.freeplane.features.attribute.Attribute;
import org.freeplane.features.attribute.NodeAttributeTableModel;
import org.freeplane.features.map.NodeModel;

/**
 * @author Cristina Constantinescu
 */
public class PersistencePropertySetter extends PropertySetter {

	@Override
	public void setProperty(Node node, String property, PropertyValueWrapper wrapper) {
		NodeModel rawNodeData = ((NodeModel) node.getOrRetrieveRawNodeData());
						
		if (NodePropertiesConstants.TEXT.equals(property)) {
			rawNodeData.setText((String) wrapper.getPropertyValue());	
			return;
		}
		
		if (MindMapPlugin.FREEPLANE_PERSISTENCE_NODE_TYPE_KEY.equals(property)) {
			throw new RuntimeException(String.format("Property with name %s shouldn't be set!", property));
		}
		
		// persist the property value in the attributes table
		NodeAttributeTableModel attributeTable = (NodeAttributeTableModel) rawNodeData.getExtension(NodeAttributeTableModel.class);		
		if (attributeTable == null) {
			attributeTable = new NodeAttributeTableModel(rawNodeData);
			rawNodeData.addExtension(attributeTable);
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
		NodeModel rawNodeData = ((NodeModel) node.getOrRetrieveRawNodeData());
		
		if (MindMapPlugin.FREEPLANE_PERSISTENCE_NODE_TYPE_KEY.equals(property)) {
			throw new RuntimeException(String.format("Property with name %s shouldn't be un-set!", property));
		}
		
		// remove attribute from the attributes table
		NodeAttributeTableModel attributeTable = (NodeAttributeTableModel) rawNodeData.getExtension(NodeAttributeTableModel.class);	
		if (attributeTable != null) {
			for (Attribute attribute : attributeTable.getAttributes()) {
				if (attribute.getName().equals(property)) {
					attributeTable.getAttributes().remove(attribute);
					break;
				}
			}
		}
		
		// remove the property from the node instance too
		node.getOrPopulateProperties().remove(property);
	}
	
}
