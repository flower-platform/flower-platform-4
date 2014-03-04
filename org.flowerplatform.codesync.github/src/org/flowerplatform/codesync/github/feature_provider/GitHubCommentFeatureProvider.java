package org.flowerplatform.codesync.github.feature_provider;

import static org.flowerplatform.codesync.github.GitHubConstants.COMMENT_BODY;
import static org.flowerplatform.codesync.github.GitHubConstants.COMMENT_CREATED_AT;
import static org.flowerplatform.codesync.github.GitHubConstants.COMMENT_ID;
import static org.flowerplatform.codesync.github.GitHubConstants.COMMENT_UPDATED_AT;
import static org.flowerplatform.codesync.github.GitHubConstants.COMMENT_USER;

import org.flowerplatform.codesync.feature_provider.NodeFeatureProvider;
import org.flowerplatform.codesync.github.adapter.GitHubCommentModelAdapter;

/**
 * @see GitHubCommentModelAdapter
 * 
 * @author Mariana Gheorghe
 */
public class GitHubCommentFeatureProvider extends NodeFeatureProvider {

	public GitHubCommentFeatureProvider() {
		valueFeatures.add(COMMENT_ID);
		valueFeatures.add(COMMENT_BODY);
		valueFeatures.add(COMMENT_USER);
		valueFeatures.add(COMMENT_CREATED_AT);
		valueFeatures.add(COMMENT_UPDATED_AT);
	}
	
}
