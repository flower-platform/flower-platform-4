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
