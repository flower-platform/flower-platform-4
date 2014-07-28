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

import java.io.File;
import java.io.InputStream;

/**
 * @author Cristina Constantinescu
 * @author Sebastian Solomon
 */
public interface IFileAccessController {

	String getName(Object file);
	
	// get relative path to a location
	String getPath(Object file);
	
	// path is relative
	Object getFile(String path) throws Exception;
	
	File getFileAsFile(Object file) throws Exception;
	
	long getLastModifiedTimestamp(Object file);
	
	InputStream getContent(Object file);
	
	void setContent(Object file, String content);

	boolean isDirectory(Object file);
	
	Object getParentFile(Object file);
	
	Object getFile(Object parent, String name);
	
	boolean createFile(Object file, boolean isDirectory);
	
	boolean exists(Object file);
	
	String getPathRelativeToFile(Object file, Object relativeTo);
	
	String getAbsolutePath (Object file);
	
	String getFileExtension (Object file);
	
	boolean isFile(Object file);
	
	Class getFileClass();
	
	Object[] listFiles(Object folder);

	void delete(Object child);
	
	String getParent(Object file);
	
	boolean rename(Object file, Object dest);
	
	String readFileToString(Object file);
	
	void writeStringToFile(Object file, String str);
	
	boolean hasChildren(Object file);
	
	long length(Object file);
}