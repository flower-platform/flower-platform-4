package org.flowerplatform.codesync.github.feature_provider;

import static org.flowerplatform.codesync.github.GitHubConstants.ADDITIONS;
import static org.flowerplatform.codesync.github.GitHubConstants.ASSIGNEE;
import static org.flowerplatform.codesync.github.GitHubConstants.BODY;
import static org.flowerplatform.codesync.github.GitHubConstants.CHANGED_FILES;
import static org.flowerplatform.codesync.github.GitHubConstants.CLOSED_AT;
import static org.flowerplatform.codesync.github.GitHubConstants.COMMENTS;
import static org.flowerplatform.codesync.github.GitHubConstants.COMMITS;
import static org.flowerplatform.codesync.github.GitHubConstants.COMMIT_COMMENTS;
import static org.flowerplatform.codesync.github.GitHubConstants.COMMIT_FILES;
import static org.flowerplatform.codesync.github.GitHubConstants.DELETIONS;
import static org.flowerplatform.codesync.github.GitHubConstants.HTML_URL;
import static org.flowerplatform.codesync.github.GitHubConstants.MERGED_AT;
import static org.flowerplatform.codesync.github.GitHubConstants.MERGED_BY;
import static org.flowerplatform.codesync.github.GitHubConstants.NUMBER;
import static org.flowerplatform.codesync.github.GitHubConstants.STATE;
import static org.flowerplatform.codesync.github.GitHubConstants.UPDATED_AT;
import static org.flowerplatform.codesync.github.GitHubConstants.USER;

import java.util.Arrays;
import java.util.List;

import org.flowerplatform.codesync.feature_provider.NodeFeatureProvider;

/**
 * @author Mariana Gheorghe
 */
public class GitHubPullRequestFeatureProvider extends NodeFeatureProvider {

	
	@SuppressWarnings("unchecked")
	@Override
	public List<?> getValueFeatures(Object element) {
		@SuppressWarnings("rawtypes")
		List features = super.getValueFeatures(element);
		features.addAll(Arrays.asList(
				NUMBER, BODY, HTML_URL, 
				STATE, CLOSED_AT, MERGED_AT, UPDATED_AT,
				ADDITIONS, DELETIONS, CHANGED_FILES, COMMITS, COMMENTS,
				USER, ASSIGNEE, MERGED_BY));
		return features;
	}

	@Override
	public List<?> getContainmentFeatures(Object element) {
		return Arrays.asList(COMMIT_FILES, COMMIT_COMMENTS);
	}

}
