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

import static org.flowerplatform.team.git.GitConstants.GIT_SCHEME;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.api.RebaseResult;
import org.eclipse.jgit.lib.ConfigConstants;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.RefUpdate.Result;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryCache;
import org.eclipse.jgit.lib.RepositoryCache.FileKey;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.FetchResult;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.RemoteConfig;
import org.eclipse.jgit.transport.RemoteRefUpdate;
import org.eclipse.jgit.transport.TrackingRefUpdate;
import org.eclipse.jgit.util.FS;
import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CoreUtils;
import org.flowerplatform.core.file.FileControllerUtils;
import org.flowerplatform.core.file.IFileAccessController;

/**
 * @author Cojocea Marius Eduard
 */
public final class GitUtils {
	
	private GitUtils() {
		
	}

	/**
	 *@author see class
	 **/
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

	/**
	 *@author see class
	 **/
	public static Object getGitDir(Object file) {
		IFileAccessController fac = FileControllerUtils.getFileAccessController();

		if (fac.exists(file)) {
			while (file != null) {
				if (FilenameUtils.getName(CoreConstants.FLOWER_PLATFORM_WORKSPACE).equals(fac.getName(file))) {
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

	/**
	 * @author Valentina Bojan
	 */
	public static boolean isRepository(Object file) {
		return getGitDir(file) != null;
	}

	/**
	 * @author Cojocea Eduard
	 */
	public static String getType(String nodeUri) {
		int indexStart = nodeUri.indexOf("|");
		int indexEnd = nodeUri.indexOf("$");
		if (indexEnd < indexStart) {
			indexEnd = nodeUri.length();
		}
		return nodeUri.substring(indexStart + 1, indexEnd);
	}
	
	/**
	 * @author Eduard Cojocea
	 */
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
		if (mergeResult.getConflicts() != null) {			
			for (String path : mergeResult.getConflicts().keySet()) {
				if (!conflicts.contains(path)) {
					conflicts.add(path);
				}
			}
		}		
		if (!conflicts.isEmpty()) {
			sb.append("\nConflicts: ");
			sb.append("\n");
			for (String path : conflicts) {
				sb.append(path);
				sb.append("\n");				
			}
		}
		
		return sb.toString();
	}

	/**
	 * @author Cristina Constantinescu
	 */
	public static String getNodeUri(String repoPath, String type, String name) {
		return CoreUtils.createNodeUriWithRepo(GIT_SCHEME, repoPath, type + (name != null ? "$" + name : ""));	
	}
	
	/**
	 * @author Eduard Cojocea 
	 */
	public static String getNodeUri(String repoPath, String type) {
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
	
	/**
	 * @author Cristina Constantinescu
	 */
	public static RemoteConfig getConfiguredRemote(Repository repository) throws IOException {
		String branch = repository.getBranch();		
		if (branch == null) {
			return null;
		}

		String remoteName = null;
		if (!ObjectId.isId(branch)) {
			remoteName = repository.getConfig().getString(
					ConfigConstants.CONFIG_BRANCH_SECTION, branch,
					ConfigConstants.CONFIG_REMOTE_SECTION);
		}

		// check if we find the configured and default Remotes
		List<RemoteConfig> allRemotes;
		try {
			allRemotes = RemoteConfig.getAllRemoteConfigs(repository.getConfig());
		} catch (URISyntaxException e) {
			allRemotes = new ArrayList<RemoteConfig>();
		}

		RemoteConfig configuredConfig = null;
		RemoteConfig defaultConfig = null;
		for (RemoteConfig config : allRemotes) {
			if (remoteName != null && config.getName().equals(remoteName)) {
				configuredConfig = config;
			}
			if (config.getName().equals(Constants.DEFAULT_REMOTE_NAME)) {
				defaultConfig = config;
			}
		}

		if (configuredConfig != null) {
			return configuredConfig;
		}

		if (defaultConfig != null) {
			if (!defaultConfig.getPushRefSpecs().isEmpty()) {
				return defaultConfig;
			}
		}
		
		return null;
	}
	
	/**
	 * @author Cristina Constantinescu
	 */
	public static boolean isRefCheckedOut(Repository repo, Ref ref) throws Exception {
		String refName = ref.getName();
		Ref leaf = ref.getLeaf();

		String branchName;
		String compareString;

		branchName = repo.getFullBranch();
		if (branchName == null) {
			return false;
		}
		if (refName.startsWith(Constants.R_HEADS)) {
			// local branch: HEAD would be on the branch
			compareString = refName;
		} else if (refName.startsWith(Constants.R_TAGS)) {
			compareString = ref.getObjectId().getName();
		} else if (refName.startsWith(Constants.R_REMOTES)) {
			// remote branch: HEAD would be on the commit id to which
			// the branch is pointing
			ObjectId id = repo.resolve(refName);
			if (id == null) {
				return false;
			}
			RevWalk rw = new RevWalk(repo);
			try {
				RevCommit commit = rw.parseCommit(id);
				compareString = commit.getId().name();
			} finally {
				rw.release();
			}
		} else if (refName.equals(Constants.HEAD)) {
			return true;
		} else {
			String leafname = leaf.getName();
			if (leafname.startsWith(Constants.R_REFS) && leafname.equals(repo.getFullBranch())) {
				return true;
			} else if (leaf.getObjectId().equals(repo.resolve(Constants.HEAD))) {
				return true;
			}
			return false;
		}
		return compareString != null && compareString.equals(branchName);
	}
		
}
