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

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.flowerplatform.codesync.CodeSyncAlgorithm;
import org.flowerplatform.codesync.CodeSyncConstants;
import org.flowerplatform.codesync.FilteredIterable;
import org.flowerplatform.codesync.adapter.IModelAdapterSet;
import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.file.IFileAccessController;
import org.flowerplatform.core.node.remote.Node;

/**
 * Mapped to platform-dependent files. Children are files that match the {@link #limitedPath}, if set.
 * 
 * @author Mariana
 */
public class FolderModelAdapter extends AstModelElementAdapter {

	public FolderModelAdapter() {
		containmentFeatures.add(CodeSyncConstants.CHILDREN);
	}
	
	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue, CodeSyncAlgorithm codeSyncAlgorithm) {
		if (CoreConstants.NAME.equals(feature)) {
			return getName(element, codeSyncAlgorithm.getFileAccessController());
		}
		return super.getValueFeatureValue(element, feature, correspondingValue, codeSyncAlgorithm);
	}
	
	@Override
	public Object getMatchKey(Object element, CodeSyncAlgorithm codeSyncAlgorithm) {
		return getName(element, codeSyncAlgorithm.getFileAccessController());
	}

	protected String getName(Object element, IFileAccessController fileAccessController) {
		return fileAccessController.getName(element);
	}
	
	@Override
	public void setValueFeatureValue(Object folder, Object feature, Object value, CodeSyncAlgorithm codeSyncAlgorithm) {
		if (CoreConstants.NAME.equals(feature)) {
			codeSyncAlgorithm.getFilesToRename().put(folder, (String) value);
		}
	}

	@Override
	public Object createChildOnContainmentFeature(Object parent, Object feature, Object correspondingChild, IModelAdapterSet modelAdapterSet, CodeSyncAlgorithm codeSyncAlgorithm) {
		if (CodeSyncConstants.CHILDREN.equals(feature)) {
			Node node = (Node) correspondingChild;
			return codeSyncAlgorithm.getFileAccessController()
					.getFile(parent, (String) node.getPropertyValue(CoreConstants.NAME));
		}
		return super.createChildOnContainmentFeature(parent, feature, correspondingChild, modelAdapterSet, codeSyncAlgorithm);
	}

	@Override
	public void removeChildrenOnContainmentFeature(Object parent, Object feature, Object child, CodeSyncAlgorithm codeSyncAlgorithm) {
		if (CodeSyncConstants.CHILDREN.equals(feature)) {
			codeSyncAlgorithm.getFilesToDelete().add(child);
		}
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element, Object feature, Iterable<?> correspondingIterable, CodeSyncAlgorithm codeSyncAlgorithm) {
		if (CodeSyncConstants.CHILDREN.equals(feature)) {
			List<?> children = getChildren(element, codeSyncAlgorithm.getFileAccessController());
			return new FilteredIterable<Object, CodeSyncFile>((Iterator<Object>) children.iterator()) {
	
				@Override
				protected boolean isAccepted(Object candidate) {
					return true;
				}

				@Override
				protected CodeSyncFile convert(Object input) {
					return new CodeSyncFile(input);
				}
				
			};
		}
		return super.getContainmentFeatureIterable(element, feature, correspondingIterable, codeSyncAlgorithm);
	}

	protected List<?> getChildren(Object modelElement, IFileAccessController fileAccessController) {
		Object[] files = fileAccessController.listFiles(modelElement);
		if (files == null) {
			return Collections.emptyList();
		}
		return Arrays.asList(files);
	}
	
	/**
	 * @author Sebastian Solomon
	 */
	@Override
	public boolean save(Object file, CodeSyncAlgorithm codeSyncAlgorithm) {
		IFileAccessController fileAccessController = codeSyncAlgorithm.getFileAccessController();
		if (!fileAccessController.exists(file)) {
			fileAccessController.createFile(file, true);
		}
		
		// remove children that were mark deleted	
		Object[] children = fileAccessController.listFiles(file);
		if (children != null) {
			for (Object child : children) {
				if (codeSyncAlgorithm.getFilesToDelete().contains(child)) {
					fileAccessController.delete(child);
					codeSyncAlgorithm.getFilesToDelete().remove(child);
				}
			}
		}
		
		// move the folder if it was marked renamed
		String newName = codeSyncAlgorithm.getFilesToRename().get(file);
		if (newName != null) {
			Object dest = fileAccessController.getFile(fileAccessController.getParentFile(file), newName);
			fileAccessController.rename(file, dest);
			codeSyncAlgorithm.getFilesToRename().remove(file);
		}
		return true;
	}
	
	@Override
	public boolean discard(Object element, CodeSyncAlgorithm codeSyncAlgorithm) {
		return true;
	}

	@Override
	protected void updateUID(Object element, Object correspondingElement) {
		// folders don't have UUIDs
	}
	
}
