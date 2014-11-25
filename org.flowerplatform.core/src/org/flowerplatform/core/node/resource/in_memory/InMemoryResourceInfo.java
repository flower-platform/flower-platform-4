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
package org.flowerplatform.core.node.resource.in_memory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mariana Gheorghe
 */
public class InMemoryResourceInfo {

	private Object resourceData;

	private List<String> sessionIds = new ArrayList<String>();

	private long lastPing;

	private String configDirsKey;

	public Object getResourceData() {
		return resourceData;
	}

	public void setResourceData(Object resourceData) {
		this.resourceData = resourceData;
	}

	public List<String> getSessionIds() {
		return sessionIds;
	}

	public void setSessionIds(List<String> sessionIds) {
		this.sessionIds = sessionIds;
	}

	public long getLastPing() {
		return lastPing;
	}

	public void setLastPing(long lastPing) {
		this.lastPing = lastPing;
	}

	public String getConfigDirsKey() {
		return configDirsKey;
	}

	public void setConfigDirsKey(String configDirsKey) {
		this.configDirsKey = configDirsKey;
	}

}