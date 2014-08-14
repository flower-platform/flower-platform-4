package org.flowerplatform.team.git.remote;

/**
 *
 * @author Cristina Brinza
 *
 */
public class GitRef implements Comparable<GitRef> {

	private String name;
	
	private String fullName;
	
	private String type;
	
	public GitRef() {
		super();
	}
	
	public GitRef(String name, String type) {
		this();
		this.name = name;
		this.type = type;
	}

	public GitRef(String name, String type, String fullName) {
		this(name, type);		
		this.fullName = fullName;
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

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@Override
	public int compareTo(GitRef o) {	
		if (this == o) {
			return 0;
		}
		return this.name.compareTo(o.name);
	}
	
}
