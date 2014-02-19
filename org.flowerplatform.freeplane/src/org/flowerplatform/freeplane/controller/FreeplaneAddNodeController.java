package org.flowerplatform.freeplane.controller;

import org.flowerplatform.core.NodePropertiesConstants;
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
	public void addNode(Node node, Node child, Node insertBeforeNode) {		
		super.addNode(node, child, insertBeforeNode);
				
		NodeModel newNodeModel = FreeplanePlugin.getInstance().getFreeplaneUtils().getNodeModel(child.getIdWithinResource());
		// create attributes table and persist the type
		NodeAttributeTableModel attributeTable = (NodeAttributeTableModel) newNodeModel.getExtension(NodeAttributeTableModel.class);
		if (attributeTable == null) {
			attributeTable = new NodeAttributeTableModel(newNodeModel);
			newNodeModel.addExtension(attributeTable);
		}		
		attributeTable.getAttributes().add(new Attribute(NodePropertiesConstants.TYPE, child.getType()));				
	}

}
