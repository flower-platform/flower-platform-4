package org.flowerplatform.codesync.github.adapter;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.egit.github.core.Comment;
import org.eclipse.egit.github.core.CommitComment;
import org.eclipse.egit.github.core.CommitFile;
import org.eclipse.egit.github.core.IRepositoryIdProvider;
import org.eclipse.egit.github.core.PullRequest;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.service.IssueService;
import org.eclipse.egit.github.core.service.PullRequestService;
import org.flowerplatform.codesync.adapter.AbstractModelAdapter;
import org.flowerplatform.codesync.feature_provider.NodeFeatureProvider;
import org.flowerplatform.codesync.github.CodeSyncGitHubPlugin;
import org.flowerplatform.codesync.github.GitHubConstants;
import org.flowerplatform.codesync.github.feature_provider.GitHubPullRequestFeatureProvider;
import org.flowerplatform.codesync.type_provider.ITypeProvider;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.remote.Node;

/**
 * Mapped to {@link PullRequest}. Children are {@link CommitFile}s and {@link Comment}s.
 * 
 * @see GitHubPullRequestFeatureProvider
 * 
 * @author Mariana Gheorghe
 */
public class GitHubPullRequestModelAdapter extends AbstractModelAdapter {

	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		PullRequest pull = getPullRequest(element);
		if (PULL_REQUEST_NUMBER.equals(feature)) {
			return pull.getNumber();
		} else if (PULL_REQUEST_BODY.equals(feature)) {
			return pull.getBody();
		} else if (PULL_REQUEST_HTML_URL.equals(feature)) {
			return pull.getHtmlUrl();
		} else if (PULL_REQUEST_STATE.equals(feature)) {
			return pull.getState();
		} else if (PULL_REQUEST_CLOSED_AT.equals(feature)) {
			return pull.getClosedAt();
		} else if (PULL_REQUEST_MERGED_AT.equals(feature)) {
			return pull.getMergedAt();
		} else if (PULL_REQUEST_UPDATED_AT.equals(feature)) {
			return pull.getUpdatedAt();
		} else if (PULL_REQUEST_ADDITIONS.equals(feature)) {
			return pull.getAdditions();
		} else if (PULL_REQUEST_DELETIONS.equals(feature)) {
			return pull.getDeletions();
		} else if (PULL_REQUEST_CHANGED_FILES.equals(feature)) {
			return pull.getChangedFiles();
		} else if (PULL_REQUEST_COMMITS.equals(feature)) {
			return pull.getCommits();
		} else if (PULL_REQUEST_COMMENTS.equals(feature)) {
			return pull.getComments();
		} else if (PULL_REQUEST_USER.equals(feature)) {
			return getLogin(pull.getUser());
		} else if (PULL_REQUEST_ASSIGNEE.equals(feature)) {
			return getLogin(pull.getAssignee());
		} else if (PULL_REQUEST_MERGED_BY.equals(feature)) {
			return getLogin(pull.getMergedBy());
		} else if (NodeFeatureProvider.NAME.equals(feature)) {
			return pull.toString();
		}
		return super.getValueFeatureValue(element, feature, correspondingValue);
	}
	
	private String getLogin(User user) {
		return user == null ? "" : user.getLogin();
	}

	@Override
	public void setValueFeatureValue(Object element, Object feature, Object value) {
		// TODO
	}

	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element, Object feature, Iterable<?> correspondingIterable) {
		IRepositoryIdProvider repository = getPullRequest(element).getBase().getRepo();
		int id = getPullRequest(element).getNumber();
		if (CONTAINMENT_COMMIT_FILES.equals(feature)) {
			try {
				return getPullRequestService().getFiles(repository, id);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		} else if (CONTAINMENT_COMMENTS.equals(feature)) {
			List<Comment> comments = new ArrayList<Comment>();
			try {
				comments.addAll(getPullRequestService().getComments(repository, id));
				comments.addAll(getIssueService().getComments(repository, id));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			return comments;
		}
		return super.getContainmentFeatureIterable(element, feature, correspondingIterable);
	}

	@Override
	public Object createChildOnContainmentFeature(Object element, Object feature, Object correspondingChild, ITypeProvider typeProvider) {
		if (CONTAINMENT_COMMENTS.equals(feature)) {
			Node node = (Node) correspondingChild;
			IRepositoryIdProvider repository = getRepository(element);
			int number = getPullRequest(element).getNumber();
			Comment comment = null;
			switch (node.getType()) {
			case GitHubConstants.COMMIT_COMMENT:
				CommitComment commitComment = new CommitComment();
				commitComment.setBody("test - " + new Date());
				commitComment.setCommitId("2adfd131f887fdb801e433d5112dcf6fd29adbdd");
				commitComment.setPath("my_proj/src/my_proj/TestClass.java");
				commitComment.setPosition(3);
				try {
					comment = getPullRequestService().createComment(repository, number, commitComment);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			case GitHubConstants.COMMENT:
				try {
					comment = getIssueService().createComment(repository, number, "test - " + new Date());
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
			
			// set the node's id
			CorePlugin.getInstance().getNodeService().setProperty(node, GitHubConstants.COMMENT_ID, comment.getId());
			CorePlugin.getInstance().getNodeService().setProperty(node, NodeFeatureProvider.NAME, String.valueOf(comment.getId()));
			
			return comment;
		}
		return super.createChildOnContainmentFeature(element, feature, correspondingChild, typeProvider);
	}

	@Override
	public void removeChildrenOnContainmentFeature(Object parent, Object feature, Object child) {
		// TODO
	}

	@Override
	public Object getMatchKey(Object element) {
		return getPullRequest(element).toString();
	}

	@Override
	public boolean hasChildren(Object modelElement) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<?> getChildren(Object modelElement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLabel(Object modelElement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getIconUrls(Object modelElement) {
		// TODO Auto-generated method stub
		return null;
	}

	protected IRepositoryIdProvider getRepository(Object element) {
		return getPullRequest(element).getBase().getRepo();
	}
	
	protected PullRequestService getPullRequestService() {
		return new PullRequestService(CodeSyncGitHubPlugin.getInstance().getClient());
	}
	
	protected IssueService getIssueService() {
		return new IssueService(CodeSyncGitHubPlugin.getInstance().getClient());
	}
	
	protected PullRequest getPullRequest(Object element) {
		return (PullRequest) element;
	}
	
}
