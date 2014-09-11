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

/**
 * @author Cristi
 */
public class DualDelegatingAuditAppender implements IAuditAppender {

	protected IAuditAppender appender1;
	
	protected IAuditAppender appender2;
	/**
	 *@author Cristina Constantinescu
	 **/
	public DualDelegatingAuditAppender(IAuditAppender appender1,
			IAuditAppender appender2) {
		super();
		this.appender1 = appender1;
		this.appender2 = appender2;
	}
	/**
	 *@author Cristina Constantinescu
	 **/
	public void append(AuditDetails auditDetails) {
		if (appender1 != null) {
			appender1.append(auditDetails);
		}
		if (appender2 != null) {
			appender2.append(auditDetails);
		}
	}

}