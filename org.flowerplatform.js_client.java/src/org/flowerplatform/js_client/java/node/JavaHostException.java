package org.flowerplatform.js_client.java.node;

import javax.ws.rs.core.Response.StatusType;

/**
 * Used as parameter in a faultCallback handler.
 * 
 * <p>
 * Corresponds to <code>mx.rpc.Fault</code>.
 * 
 * @author Cristina Constantinescu
 */
public class JavaHostException {
	
	private StatusType status;
	
	private String message;
	
	public JavaHostException(String message, StatusType status) {
		this.status = status;
		this.message = message;
	}
	
	public int faultCode() {
		return status.getStatusCode();
	}
	
	public String faultString() {
		return status.getReasonPhrase();
	}
	
	public String faultContent() {
		return message;
	}

	@Override
	public String toString() {
		return "Fault [faultCode()=" + faultCode() + ", faultString()=" + faultString() + ", faultContent()=" + faultContent() + "]";
	}
	
}
