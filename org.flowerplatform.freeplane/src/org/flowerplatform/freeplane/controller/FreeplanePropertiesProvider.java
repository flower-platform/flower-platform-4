package org.flowerplatform.freeplane.controller;

import org.flowerplatform.core.node.remote.Node;
import org.freeplane.features.attribute.Attribute;
import org.freeplane.features.attribute.NodeAttributeTableModel;
import org.freeplane.features.map.NodeModel;

/**
 * @author Cristina Constantinescu
 * @author Mariana Gheorghe
 */
public class FreeplanePropertiesProvider extends MindMapBasicPropertiesProvider {

	@Override
	public void populateWithProperties(Node node) {		
		super.populateWithProperties(node);
		
		// properties are populated from the attributes table
		NodeAttributeTableModel attributeTable = NodeAttributeTableModel.getModel(((NodeModel) node.getOrRetrieveRawNodeData()));
		if (attributeTable != null) {
			for (Attribute attribute : attributeTable.getAttributes()) {
				node.getProperties().put(attribute.getName(), attribute.getValue());
			}
		}
	}
	
}
