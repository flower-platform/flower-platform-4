package org.flowerplatform.codesync.github.feature_provider;

import static org.flowerplatform.codesync.github.GitHubConstants.PULL_REQUEST_ADDITIONS;
import static org.flowerplatform.codesync.github.GitHubConstants.PULL_REQUEST_ASSIGNEE;
import static org.flowerplatform.codesync.github.GitHubConstants.PULL_REQUEST_BODY;
import static org.flowerplatform.codesync.github.GitHubConstants.PULL_REQUEST_CHANGED_FILES;
import static org.flowerplatform.codesync.github.GitHubConstants.PULL_REQUEST_CLOSED_AT;
import static org.flowerplatform.codesync.github.GitHubConstants.PULL_REQUEST_COMMENTS;
import static org.flowerplatform.codesync.github.GitHubConstants.PULL_REQUEST_COMMITS;
import static org.flowerplatform.codesync.github.GitHubConstants.CONTAINMENT_COMMENTS;
import static org.flowerplatform.codesync.github.GitHubConstants.CONTAINMENT_COMMIT_FILES;
import static org.flowerplatform.codesync.github.GitHubConstants.PULL_REQUEST_DELETIONS;
import static org.flowerplatform.codesync.github.GitHubConstants.PULL_REQUEST_HTML_URL;
import static org.flowerplatform.codesync.github.GitHubConstants.PULL_REQUEST_MERGED_AT;
import static org.flowerplatform.codesync.github.GitHubConstants.PULL_REQUEST_MERGED_BY;
import static org.flowerplatform.codesync.github.GitHubConstants.PULL_REQUEST_NUMBER;
import static org.flowerplatform.codesync.github.GitHubConstants.PULL_REQUEST_STATE;
import static org.flowerplatform.codesync.github.GitHubConstants.PULL_REQUEST_UPDATED_AT;
import static org.flowerplatform.codesync.github.GitHubConstants.PULL_REQUEST_USER;

import java.util.Arrays;
import java.util.List;

import org.flowerplatform.codesync.feature_provider.NodeFeatureProvider;
import org.flowerplatform.codesync.github.adapter.GitHubPullRequestModelAdapter;

/**
 * @see GitHubPullRequestModelAdapter
 * 
 * @author Mariana Gheorghe
 */
public class GitHubPullRequestFeatureProvider extends NodeFeatureProvider {

	
	@SuppressWarnings("unchecked")
	@Override
	public List<?> getValueFeatures(Object element) {
		@SuppressWarnings("rawtypes")
		List features = super.getValueFeatures(element);
		features.addAll(Arrays.asList(
				PULL_REQUEST_NUMBER, PULL_REQUEST_BODY, PULL_REQUEST_HTML_URL, 
				PULL_REQUEST_STATE, PULL_REQUEST_CLOSED_AT, PULL_REQUEST_MERGED_AT, PULL_REQUEST_UPDATED_AT,
				PULL_REQUEST_ADDITIONS, PULL_REQUEST_DELETIONS, PULL_REQUEST_CHANGED_FILES, PULL_REQUEST_COMMITS, PULL_REQUEST_COMMENTS,
				PULL_REQUEST_USER, PULL_REQUEST_ASSIGNEE, PULL_REQUEST_MERGED_BY));
		return features;
	}

	@Override
	public List<?> getContainmentFeatures(Object element) {
		return Arrays.asList(CONTAINMENT_COMMIT_FILES, CONTAINMENT_COMMENTS);
	}

}
