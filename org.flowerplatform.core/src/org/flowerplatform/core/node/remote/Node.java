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
		
	public Node(String nodeUri) {
		setNodeUri(nodeUri);
	}
	
	public Node(String nodeUri, String type) {
		this(nodeUri);
		setType(type);
	}
	
	public Node(String scheme, String ssp, String fragment) {
		setNodeUri(Utils.getUri(scheme, ssp, fragment));
	}

	public Node(String scheme, String ssp, String fragment, String type) {
		this(scheme, ssp, fragment);
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
	
	public String getFragment() {
		return Utils.getFragment(nodeUri);
	}

	public String getSchemeSpecificPart() {
		return Utils.getSchemeSpecificPart(nodeUri);
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
	 * @return The properties map (populated if not already populated).
	 */
	public Map<String, Object> getOrPopulateProperties() {
		if (!propertiesPopulated) {	
			// lazy population
			CorePlugin.getInstance().getNodeService().populateNodeProperties(this, new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService()));
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
	
	/**
	 * @author Sebastian Solomon
	 */
	public Object getPropertyValue(String property) {
		if (properties.containsKey(property)) {
			return properties.get(property);
		} else {
			return CorePlugin.getInstance().getNodeService().getDefaultPropertyValue(this, property, new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService()));
		}
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
