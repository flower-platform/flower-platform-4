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
	
	/**
	 *@author see class
	 **/
	public GitRef() {
		super();
	}
	
	/**
	 *@author see class
	 **/
	public GitRef(String name, String type) {
		this();
		this.name = name;
		this.type = type;
	}

	/**
	 *@author see class
	 **/
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

