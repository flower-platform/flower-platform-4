package org.flowerplatform.codesync.github.feature_provider;

import static org.flowerplatform.codesync.github.GitHubPropertiesConstants.LINE;
import static org.flowerplatform.codesync.github.GitHubPropertiesConstants.PATH;
import static org.flowerplatform.codesync.github.GitHubPropertiesConstants.POSITION;

import java.util.Arrays;
import java.util.List;

public class CommitCommentFeatureProvider extends CommentFeatureProvider {

	@SuppressWarnings("unchecked")
	@Override
	public List<?> getValueFeatures(Object element) {
		@SuppressWarnings("rawtypes")
		List features = super.getValueFeatures(element);
		features.addAll(Arrays.asList(
				PATH, LINE, POSITION));
		return features;
	}

}
