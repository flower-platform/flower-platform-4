package org.flowerplatform.codesync.github.feature_provider;

import static org.flowerplatform.codesync.github.GitHubConstants.COMMIT_COMMENT_COMMIT_ID;
import static org.flowerplatform.codesync.github.GitHubConstants.COMMIT_COMMENT_LINE;
import static org.flowerplatform.codesync.github.GitHubConstants.COMMIT_COMMENT_PATH;
import static org.flowerplatform.codesync.github.GitHubConstants.COMMIT_COMMENT_POSITION;

import java.util.Arrays;
import java.util.List;

import org.flowerplatform.codesync.github.adapter.GitHubCommitCommentModelAdapter;

/**
 * @see GitHubCommitCommentModelAdapter
 * 
 * @author Mariana Gheorghe
 */
public class GitHubCommitCommentFeatureProvider extends GitHubCommentFeatureProvider {

	@SuppressWarnings("unchecked")
	@Override
	public List<?> getValueFeatures(Object element) {
		@SuppressWarnings("rawtypes")
		List features = super.getValueFeatures(element);
		features.addAll(Arrays.asList(
				COMMIT_COMMENT_COMMIT_ID, COMMIT_COMMENT_PATH, 
				COMMIT_COMMENT_LINE, COMMIT_COMMENT_POSITION));
		return features;
	}

}
