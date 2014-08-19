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
package org.flowerplatform.core.file;

import java.io.File;
import java.io.InputStream;

/**
 * @author Cristina Constantinescu
 * @author Sebastian Solomon
 */
public interface IFileAccessController {

	/**
	 * @author Mariana Gheorghe
	 */
	String getName(Object file);
	
	// get relative path to a location
	/**
	 * @author Valentina Bojan
	 */
	String getPath(Object file);
	
	// path is relative
	/**
	 * @author Valentina Bojan
	 */
	Object getFile(String path) throws Exception;
	
	/**
	 * @author Valentina Bojan
	 */
	File getFileAsFile(Object file) throws Exception;
	
	/**
	 * @author Mariana Gheorghe
	 */
	long getLastModifiedTimestamp(Object file);
	
	/**
	 * @author Mariana Gheorghe
	 */
	InputStream getContent(Object file);
	
	/**
	 * @author Mariana Gheorghe
	 */
	void setContent(Object file, String content);

	/**
	 * @author Mariana Gheorghe
	 */
	boolean isDirectory(Object file);
	
	/**
	 * @author Mariana Gheorghe
	 */
	Object getParentFile(Object file);
	
	/**
	 * @author Mariana Gheorghe
	 */
	Object getFile(Object parent, String name);
	
	/**
	 * @author see class
	 */
	boolean createFile(Object file, boolean isDirectory);
	
	/**
	 * @author Mariana Gheorghe
	 */
	boolean exists(Object file);
	
	/**
	 * @author Mariana Gheorghe
	 */
	String getPathRelativeToFile(Object file, Object relativeTo);
	
	/**
	 * @author Mariana Gheorghe
	 */
	String getAbsolutePath(Object file);
	
	/**
	 * @author Mariana Gheorghe
	 */
	String getFileExtension(Object file);
	
	/**
	 * @author Mariana Gheorghe
	 */
	boolean isFile(Object file);
	
	/**
	 * @author Mariana Gheorghe
	 */
	Class getFileClass();
	
	/**
	 * @author Mariana Gheorghe
	 */
	Object[] listFiles(Object folder);

	/**
	 * @author Solomon Sebastian
	 */
	void delete(Object child);
	
	/**
	 * @author Mariana Gherghe
	 */
	String getParent(Object file);
	
	/**
	 * @author Solomon Sebastian
	 */
	boolean rename(Object file, Object dest);
	
	/**
	 * @author Mariana Gheorghe
	 */
	String readFileToString(Object file);
	
	/**
	 * @author Mariana Gheorghe
	 */
	void writeStringToFile(Object file, String str);
	
	/**
	 * @author Solomon Sebastian
	 */
	boolean hasChildren(Object file);
	
	/**
	 * @author Solomon Sebastian
	 */
	long length(Object file);
}