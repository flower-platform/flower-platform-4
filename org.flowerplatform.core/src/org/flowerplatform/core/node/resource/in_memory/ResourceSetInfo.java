package org.flowerplatform.core.node.resource.in_memory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.flowerplatform.core.node.update.Command;
import org.flowerplatform.core.node.update.remote.Update;

/**
 * @author Mariana Gheorghe
 */
public class ResourceSetInfo {

	private List<String> resourceUris = new ArrayList<String>();
	
	private long loadedTimestamp = new Date().getTime();
	
	private List<Update> updates = new ArrayList<Update>();
	
	/**
	 * @author Claudiu Matei
	 */
	private List<Command> commands = new ArrayList<Command>();
	
	/**
	 * @author Claudiu Matei
	 */
	private String commandToUndoId; 

	/**
	 * @author Claudiu Matei
	 */
	private String commandToRedoId; 
	
	public List<String> getResourceUris() {
		return resourceUris;
	}

	public List<Update> getUpdates() {
		return updates;
	}

	public long getLoadedTimestamp() {
		return loadedTimestamp;
	}

	public void setLoadedTimestamp(long loadedTimestamp) {
		this.loadedTimestamp = loadedTimestamp;
	}

	public List<Command> getCommandStack() {
		return commands;
	}

	public String getCommandToUndoId() {
		return commandToUndoId;
	}

	public void setCommandToUndoId(String commandToUndoId) {
		this.commandToUndoId = commandToUndoId;
	}
	
	public String getCommandToRedoId() {
		return commandToRedoId;
	}

	public void setCommandToRedoId(String commandToRedoId) {
		this.commandToRedoId = commandToRedoId;
	}
	
}
