package org.flowerplatform.codesync.github.feature_provider;

import static org.flowerplatform.codesync.github.GitHubConstants.COMMIT_COMMENT_COMMIT_ID;
import static org.flowerplatform.codesync.github.GitHubConstants.COMMIT_COMMENT_PATH;
import static org.flowerplatform.codesync.github.GitHubConstants.COMMIT_COMMENT_POSITION;

import org.flowerplatform.codesync.github.adapter.GitHubCommitCommentModelAdapter;

/**
 * @see GitHubCommitCommentModelAdapter
 * 
 * @author Mariana Gheorghe
 */
public class GitHubCommitCommentFeatureProvider extends GitHubCommentFeatureProvider {

	public GitHubCommitCommentFeatureProvider() {
		valueFeatures.add(COMMIT_COMMENT_COMMIT_ID);
		valueFeatures.add(COMMIT_COMMENT_PATH);
		valueFeatures.add(COMMIT_COMMENT_POSITION);
	}

}
