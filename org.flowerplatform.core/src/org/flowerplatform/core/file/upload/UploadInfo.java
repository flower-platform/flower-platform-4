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
package org.flowerplatform.core.file.upload;

/**
 * Holds information regarding an upload request like:
 * <ul>
 * 	<li> location where to upload the file
 * 	<li> temporary location where to store the zip archive 
 * 		(is different from previous location if the user wants to upzip it first)
 * 	<li> if the archive must be unzipped before storing in selected location.
 * 	<li> the time when an upload request is registered.
 * </ul>
 * 
 * @author Cristina Constantinescu
 */
public class UploadInfo {

	private String location;	
	private String tmpLocation;	
	private boolean unzipFile;
	private long timestamp;
	private String sessionId;
	
	public String getLocation() {
		return location;
	}

	//CHECKSTYLE:OFF
	public UploadInfo setLocation(String location) {
		this.location = location;
		return this;
	}

	public String getTmpLocation() {
		return tmpLocation;
	}

	public UploadInfo setTmpLocation(String tmpLocation) {
		this.tmpLocation = tmpLocation;
		return this;
	}
	//CHECKSTYLE:ON

	/**
	 *@author Cristina Constantinescu
	 **/
	public boolean unzipFile() {
		return unzipFile;
	}
	
	//CHECKSTYLE:OFF
	public UploadInfo setUnzipFile(boolean unzipFile) {
		this.unzipFile = unzipFile;
		return this;
	}
	//CHECKSTYLE:ON
	
	public long getTimestamp() {
		return timestamp;
	}
	
	//CHECKSTYLE:OFF
	public UploadInfo setTimestamp(long timestamp) {
		this.timestamp = timestamp;
		return this;
	}
	//CHECKSTYLE:ON

	public String getSessionId() {
		return sessionId;
	}

	/**
	 *@author Cristina Constantinescu
	 **/
	public UploadInfo setSessionId(String sessionIdentifier) {
		this.sessionId = sessionIdentifier;
		return this;
	}

	@Override
	public String toString() {
		return "UploadInfo [location=" + location + ", tmpLocation=" + tmpLocation + ", unzipFile=" + unzipFile + ", timestamp=" + timestamp + ", sessionId=" + sessionId + "]";
	}	
	
}