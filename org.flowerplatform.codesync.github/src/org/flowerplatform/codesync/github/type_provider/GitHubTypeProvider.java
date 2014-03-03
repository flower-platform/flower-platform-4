package org.flowerplatform.codesync.github.type_provider;

import static org.flowerplatform.codesync.github.GitHubConstants.COMMENT;
import static org.flowerplatform.codesync.github.GitHubConstants.COMMIT_COMMENT;
import static org.flowerplatform.codesync.github.GitHubConstants.COMMIT_FILE;
import static org.flowerplatform.codesync.github.GitHubConstants.PULL_REQUEST;
import static org.flowerplatform.codesync.github.GitHubConstants.REPOSITORY;

import org.eclipse.egit.github.core.Comment;
import org.eclipse.egit.github.core.CommitComment;
import org.eclipse.egit.github.core.CommitFile;
import org.eclipse.egit.github.core.PullRequest;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryId;
import org.flowerplatform.codesync.type_provider.ClassTypeProvider;

/**
 * @author Mariana Gheorghe
 */
public class GitHubTypeProvider extends ClassTypeProvider {

	public GitHubTypeProvider() {
		classToTypeMap.put(Repository.class, REPOSITORY);
		classToTypeMap.put(RepositoryId.class, REPOSITORY);
		classToTypeMap.put(PullRequest.class, PULL_REQUEST);
		classToTypeMap.put(CommitFile.class, COMMIT_FILE);
		classToTypeMap.put(CommitComment.class, COMMIT_COMMENT);
		classToTypeMap.put(Comment.class, COMMENT);
	}

}
