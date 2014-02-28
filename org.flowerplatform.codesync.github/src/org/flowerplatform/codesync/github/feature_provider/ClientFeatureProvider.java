package org.flowerplatform.codesync.github.feature_provider;

import java.util.Arrays;
import java.util.List;

import org.flowerplatform.codesync.feature_provider.NodeFeatureProvider;
import org.flowerplatform.codesync.github.GitHubPropertiesConstants;

public class ClientFeatureProvider extends NodeFeatureProvider {

	@Override
	public List<?> getContainmentFeatures(Object element) {
		return Arrays.asList(GitHubPropertiesConstants.PULL_REQUESTS);
	}
	
}
