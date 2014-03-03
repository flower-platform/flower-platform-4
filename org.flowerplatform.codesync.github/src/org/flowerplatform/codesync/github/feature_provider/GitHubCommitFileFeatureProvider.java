package org.flowerplatform.codesync.github.feature_provider;

import static org.flowerplatform.codesync.github.GitHubConstants.ADDITIONS;
import static org.flowerplatform.codesync.github.GitHubConstants.BLOB_URL;
import static org.flowerplatform.codesync.github.GitHubConstants.CHANGES;
import static org.flowerplatform.codesync.github.GitHubConstants.DELETIONS;
import static org.flowerplatform.codesync.github.GitHubConstants.FILENAME;
import static org.flowerplatform.codesync.github.GitHubConstants.PATCH;
import static org.flowerplatform.codesync.github.GitHubConstants.STATUS;

import java.util.Arrays;
import java.util.List;

import org.flowerplatform.codesync.feature_provider.NodeFeatureProvider;

/**
 * @author Mariana Gheorghe
 */
public class GitHubCommitFileFeatureProvider extends NodeFeatureProvider {

	@SuppressWarnings("unchecked")
	@Override
	public List<?> getValueFeatures(Object element) {
		@SuppressWarnings("rawtypes")
		List features = super.getValueFeatures(element);
		features.addAll(Arrays.asList(
				FILENAME, STATUS, BLOB_URL,
				PATCH, CHANGES, ADDITIONS, DELETIONS));
		return features;
	}

}
