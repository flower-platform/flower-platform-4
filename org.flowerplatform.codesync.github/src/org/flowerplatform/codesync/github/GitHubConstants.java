package org.flowerplatform.codesync.github;

/**
 * @author Mariana Gheorghe
 */
public class GitHubConstants {

	/////////////////////////////////////
	// Types Constants
	/////////////////////////////////////
	
	public static final String REPOSITORY = "gitHubRepository";
	public static final String PULL_REQUEST = "gitHubPullRequest";
	public static final String COMMIT_FILE = "gitHubCommitFile";
	public static final String COMMIT_COMMENT = "gitHubCommitComment";
	public static final String COMMENT = "gitHubComment";
	
	/////////////////////////////////////
	// Node Properties Constants
	/////////////////////////////////////
	
	// Pull Requests
	
	public static final String PULL_REQUESTS = "pullRequests";
	
	public static final String NUMBER = "number";
	public static final String BODY = "body";
	public static final String HTML_URL = "htmlUrl";
	
	public static final String STATE = "state";
	public static final String CLOSED_AT = "closedAt";
	public static final String MERGED_AT = "mergedAt";
	public static final String UPDATED_AT = "updatedAt";
	
	public static final String ADDITIONS = "additions";
	public static final String DELETIONS = "deletions";
	public static final String CHANGED_FILES = "changedFiles";
	public static final String COMMITS = "commits";
	public static final String COMMENTS = "comments";
	
	public static final String USER = "user";
	public static final String ASSIGNEE = "assignee";
	public static final String MERGED_BY = "mergedBy";
	
	// Commit Files
	
	public static final String COMMIT_FILES = "commitFiles";
	
	public static final String FILENAME = "fileName";
	public static final String STATUS = "status";
	public static final String BLOB_URL = "blobURL";
	
	public static final String PATCH = "patch";
	public static final String CHANGES = "changes";
	
	
	// Commit Comments
	
	public static final String COMMIT_COMMENTS = "comments";
	
	public static final String COMMENT_ID = "id";
	public static final String CREATED_AT = "createdAt";
	public static final String PATH = "path";
	public static final String LINE = "line";
	public static final String POSITION = "position";

	public static final String GITHUB = "gitHub";
	
}
