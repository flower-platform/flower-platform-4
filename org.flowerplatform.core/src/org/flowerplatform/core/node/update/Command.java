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
package org.flowerplatform.core.node.update;


/**
 * @author Claudiu Matei
 *
 */
public class Command {

	private String id;
	
	private String resourceSet;
	
	private String title;
	
	private String lastUpdateIdBeforeCommandExecution;
	
	private String lastUpdateIdAfterCommandExecution;

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public String getResourceSet() {
		return resourceSet;
	}

	public void setResourceSet(String resourceSet) {
		this.resourceSet = resourceSet;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getLastUpdateIdBeforeCommandExecution() {
		return lastUpdateIdBeforeCommandExecution;
	}

	public void setLastUpdateIdBeforeCommandExecution(String lastUpdateIdBeforeCommandExecution) {
		this.lastUpdateIdBeforeCommandExecution = lastUpdateIdBeforeCommandExecution;
	}

	public String getLastUpdateIdAfterCommandExecution() {
		return lastUpdateIdAfterCommandExecution;
	}
	
	public void setLastUpdateIdAfterCommandExecution(String lastUpdateId) {
		this.lastUpdateIdAfterCommandExecution = lastUpdateId;
	}

	@Override
	public boolean equals(Object command) {
		if (command == null || !(command instanceof Command)) {
			return false;
		}
		return id.equals(((Command) command).getId());
	}
	
	
}
