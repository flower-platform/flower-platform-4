package org.flowerplatform.core.node.resource.in_memory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Mariana Gheorghe
 */
public class SessionInfo {

	private List<String> subscribedResourceUris = new ArrayList<String>();
	
	private Map<String, Object> properties = new HashMap<String, Object>();
	
	public List<String> getSubscribedResourceUris() {
		return subscribedResourceUris;
	}
	
	public Map<String, Object> getProperties() {
		return properties;
	}
	
}
