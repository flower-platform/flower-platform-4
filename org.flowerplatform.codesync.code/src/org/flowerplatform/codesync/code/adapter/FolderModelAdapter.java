/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
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
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package org.flowerplatform.codesync.code.adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowerplatform.codesync.CodeSyncPlugin;
import org.flowerplatform.codesync.FilteredIterable;
import org.flowerplatform.codesync.NodeFeatureProvider;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.mindmap.remote.Node;

/**
 * Mapped to platform-dependent files. Children are files that match the {@link #limitedPath}, if set.
 * 
 * @author Mariana
 */
public class FolderModelAdapter extends AstModelElementAdapter {

	protected String limitedPath;
	
	private List<Object> filesToDelete = new ArrayList<Object>();

	private Map<Object, String> filesToRename = new HashMap<Object, String>();
	
	public FolderModelAdapter() {
		super();
	}
	
	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		if (Node.NAME.equals(feature)) {
			return getLabel(element);
		}
		if (Node.TYPE.equals(feature)) {
			return CodeSyncPlugin.FOLDER;
		}
		return null;
	}
	
	@Override
	public Object getMatchKey(Object element) {
		return getLabel(element);
	}

	@Override
	public void setValueFeatureValue(Object folder, Object feature, Object value) {
//		if (CodeSyncPackage.eINSTANCE.getCodeSyncElement_Name().equals(feature)) {
//			filesToRename.put(folder, (String) value);
//		}
	}

	@Override
	public Object createChildOnContainmentFeature(Object element, Object feature, Object correspondingChild) {
//		if (CodeSyncPackage.eINSTANCE.getCodeSyncElement_Children().equals(feature)) {
//			CodeSyncElement cse = (CodeSyncElement) correspondingChild;
//			return EditorPlugin.getInstance().getFileAccessController().createNewFile(element, (String) CodeSyncOperationsService.getInstance().getKeyFeatureValue(cse));
//		}
		return null;
	}

	@Override
	public void removeChildrenOnContainmentFeature(Object parent, Object feature, Object child) {
//		if (CodeSyncPackage.eINSTANCE.getCodeSyncElement_Children().equals(feature)) {
//			filesToDelete.add(child);
//		}
	}

	@Override
	public boolean hasChildren(Object modelElement) {
		return getChildren(modelElement).size() > 0;
	}

	@Override
	public List<?> getChildren(Object modelElement) {
		Object[] files = CorePlugin.getInstance().getFileAccessController().listFiles(modelElement);
		if (files == null) {
			return Collections.emptyList();
		}
		return Arrays.asList(files);
	}

	@Override
	public String getLabel(Object modelElement) {
		return CorePlugin.getInstance().getFileAccessController().getName(modelElement);
	}

	@Override
	public List<String> getIconUrls(Object modelElement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element, Object feature, Iterable<?> correspondingIterable) {
		if (NodeFeatureProvider.CHILDREN.equals(feature)) {
			return new FilteredIterable(getChildren(element).iterator()) {
	
				@Override
				protected boolean isAccepted(Object candidate) {
					String candidatePath = CodeSyncPlugin.getInstance().getProjectAccessController().getPathRelativeToProject(candidate);
					candidatePath = candidatePath.replaceAll("\\\\", "/");
					if (limitedPath == null 
							|| limitedPath.startsWith(candidatePath) 	// accept candidate a/b/c for limit a/b/c/d
							|| candidatePath.startsWith(limitedPath)) {	// accept candidate a/b/c/d for limit a/b/c 
						return true;
					}
					return false;
				}
				
			};
		}
		return Collections.emptyList();
	}

	public String getLimitedPath() {
		return limitedPath;
	}

	public void setLimitedPath(String limitedPath) {
		this.limitedPath = limitedPath;
	}

	@Override
	public Object createCorrespondingModelElement(Object element) {
		return null;
	}
	
	/**
	 * @author Sebastian Solomon
	 */
	@Override
	public boolean save(Object file) {
//		IFileAccessController fileAccessController = EditorPlugin.getInstance().getFileAccessController();
//		if (!fileAccessController.exists(file)) {
//			fileAccessController.createNewFile(file);
//		}
//		
//		// remove children that were mark deleted	
//		Object[] children = fileAccessController.listFiles(file);
//		if (children != null) {
//			for (Object child : children) {
//				if (filesToDelete.contains(child)) {
//					fileAccessController.delete(child);
//					filesToDelete.remove(child);
//				}
//			}
//		}
//		
//		// move the folder if it was marked renamed
//		String newName = filesToRename.get(file);
//		if (newName != null) {
//			Object dest = fileAccessController.createNewFile(fileAccessController.getParentFile(file), newName);
//			fileAccessController.rename(file, dest);
//			filesToRename.remove(file);
//		}
		return true;
	}
	
	@Override
	public boolean discard(Object element) {
		return true;
	}

	@Override
	protected void updateUID(Object element, Object correspondingElement) {
		// folders don't have UUIDs
	}
	
}