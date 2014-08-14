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
package org.flowerplatform.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Sebastian Solomon
 */
public class RemoteMethodInvocationInfo {
	private String serviceId;

	private String methodName;

	private List<?> parameters;

	private long startTimestamp;

	private Object returnValue;
	
	private Map<String, Object> enrichedReturnValue = new HashMap<String, Object>();
	
	/**
	 * @author Cristina Constantinescu
	 */
	private Map<?, ?> headers;
	
	
	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public List<?> getParameters() {
		return parameters;
	}

	public void setParameters(List<?> parameters) {
		this.parameters = parameters;
	}

	public long getStartTimestamp() {
		return startTimestamp;
	}

	public void setStartTimestamp(long startTimestamp) {
		this.startTimestamp = startTimestamp;
	}

	public Object getReturnValue() {
		return returnValue;
	}

	public void setReturnValue(Object returnValue) {
		this.returnValue = returnValue;
	}

	/**
	 * @author Cristina Constantinescu
	 */
	public Map<?, ?> getHeaders() {
		return headers;
	}

	/**
	 * @author Cristina Constantinescu
	 */
	public void setHeaders(Map<?, ?> headers) {
		this.headers = headers;
	}

	/**
	 * A map that contains the {@link #returnValue} from the invoked method, 
	 * as well as extra info set by the {@link RemoteMethodInvocationListener}.
	 * 
	 * @author Mariana Gheorghe
	 */
	public Map<String, Object> getEnrichedReturnValue() {
		return enrichedReturnValue;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getResourceSets() {
		return (List<String>) getHeaders().get(CoreConstants.RESOURCE_SETS);
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getResourceUris() {
		return (List<String>) getHeaders().get(CoreConstants.RESOURCE_URIS);
	}

	public long getTimestampOfLastRequest() {
		Object timestamp = getHeaders().get(CoreConstants.LAST_UPDATE_TIMESTAMP);
		if (timestamp == null) {
			return 0;
		}
		return ((Number) timestamp).longValue();
	}
}
