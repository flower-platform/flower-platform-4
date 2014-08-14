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
package org.flowerplatform.ant;

import java.io.File;
import java.io.FileFilter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.flowerplatform.ant.utils.FileIterator;
import org.flowerplatform.ant.utils.FileUtil;

/**
 * 
 * @author Florin
 */
public class HeaderUpdaterTask extends Task {

	private File workspaceFolder;

	private String projectFilterRegex;
	
	private String ignoreFilterRegex;
	
	private String fileExtension;

	private File headerFile;

	private String startToken;
	
	private String endToken;
	
	@Override
	public void execute() throws BuildException {

		if (workspaceFolder == null) {
			throw new BuildException("rootFolder is null");
		}
		if (fileExtension == null) {
			throw new BuildException("fileExtension is null");
		}
		if (headerFile == null) {
			throw new BuildException("headerFile is null");
		}
		if (startToken == null) {
			throw new BuildException("startToken is null");
		}
		if (endToken == null) {
			throw new BuildException("endToken is null");
		}
		
		Pattern pattern;		
		if (fileExtension.equals("mxml")) {
			pattern = Pattern.compile("<!--[\\s\\S]*?" + startToken + "[\\s\\S]*?" + endToken + "[\\s\\S]*?-->");
		} else {
			pattern = Pattern.compile("/\\*[\\s\\S]*?" + startToken + "[\\s\\S]*?" + endToken + "[\\s\\S]*?\\*/"); 
		}
		 
		Pattern xmlVersionPattern = Pattern.compile("<\\?xml version.*?encoding.*?\\?>"); // <?xml version="1.0" encoding="utf-8"?>
		
		String newHeaderText = FileUtil.readFile(headerFile); 		
		
		for (FileIterator it = new FileIterator(workspaceFolder, new ProjectFileFilter()); it.hasNext();) {
			File file = it.next();
			if (file.getPath().matches(ignoreFilterRegex)) {
				continue;
			}
			if (file.isFile() && file.getName().endsWith(fileExtension)) {

				String fileText = FileUtil.readFile(file);
	            Matcher matcher = pattern.matcher(fileText);
	            if (matcher.find()) { 
	            	if (!fileText.contains(newHeaderText)) {
	            		// remove old header and add new one
	            		fileText = matcher.replaceFirst(newHeaderText);
	            		FileUtil.writeFile(file, fileText);
	            	}
	            } else { 
	            	// header must be added
	            	if (fileExtension.equals("mxml")) {
	            		int indexToInsert = 0;
	            			            	
	            		Matcher xmlMatcher = xmlVersionPattern.matcher(fileText);
	            		if (xmlMatcher.find()) { 
	            			indexToInsert = xmlMatcher.end();
	            		}
	            		fileText = fileText.substring(0, indexToInsert) + System.getProperty("line.separator") + newHeaderText + fileText.substring(indexToInsert);
	            		
	            	} else {
		            	fileText  = newHeaderText + System.getProperty("line.separator") + fileText;		            	
	            	}
	            	FileUtil.writeFile(file, fileText);
	            }
			}
		}				
	}

	class ProjectFileFilter implements FileFilter {

		@Override
		public boolean accept(File pathname) {
			if (pathname.getParentFile().equals(workspaceFolder) && pathname.getName().matches(projectFilterRegex)) {
				return true;
			}
			return false;
		}
		
	}
	
	public String getFileExtension() {
		return fileExtension;
	}

	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}

	public File getHeaderFile() {
		return headerFile;
	}

	public void setHeaderFile(File headerFile) {
		this.headerFile = headerFile;
	}

	public String getEndToken() {
		return endToken;
	}

	public void setEndToken(String endToken) {
		this.endToken = endToken;
	}

	public String getStartToken() {
		return startToken;
	}

	public void setStartToken(String startToken) {
		this.startToken = startToken;
	}

	public File getWorkspaceFolder() {
		return workspaceFolder;
	}

	public void setWorkspaceFolder(File workspaceFolder) {
		this.workspaceFolder = workspaceFolder;
	}

	public String getProjectFilterRegex() {
		return projectFilterRegex;
	}

	public void setProjectFilterRegex(String projectFilterRegex) {
		this.projectFilterRegex = projectFilterRegex;
	}

	public String getIgnoreFilterRegex() {
		return ignoreFilterRegex;
	}

	public void setIgnoreFilterRegex(String ignoreFilterRegex) {
		this.ignoreFilterRegex = ignoreFilterRegex;
	}
}