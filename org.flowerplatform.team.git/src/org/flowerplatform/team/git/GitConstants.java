package org.flowerplatform.team.git;

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
	
	public static final String GIT_STAGING_CATEGORY = "gitStagingCategory";
	
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
}
