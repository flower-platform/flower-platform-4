package org.flowerplatform.core.node.remote;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Cristian Spiescu
 */
public class Node {
	private String type;
	
	private String resource;
	
	private String id;
	
	private Map<String, Object> properties;

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

	public Map<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	public Map<String, Object> getOrCreateProperties() {
		if (properties == null) {
			properties = new HashMap<String, Object>();
		}
		return properties;
	}

	public Map<String, Object> getPopulatedProperties() {
		throw new UnsupportedOperationException();
	}

}
