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
	
	private String idWithinResource;
	
	private String fullNodeId;
	
	private Map<String, Object> properties;
	private boolean populated;

	private Object rawNodeData;
	private boolean rawNodeDataRetrieved;
		
	public Node(String type, String resource, String idWithinResource, Object rawNodeData) {		
		this.type = type;
		this.resource = resource;
		this.idWithinResource = idWithinResource;
		
		calculateFullNodeId();
		setRawNodeData(rawNodeData);
	}

	public Node(String fullNodeId) {
		String[] tokens = fullNodeId.split("\\|");
						
		this.type = tokens[0];
		this.resource = tokens[1];
		
		if (tokens.length == 3) {
			this.idWithinResource = tokens[2];
		}
		this.fullNodeId = fullNodeId;
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
	
	public String getIdWithinResource() {
		return idWithinResource;
	}

	public void setIdWithinResource(String idWithinResource) {
		this.idWithinResource = idWithinResource;
		calculateFullNodeId();
	}

	public String getFullNodeId() {
		return fullNodeId;
	}

	private void calculateFullNodeId() {
		this.fullNodeId = String.format("%s|%s|%s", this.idWithinResource, this.type, this.resource);
	}
	
	public Map<String, Object> getProperties() {
		if (properties == null) {
			properties = new HashMap<String, Object>();
		}
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

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
		result = prime * result	+ ((fullNodeId == null) ? 0 : fullNodeId.hashCode());
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
		if (fullNodeId == null) {
			if (other.fullNodeId != null)
				return false;
		} else if (!fullNodeId.equals(other.fullNodeId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("Node [fullNodeId = %s]", fullNodeId);
	}
	
}
