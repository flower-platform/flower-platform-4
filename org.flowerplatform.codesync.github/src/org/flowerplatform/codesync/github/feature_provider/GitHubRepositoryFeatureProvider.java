package org.flowerplatform.codesync.github.feature_provider;

import static org.flowerplatform.codesync.github.GitHubConstants.CONTAINMENT_PULL_REQUESTS;

import org.flowerplatform.codesync.feature_provider.NodeFeatureProvider;
import org.flowerplatform.codesync.github.adapter.GitHubRepositoryModelAdapter;

/**
 * @see GitHubRepositoryModelAdapter
 * 
 * @author Mariana Gheorghe
 */
public class GitHubRepositoryFeatureProvider extends NodeFeatureProvider {

	public GitHubRepositoryFeatureProvider() {
		containmentFeatures.add(CONTAINMENT_PULL_REQUESTS);
	}
	
}
