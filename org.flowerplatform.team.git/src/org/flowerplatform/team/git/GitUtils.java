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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryCache;
import org.eclipse.jgit.lib.RepositoryCache.FileKey;
import org.eclipse.jgit.util.FS;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.file.FileControllerUtils;
import org.flowerplatform.core.file.IFileAccessController;

/**
 * @author Cojocea Marius Eduard
 */
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

	public static String getType(String nodeUri){
		int indexStart = nodeUri.indexOf("|");
		int indexEnd = nodeUri.indexOf("$");
		if(indexEnd < indexStart){
			indexEnd = nodeUri.length();
		}
		return nodeUri.substring(indexStart + 1, indexEnd);
	}
	
	public static String getName(String nodeUri){
		int indexStart = nodeUri.indexOf("$");
		int indexEnd = nodeUri.length();
		return nodeUri.substring(indexStart + 1, indexEnd);
	}


	/**
	 * @author Cristina Constantienscu
	 * @author Tita Andreea
	 */
	//  handleMergeResult from fp3 
	public static String handleMergeResult(MergeResult mergeResult) {
		StringBuilder sb = new StringBuilder();		
		if (mergeResult == null) {
			return sb.toString();
		}
		sb.append("Status: ");
		sb.append(mergeResult.getMergeStatus());
		sb.append("\n");
		
		if (mergeResult.getMergedCommits() != null) {
			sb.append("\nMerged commits: ");
			sb.append("\n");
			for (ObjectId id : mergeResult.getMergedCommits()) {
				sb.append(id.getName());
				sb.append("\n");
			}
		}
		
		if (mergeResult.getCheckoutConflicts() != null) {
			sb.append("\nConflicts: ");
			sb.append("\n");
			for (String conflict : mergeResult.getCheckoutConflicts()) {
				sb.append(conflict);
				sb.append("\n");
			}
		}
				
		if (mergeResult.getFailingPaths() != null) {
			sb.append("\nFailing paths: ");
			sb.append("\n");
			for (String path : mergeResult.getFailingPaths().keySet()) {
				sb.append(path);
				sb.append(" -> ");
				sb.append(mergeResult.getFailingPaths().get(path).toString());
				sb.append("\n");
			}
		}
		
		if(mergeResult.getConflicts() != null) {
			sb.append("\nConflicts: ");
			sb.append("\n");
			for(String path : mergeResult.getConflicts().keySet()) {
				sb.append(path);
				int[][] c = mergeResult.getConflicts().get(path);
				for(int i = 0; i < c.length; i++) {
					sb.append(" Conflict #" + i);
					for(int j = 0; j < (c[i].length) - 1; j++) {
						if(c[i][j] >= 0){
							sb.append("  Chunk for " + mergeResult.getMergedCommits()[j] + " starts on line " + c[i][j]);
						}
					}
				}
			}
		}
		
		return sb.toString();
	}
		

	public static void delete(File f) {	
		if (f.isDirectory() && !Files.isSymbolicLink(Paths.get(f.toURI()))) {		
			for (File c : f.listFiles()) {
				delete(c);
			}
		}
		f.delete();
	}
	
	public static String getNodePath(String nodeUri){
		int index = nodeUri.indexOf("|");
		if (index < 0) {
			index = nodeUri.length();
		}
		String nodePath = nodeUri.substring(nodeUri.indexOf(":") + 1, index);
		return nodePath;
	}

}
