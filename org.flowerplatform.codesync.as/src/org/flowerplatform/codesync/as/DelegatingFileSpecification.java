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
package org.flowerplatform.codesync.as;

import java.io.FileNotFoundException;
import java.io.Reader;
import java.io.StringReader;

import org.apache.flex.compiler.filespecs.IFileSpecification;
import org.apache.flex.utils.FilenameNormalization;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.file.IFileAccessController;
import org.flowerplatform.util.file.FileHolder;

/**
 * This implementation delegates to {@link IFileAccessController}.
 * 
 * @author Mariana Gheorghe
 */
public class DelegatingFileSpecification implements IFileSpecification {

	IFileAccessController fileAccessController = CorePlugin.getInstance().getFileAccessController();
	
	private Object file;
	
	public DelegatingFileSpecification(Object file) {
		this.file = file;
	}
	
	@Override
	public String getPath() {
		if (file instanceof FileHolder) {
			String path = ((FileHolder) file).getPath();
			return FilenameNormalization.normalize(path);
		}
		return FilenameNormalization.normalize(fileAccessController.getAbsolutePath(file));
	}

	@Override
	public Reader createReader() throws FileNotFoundException {
		if (file instanceof FileHolder) {
			return new StringReader(((FileHolder) file).getContent());
		}
		String content = fileAccessController.readFileToString(file);
		return new StringReader(content);
	}

	@Override
	public long getLastModified() {
		if (file instanceof FileHolder) {
			return 0;
		}
		return fileAccessController.getLastModifiedTimestamp(file);
	}

	@Override
	public boolean isOpenDocument() {
		return false;
	}
}