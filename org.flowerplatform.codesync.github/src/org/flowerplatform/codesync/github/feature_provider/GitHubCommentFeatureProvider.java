package org.flowerplatform.codesync.github.feature_provider;

import static org.flowerplatform.codesync.github.GitHubConstants.COMMENT_BODY;
import static org.flowerplatform.codesync.github.GitHubConstants.COMMENT_CREATED_AT;
import static org.flowerplatform.codesync.github.GitHubConstants.COMMENT_ID;
import static org.flowerplatform.codesync.github.GitHubConstants.COMMENT_UPDATED_AT;
import static org.flowerplatform.codesync.github.GitHubConstants.COMMENT_USER;

import java.util.Arrays;
import java.util.List;

import org.flowerplatform.codesync.feature_provider.NodeFeatureProvider;
import org.flowerplatform.codesync.github.adapter.GitHubCommentModelAdapter;

/**
 * @see GitHubCommentModelAdapter
 * 
 * @author Mariana Gheorghe
 */
public class GitHubCommentFeatureProvider extends NodeFeatureProvider {

	@SuppressWarnings("unchecked")
	@Override
	public List<?> getValueFeatures(Object element) {
		@SuppressWarnings("rawtypes")
		List features = super.getValueFeatures(element);
		features.addAll(Arrays.asList(
				COMMENT_ID, COMMENT_BODY, COMMENT_USER,
				COMMENT_CREATED_AT, COMMENT_UPDATED_AT));
		return features;
	}
	
}
