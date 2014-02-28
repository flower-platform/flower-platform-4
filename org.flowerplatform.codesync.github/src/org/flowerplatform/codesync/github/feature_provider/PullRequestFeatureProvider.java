package org.flowerplatform.codesync.github.feature_provider;

import static org.flowerplatform.codesync.github.GitHubPropertiesConstants.ADDITIONS;
import static org.flowerplatform.codesync.github.GitHubPropertiesConstants.ASSIGNEE;
import static org.flowerplatform.codesync.github.GitHubPropertiesConstants.BODY;
import static org.flowerplatform.codesync.github.GitHubPropertiesConstants.CHANGED_FILES;
import static org.flowerplatform.codesync.github.GitHubPropertiesConstants.CLOSED_AT;
import static org.flowerplatform.codesync.github.GitHubPropertiesConstants.COMMENTS;
import static org.flowerplatform.codesync.github.GitHubPropertiesConstants.COMMITS;
import static org.flowerplatform.codesync.github.GitHubPropertiesConstants.COMMIT_COMMENTS;
import static org.flowerplatform.codesync.github.GitHubPropertiesConstants.COMMIT_FILES;
import static org.flowerplatform.codesync.github.GitHubPropertiesConstants.DELETIONS;
import static org.flowerplatform.codesync.github.GitHubPropertiesConstants.HTML_URL;
import static org.flowerplatform.codesync.github.GitHubPropertiesConstants.MERGED_AT;
import static org.flowerplatform.codesync.github.GitHubPropertiesConstants.MERGED_BY;
import static org.flowerplatform.codesync.github.GitHubPropertiesConstants.NUMBER;
import static org.flowerplatform.codesync.github.GitHubPropertiesConstants.STATE;
import static org.flowerplatform.codesync.github.GitHubPropertiesConstants.UPDATED_AT;
import static org.flowerplatform.codesync.github.GitHubPropertiesConstants.USER;

import java.util.Arrays;
import java.util.List;

import org.flowerplatform.codesync.feature_provider.NodeFeatureProvider;

public class PullRequestFeatureProvider extends NodeFeatureProvider {

	
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
