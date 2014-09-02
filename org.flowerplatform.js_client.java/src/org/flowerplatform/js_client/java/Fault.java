package org.flowerplatform.js_client.java;

import javax.ws.rs.core.Response.StatusType;

public class Fault {

	private StatusType status;
	
	public Fault(StatusType status) {
		this.status = status;
	}
	
	public int faultCode() {
		return status.getStatusCode();
	}
	
	public String faultString() {
		return status.getReasonPhrase();
	}
	
	public String faultContent() {
		return "";
	}

	@Override
	public String toString() {
		return "Fault [faultCode()=" + faultCode() + ", faultString()="
				+ faultString() + ", faultContent()=" + faultContent() + "]";
	}
	
}
