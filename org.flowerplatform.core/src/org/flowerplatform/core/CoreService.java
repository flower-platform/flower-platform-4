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
package org.flowerplatform.core;

import java.io.File;

import org.flowerplatform.core.file.FileControllerUtils;

/**
 * @author Cristina Constantinescu
 */
public class CoreService {

	public String[] getVersions() {
		return new String[] {CoreConstants.APP_VERSION, CoreConstants.API_VERSION};
	}
	
	/**
	 * @author Alina Bratu
	 * @return a string containing image names from user workspace, separated by semicolons
	 * @throws Exception 
	 */
	
	public String getCustomIconsPaths(String repo) throws Exception {
		String results = "";
		File[] files = FileControllerUtils.getFileAccessController().getFileAsFile(FileControllerUtils.getFileAccessController().getFile(repo + "/mindmap_icons/")).listFiles();
		for (File file : files) {
		    if (file.isFile() && file.getName().endsWith(".png")) {
		        results += file.getName().substring(0,file.getName().lastIndexOf('.')) + ";";
		    }
		}
		results = results.substring(0, results.lastIndexOf(';'));
		return results;
	}
}