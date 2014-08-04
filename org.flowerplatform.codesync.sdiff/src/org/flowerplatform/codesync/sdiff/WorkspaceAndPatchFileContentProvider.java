package org.flowerplatform.codesync.sdiff;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.apache.commons.io.IOUtils;
import org.eclipse.compare.internal.core.patch.FilePatch2;
import org.eclipse.compare.patch.IFilePatchResult;
import org.eclipse.compare.patch.PatchConfiguration;
import org.eclipse.compare.patch.ReaderCreator;
import org.flowerplatform.core.CorePlugin;

/**
 * Class which provides a FileContent object which is obtained from a file from
 * the current workspace and a patch of an old revision of the same file.
 * @author Mariana Gheorghe
 * @author Valentina-Camelia Bojan
 */

@SuppressWarnings("restriction")
public class WorkspaceAndPatchFileContentProvider implements IFileContentProvider {

	@Override
	public FileContent getFileContent(String filePath, String repo, Object patch) {
		String newFileContent, oldFileContent;
		Object file;

		// get new file content
		try {
			file = CorePlugin.getInstance().getFileAccessController().getFile(repo + "/" + filePath);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		if (CorePlugin.getInstance().getFileAccessController().exists(file)) {
			newFileContent = CorePlugin.getInstance().getFileAccessController().readFileToString(file);
		} else {
			newFileContent = null;
		}

		// get old file content using new file content and patch
		PatchConfiguration configuration = new PatchConfiguration();
		configuration.setReversed(true);
		IFilePatchResult result = ((FilePatch2)patch).apply(new StringReaderCreator(newFileContent),
															configuration,
															null);
		try {
			oldFileContent = IOUtils.toString(result.getPatchedContents());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		if (oldFileContent.isEmpty()) {
			oldFileContent = null;
		}
		return new FileContent(oldFileContent, newFileContent);
	}

	class StringReaderCreator extends ReaderCreator {

		private String content;

		public StringReaderCreator(String content) {
			if (content == null) {
				content = "";
			}
			this.content = content;
		}

		@Override
		public Reader createReader() {
			return new StringReader(content);
		}
	}
}
