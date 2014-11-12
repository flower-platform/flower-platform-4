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
