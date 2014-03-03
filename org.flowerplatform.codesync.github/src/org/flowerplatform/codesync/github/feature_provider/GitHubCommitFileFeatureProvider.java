package org.flowerplatform.codesync.github.feature_provider;

import static org.flowerplatform.codesync.github.GitHubConstants.COMMIT_FILE_ADDITIONS;
import static org.flowerplatform.codesync.github.GitHubConstants.COMMIT_FILE_BLOB_URL;
import static org.flowerplatform.codesync.github.GitHubConstants.COMMIT_FILE_CHANGES;
import static org.flowerplatform.codesync.github.GitHubConstants.COMMIT_FILE_DELETIONS;
import static org.flowerplatform.codesync.github.GitHubConstants.COMMIT_FILE_NAME;
import static org.flowerplatform.codesync.github.GitHubConstants.COMMIT_FILE_PATCH;
import static org.flowerplatform.codesync.github.GitHubConstants.COMMIT_FILE_STATUS;

import java.util.Arrays;
import java.util.List;

import org.flowerplatform.codesync.feature_provider.NodeFeatureProvider;
import org.flowerplatform.codesync.github.adapter.GitHubCommitFileModelAdapter;

/**
 * @see GitHubCommitFileModelAdapter
 * 
 * @author Mariana Gheorghe
 */
public class GitHubCommitFileFeatureProvider extends NodeFeatureProvider {

	@SuppressWarnings("unchecked")
	@Override
	public List<?> getValueFeatures(Object element) {
		@SuppressWarnings("rawtypes")
		List features = super.getValueFeatures(element);
		features.addAll(Arrays.asList(
				COMMIT_FILE_NAME, COMMIT_FILE_STATUS, COMMIT_FILE_BLOB_URL,
				COMMIT_FILE_PATCH, COMMIT_FILE_CHANGES, COMMIT_FILE_ADDITIONS, COMMIT_FILE_DELETIONS));
		return features;
	}

}
