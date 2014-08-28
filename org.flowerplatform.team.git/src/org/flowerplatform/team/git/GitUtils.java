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
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.flowerplatform.team.git.GitConstants.GIT_SCHEME;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.api.RebaseResult;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryCache;
import org.eclipse.jgit.lib.RefUpdate.Result;
import org.eclipse.jgit.lib.RepositoryCache.FileKey;
import org.eclipse.jgit.transport.FetchResult;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.RemoteRefUpdate;
import org.eclipse.jgit.transport.TrackingRefUpdate;
import org.eclipse.jgit.util.FS;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.CoreUtils;
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

	public static String getType(String nodeUri) {
		int indexStart = nodeUri.indexOf("|");
		int indexEnd = nodeUri.indexOf("$");
		if (indexEnd < indexStart) {
			indexEnd = nodeUri.length();
		}
		return nodeUri.substring(indexStart + 1, indexEnd);
	}
	
	public static String getName(String nodeUri) {
		int indexStart = nodeUri.indexOf("$");
		int indexEnd = nodeUri.length();
		return nodeUri.substring(indexStart + 1, indexEnd);
	}

	/**
	 * @author Cristina Constantienscu
	 * @author Tita Andreea
	 */
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
		
		List<String> conflicts = new ArrayList<>();  
		if(mergeResult.getConflicts() != null) {			
			for(String path : mergeResult.getConflicts().keySet()) {
				if (!conflicts.contains(path)) {
					conflicts.add(path);
				}
			}
		}		
		if(!conflicts.isEmpty()) {
			sb.append("\nConflicts: ");
			sb.append("\n");
			for(String path : conflicts) {
				sb.append(path);
				sb.append("\n");				
			}
		}
		
		return sb.toString();
	}

	public static String getNodeUri(String repoPath,String type,String name) {
		return CoreUtils.createNodeUriWithRepo(GIT_SCHEME, repoPath, type + (name != null ? "$" + name : ""));	
	}
	
	public static String getNodeUri(String repoPath,String type){
		return getNodeUri(repoPath, type, null);
	}
	
	/**
	 * @author Cristina Constantinescu
	 */
	public static String handlePushResult(PushResult pushResult) {
		StringBuilder sb = new StringBuilder();		
		
		sb.append(pushResult.getMessages());
		sb.append("\n");
		
		for (RemoteRefUpdate rru : pushResult.getRemoteUpdates()) {
			String rm = rru.getRemoteName();
			RemoteRefUpdate.Status status = rru.getStatus();
			sb.append(rm);
			sb.append(" -> ");
			sb.append(status.name());
			sb.append("\n");
		}
		return sb.toString();
	}
		
	/**
	 * @author Cristina Constantinescu
	 */
	public static String handleRebaseResult(RebaseResult rebaseResult) {
		StringBuilder sb = new StringBuilder();		
		
		sb.append("Status: ");
		sb.append(rebaseResult.getStatus());
		sb.append("\n");
		
		if (rebaseResult.getConflicts() != null) {
			sb.append("\nConflicts: ");
			sb.append("\n");
			for (String conflict : rebaseResult.getConflicts()) {
				sb.append(conflict);
				sb.append("\n");
			}
		}
		
		if (rebaseResult.getFailingPaths() != null) {
			sb.append("\nFailing paths: ");
			sb.append("\n");
			for (String path : rebaseResult.getFailingPaths().keySet()) {
				sb.append(path);
				sb.append(" -> ");
				sb.append(rebaseResult.getFailingPaths().get(path).toString());
				sb.append("\n");
			}
		}
		return sb.toString();
	}
	
	/**
	 * @author Cristina Constantinescu
	 * 
	 * Creates a string message to be displayed on client side
	 * to inform the user about fetch result operation.
	 */
	public static String handleFetchResult(FetchResult fetchResult) {
		StringBuilder sb = new StringBuilder();		
		if (fetchResult.getTrackingRefUpdates().size() > 0) {
			// handle result
			for (TrackingRefUpdate updateRes : fetchResult.getTrackingRefUpdates()) {
				sb.append(updateRes.getRemoteName());
				sb.append(" -> ");
				sb.append(updateRes.getLocalName());			
				sb.append(" ");
				sb.append(updateRes.getOldObjectId() == null ? "" : updateRes.getOldObjectId().abbreviate(7).name());
				sb.append("..");
				sb.append(updateRes.getNewObjectId() == null ? "" : updateRes.getNewObjectId().abbreviate(7).name());
				sb.append(" ");
				Result res = updateRes.getResult();
				switch (res) {
					case NOT_ATTEMPTED :
					case NO_CHANGE :
					case NEW :
					case FORCED :
					case FAST_FORWARD :
					case RENAMED :
						sb.append("OK.");
						break;
					case REJECTED :
						sb.append("Fetch rejected, not a fast-forward.");					
					case REJECTED_CURRENT_BRANCH :
						sb.append("Rejected because trying to delete the current branch.");					
					default :
						sb.append(res.name());				
				}
				sb.append("\n");	
			}
		} else {
			sb.append("OK.");
		}
		return sb.toString();
	}
	
}
