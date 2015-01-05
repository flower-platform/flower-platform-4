/* license-start
 * 
 * Copyright (C) 2008 - 2014 Crispico Software, <http://www.crispico.com/>.
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

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IPersistenceController;
import org.flowerplatform.core.node.controller.IPropertiesProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.AbstractController;
import org.freeplane.features.attribute.Attribute;
import org.freeplane.features.attribute.NodeAttributeTableModel;
import org.freeplane.features.map.NodeModel;

/**
 * @author Cristina Constantinescu
 */
public class PersistencePropertiesProvider extends AbstractController implements IPropertiesProvider, IPersistenceController {

	@Override
	public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
		// properties are populated from the attributes table
		NodeAttributeTableModel attributeTable = NodeAttributeTableModel.getModel(((NodeModel) node.getRawNodeData()));
		if (attributeTable != null) {
			for (Attribute attribute : attributeTable.getAttributes()) {
				node.getProperties().put(attribute.getName(), attribute.getValue());
			}
		}
	}

}
