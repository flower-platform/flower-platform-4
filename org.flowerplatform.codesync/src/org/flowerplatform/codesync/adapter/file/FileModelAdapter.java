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
package org.flowerplatform.codesync.adapter.file;

import org.flowerplatform.codesync.CodeSyncAlgorithm;
import org.flowerplatform.codesync.CodeSyncPlugin;
import org.flowerplatform.codesync.adapter.DelegatingModelAdapter;
import org.flowerplatform.codesync.adapter.IModelAdapter;

/**
 * @author Mariana Gheorghe
 */
public class FileModelAdapter extends DelegatingModelAdapter {

	protected IModelAdapter getDelegate(Object element) {
		String extension = null;
		if (element instanceof String) {
			extension = (String) element;
		} else {
			extension = CodeSyncAlgorithm.fileAccessController.getFileExtension(element);
		}
		String technology = CodeSyncPlugin.getInstance().getFileModelAdapterDelegate(extension);
		return CodeSyncPlugin.getInstance().getModelAdapterSet(technology).getFileModelAdapterDelegate();
	}
	
}