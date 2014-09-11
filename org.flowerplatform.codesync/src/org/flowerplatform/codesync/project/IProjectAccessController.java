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
package org.flowerplatform.codesync.project;

/**
 * @author Mariana Gheorghe
 * @author Sebastian Solomon
 */
public interface IProjectAccessController {
	
	/**
	 * @param path relative to project
	 */
	Object getFile(Object project, String path);
	
	/**
	 *@author Mariana Gheorghe
	 **/
	Object getContainingProjectForFile(Object file);
	
	/**
	 *@author Mariana Gheorghe
	 **/
	String getPathRelativeToProject(Object file);
	
	//needed for synchronization. because in Eclipse we can't use project.findMember(path) (it return null if resource dosn't exist). We use instead .getFile/getFolder
	/**
	 *@author Mariana Gheorghe
	 **/
	Object getFolder(Object project, String path);
	
}