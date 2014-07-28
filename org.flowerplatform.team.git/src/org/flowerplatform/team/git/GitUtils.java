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
package org.flowerplatform.team.git;

import org.apache.commons.io.FilenameUtils;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryCache;
import org.eclipse.jgit.lib.RepositoryCache.FileKey;
import org.eclipse.jgit.util.FS;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.file.FileControllerUtils;
import org.flowerplatform.core.file.IFileAccessController;

public class GitUtils {

	public static Repository getRepository(Object repoFile) {
		IFileAccessController fac = FileControllerUtils.getFileAccessController();

		Object gitDir = getGitDir(repoFile);
		if (gitDir != null) {
			try {
				Repository repository = RepositoryCache.open(FileKey.exact(fac.getFileAsFile(gitDir), FS.DETECTED));
				return repository;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return null;
	}

	public static Object getGitDir(Object file) {
		IFileAccessController fac = FileControllerUtils.getFileAccessController();

		if (fac.exists(file)) {
			while (file != null) {
				if (FilenameUtils.getName(CorePlugin.getInstance().getWorkspaceLocation()).equals(fac.getName(file))) {
					return null;
				}
				try {
					if (RepositoryCache.FileKey.isGitRepository(fac.getFileAsFile(file), FS.DETECTED)) {
						return file;
					} else if (RepositoryCache.FileKey.isGitRepository(fac.getFileAsFile(fac.getFile(file, Constants.DOT_GIT)), FS.DETECTED)) {
						return fac.getFileAsFile(fac.getFile(file, Constants.DOT_GIT));
					}
					file = fac.getParentFile(file);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
		return null;
	}

	public static boolean isRepository(Object file) {
		return getGitDir(file) != null;
	}

}