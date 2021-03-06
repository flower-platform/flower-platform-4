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
package org.flowerplatform.codesync.type_provider;

import org.flowerplatform.codesync.CodeSyncAlgorithm;
import org.flowerplatform.codesync.CodeSyncConstants;
import org.flowerplatform.codesync.adapter.file.CodeSyncFile;
import org.flowerplatform.core.file.IFileAccessController;

/**
 * @author Mariana Gheorghe
 */
public class FileTypeProvider implements ITypeProvider {

	@Override
	public String getType(Object object, CodeSyncAlgorithm codeSyncAlgorithm) {
		IFileAccessController fileAccessController = codeSyncAlgorithm.getFileAccessController();
		if (object instanceof CodeSyncFile) {
			Object file = ((CodeSyncFile) object).getFile();
			if (fileAccessController.isFile(file)) {
				if (fileAccessController.isDirectory(file)) {
					return CodeSyncConstants.FOLDER;
				} else {
					return CodeSyncConstants.FILE;
				}
			}
		}
		return null;
	}

}