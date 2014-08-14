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
package org.flowerplatform.codesync.adapter.file;

/**
 * @author Mariana Gheorghe
 */
public class CodeSyncFile {

	private Object file;
	
	private Object fileInfo;
	
	/**
	 *@author see class
	 */
	public CodeSyncFile(Object file) {
		this.file = file;
	}

	public Object getFile() {
		return file;
	}

	public Object getFileInfo() {
		return fileInfo;
	}

	public void setFileInfo(Object fileInfo) {
		this.fileInfo = fileInfo;
	}

	@Override
	public String toString() {
		return file.toString();
	}
	
}