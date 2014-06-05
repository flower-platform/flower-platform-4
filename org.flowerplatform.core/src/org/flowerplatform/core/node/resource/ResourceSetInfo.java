package org.flowerplatform.core.node.resource;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.core.node.update.remote.Update;

/**
 * @author Mariana Gheorghe
 */
public class ResourceSetInfo {

	private List<String> resourceUris = new ArrayList<String>();
	
	private long loadedTimestamp;
	
	private List<Update> updates = new ArrayList<Update>();
	
	private List<Object> commands;

	public List<String> getResourceUris() {
		return resourceUris;
	}

	public List<Update> getUpdates() {
		return updates;
	}

	public List<Object> getCommands() {
		return commands;
	}

	public long getLoadedTimestamp() {
		return loadedTimestamp;
	}
	
}
