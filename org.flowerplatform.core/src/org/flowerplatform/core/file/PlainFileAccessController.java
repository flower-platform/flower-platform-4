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
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.flowerplatform.core.CorePlugin;

/**
 * @author Cristina Constantinescu
 * @author Sebastian Solomon
 */
public class PlainFileAccessController implements IFileAccessController {

	@Override
	public long getLastModifiedTimestamp(Object file) {
		return ((File) file).lastModified();
	}

	@Override
	public String getName(Object file) {
		return ((File) file).getName();		
	}
	
	/**
	 * @see IOUtils
	 * @see FileUtils	
	 */
	@Override
	public InputStream getContent(Object file) {		
		try {			
			return FileUtils.openInputStream((File) file);
		} catch (Throwable e) {
			throw new RuntimeException("Error while loading file content " + file, e);
		}		
	}

	@Override
	public void setContent(Object file, String content) {
		try {			
			FileUtils.writeStringToFile((File) file, content);
		} catch (IOException e) {
			throw new RuntimeException("Error while saving the file " + file, e);
		}
	}
	
	@Override
	public String getFileExtension(Object object) {
		File file = (File) object;
		String name = file.getName();
		int index = name.lastIndexOf(".");
		if (index >= 0) {
			return name.substring(index + 1);
		}
		return "";
	}
	
	@Override
	public boolean isFile(Object file) {
		return (file instanceof File);
	}
	
	@Override
	public Class getFileClass() {
		return File.class;
	}
	
	@Override
	public File[] listFiles(Object folder) {
		return ((File) folder).listFiles();
	}
	
	@Override
	public void delete(Object folder) {
		File[] files = ((File) folder).listFiles();
		if (files != null) {
			for (File f : files) {
				if (f.isDirectory()) {
					delete(f);
				} else {
					f.delete();
				}
			}
		}
		((File) folder).delete();
	}
	
	@Override
	public Object getParentFile(Object file) {
		return ((File) file).getParentFile();
	}
	
	@Override
	public String getParent(Object file) {
		return ((File) file).getParent();
	}
	@Override
	public boolean rename(Object file, Object dest) {
		return ((File) file).renameTo((File) dest);
	}
	
	@Override
	public String readFileToString(Object file) {
		try {
			return FileUtils.readFileToString((File) file);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void writeStringToFile(Object file, String str) {
		try {
			FileUtils.writeStringToFile((File) file, str);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public boolean isDirectory(Object file) {
		return ((File) file).isDirectory();
	}

	/**
	 * Also creates the parent directory of this file,
	 * if it doesn't exist.
	 * 
	 * @author Mariana Gheorghe
	 * @author Sebastian Solomon
	 */
	@Override
	public boolean createFile(Object file, boolean isDirectory) {
		if (isDirectory) {
			return ((File) file).mkdirs();
		}
		try {
			File realFile = (File) file;
			File parentFile = realFile.getParentFile();
			if (parentFile != null && !parentFile.exists()) {
				parentFile.mkdirs();
			}
			return realFile.createNewFile();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public Object getFile(Object file, String name) {
		return new File((File) file, name);
	}

	@Override
	public boolean exists(Object file) {
		return ((File) file).exists();
	}

	@Override
	public String getPathRelativeToFile(Object file, Object relativeTo) {
		String relative = ((File) relativeTo).toURI().relativize(((File) file).toURI()).getPath();
		if (relative.length() > 0 && relative.endsWith("/")) {
			relative = relative.substring(0, relative.length() - 1);
		}
		return relative;
	}

	@Override
	public String getAbsolutePath(Object file) {
		return ((File) file).getAbsolutePath();
	}

	/**
	 * @author Cristina Constantinescu
	 */
	@Override
	public String getPath(Object file) {
		return getPathRelativeToFile((File) file, new File(CorePlugin.getInstance().getWorkspaceLocation()));
	}

	/**
	 * @author Cristina Constantinescu
	 */
	@Override
	public Object getFile(String path) throws Exception {
		// path == null -> path to workspace location
		return new File(CorePlugin.getInstance().getWorkspaceLocation(), path == null ? "" : path);
	}
	
	@Override
	public File getFileAsFile(Object file) throws Exception {
		return (File) file;
	}

	@Override
	public boolean hasChildren(Object file) {
		if (((File) file).list() == null) {
			return false;
		}
		return ((File) file).list().length > 0;
	}

	@Override
	public long length(Object file) {
		return ((File) file).length();
	}
}