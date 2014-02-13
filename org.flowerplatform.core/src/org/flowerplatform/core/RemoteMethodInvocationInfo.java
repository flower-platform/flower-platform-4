package org.flowerplatform.core;

import java.util.List;

/**
 * @author Sebastian Solomon
 */
public class RemoteMethodInvocationInfo {
	String serviceId;
	
	String methodName;
	
	List<?> parameters;
	
	long startTimestamp;

	public RemoteMethodInvocationInfo(String serviceId, String methodName,
			List<?> parameters) {
		super();
		this.serviceId = serviceId;
		this.methodName = methodName;
		this.parameters = parameters;
	}
	
	public RemoteMethodInvocationInfo(String serviceId, String methodName,
			List<?> parameters, long timestamp) {
		this(serviceId, methodName, parameters);
		this.startTimestamp = timestamp;
		
	}

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
}
