/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * license-end
 */
package org.flowerplatform.freeplane.controller;

import static org.flowerplatform.mindmap.MindMapConstants.FREEPLANE_PERSISTENCE_NODE_TYPE_KEY;

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IPersistenceController;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.freeplane.features.attribute.Attribute;
import org.freeplane.features.attribute.NodeAttributeTableModel;
import org.freeplane.features.map.NodeModel;

/**
 * @author Cristina Constantinescu
 */
public class PersistenceAddNodeProvider extends MindMapAddNodeController implements IPersistenceController {

	@Override
	public void addNode(Node node, Node child, ServiceContext<NodeService> context) {		
		super.addNode(node, child, context);
		
		NodeModel rawNodeData = ((NodeModel) child.getRawNodeData());
		// create attributes table and persist the type
		NodeAttributeTableModel attributeTable = (NodeAttributeTableModel) rawNodeData.getExtension(NodeAttributeTableModel.class);
		if (attributeTable == null) {
			attributeTable = new NodeAttributeTableModel(rawNodeData);
			rawNodeData.addExtension(attributeTable);
		}		
		attributeTable.getAttributes().add(new Attribute(FREEPLANE_PERSISTENCE_NODE_TYPE_KEY, child.getType()));
		
		for (String attr : context.getContext().keySet()) {
			Object value = context.get(attr);
			attributeTable.getAttributes().add(new Attribute(attr, value));						
		}
		rawNodeData.getMap().setSaved(false);
	}
		
}
