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
package org.flowerplatform.util.log;

import org.slf4j.MDC;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 * @author Cristi
 */
public class LogBasedAuditAppender implements IAuditAppender {

	private static Marker auditMarker = MarkerFactory.getMarker("AUDIT");
	
	/**
	 * @author Cristina Constantinescu
	 */
	protected void addEntriesInMDC(AuditDetails auditDetails) {
		if (auditDetails.getParam0() != null) {
			MDC.put("param0", auditDetails.getParam0().toString());
		}
		if (auditDetails.getParam1() != null) {
			MDC.put("param1", auditDetails.getParam1().toString());
		}
		if (auditDetails.getParam2() != null) {
			MDC.put("param2", auditDetails.getParam2().toString());
		}
	}
	
	/**
	 * @author Cristina Constantinescu
	 */
	protected void removeEntriesFromMDC(AuditDetails auditDetails) {
		if (auditDetails.getParam0() != null) {
			MDC.remove("param0");
		}
		if (auditDetails.getParam1() != null) {
			MDC.remove("param1");
		}
		if (auditDetails.getParam2() != null) {
			MDC.remove("param2");
		}
	}
	
	/**
	 * @author Cristina Constantinescu
	 */
	public void append(AuditDetails auditDetails) {
		if (auditDetails.getLogger().isInfoEnabled()) {
			addEntriesInMDC(auditDetails);
			auditDetails.getLogger().info(auditMarker, auditDetails.getAuditCategory());
			removeEntriesFromMDC(auditDetails);
		}		
	}

}