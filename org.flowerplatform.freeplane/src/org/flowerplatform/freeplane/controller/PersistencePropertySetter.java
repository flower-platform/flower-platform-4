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

import java.util.Map;

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IPersistenceController;
import org.flowerplatform.core.node.controller.IPropertySetter;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.PropertyWrapper;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.mindmap.MindMapConstants;
import org.flowerplatform.util.controller.AbstractController;
import org.freeplane.features.attribute.Attribute;
import org.freeplane.features.attribute.NodeAttributeTableModel;
import org.freeplane.features.map.NodeModel;

/**
 * @author Cristina Constantinescu
 */
public class PersistencePropertySetter extends AbstractController implements IPropertySetter, IPersistenceController {

	@Override
	public void setProperties(Node node, Map<String, Object> properties, ServiceContext<NodeService> context) {
		NodeModel rawNodeData = ((NodeModel) node.getRawNodeData());
		
		for (String property : properties.keySet()) {
			Object value = properties.get(property); 

			if (MindMapConstants.FREEPLANE_PERSISTENCE_NODE_TYPE_KEY.equals(property)) {
				throw new RuntimeException(String.format("Property with name %s shouldn't be set because it's reserved. Please use another key!", property));
			}
			
			// persist the property value in the attributes table
			NodeAttributeTableModel attributeTable = (NodeAttributeTableModel) rawNodeData.getExtension(NodeAttributeTableModel.class);		
			if (attributeTable == null) {
				attributeTable = new NodeAttributeTableModel(rawNodeData);
				rawNodeData.addExtension(attributeTable);
			}		
			
			Object propertyValue = value instanceof PropertyWrapper ? ((PropertyWrapper) value).getValue() : value;
	
			boolean set = false;
			for (Attribute attribute : attributeTable.getAttributes()) {
				if (attribute.getName().equals(property)) {
					// there was already an attribute with this value; overwrite it
					attribute.setValue(propertyValue);
					set = true;
					break;
				}
			}
			if (!set) {
				// new attribute; add it
				attributeTable.getAttributes().add(new Attribute(property, propertyValue));
			}
			rawNodeData.getMap().setSaved(false);
			
			// set the property on the node instance too
			node.getOrPopulateProperties(new ServiceContext<NodeService>(context.getService())).put(property, value);
		}
	}

	@Override
	public void unsetProperty(Node node, String property, ServiceContext<NodeService> context) {
		NodeModel rawNodeData = ((NodeModel) node.getRawNodeData());
		
		if (MindMapConstants.FREEPLANE_PERSISTENCE_NODE_TYPE_KEY.equals(property)) {
			throw new RuntimeException(String.format("Property with name %s shouldn't be un-set!", property));
		}
		
		// remove attribute from the attributes table
		NodeAttributeTableModel attributeTable = (NodeAttributeTableModel) rawNodeData.getExtension(NodeAttributeTableModel.class);	
		if (attributeTable != null) {
			for (Attribute attribute : attributeTable.getAttributes()) {
				if (attribute.getName().equals(property)) {
					attributeTable.getAttributes().remove(attribute);
					rawNodeData.getMap().setSaved(false);
					break;
				}
			}
		}
		
		// remove the property from the node instance too
		node.getOrPopulateProperties(new ServiceContext<NodeService>(context.getService())).remove(property);
	}
	
}
