package org.flowerplatform.freeplane.controller;

import java.util.Map;

import org.flowerplatform.core.CorePlugin;
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
	public void addNode(Node node, Map<String, Object> properties, Node insertBeforeNode) {
		String type = (String) properties.get(CorePlugin.TYPE_KEY);
		if (type == null) {
			throw new RuntimeException(String.format("Property %s should be contained in map!", CorePlugin.TYPE_KEY));
		}	
		
		super.addNode(node, properties, insertBeforeNode);
		
		Node child = (Node) properties.get(CorePlugin.NODE_KEY);
		if (child == null) {
			throw new RuntimeException(String.format("Property %s should be contained in map, probably added by the persistence AddNodeController!", CorePlugin.NODE_KEY));
		}	
		
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
