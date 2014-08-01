package org.flowerplatform.team.git.remote;

/**
 *
 * @author Cristina Brinza
 *
 */

public class GitRef {

	private String name;
	private String type;
	
	public GitRef() {
		super();
	}
	
	public GitRef(String name, String type) {
		super();
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
