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
	
	/////////////////////////////////////////////////////////////
	//Schemes
	/////////////////////////////////////////////////////////////
	
	public static final String GIT_SCHEME = "git";
	public static final String GIT_LOCAL_BRANCHES_SCHEME = "gitLocalBranches";
	public static final String GIT_REMOTE_BRANCHES_SCHEME = "gitRemoteBranches";
	public static final String GIT_TAGS_SCHEME = "gitTags";
	public static final String GIT_REMOTES_SCHEME = "gitRemotes";
	public static final String GIT_BRANCH_SCHEME = "gitBranch";
	public static final String GIT_TAG_SCHEME = "gitTag";
	public static final String GIT_REMOTE_SCHEME = "gitRemote";
	
	/////////////////////////////////////////////////////////////
	//Leaf Git Node Properties
	/////////////////////////////////////////////////////////////
	
	public static final String NAME = "name";
	public static final String FULL_NAME = "fullName";
	public static final String CONFIG_REMOTE = "configRemote";
	public static final String CONFIG_UPSTREAM_BRANCH = "configUpstreamBranch";
	public static final String CONFIG_REBASE = "configRebase";	
	
	/////////////////////////////////////////////////////////////
	//Git Images
	/////////////////////////////////////////////////////////////
	
	public static final String UNTRACKED = "images/team.git/untracked.gif";	
	public static final String STAGE_REMOVED = "images/team.git/staged_removed.gif";
	public static final String STAGE_ADDED = "images/team.git/staged_added.gif";
	public static final String UNSTAGED = "images/team.git/unstaged.gif";
	public static final String STAGED = "images/team.git/staged.gif";
	public static final String FILE =  "images/core/file.gif";
	public static final String CONFLICTED =  "images/team.git/conflict.gif";
	
	/////////////////////////////////////////////////////////////
	//Git File States
	/////////////////////////////////////////////////////////////
	
	public static final String ADD = "ADD";	
	public static final String DELETE = "DELETE";
	public static final String MODIFY = "MODIFY";
	public static final String RENAME = "RENAME";
	public static final String COPY = "COPY";
}
