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

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import org.flowerplatform.codesync.CodeSyncAlgorithm;
import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.file.IFileAccessController;

/**
 * @author Mariana Gheorghe
 * @author Sebastian Solomon
 */
public abstract class AbstractFileModelAdapter extends AstModelElementAdapter {

	/**
	 * @author see class
	 */
	protected CodeSyncFile getCodeSyncFile(Object file) {
		return (CodeSyncFile) file;
	}
	
	/**
	 * @author see class
	 */
	protected Object getOrCreateFileInfo(Object element, IFileAccessController fileAccessController) {
		CodeSyncFile codeSyncFile = getCodeSyncFile(element);
		if (codeSyncFile.getFileInfo() == null) {
			codeSyncFile.setFileInfo(createFileInfo(codeSyncFile.getFile(), fileAccessController));
		}
		return codeSyncFile.getFileInfo();
	}
	
	/**
	 * @author see class
	 */
	protected abstract Object createFileInfo(Object file, IFileAccessController fileAccessController);
	
	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue, CodeSyncAlgorithm codeSyncAlgorithm) {
		if (CoreConstants.NAME.equals(feature)) {
			return getName(element, codeSyncAlgorithm.getFileAccessController());
		}
		return super.getValueFeatureValue(element, feature, correspondingValue, codeSyncAlgorithm);
	}
	
	@Override
	public void setValueFeatureValue(Object file, Object feature, Object value, CodeSyncAlgorithm codeSyncAlgorithm) {
		if (CoreConstants.NAME.equals(feature)) {
			codeSyncAlgorithm.getFilesToRename().put(getCodeSyncFile(file).getFile(), (String) value);
		}
	}
	
	@Override
	public Object getMatchKey(Object element, CodeSyncAlgorithm codeSyncAlgorithm) {
		return getName(element, codeSyncAlgorithm.getFileAccessController());
	}

	/**
	 * @author see class
	 */
	protected String getName(Object element, IFileAccessController fileAccessController) {
		return fileAccessController.getName(getCodeSyncFile(element).getFile());
	}
	
	/**
	 * @author Mariana Gheroghe
	 * Creates the file, if it does not exist, and commits all the modifications recorded by the AST.
	 */
	@Override
	public boolean save(Object element, CodeSyncAlgorithm codeSyncAlgorithm) {
		IFileAccessController fileAccessController = codeSyncAlgorithm.getFileAccessController();
		Object file = getCodeSyncFile(element).getFile();
		
		if (!fileAccessController.exists(file)) {
			fileAccessController.createFile(file, false);
		}
		
		if (fileAccessController.exists(file)) {
			Object fileInfo = getCodeSyncFile(element).getFileInfo();
			if (fileInfo != null) {
				Document document;
				try {
					document = new Document(fileAccessController.readFileToString(file));
					TextEdit edits = rewrite(document, fileInfo);
					if (edits != null && edits.getChildrenSize() != 0) {
						edits.apply(document);
						fileAccessController.writeStringToFile(file, document.get());
					}
				} catch (MalformedTreeException | BadLocationException e) {
					throw new RuntimeException(e);
				}
			}
		}
		
		String newName = codeSyncAlgorithm.getFilesToRename().get(file);
		if (newName != null) {
			Object dest = fileAccessController.getFile(fileAccessController.getParent(file), newName);
			fileAccessController.rename(file, dest);
		}
		
		// no need to call save for the AST
		return false;
	}
	
	/**
	 * @author see class
	 */
	protected abstract TextEdit rewrite(Document document, Object fileInfo);
	
	/**
	 * Discards the AST corresponding to this file.
	 */
	@Override
	public boolean discard(Object element, CodeSyncAlgorithm codeSyncAlgorithm) {
		// no need to call discard for the AST
		return false;
	}
	
	@Override
	protected void updateUID(Object element, Object correspondingElement) {
		// nothing to do
	}
	public void testmethod(){;}
}