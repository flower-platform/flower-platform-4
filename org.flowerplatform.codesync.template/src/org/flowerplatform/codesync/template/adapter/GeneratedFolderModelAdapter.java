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
package org.flowerplatform.codesync.template.adapter;

import org.flowerplatform.codesync.adapter.file.FolderModelAdapter;
import org.flowerplatform.core.file.IFileAccessController;

/**
 * Mapped to folders containing generated files. Does not return any children,
 * because we want the files to be completely regenerated.
 * 
 * @author Mariana Gheorghe
 */
public class GeneratedFolderModelAdapter extends FolderModelAdapter {

	@Override
	protected boolean isFileAccepted(Object file, IFileAccessController fileAccessController) {
		return false;
	}
	
}
