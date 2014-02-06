package org.flowerplatform.freeplane.controller;

import org.flowerplatform.core.node.controller.PropertiesProvider;
import org.flowerplatform.core.node.remote.Node;
import org.freeplane.features.attribute.Attribute;
import org.freeplane.features.attribute.NodeAttributeTableModel;
import org.freeplane.features.map.NodeModel;

/**
 * @author Cristina Constantinescu
 * @author Mariana Gheorghe
 */
public class FreeplanePropertiesProvider extends PropertiesProvider<NodeModel> {

	@Override
	public void populateWithProperties(Node node, NodeModel rawNodeData) {		
		node.getOrCreateProperties().put("body", rawNodeData.getText());
		node.getOrCreateProperties().put("hasChildren", rawNodeData.hasChildren());
		
		// properties are populated from the attributes table
		NodeAttributeTableModel attributeTable = NodeAttributeTableModel.getModel(rawNodeData);
		for (Attribute attribute : attributeTable.getAttributes()) {
			node.getOrCreateProperties().put(attribute.getName(), attribute.getValue());
		}
	}
	
}
