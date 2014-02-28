package org.flowerplatform.codesync.github.feature_provider;

import static org.flowerplatform.codesync.github.GitHubPropertiesConstants.ADDITIONS;
import static org.flowerplatform.codesync.github.GitHubPropertiesConstants.BLOB_URL;
import static org.flowerplatform.codesync.github.GitHubPropertiesConstants.CHANGES;
import static org.flowerplatform.codesync.github.GitHubPropertiesConstants.DELETIONS;
import static org.flowerplatform.codesync.github.GitHubPropertiesConstants.FILENAME;
import static org.flowerplatform.codesync.github.GitHubPropertiesConstants.PATCH;
import static org.flowerplatform.codesync.github.GitHubPropertiesConstants.STATUS;

import java.util.Arrays;
import java.util.List;

import org.flowerplatform.codesync.feature_provider.NodeFeatureProvider;

public class CommitFileFeatureProvider extends NodeFeatureProvider {

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
