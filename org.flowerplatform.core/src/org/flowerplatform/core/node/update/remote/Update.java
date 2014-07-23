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
package org.flowerplatform.core.node.update.remote;

import java.util.Date;

/**
 * @author Cristina Constantinescu
 */
public class Update implements Comparable<Update> {

	private String type;
	
	private String fullNodeId;
	
	private long timestamp = new Date().getTime();
		
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFullNodeId() {
		return fullNodeId;
	}

	public void setFullNodeId(String fullNodeId) {
		this.fullNodeId = fullNodeId;
	}

	public Update setFullNodeIdAs(String fullNodeId) {
		this.fullNodeId = fullNodeId;
		return this;
	}
	
	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	@Override
	public int compareTo(Update o) {
		return Long.compare(getTimestamp(), o.getTimestamp());
	}

	@Override
	public String toString() {
		return "Update [node=" + fullNodeId + ", timestamp=" + timestamp + "]";
	}
		
}