package org.flowerplatform.freeplane.controller;

import org.flowerplatform.core.node.controller.AddNodeController;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.freeplane.FreeplanePlugin;
import org.freeplane.features.attribute.Attribute;
import org.freeplane.features.attribute.NodeAttributeTableModel;
import org.freeplane.features.map.NodeModel;

/**
 * @author Cristina Constantinescu
 * @author Mariana Gheorghe
 */
public class FreeplaneAddNodeController extends AddNodeController {

	@Override
	public void addNode(Node node, Node child) {
		NodeModel parentModel = FreeplanePlugin.getInstance().getFreeplaneUtils().getNodeModel(node.getId());
		NodeModel newNodeModel = new NodeModel("", parentModel.getMap());
		newNodeModel.setLeft(false);

		// create attributes table and persist the type
		NodeAttributeTableModel attributeTable = (NodeAttributeTableModel) newNodeModel.getExtension(NodeAttributeTableModel.class);
		if (attributeTable == null) {
			attributeTable = new NodeAttributeTableModel(newNodeModel);
			newNodeModel.addExtension(attributeTable);
		}		
		attributeTable.getAttributes().add(new Attribute("type", child.getType()));
		
		parentModel.insert(newNodeModel, parentModel.getChildCount());
		
		// set the id on the node instance
		child.setId(newNodeModel.createID());
	}

}
