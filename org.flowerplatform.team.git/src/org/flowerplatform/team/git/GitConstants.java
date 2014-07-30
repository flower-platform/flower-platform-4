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
	public static final String GIT_REF = UtilConstants.CATEGORY_PREFIX + "gitRef";
	public static final String GIT_STAGING_CATEGORY = UtilConstants.CATEGORY_PREFIX + "git";
	
	/////////////////////////////////////////////////////////////
	//Schemes
	/////////////////////////////////////////////////////////////
	
	public static final String GIT_SCHEME = "git";
	
	/////////////////////////////////////////////////////////////
	//Leaf Git Node Properties
	/////////////////////////////////////////////////////////////
	
	public static final String NAME = "name";
	public static final String IS_REPO = "isRepo";
	public static final String FULL_NAME = "fullName";
	public static final String CONFIG_REMOTE = "configRemote";
	public static final String CONFIG_UPSTREAM_BRANCH = "configUpstreamBranch";

	public static final String CONFIG_REBASE = "configRebase";
	public static final String CURRENT_BRANCH = "currentBranch";
	public static final String CURRENT_COMMIT = "currentCommit";
	public static final String IS_CHECKOUT = "isCheckout";
	public static final String FETCH_REF_SPECS = "fetchRefSpecs";
	public static final String PUSH_REF_SPECS = "pushRefSpecs";
	public static final String REMOTE_URIS = "URIs";
	public static final String COMMIT_ID = "commitID";
	public static final String COMMIT_MESSAGE = "commitMessage";

}
