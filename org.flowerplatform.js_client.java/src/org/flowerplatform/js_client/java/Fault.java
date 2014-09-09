package org.flowerplatform.js_client.java;

import javax.ws.rs.core.Response.StatusType;

/**
 * Used as parameter in a faultCallback handler.
 * 
 * <p>
 * Corresponds to <code>mx.rpc.Fault</code>.
 * 
 * @author Cristina Constantinescu
 */
//TODO CC: this class can be improved to show exception message too
// TODO CS: pai sa facem asta; are cred legatura cu o intrebare despre semnatura lui fault de prin AS
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
