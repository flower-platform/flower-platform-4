package org.flowerplatform.codesync.github.feature_provider;

import static org.flowerplatform.codesync.github.GitHubConstants.LINE;
import static org.flowerplatform.codesync.github.GitHubConstants.PATH;
import static org.flowerplatform.codesync.github.GitHubConstants.POSITION;

import java.util.Arrays;
import java.util.List;

/**
 * @author Mariana Gheorghe
 */
public class GitHubCommitCommentFeatureProvider extends GitHubCommentFeatureProvider {

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
