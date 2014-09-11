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
package org.flowerplatform.core.file;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import org.flowerplatform.util.file.FileHolder;

/**
 * @author Mariana Gheorghe
 */
public class FileHolderAccessController implements IFileAccessController {

	/**
	 *@author see class
	 **/
	protected FileHolder getFileHolder(Object file) {
		return (FileHolder) file;
	}
	
	@Override
	public String getName(Object file) {
		return getFileHolder(file).getName();
	}

	@Override
	public String getPath(Object file) {
		return getFileHolder(file).getPath();
	}

	@Override
	public Object getFile(String path) throws Exception {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public File getFileAsFile(Object file) throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	public long getLastModifiedTimestamp(Object file) {
		return 0;
	}

	@Override
	public InputStream getContent(Object file) {
		return new ByteArrayInputStream(getFileHolder(file).getContent().getBytes());
	}

	@Override
	public void setContent(Object file, String content) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isDirectory(Object file) {
		return false;
	}

	@Override
	public Object getParentFile(Object file) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object getFile(Object parent, String name) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean createFile(Object file, boolean isDirectory) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean exists(Object file) {
		return getFileHolder(file).exists();
	}

	@Override
	public String getPathRelativeToFile(Object file, Object relativeTo) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getAbsolutePath(Object file) {
		return getFileHolder(file).getPath();
	}

	@Override
	public String getFileExtension(Object file) {
		String name = getFileHolder(file).getName();
		int index = name.lastIndexOf(".");
		if (index < 0) {
			return null;
		}
		return name.substring(index + 1);
	}

	@Override
	public boolean isFile(Object file) {
		if (file instanceof FileHolder) {
			return true;
		}
		return false;
	}

	@Override
	public Class getFileClass() {
		return FileHolder.class;
	}

	@Override
	public Object[] listFiles(Object folder) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void delete(Object child) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getParent(Object file) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean rename(Object file, Object dest) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String readFileToString(Object file) {
		return getFileHolder(file).getContent();
	}

	@Override
	public void writeStringToFile(Object file, String str) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean hasChildren(Object file) {
		return false;
	}

	@Override
	public long length(Object file) {
		return 0;
	}
}