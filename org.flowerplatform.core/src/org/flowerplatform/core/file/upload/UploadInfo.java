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

	public boolean unzipFile() {
		return unzipFile;
	}

	public UploadInfo setUnzipFile(boolean unzipFile) {
		this.unzipFile = unzipFile;
		return this;
	}
	
	public long getTimestamp() {
		return timestamp;
	}

	public UploadInfo setTimestamp(long timestamp) {
		this.timestamp = timestamp;
		return this;
	}

	public String getSessionId() {
		return sessionId;
	}

	public UploadInfo setSessionId(String sessionId) {
		this.sessionId = sessionId;
		return this;
	}

	@Override
	public String toString() {
		return "UploadInfo [location=" + location + ", tmpLocation=" + tmpLocation + ", unzipFile=" + unzipFile + ", timestamp=" + timestamp + ", sessionId=" + sessionId + "]";
	}	
	
}
