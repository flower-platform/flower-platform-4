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
	
	public Node() {
			}
	
	/**
	 * @author Sebastian Solomon
	 */
	public Node(String type, String resource, String id) {
		this.type = type;
		this.resource = resource;
		this.id = id;
	}
	
	/**
	 * @author Sebastian Solomon
	 */
	public Node(String type, String resource, String id, Map<String, Object> properties) {
		this(type, resource, id);
		this.properties = properties;
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

	@Override
	public String toString() {
		return String.format("Node [body = %s, type = %s]", 
				getOrCreateProperties().get("body"), type);
	}
	
	/**
	 * @author Sebastian Solomon
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Node)) {
			return false;
		}

		Node node = (Node) obj;

		if (this.id == null) {
			if (node.id != null) {
				return false;
			}
		} else {
			if (!this.id.equals(node.id))
				return false;
		}

		if (this.properties == null) {
			if (node.properties != null) {
				return false;
			}
		} else {
			if (!this.properties.equals(node.properties))
				return false;
		}

		if (this.type == null) {
			if (node.type != null) {
				return false;
			}
		} else {
			if (!this.type.equals(node.type))
				return false;
		}

		return true;
	}

	/**
	 * @author Sebastian Solomon
	 */
	@Override
	public int hashCode() {
		return (id == null ? 0 : id.hashCode())
				+ (type == null ? 0 : type.hashCode())
				+ (properties == null ? 0 : properties.hashCode()) / 3;
	}

}
