package org.flowerplatform.core.node.update;

import java.util.UUID;

/**
 * @author Claudiu Matei
 *
 */
public class Command {

	private String id=UUID.randomUUID().toString();
	
	private String title;
	
	private long firstUpdateTimestamp, lastUpdateTimestamp;

	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getFirstUpdateTimestamp() {
		return firstUpdateTimestamp;
	}

	public void setFirstUpdateTimestamp(long firstUpdateTimestamp) {
		this.firstUpdateTimestamp = firstUpdateTimestamp;
	}

	public long getLastUpdateTimestamp() {
		return lastUpdateTimestamp;
	}

	public void setLastUpdateTimestamp(long lastUpdateTimestamp) {
		this.lastUpdateTimestamp = lastUpdateTimestamp;
	}

	
}
