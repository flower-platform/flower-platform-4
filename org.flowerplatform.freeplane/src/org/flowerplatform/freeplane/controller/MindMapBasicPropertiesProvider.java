package org.flowerplatform.freeplane.controller;

import static org.flowerplatform.core.NodePropertiesConstants.HAS_CHILDREN;
import static org.flowerplatform.core.NodePropertiesConstants.TEXT;

import org.flowerplatform.core.node.controller.PropertiesProvider;
import org.flowerplatform.core.node.remote.Node;
import org.freeplane.features.attribute.Attribute;
import org.freeplane.features.attribute.NodeAttributeTableModel;
import org.freeplane.features.map.NodeModel;
import org.freeplane.features.nodestyle.NodeSizeModel;

/**
 * @author Cristina Constantinescu
 */
public class MindMapBasicPropertiesProvider extends PropertiesProvider {

	@Override
	public void populateWithProperties(Node node) {
		node.getProperties().put(TEXT, ((NodeModel) node.getOrRetrieveRawNodeData()).getText());
		// TODO CC: temporary code
		node.getProperties().put(HAS_CHILDREN, ((NodeModel) node.getOrRetrieveRawNodeData()).hasChildren());
		
		// properties are populated from the attributes table
		NodeAttributeTableModel attributeTable = NodeAttributeTableModel.getModel(((NodeModel) node.getOrRetrieveRawNodeData()));
		if (attributeTable != null) {
			for (Attribute attribute : attributeTable.getAttributes()) {
				node.getProperties().put(attribute.getName(), attribute.getValue());
			}
		}
		
		NodeModel rawNodeData = ((NodeModel) node.getOrRetrieveRawNodeData());
		if (NodeSizeModel.getModel(rawNodeData) == null) {
			NodeSizeModel.createNodeSizeModel(rawNodeData);
		}
		
		Integer value = NodeSizeModel.getModel(rawNodeData).getMinNodeWidth();
		// if NOT_SET -> don't show -1, show empty data
		node.getProperties().put("min_width", value == NodeSizeModel.NOT_SET ? "" : value);
			
		value = NodeSizeModel.getModel(rawNodeData).getMaxNodeWidth();		
		// if NOT_SET -> don't show -1, show empty data
		node.getProperties().put("max_width", value == NodeSizeModel.NOT_SET ? "" : value);	
	}

}
