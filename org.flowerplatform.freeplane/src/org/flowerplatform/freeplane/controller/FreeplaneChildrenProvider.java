package org.flowerplatform.freeplane.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.flowerplatform.core.node.controller.ChildrenProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.freeplane.FreeplanePlugin;
import org.flowerplatform.util.Pair;
import org.freeplane.features.attribute.Attribute;
import org.freeplane.features.attribute.NodeAttributeTableModel;
import org.freeplane.features.map.NodeModel;

/**
 * @author Cristina Constantinescu
 */
public class FreeplaneChildrenProvider extends ChildrenProvider {
			
	@Override
	public List<Pair<Node, Object>> getChildren(Node node) {
		NodeModel nodeModel = FreeplanePlugin.getInstance().getFreeplaneUtils().getNodeModel(node.getId());
		if (node.getId() == null) {
			return Collections.singletonList(new Pair<Node, Object>(getEmptyNode(nodeModel), nodeModel));
		}
		List<Pair<Node, Object>> children = new ArrayList<Pair<Node, Object>>();		
		for (NodeModel child : nodeModel.getChildren()) {
			children.add(new Pair<Node, Object>(getEmptyNode(child), child));
		}
			
		return children;		
	}
	
		
	public Node getEmptyNode(NodeModel nodeModel) {
		Node node = new Node();
		node.setId(nodeModel.createID());
		// get type from attributes table
		NodeAttributeTableModel attributeTable = (NodeAttributeTableModel) nodeModel.getExtension(NodeAttributeTableModel.class);
		if (attributeTable != null) {
			for (Attribute attribute : attributeTable.getAttributes()) {
				if (attribute.getName().equals("type")) {
					node.setType((String) attribute.getValue());
					break;
				}
			}
		}
		if (node.getType() == null) { 
			// no type provided, maybe this node is provided by a random .mm file, so set type to freeplaneNode
			node.setType(FreeplanePlugin.FREEPLANE_NODE_TYPE);			
		}
		
		// TODO CC: temporary code
		if (FreeplanePlugin.FREEPLANE_NODE_TYPE.equals(node.getType())) {
			node.setResource("mm://path_to_resource");
		}
		return node;
	}

}
