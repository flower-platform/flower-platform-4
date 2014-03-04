package org.flowerplatform.codesync.github.feature_provider;

import static org.flowerplatform.codesync.github.GitHubConstants.CONTAINMENT_COMMENTS;
import static org.flowerplatform.codesync.github.GitHubConstants.CONTAINMENT_COMMIT_FILES;
import static org.flowerplatform.codesync.github.GitHubConstants.PULL_REQUEST_ADDITIONS;
import static org.flowerplatform.codesync.github.GitHubConstants.PULL_REQUEST_ASSIGNEE;
import static org.flowerplatform.codesync.github.GitHubConstants.PULL_REQUEST_BASE;
import static org.flowerplatform.codesync.github.GitHubConstants.PULL_REQUEST_BODY;
import static org.flowerplatform.codesync.github.GitHubConstants.PULL_REQUEST_CHANGED_FILES;
import static org.flowerplatform.codesync.github.GitHubConstants.PULL_REQUEST_CLOSED_AT;
import static org.flowerplatform.codesync.github.GitHubConstants.PULL_REQUEST_COMMENTS;
import static org.flowerplatform.codesync.github.GitHubConstants.PULL_REQUEST_COMMITS;
import static org.flowerplatform.codesync.github.GitHubConstants.PULL_REQUEST_DELETIONS;
import static org.flowerplatform.codesync.github.GitHubConstants.PULL_REQUEST_HEAD;
import static org.flowerplatform.codesync.github.GitHubConstants.PULL_REQUEST_HTML_URL;
import static org.flowerplatform.codesync.github.GitHubConstants.PULL_REQUEST_MERGEABLE;
import static org.flowerplatform.codesync.github.GitHubConstants.PULL_REQUEST_MERGED;
import static org.flowerplatform.codesync.github.GitHubConstants.PULL_REQUEST_MERGED_AT;
import static org.flowerplatform.codesync.github.GitHubConstants.PULL_REQUEST_MERGED_BY;
import static org.flowerplatform.codesync.github.GitHubConstants.PULL_REQUEST_MILESTONE;
import static org.flowerplatform.codesync.github.GitHubConstants.PULL_REQUEST_NUMBER;
import static org.flowerplatform.codesync.github.GitHubConstants.PULL_REQUEST_STATE;
import static org.flowerplatform.codesync.github.GitHubConstants.PULL_REQUEST_TITLE;
import static org.flowerplatform.codesync.github.GitHubConstants.PULL_REQUEST_UPDATED_AT;
import static org.flowerplatform.codesync.github.GitHubConstants.PULL_REQUEST_USER;

import org.flowerplatform.codesync.feature_provider.NodeFeatureProvider;
import org.flowerplatform.codesync.github.adapter.GitHubPullRequestModelAdapter;

/**
 * @see GitHubPullRequestModelAdapter
 * 
 * @author Mariana Gheorghe
 */
public class GitHubPullRequestFeatureProvider extends NodeFeatureProvider {

	public GitHubPullRequestFeatureProvider() {
		valueFeatures.add(PULL_REQUEST_NUMBER);
		valueFeatures.add(PULL_REQUEST_TITLE);
		valueFeatures.add(PULL_REQUEST_BODY);
		valueFeatures.add(PULL_REQUEST_HTML_URL);
		valueFeatures.add(PULL_REQUEST_MILESTONE);
		
		valueFeatures.add(PULL_REQUEST_BASE);
		valueFeatures.add(PULL_REQUEST_HEAD);
		
		valueFeatures.add(PULL_REQUEST_STATE);
		valueFeatures.add(PULL_REQUEST_CLOSED_AT);
		valueFeatures.add(PULL_REQUEST_MERGED_AT);
		valueFeatures.add(PULL_REQUEST_UPDATED_AT);
		valueFeatures.add(PULL_REQUEST_MERGED);
		valueFeatures.add(PULL_REQUEST_MERGEABLE);
		
		valueFeatures.add(PULL_REQUEST_ADDITIONS);
		valueFeatures.add(PULL_REQUEST_DELETIONS);
		valueFeatures.add(PULL_REQUEST_CHANGED_FILES);
		valueFeatures.add(PULL_REQUEST_COMMITS);
		valueFeatures.add(PULL_REQUEST_COMMENTS);
		valueFeatures.add(PULL_REQUEST_USER);
		valueFeatures.add(PULL_REQUEST_ASSIGNEE);
		valueFeatures.add(PULL_REQUEST_MERGED_BY);

		containmentFeatures.add(CONTAINMENT_COMMIT_FILES);
		containmentFeatures.add(CONTAINMENT_COMMENTS);
	}

}
