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