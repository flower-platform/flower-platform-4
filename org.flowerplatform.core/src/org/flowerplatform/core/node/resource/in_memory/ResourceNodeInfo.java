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
package org.flowerplatform.core.node.resource.in_memory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.flowerplatform.core.node.update.remote.Update;

/**
 * @author Cristina Constantinescu
 * @author Mariana Gheorghe
 */
public class ResourceNodeInfo {

	private Object rawResourceData;
	
	private String resourceCategory;
	
	private long loadedTimestamp;
	
	private List<String> sessions = new ArrayList<String>();
	
	private List<Update> updates = new ArrayList<Update>();
	
	private long updateRequestedTimestamp;

	public Object getRawResourceData() {
		return rawResourceData;
	}
	
	public void setRawResourceData(Object rawResourceData) {
		this.rawResourceData = rawResourceData;
		loadedTimestamp = new Date().getTime();
		updates.clear();
	}
	
	public String getResourceCategory() {
		return resourceCategory;
	}

	public void setResourceCategory(String resourceCategory) {
		this.resourceCategory = resourceCategory;
	}
	
	public long getLoadedTimestamp() {
		return loadedTimestamp;
	}

	public List<String> getSessions() {
		return sessions;
	}
	
	public List<Update> getUpdates() {
		return updates;
	}
	
	public long getUpdateRequestedTimestamp() {
		return updateRequestedTimestamp;
	}
	
	public void setUpdateRequestedTimestamp(long updateRequestedTimestamp) {
		this.updateRequestedTimestamp = updateRequestedTimestamp;
	}
	
}