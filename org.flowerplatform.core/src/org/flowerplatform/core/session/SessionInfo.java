package org.flowerplatform.core.session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Mariana Gheorghe
 */
public class SessionInfo {

	private Map<String, Object> properties = new HashMap<String, Object>();
	
	private List<String> resourceUris = new ArrayList<String>();

	public Map<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	public List<String> getResourceUris() {
		return resourceUris;
	}

	public void setResourceUris(List<String> resourceUris) {
		this.resourceUris = resourceUris;
	}
	
}
