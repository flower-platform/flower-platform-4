package org.flowerplatform.core.node.remote;

import static org.flowerplatform.core.node.controller.PropertiesProvider.PROPERTIES_PROVIDER;
import static org.flowerplatform.core.node.controller.RawNodeDataProvider.RAW_NODE_DATA_PROVIDER;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.controller.PropertiesProvider;
import org.flowerplatform.core.node.controller.RawNodeDataProvider;
import org.flowerplatform.util.controller.TypeDescriptor;

/**
 * @author Cristian Spiescu
 * @author Cristina Constantinescu
 */
public class Node {
	
	private String type;
	
	private String resource;
	
	private String id;
		
	private Map<String, Object> properties;
	private boolean populated;

	private Object rawNodeData;
	private boolean rawNodeDataRetrieved;
	
	public Node() {
	}
	
	public Node(String type, String resource, String id, Object rawNodeData) {
		this.id = id;
		this.type = type;
		this.resource = resource;
		
		setRawNodeData(rawNodeData);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Should be used for writing values in the map. Probably by {@link PropertiesProvider}.
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
	 * <strong>WARNING:</strong> shouldn't be used for writing values. E.g. if {@link PropertiesProvider}'s try
	 * to use this method, an infinite call loop will be created ({@link StackOverflowError}).
	 * 
	 * @return The properties map (populated if not already populated).
	 */
	public Map<String, Object> getOrPopulateProperties() {
		if (!populated) {	
			// lazy population
			TypeDescriptor descriptor = CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getExpectedTypeDescriptor(type);
			if (descriptor == null) {
				return null;
			}
			
			List<PropertiesProvider> providers = descriptor.getAdditiveControllers(PROPERTIES_PROVIDER, this);
			for (PropertiesProvider provider : providers) {
				provider.populateWithProperties(this);
			}
			populated = true;
		}
		return getProperties();
	}
			
	public Object getOrRetrieveRawNodeData() {
		if (!rawNodeDataRetrieved) {
			// lazy initialization
			TypeDescriptor descriptor = CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getExpectedTypeDescriptor(type);
			if (descriptor == null) {
				return null; 
			}		
			RawNodeDataProvider<Object> rawNodeDataProvider = descriptor.getSingleController(RAW_NODE_DATA_PROVIDER, this);	
			setRawNodeData(rawNodeDataProvider.getRawNodeData(this));			
		}
		return rawNodeData;
	}

	private void setRawNodeData(Object rawNodeData) {
		this.rawNodeData = rawNodeData;
		rawNodeDataRetrieved = true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((resource == null) ? 0 : resource.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (resource == null) {
			if (other.resource != null)
				return false;
		} else if (!resource.equals(other.resource))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("Node [body = %s, type = %s, resource = %s]", getProperties().get("body"), type, resource);
	}
	
}
