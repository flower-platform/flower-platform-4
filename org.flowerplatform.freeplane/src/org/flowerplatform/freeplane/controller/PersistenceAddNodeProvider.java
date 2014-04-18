package org.flowerplatform.freeplane.controller;

import static org.flowerplatform.mindmap.MindMapConstants.FREEPLANE_PERSISTENCE_NODE_TYPE_KEY;

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.freeplane.features.attribute.Attribute;
import org.freeplane.features.attribute.NodeAttributeTableModel;
import org.freeplane.features.map.NodeModel;

/**
 * @author Cristina Constantinescu
 */
public class PersistenceAddNodeProvider extends MindMapAddNodeController {

	@Override
	public void addNode(Node node, Node child, ServiceContext<NodeService> context) {		
		super.addNode(node, child, context);
		
		NodeModel rawNodeData = ((NodeModel) child.getOrRetrieveRawNodeData());
		// create attributes table and persist the type
		NodeAttributeTableModel attributeTable = (NodeAttributeTableModel) rawNodeData.getExtension(NodeAttributeTableModel.class);
		if (attributeTable == null) {
			attributeTable = new NodeAttributeTableModel(rawNodeData);
			rawNodeData.addExtension(attributeTable);
		}		
		attributeTable.getAttributes().add(new Attribute(FREEPLANE_PERSISTENCE_NODE_TYPE_KEY, child.getType()));
		rawNodeData.getMap().setSaved(false);
	}
		
}
