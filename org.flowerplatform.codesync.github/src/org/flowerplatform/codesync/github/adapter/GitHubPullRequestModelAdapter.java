package org.flowerplatform.codesync.github.adapter;

import static org.flowerplatform.codesync.CodeSyncPropertiesConstants.NAME;
import static org.flowerplatform.codesync.github.GitHubConstants.COMMENT_BODY;
import static org.flowerplatform.codesync.github.GitHubConstants.COMMIT_COMMENT_COMMIT_ID;
import static org.flowerplatform.codesync.github.GitHubConstants.COMMIT_COMMENT_PATH;
import static org.flowerplatform.codesync.github.GitHubConstants.COMMIT_COMMENT_POSITION;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.egit.github.core.Comment;
import org.eclipse.egit.github.core.CommitComment;
import org.eclipse.egit.github.core.CommitFile;
import org.eclipse.egit.github.core.IRepositoryIdProvider;
import org.eclipse.egit.github.core.PullRequest;
import org.flowerplatform.codesync.github.GitHubConstants;
import org.flowerplatform.codesync.github.feature_provider.GitHubPullRequestFeatureProvider;
import org.flowerplatform.codesync.type_provider.ITypeProvider;
import org.flowerplatform.core.node.remote.Node;

/**
 * Mapped to {@link PullRequest}. Children are {@link CommitFile}s and {@link Comment}s.
 * 
 * @see GitHubPullRequestFeatureProvider
 * 
 * @author Mariana Gheorghe
 */
public class GitHubPullRequestModelAdapter extends GitHubAbstractModelAdapter {

	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		PullRequest request = getPullRequest(element);
		if (PULL_REQUEST_NUMBER.equals(feature)) {
			return request.getNumber();
		} else if (PULL_REQUEST_TITLE.equals(feature)) {
			return request.getTitle();
		} else if (PULL_REQUEST_BODY.equals(feature)) {
			return request.getBody();
		} else if (PULL_REQUEST_HTML_URL.equals(feature)) {
			return request.getHtmlUrl();
		} else if (PULL_REQUEST_MILESTONE.equals(feature)) {
			return request.getMilestone() == null ? null : request.getMilestone().getTitle();
		} else if (PULL_REQUEST_BASE.equals(feature)) {
			return request.getBase().getLabel();
		} else if (PULL_REQUEST_HEAD.equals(feature)) {
			return request.getHead().getLabel();
		} else if (PULL_REQUEST_STATE.equals(feature)) {
			return request.getState();
		} else if (PULL_REQUEST_CLOSED_AT.equals(feature)) {
			return request.getClosedAt();
		} else if (PULL_REQUEST_MERGED_AT.equals(feature)) {
			return request.getMergedAt();
		} else if (PULL_REQUEST_UPDATED_AT.equals(feature)) {
			return request.getUpdatedAt();
		} else if (PULL_REQUEST_MERGED.equals(feature)) {
			return request.isMerged();
		} else if (PULL_REQUEST_MERGEABLE.equals(feature)) {
			return request.isMergeable();
		} else if (PULL_REQUEST_ADDITIONS.equals(feature)) {
			return request.getAdditions();
		} else if (PULL_REQUEST_DELETIONS.equals(feature)) {
			return request.getDeletions();
		} else if (PULL_REQUEST_CHANGED_FILES.equals(feature)) {
			return request.getChangedFiles();
		} else if (PULL_REQUEST_COMMITS.equals(feature)) {
			return request.getCommits();
		} else if (PULL_REQUEST_COMMENTS.equals(feature)) {
			return request.getComments();
		} else if (PULL_REQUEST_USER.equals(feature)) {
			return getLogin(request.getUser());
		} else if (PULL_REQUEST_ASSIGNEE.equals(feature)) {
			return getLogin(request.getAssignee());
		} else if (PULL_REQUEST_MERGED_BY.equals(feature)) {
			return getLogin(request.getMergedBy());
		} else if (NAME.equals(feature)) {
			return request.toString();
		}
		return super.getValueFeatureValue(element, feature, correspondingValue);
	}
	
	@Override
	public void setValueFeatureValue(Object element, Object feature, Object value) {
		boolean valueSet = false;
		PullRequest request = getPullRequest(element);
		IRepositoryIdProvider repository = request.getBase().getRepo();
		if (PULL_REQUEST_TITLE.equals(feature)) {
			request.setTitle((String) value);
			valueSet = true;
		} else if (PULL_REQUEST_BODY.equals(feature)) {
			request.setBody((String) value);
			valueSet = true;
		} else if (PULL_REQUEST_STATE.equals(feature)) {
			request.setState((String) value);
			valueSet = true;
		}
		
		if (valueSet) {
			try {
				getPullRequestService().editPullRequest(repository, request);
				return;
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		
		super.setValueFeatureValue(element, feature, value);
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
			IRepositoryIdProvider repository = getPullRequest(element).getBase().getRepo();
			int number = getPullRequest(element).getNumber();
			Comment comment = null;
			switch (node.getType()) {
			case GitHubConstants.COMMIT_COMMENT:
				CommitComment commitComment = new CommitComment();
				commitComment.setBody((String) node.getOrPopulateProperties().get(COMMENT_BODY));
				commitComment.setCommitId((String) node.getOrPopulateProperties().get(COMMIT_COMMENT_COMMIT_ID));
				commitComment.setPath((String) node.getOrPopulateProperties().get(COMMIT_COMMENT_PATH));
				commitComment.setPosition((int) node.getOrPopulateProperties().get(COMMIT_COMMENT_POSITION));
				try {
					comment = getPullRequestService().createComment(repository, number, commitComment);
					break;
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			case GitHubConstants.COMMENT:
				try {
					comment = getIssueService().createComment(repository, number, 
							(String) (node.getOrPopulateProperties().get(COMMENT_BODY)));
					break;
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
			
			// set the node's properties from the newly created comment
			setPropertyValueFromNode(node, GitHubConstants.COMMENT_ID, comment.getId());
			setPropertyValueFromNode(node, NAME, String.valueOf(comment.getId()));
			setPropertyValueFromNode(node, GitHubConstants.COMMENT_USER, comment.getUser().getLogin());
			setPropertyValueFromNode(node, GitHubConstants.COMMENT_CREATED_AT, comment.getCreatedAt());
			setPropertyValueFromNode(node, GitHubConstants.COMMENT_UPDATED_AT, comment.getUpdatedAt());
			
			return comment;
		}
		return super.createChildOnContainmentFeature(element, feature, correspondingChild, typeProvider);
	}

	@Override
	public void removeChildrenOnContainmentFeature(Object parent, Object feature, Object child) {
		if (CONTAINMENT_COMMENTS.equals(feature)) {
			IRepositoryIdProvider repository = getPullRequest(parent).getBase().getRepo();
			if (child instanceof CommitComment) {
				try {
					getPullRequestService().deleteComment(repository, ((CommitComment) child).getId());
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			} else if (child instanceof Comment) {
				try {
					getIssueService().deleteComment(repository, ((Comment) child).getId());
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
			return;
		}
		super.removeChildrenOnContainmentFeature(parent, feature, child);
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
	
	protected PullRequest getPullRequest(Object element) {
		return (PullRequest) element;
	}
	
}
