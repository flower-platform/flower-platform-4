package org.flowerplatform.codesync.github;

/**
 * @author Mariana Gheorghe
 */
public class GitHubConstants {

	public static final String GITHUB = "gitHub";
	
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
	
	public static final String CONTAINMENT_PULL_REQUESTS = "pullRequests";
	
	public static final String PULL_REQUEST_NUMBER = "number";
	public static final String PULL_REQUEST_BODY = "body";
	public static final String PULL_REQUEST_HTML_URL = "htmlUrl";
	
	public static final String PULL_REQUEST_STATE = "state";
	public static final String PULL_REQUEST_CLOSED_AT = "closedAt";
	public static final String PULL_REQUEST_MERGED_AT = "mergedAt";
	public static final String PULL_REQUEST_UPDATED_AT = "updatedAt";
	
	public static final String PULL_REQUEST_ADDITIONS = "additions";
	public static final String PULL_REQUEST_DELETIONS = "deletions";
	public static final String PULL_REQUEST_CHANGED_FILES = "changedFiles";
	public static final String PULL_REQUEST_COMMITS = "commits";
	public static final String PULL_REQUEST_COMMENTS = "comments";
	
	public static final String PULL_REQUEST_USER = "user";
	public static final String PULL_REQUEST_ASSIGNEE = "assignee";
	public static final String PULL_REQUEST_MERGED_BY = "mergedBy";
	
	// Commit Files
	
	public static final String CONTAINMENT_COMMIT_FILES = "commitFiles";
	
	public static final String COMMIT_FILE_NAME = "fileName";
	public static final String COMMIT_FILE_BLOB_URL = "blobURL";
	public static final String COMMIT_FILE_STATUS = "status";

		public static final String COMMIT_FILE_STATUS_ADDED = "added";
		public static final String COMMIT_FILE_STATUS_MODIFIED = "modified";
		public static final String COMMIT_FILE_STATUS_RENAMED = "renamed";
		public static final String COMMIT_FILE_STATUS_REMOVED = "removed";
	
	public static final String COMMIT_FILE_PATCH = "patch";
	public static final String COMMIT_FILE_ADDITIONS = "additions";
	public static final String COMMIT_FILE_DELETIONS = "deletions";
	public static final String COMMIT_FILE_CHANGES = "changes";
	
	// Commit Comments
	
	public static final String CONTAINMENT_COMMENTS = "comments";
	
	public static final String COMMENT_ID = "id";
	public static final String COMMENT_BODY = "body";
	
	public static final String COMMENT_USER = "user";
	public static final String COMMENT_CREATED_AT = "createdAt";
	public static final String COMMENT_UPDATED_AT = "updatedAt";
	
	public static final String COMMIT_COMMENT_COMMIT_ID = "commitId";
	public static final String COMMIT_COMMENT_PATH = "path";
	public static final String COMMIT_COMMENT_LINE = "line";
	public static final String COMMIT_COMMENT_POSITION = "position";

	////////////////////////////////////////////
	// Icons and Decorators Constants
	////////////////////////////////////////////
	
	private static final String IMAGES = "images/";
	
	public static final String IMG_REPOSITORY = IMAGES + "gitrepository.gif";
	public static final String IMG_PULL_REQUEST = IMAGES + "arrow_merge.png";
	public static final String IMG_FILE = IMAGES + "file.gif";
	public static final String IMG_COMMENT = IMAGES + "comment.png";
	
	private static final String DECORATOR = IMAGES + "decorator/";
	
	public static final String DECORATOR_ADDED = DECORATOR + "staged_added.gif";
	public static final String DECORATOR_REMOVED = DECORATOR + "staged_removed.gif";
	public static final String DECORATOR_RENAMED = DECORATOR + "staged_renamed.gif";
	public static final String DECORATOR_MODIFIED = DECORATOR + "staged.gif";
	
}
