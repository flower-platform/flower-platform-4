package org.flowerplatform.codesync.github.adapter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.IGitHubConstants;
import org.eclipse.egit.github.core.service.IssueService;
import org.eclipse.egit.github.core.service.PullRequestService;
import org.flowerplatform.codesync.adapter.AbstractModelAdapter;
import org.flowerplatform.codesync.github.CodeSyncGitHubPlugin;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.remote.Node;

/**
 * @author Mariana Gheorghe
 */
public abstract class GitHubAbstractModelAdapter extends AbstractModelAdapter {

	protected PullRequestService getPullRequestService() {
		return new PullRequestService(CodeSyncGitHubPlugin.getInstance().getClient());
	}
	
	protected IssueService getIssueService() {
		return new IssueService(CodeSyncGitHubPlugin.getInstance().getClient());
	}
	
	/**
	 * Gets the repository id from an URL, e.g. /repos/<b>:owner/:repo</b>/pulls/comments/:number
	 */
	protected String getRepositoryIdFromURL(String url) {
		Pattern pattern = Pattern.compile(IGitHubConstants.SEGMENT_REPOS + "/(.+?/.+?)/");
		Matcher matcher = pattern.matcher(url);
		matcher.find();
		return matcher.group(1);
	}
	
	protected String getLogin(User user) {
		return user == null ? "" : user.getLogin();
	}
	
	protected Object getPropertyValueFromNode(Node node, String property) {
		return node.getOrPopulateProperties().get(property);
	}
	
	protected void setPropertyValueFromNode(Node node, String property, Object value) {
		if (value != null) {
			CorePlugin.getInstance().getNodeService().setProperty(node, property, value);
		}
	}

}
