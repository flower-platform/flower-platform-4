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
package org.flowerplatform.core.node.remote;

import java.util.HashMap;
import java.util.Map;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IPropertiesProvider;
import org.flowerplatform.util.Utils;

/**
 * <p>
 * This is a remote class (transferable to client). But only server -> client.
 * 
 * @see NodeService
 * @author Cristian Spiescu
 * @author Cristina Constantinescu
 */
public class Node {
	
	private String type;
	
	private String nodeUri;
	
	private Map<String, Object> properties;
	
	private boolean propertiesPopulated;

	private Object rawNodeData;
		
	public Node(String nodeUri, String type) {
		setNodeUri(nodeUri);
		setType(type);
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNodeUri() {
		return nodeUri;
	}
	
	public void setNodeUri(String nodeUri) {
		this.nodeUri = nodeUri;
	}
	
	public String getScheme() {
		return Utils.getScheme(nodeUri);
	}
	
	/**
	 * Should be used for writing values in the map. Probably by {@link IPropertiesProvider}.
	 * 
	 * @return The properties map (lazy initialized in here), without any other processing.
	 */
	public Map<String, Object> getProperties() {
		if (properties == null) {
			properties = new HashMap<String, Object>();
		}
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	/**
	 * Populates the node (if not populated). Should be used for reading values from the map.
	 * 
	 * <p>
	 * <strong>WARNING:</strong> shouldn't be used for writing values. E.g. if {@link IPropertiesProvider}'s try
	 * to use this method, an infinite call loop will be created ({@link StackOverflowError}).
	 * 
	 * <p>
	 * Note: used {@link #getPropertyValue(String)} when necessary.
	 * 
	 * @return The properties map (populated if not already populated).
	 */
	public Map<String, Object> getOrPopulateProperties(ServiceContext<NodeService> context) {
		if (!propertiesPopulated) {	
			// lazy population
			CorePlugin.getInstance().getNodeService().populateNodeProperties(this, context);
			propertiesPopulated = true;
		}
		return getProperties();
	}
	
	public Object getRawNodeData() {
		return rawNodeData;
	}
	
	public void setRawNodeData(Object rawNodeData) {
		this.rawNodeData = rawNodeData;
	}
		
	public Object getPropertyValue(String property) {
		Object propertyObj = getPropertyValueOrWrapper(property);
		if (propertyObj instanceof PropertyWrapper) {
			return ((PropertyWrapper) propertyObj).getValue();
		}
		return propertyObj;
	}
	
	public Object getPropertyValueOrWrapper(String property) {
		ServiceContext<NodeService> context = new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService());
		if (!getOrPopulateProperties(context).containsKey(property)) {
			return CorePlugin.getInstance().getNodeService().getDefaultPropertyValue(this, property, new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService()));
		}
		return getOrPopulateProperties(context).get(property);
	}
	
	@Override
	public int hashCode() {
		return getNodeUri().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Node) {
			return getNodeUri().equals(((Node) obj).getNodeUri());
		}
		return false;
	}
	
	@Override
	public String toString() {
		return String.format("Node [fullNodeId = %s]", getNodeUri());
	}

}
