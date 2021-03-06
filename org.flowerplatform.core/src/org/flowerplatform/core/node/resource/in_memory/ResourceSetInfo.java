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
