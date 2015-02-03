/* license-start
 * 
 * Copyright (C) 2008 - 2014 Crispico Software, <http://www.crispico.com/>.
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
