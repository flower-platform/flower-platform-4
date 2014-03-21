package org.flowerplatform.freeplane.controller;

import static org.flowerplatform.mindmap.MindMapPlugin.FREEPLANE_PERSISTENCE_NODE_TYPE_KEY;

import org.flowerplatform.core.node.remote.Node;
import org.freeplane.features.attribute.Attribute;
import org.freeplane.features.attribute.NodeAttributeTableModel;
import org.freeplane.features.map.NodeModel;

/**
 * @author Cristina Constantinescu
 */
public class PersistenceAddNodeProvider extends MindMapAddNodeController {

	@Override
	public void addNode(Node node, Node child, Node insertBeforeNode) {		
		super.addNode(node, child, insertBeforeNode);
		
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
