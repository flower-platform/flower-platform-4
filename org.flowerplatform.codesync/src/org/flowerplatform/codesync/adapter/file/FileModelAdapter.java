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
package org.flowerplatform.codesync.adapter.file;

import org.flowerplatform.codesync.CodeSyncAlgorithm;
import org.flowerplatform.codesync.CodeSyncConstants;
import org.flowerplatform.codesync.CodeSyncPlugin;
import org.flowerplatform.codesync.adapter.DelegatingModelAdapter;
import org.flowerplatform.codesync.adapter.IModelAdapter;
import org.flowerplatform.codesync.adapter.IModelAdapterSet;
import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.node.remote.Node;

/**
 * Registered for all {@link FileModelAdapterSet}s. Delegates to
 * a technology-specific model adapter based on the file's extension.
 * 
 * @author Mariana Gheorghe
 */
public class FileModelAdapter extends DelegatingModelAdapter {

	/**
	 * @param element may be a string representing the extension,
	 * or a file recognized by the file access controller
	 */
	protected IModelAdapter getDelegate(Object element, CodeSyncAlgorithm codeSyncAlgorithm) {
		String extension = null;
		if (element instanceof String) {
			extension = (String) element;
		} else {
			extension = codeSyncAlgorithm.getFileAccessController().getFileExtension(((CodeSyncFile) element).getFile());
		}
		String technology = CodeSyncPlugin.getInstance().getTechnologyForExtension(extension);
		if (technology == null) {
			return new UnknownFileModelAdapter();
		}
		return CodeSyncPlugin.getInstance().getModelAdapterSet(technology).getFileModelAdapterDelegate();
	}

	@Override
	public Object createChildOnContainmentFeature(Object parent, Object feature, Object correspondingChild,
			IModelAdapterSet modelAdapterSet, CodeSyncAlgorithm codeSyncAlgorithm) {
		if (CodeSyncConstants.CHILDREN.equals(feature)) {
			// TODO should not cast to node
			Node node = (Node) correspondingChild;
			String name = (String) node.getPropertyValue(CoreConstants.NAME);
			Object newFile = codeSyncAlgorithm.getFileAccessController().getFile(
					((CodeSyncFile) parent).getFile(), name);
			return new CodeSyncFile(newFile, false);
		}
		return super.createChildOnContainmentFeature(parent, feature, correspondingChild, modelAdapterSet,
				codeSyncAlgorithm);
	}
	
}