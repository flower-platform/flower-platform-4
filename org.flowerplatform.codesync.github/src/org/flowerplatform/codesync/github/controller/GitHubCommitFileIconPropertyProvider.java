package org.flowerplatform.codesync.github.controller;

import static org.flowerplatform.codesync.github.GitHubConstants.COMMIT_FILE_STATUS;
import static org.flowerplatform.codesync.github.GitHubConstants.COMMIT_FILE_STATUS_ADDED;
import static org.flowerplatform.codesync.github.GitHubConstants.COMMIT_FILE_STATUS_MODIFIED;
import static org.flowerplatform.codesync.github.GitHubConstants.COMMIT_FILE_STATUS_REMOVED;
import static org.flowerplatform.codesync.github.GitHubConstants.COMMIT_FILE_STATUS_RENAMED;
import static org.flowerplatform.codesync.github.GitHubConstants.DECORATOR_ADDED;
import static org.flowerplatform.codesync.github.GitHubConstants.DECORATOR_MODIFIED;
import static org.flowerplatform.codesync.github.GitHubConstants.DECORATOR_REMOVED;
import static org.flowerplatform.codesync.github.GitHubConstants.DECORATOR_RENAMED;

import org.flowerplatform.codesync.github.CodeSyncGitHubPlugin;
import org.flowerplatform.core.node.controller.ConstantValuePropertyProvider;
import org.flowerplatform.core.node.remote.Node;

/**
 * @author Mariana Gheorghe
 */
public class GitHubCommitFileIconPropertyProvider extends ConstantValuePropertyProvider {

	public GitHubCommitFileIconPropertyProvider(String property, Object value) {
		super(property, value);
		
		setOrderIndex(50000);
	}

	@Override
	public void populateWithProperties(Node node) {
		String status = (String) node.getProperties().get(COMMIT_FILE_STATUS);
		if (status == null) {
			return;
		}
		
		String decorator = null;
		switch (status) {
		case COMMIT_FILE_STATUS_ADDED: 
			decorator = DECORATOR_ADDED;
			break;
		case COMMIT_FILE_STATUS_MODIFIED:
			decorator = DECORATOR_MODIFIED;
			break;
		case COMMIT_FILE_STATUS_RENAMED:
			decorator = DECORATOR_RENAMED;
			break;
		case COMMIT_FILE_STATUS_REMOVED:
			decorator = DECORATOR_REMOVED;
			break;
		default:
			break;
		}
		String icon = (String) getValue();
		if (decorator != null) {
			icon = icon + "|" + CodeSyncGitHubPlugin.getInstance().getResourcePath(decorator);
		}
		node.getProperties().put(getProperty(), icon);
	}

}
