package org.flowerplatform.core.node.resource.in_memory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Mariana Gheorghe
 */
public class SessionInfo {

	private List<String> subscribedResourceNodeIds = new ArrayList<String>();
	
	private Map<String, Object> properties = new HashMap<String, Object>();
	
	public List<String> getSubscribedResourceNodeIds() {
		return subscribedResourceNodeIds;
	}
	
	public void setSubscribedResourceNodeIds(List<String> subscribedResourceNodeIds) {
		this.subscribedResourceNodeIds = subscribedResourceNodeIds;
	}
	
	public Map<String, Object> getProperties() {
		return properties;
	}
	
	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}
	
}
