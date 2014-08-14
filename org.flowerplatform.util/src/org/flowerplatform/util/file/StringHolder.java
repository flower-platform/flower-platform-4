/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
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
package org.flowerplatform.util.file;

/**
 * @author Mariana Gheorghe
 */
public class StringHolder extends FileHolder {

	private String content;
	
	private String path;
	
	/**
	 * @author see class
	 */
	public StringHolder(String path, String content) {
		this.path = path;
		this.content = content;
	}
	
	@Override
	public boolean exists() {
		return content != null;
	}

	@Override
	public String getPath() {
		return path;
	}
	
	@Override
	public String getName() {
		int index = path.lastIndexOf("/");
		if (index < 0) {
			return path;
		}
		return path.substring(index + 1);
	}

	@Override
	public String getContent() {
		return content;
	}

}