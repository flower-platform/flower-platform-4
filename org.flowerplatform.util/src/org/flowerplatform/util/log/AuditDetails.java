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
package org.flowerplatform.util.log;

import org.slf4j.Logger;

/**
 * @author Mariana
 * @author Cristi
 */
public class AuditDetails {
	
	private Logger logger;
	private long time;
	private long duration;
	private String auditCategory;
	private Object param0;
	private Object param1;
	private Object param2;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public AuditDetails(Logger logger, String auditCategory, Object... params) {
		time = System.currentTimeMillis();
		this.auditCategory = auditCategory;
		this.logger = logger;
		if (params.length > 3) {
			throw new IllegalArgumentException("Too much parameters; max is 3");
		} 
		
		if (params.length > 2) {
			param2 = params[2];
		}
		if (params.length > 1) {
			param1 = params[1];
		}
		if (params.length > 0) {
			param0 = params[0];
		}
	}

	/**
	 * @author Cristina Constantinescu
	 */
	public void measureDuration() {
		duration = System.currentTimeMillis() - time;
	}
	
	public Object getParam0() {
		return param0;
	}

	public Object getParam1() {
		return param1;
	}

	public Object getParam2() {
		return param2;
	}

	public long getTime() {
		return time;
	}

	public long getDuration() {
		return duration;
	}
	
	public String getAuditCategory() {
		return auditCategory;
	}

	public Logger getLogger() {
		return logger;
	}
}