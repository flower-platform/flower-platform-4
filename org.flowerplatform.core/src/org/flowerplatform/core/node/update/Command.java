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
	
	private String lastUpdateId;

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

	public String getLastUpdateId() {
		return lastUpdateId;
	}
	
	public void setLastUpdateId(String lastUpdateId) {
		this.lastUpdateId = lastUpdateId;
	}

	@Override
	public boolean equals(Object command) {
		if (command == null || !(command instanceof Command)) {
			return false;
		}
		return id.equals(((Command) command).getId());
	}
	
	
}
