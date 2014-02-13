package org.flowerplatform.freeplane.controller;

import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.freeplane.FreeplanePlugin;
import org.freeplane.features.attribute.Attribute;
import org.freeplane.features.attribute.NodeAttributeTableModel;
import org.freeplane.features.map.NodeModel;

/**
 * @author Cristina Constantinescu
 * @author Mariana Gheorghe
 */
public class FreeplaneAddNodeController extends MindMapBasicAddNodeController {

	@Override
	public void addNode(Node node, Node child, Node currentChildAtInsertionPoint) {
		super.addNode(node, child, currentChildAtInsertionPoint);
		
		NodeModel newNodeModel = FreeplanePlugin.getInstance().getFreeplaneUtils().getNodeModel(child.getId());
		// create attributes table and persist the type
		NodeAttributeTableModel attributeTable = (NodeAttributeTableModel) newNodeModel.getExtension(NodeAttributeTableModel.class);
		if (attributeTable == null) {
			attributeTable = new NodeAttributeTableModel(newNodeModel);
			newNodeModel.addExtension(attributeTable);
		}		
		attributeTable.getAttributes().add(new Attribute("type", child.getType()));				
	}

}
