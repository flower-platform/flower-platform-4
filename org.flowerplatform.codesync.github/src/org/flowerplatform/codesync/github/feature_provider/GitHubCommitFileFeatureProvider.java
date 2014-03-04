package org.flowerplatform.codesync.github.feature_provider;

import static org.flowerplatform.codesync.github.GitHubConstants.COMMIT_FILE_ADDITIONS;
import static org.flowerplatform.codesync.github.GitHubConstants.COMMIT_FILE_BLOB_URL;
import static org.flowerplatform.codesync.github.GitHubConstants.COMMIT_FILE_CHANGES;
import static org.flowerplatform.codesync.github.GitHubConstants.COMMIT_FILE_DELETIONS;
import static org.flowerplatform.codesync.github.GitHubConstants.COMMIT_FILE_NAME;
import static org.flowerplatform.codesync.github.GitHubConstants.COMMIT_FILE_PATCH;
import static org.flowerplatform.codesync.github.GitHubConstants.COMMIT_FILE_SHA;
import static org.flowerplatform.codesync.github.GitHubConstants.COMMIT_FILE_STATUS;

import org.flowerplatform.codesync.feature_provider.NodeFeatureProvider;
import org.flowerplatform.codesync.github.adapter.GitHubCommitFileModelAdapter;

/**
 * @see GitHubCommitFileModelAdapter
 * 
 * @author Mariana Gheorghe
 */
public class GitHubCommitFileFeatureProvider extends NodeFeatureProvider {

	public GitHubCommitFileFeatureProvider() {
		valueFeatures.add(COMMIT_FILE_NAME);
		valueFeatures.add(COMMIT_FILE_SHA);
		valueFeatures.add(COMMIT_FILE_STATUS);
		valueFeatures.add(COMMIT_FILE_BLOB_URL);
		valueFeatures.add(COMMIT_FILE_PATCH);
		valueFeatures.add(COMMIT_FILE_CHANGES);
		valueFeatures.add(COMMIT_FILE_ADDITIONS);
		valueFeatures.add(COMMIT_FILE_DELETIONS);
	}

}
