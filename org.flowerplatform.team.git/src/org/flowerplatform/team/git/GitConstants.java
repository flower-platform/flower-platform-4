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

import org.flowerplatform.util.UtilConstants;

/**
 * @author Cojocea Marius Eduard
 */
public class GitConstants {

	/////////////////////////////////////////////////////////////
	//Node type in Git folder hierarchy
	/////////////////////////////////////////////////////////////
	
	public static final String GIT_REPO_TYPE = "gitRepo";
	public static final String GIT_LOCAL_BRANCHES_TYPE = "gitLocalBranches";
	public static final String GIT_REMOTE_BRANCHES_TYPE = "gitRemoteBranches";
	public static final String GIT_TAGS_TYPE = "gitTags";
	public static final String GIT_REMOTES_TYPE = "gitRemotes";
	public static final String GIT_LOCAL_BRANCH_TYPE = "gitLocalBranch";
	public static final String GIT_REMOTE_BRANCH_TYPE = "gitRemoteBranch";
	public static final String GIT_TAG_TYPE = "gitTag";
	public static final String GIT_REMOTE_TYPE = "gitRemote";
		
	////////////////////////////////////////////////////////////
	//Categories
	////////////////////////////////////////////////////////////
	
	public static final String GIT_CATEGORY = UtilConstants.CATEGORY_PREFIX + "git";
	public static final String GIT_REF = UtilConstants.CATEGORY_PREFIX + "gitRef";
	
	/////////////////////////////////////////////////////////////
	//Schemes
	/////////////////////////////////////////////////////////////
	
	public static final String GIT_SCHEME = "git";
	
	/////////////////////////////////////////////////////////////
	// Git Node Properties
	/////////////////////////////////////////////////////////////
	
	public static final String NAME = "name";
	public static final String IS_GIT_REPOSITORY = "isGitRepository";
	public static final String FULL_NAME = "fullName";
	public static final String CONFIG_REMOTE = "configRemote";

	public static final String CONFIG_UPSTREAM_BRANCH = "configUpstreamBranch";
	public static final String CONFIG_REBASE = "configRebase";

	public static final String CURRENT_BRANCH = "currentBranch";
	public static final String CURRENT_COMMIT = "currentCommit";
	public static final String IS_CHECKEDOUT = "isCheckedOut";
	public static final String FETCH_REF_SPECS = "fetchRefSpecs";
	public static final String PUSH_REF_SPECS = "pushRefSpecs";
	public static final String REMOTE_URIS = "URIs";
	public static final String COMMIT_ID = "commitID";
	public static final String COMMIT_MESSAGE = "commitMessage";

	/////////////////////////////////////////////////////////////
	// Other
	/////////////////////////////////////////////////////////////

	public static final String TEPORARY_LOCATION = "\tmp";
	public static final int NETWORK_TIMEOUT_SEC = 30;
	
	public static final String RESET_SOFT = "soft";
	public static final String RESET_MIXED = "mixed";
	public static final String RESET_HARD = "hard";

	/////////////////////////////////////////////////////////////
	//Git File States
	/////////////////////////////////////////////////////////////
		
	public static final String UNTRACKED = "images/team.git/untracked.gif";	
	public static final String STAGE_REMOVED = "images/team.git/staged_removed.gif";
	public static final String STAGE_ADDED = "images/team.git/staged_added.gif";
	public static final String UNSTAGED = "images/team.git/unstaged.gif";
	public static final String STAGED = "images/team.git/staged.gif";
	public static final String FILE =  "images/core/file.gif";
	public static final String CONFLICTED =  "images/team.git/conflict.gif";
	
	public static final String ADD = "ADD";	
	public static final String DELETE = "DELETE";
	public static final String MODIFY = "MODIFY";
	public static final String RENAME = "RENAME";
	public static final String COPY = "COPY";

	public static final String GIT_PREFIX_SESSION = "git|";

}
