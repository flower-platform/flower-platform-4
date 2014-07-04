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