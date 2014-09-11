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
package org.flowerplatform.codesync.sdiff;

/**
 * A FileContent object encapsulates the content of a file from an old git
 * revision as well as the content of the same file from a new git revision.
 * 
 * @author Valentina-Camelia Bojan
 */
public class FileContent {

	private String newContent;

	private String oldContent;

	/**
	 *@author see class
	 **/
	public FileContent(String oldContent, String newContent) {
		this.oldContent = oldContent;
		this.newContent = newContent;
	}

	public String getNewContent() {
		return newContent;
	}

	public String getOldContent() {
		return oldContent;
	}

}