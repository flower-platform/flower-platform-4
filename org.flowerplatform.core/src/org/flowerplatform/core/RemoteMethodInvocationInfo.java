package org.flowerplatform.core;

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
}
