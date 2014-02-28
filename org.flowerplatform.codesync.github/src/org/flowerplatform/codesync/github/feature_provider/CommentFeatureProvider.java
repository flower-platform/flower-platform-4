package org.flowerplatform.codesync.github.feature_provider;

import static org.flowerplatform.codesync.github.GitHubPropertiesConstants.BODY;
import static org.flowerplatform.codesync.github.GitHubPropertiesConstants.COMMENT_ID;
import static org.flowerplatform.codesync.github.GitHubPropertiesConstants.CREATED_AT;
import static org.flowerplatform.codesync.github.GitHubPropertiesConstants.UPDATED_AT;
import static org.flowerplatform.codesync.github.GitHubPropertiesConstants.USER;

import java.util.Arrays;
import java.util.List;

import org.flowerplatform.codesync.feature_provider.NodeFeatureProvider;

public class CommentFeatureProvider extends NodeFeatureProvider {

	@SuppressWarnings("unchecked")
	@Override
	public List<?> getValueFeatures(Object element) {
		@SuppressWarnings("rawtypes")
		List features = super.getValueFeatures(element);
		features.addAll(Arrays.asList(
				COMMENT_ID, BODY, USER,
				CREATED_AT, UPDATED_AT));
		return features;
	}
	
}
