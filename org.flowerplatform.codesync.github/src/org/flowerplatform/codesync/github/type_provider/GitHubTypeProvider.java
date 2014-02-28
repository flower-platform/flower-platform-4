package org.flowerplatform.codesync.github.type_provider;

import org.eclipse.egit.github.core.Comment;
import org.eclipse.egit.github.core.CommitComment;
import org.eclipse.egit.github.core.CommitFile;
import org.eclipse.egit.github.core.PullRequest;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.flowerplatform.codesync.type_provider.ClassTypeProvider;

/**
 * @author Mariana Gheorghe
 */
public class GitHubTypeProvider extends ClassTypeProvider {

	public static final String CLIENT = "gitHubClient";
	public static final String PULL_REQUEST = "gitHubPullRequest";
	public static final String COMMIT_FILE = "gitHubCommitFile";
	public static final String COMMIT_COMMENT = "gitHubCommitComment";
	public static final String COMMENT = "gitHubComment";
	
	public GitHubTypeProvider() {
		classToTypeMap.put(GitHubClient.class, CLIENT);
		classToTypeMap.put(PullRequest.class, PULL_REQUEST);
		classToTypeMap.put(CommitFile.class, COMMIT_FILE);
		classToTypeMap.put(CommitComment.class, COMMIT_COMMENT);
		classToTypeMap.put(Comment.class, COMMENT);
	}

}
