package org.flowerplatform.core.node.remote;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.PropertiesProvider;
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
	
	public static final String FULL_NODE_ID_SEPARATOR = "|";

	private static final Pattern FULL_NODE_ID_PATTERN = Pattern.compile("\\((.*?)\\|(\\(?.*\\)?)\\|(.*)\\)");
	
	private String type;
	
	private String resource;
	
	private String idWithinResource;
	
	private String cachedFullNodeId;
	
	private Map<String, Object> properties;
	private boolean propertiesPopulated;

	private Object rawNodeData;
	private boolean rawNodeDataRetrieved;
		
	public Node(String type, String resource, String idWithinResource, Object rawNodeData) {		
		this.type = type;
		this.resource = resource;
		this.idWithinResource = idWithinResource;
		
		if (rawNodeData != null) {
			setRawNodeData(rawNodeData);
		}
	}

	public Node(String fullNodeId) {
		if (StringUtils.countMatches(fullNodeId, FULL_NODE_ID_SEPARATOR) < 2) { 
			throw new RuntimeException("fullNodeId must have the following format: <type>|<resource>|<id>! Received " + fullNodeId);
		}
		
		Matcher matcher = FULL_NODE_ID_PATTERN.matcher(fullNodeId);
		if (matcher.find()) {
			type = matcher.group(1);
			resource = matcher.group(2).isEmpty() ? null : matcher.group(2);
			idWithinResource = matcher.group(3).isEmpty() ? null : matcher.group(3);
			cachedFullNodeId = fullNodeId;
		}
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
		cachedFullNodeId = null;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
		cachedFullNodeId = null;
	}
	
	public String getIdWithinResource() {
		return idWithinResource;
	}

	public void setIdWithinResource(String idWithinResource) {
		this.idWithinResource = idWithinResource;
		cachedFullNodeId = null;
	}

	public String getFullNodeId() {
		if (cachedFullNodeId == null) {
			cachedFullNodeId = "(" + Utils.defaultIfNull(type) + FULL_NODE_ID_SEPARATOR + Utils.defaultIfNull(resource) + FULL_NODE_ID_SEPARATOR + Utils.defaultIfNull(idWithinResource) + ")";
		}
		return cachedFullNodeId;
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
		if (!propertiesPopulated) {	
			// lazy population
			CorePlugin.getInstance().getNodeService().populateNodeProperties(this);
			propertiesPopulated = true;
		}
		return getProperties();
	}
			
	public Object getOrRetrieveRawNodeData() {
		if (!rawNodeDataRetrieved) {
			// lazy initialization
			setRawNodeData(CorePlugin.getInstance().getNodeService().getRawNodeData(this));		
		}
		return rawNodeData;
	}

	private void setRawNodeData(Object rawNodeData) {
		this.rawNodeData = rawNodeData;
		rawNodeDataRetrieved = true;
	}
	
	@Override
	public int hashCode() {
		return getFullNodeId().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Node) {
			return getFullNodeId().equals(((Node) obj).getFullNodeId());
		}
		return false;
	}

	@Override
	public String toString() {
		return String.format("Node [fullNodeId = %s]", getFullNodeId());
	}

}
