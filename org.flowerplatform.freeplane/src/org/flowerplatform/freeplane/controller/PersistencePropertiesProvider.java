package org.flowerplatform.freeplane.controller;

import static org.flowerplatform.core.NodePropertiesConstants.TEXT;

import org.flowerplatform.core.node.controller.PropertiesProvider;
import org.flowerplatform.core.node.remote.Node;
import org.freeplane.features.attribute.Attribute;
import org.freeplane.features.attribute.NodeAttributeTableModel;
import org.freeplane.features.map.NodeModel;

/**
 * @author Cristina Constantinescu
 */
public class PersistencePropertiesProvider extends PropertiesProvider {

	@Override
	public void populateWithProperties(Node node) {
		node.getProperties().put(TEXT, ((NodeModel) node.getOrRetrieveRawNodeData()).getText());
		
		// properties are populated from the attributes table
		NodeAttributeTableModel attributeTable = NodeAttributeTableModel.getModel(((NodeModel) node.getOrRetrieveRawNodeData()));
		if (attributeTable != null) {
			for (Attribute attribute : attributeTable.getAttributes()) {
				node.getProperties().put(attribute.getName(), attribute.getValue());
			}
		}
	}

}