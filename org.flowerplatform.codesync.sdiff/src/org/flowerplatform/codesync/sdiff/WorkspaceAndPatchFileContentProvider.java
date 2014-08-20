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
		FilePatch2 filePatch = (FilePatch2) patch;
		
		switch (filePatch.getDiffType(false)){
		case 1:
			oldFileContent = null;
			break;
		default:
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