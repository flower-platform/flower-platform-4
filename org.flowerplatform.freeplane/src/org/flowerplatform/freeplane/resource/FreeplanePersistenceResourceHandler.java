package org.flowerplatform.freeplane.resource;

import org.flowerplatform.mindmap.MindMapConstants;
import org.freeplane.features.attribute.Attribute;
import org.freeplane.features.attribute.NodeAttributeTableModel;
import org.freeplane.features.map.NodeModel;

/**
 * @author Mariana Gheorghe
 * @author Cristina Constantinescu
 */
public class FreeplanePersistenceResourceHandler extends FreeplaneMindmapResourceHandler {

	@Override
	protected String getType(String nodeUri, NodeModel nodeModel) {
		// get type from attributes table
		NodeAttributeTableModel attributeTable = (NodeAttributeTableModel) nodeModel.getExtension(NodeAttributeTableModel.class);
		if (attributeTable != null) {
			for (Attribute attribute : attributeTable.getAttributes()) {
				if (attribute.getName().equals(MindMapConstants.FREEPLANE_PERSISTENCE_NODE_TYPE_KEY)) {
					return (String) attribute.getValue();
				}
			}
		}
		throw new RuntimeException("Node " + nodeUri + " does not have a type!");
	}

}
