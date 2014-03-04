package org.flowerplatform.codesync.github.adapter;

import static org.flowerplatform.codesync.CodeSyncPropertiesConstants.NAME;
import static org.flowerplatform.codesync.github.GitHubConstants.CONTAINMENT_PULL_REQUESTS;
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
import static org.flowerplatform.codesync.github.GitHubConstants.PULL_REQUEST_STATE_CLOSED;
import static org.flowerplatform.codesync.github.GitHubConstants.PULL_REQUEST_STATE_OPEN;
import static org.flowerplatform.codesync.github.GitHubConstants.PULL_REQUEST_TITLE;
import static org.flowerplatform.codesync.github.GitHubConstants.PULL_REQUEST_UPDATED_AT;
import static org.flowerplatform.codesync.github.GitHubConstants.PULL_REQUEST_USER;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.egit.github.core.IRepositoryIdProvider;
import org.eclipse.egit.github.core.PullRequest;
import org.eclipse.egit.github.core.PullRequestMarker;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.service.PullRequestService;
import org.flowerplatform.codesync.github.GitHubConstants;
import org.flowerplatform.codesync.github.feature_provider.GitHubRepositoryFeatureProvider;
import org.flowerplatform.codesync.type_provider.ITypeProvider;
import org.flowerplatform.core.node.remote.Node;

/**
 * @see GitHubRepositoryFeatureProvider
 * 
 * @author Mariana Gheorghe
 */
public class GitHubRepositoryModelAdapter extends GitHubAbstractModelAdapter {

	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		if (NAME.equals(feature)) {
			return getRepository(element).generateId();
		}
		return super.getValueFeatureValue(element, feature, correspondingValue);
	}

	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element, Object feature, Iterable<?> correspondingIterable) {
		if (GitHubConstants.CONTAINMENT_PULL_REQUESTS.equals(feature)) {
			PullRequestService service = getPullRequestService();
			IRepositoryIdProvider repository = getRepository(element);
			
			try {
				List<PullRequest> pullRequests = new ArrayList<PullRequest>();
				pullRequests.addAll(service.getPullRequests(repository, PULL_REQUEST_STATE_OPEN));
				pullRequests.addAll(service.getPullRequests(repository, PULL_REQUEST_STATE_CLOSED));
				return pullRequests;
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return super.getContainmentFeatureIterable(element, feature, correspondingIterable);
	}

	@Override
	public void setValueFeatureValue(Object element, Object feature, Object value) {
		super.setValueFeatureValue(element, feature, value);
	}

	@Override
	public Object createChildOnContainmentFeature(Object element, Object feature, Object correspondingChild, ITypeProvider typeProvider) {
		if (CONTAINMENT_PULL_REQUESTS.equals(feature)) {
			IRepositoryIdProvider repository = getRepository(element);
			PullRequest request = new PullRequest();
			
			Node node = (Node) correspondingChild;
			request.setTitle((String) getPropertyValueFromNode(node, PULL_REQUEST_TITLE));
			request.setBody((String) getPropertyValueFromNode(node, PULL_REQUEST_BODY));
			
			PullRequestMarker base = new PullRequestMarker();
			base.setLabel((String) getPropertyValueFromNode(node, PULL_REQUEST_BASE));
			request.setBase(base);
			PullRequestMarker head = new PullRequestMarker();
			head.setLabel((String) getPropertyValueFromNode(node, PULL_REQUEST_HEAD));
			request.setHead(head);
			
			try {
				request = getPullRequestService().createPullRequest(repository, request);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			
			// 
			setPropertyValueFromNode(node, NAME, request.toString());
			setPropertyValueFromNode(node, PULL_REQUEST_NUMBER, request.getNumber());
			setPropertyValueFromNode(node, PULL_REQUEST_HTML_URL, request.getHtmlUrl());
			setPropertyValueFromNode(node, PULL_REQUEST_MILESTONE, request.getMilestone());
			setPropertyValueFromNode(node, PULL_REQUEST_STATE, request.getState());
			setPropertyValueFromNode(node, PULL_REQUEST_CLOSED_AT, request.getClosedAt());
			setPropertyValueFromNode(node, PULL_REQUEST_MERGED_AT, request.getMergedAt());
			setPropertyValueFromNode(node, PULL_REQUEST_UPDATED_AT, request.getUpdatedAt());
			setPropertyValueFromNode(node, PULL_REQUEST_MERGED, request.isMerged());
			setPropertyValueFromNode(node, PULL_REQUEST_MERGEABLE, request.isMergeable());
			setPropertyValueFromNode(node, PULL_REQUEST_ADDITIONS, request.getAdditions());
			setPropertyValueFromNode(node, PULL_REQUEST_DELETIONS, request.getDeletions());
			setPropertyValueFromNode(node, PULL_REQUEST_CHANGED_FILES, request.getChangedFiles());
			setPropertyValueFromNode(node, PULL_REQUEST_COMMITS, request.getCommits());
			setPropertyValueFromNode(node, PULL_REQUEST_COMMENTS, request.getComments());
			setPropertyValueFromNode(node, PULL_REQUEST_USER, getLogin(request.getUser()));
			setPropertyValueFromNode(node, PULL_REQUEST_ASSIGNEE, getLogin(request.getAssignee()));
			setPropertyValueFromNode(node, PULL_REQUEST_MERGED_BY, getLogin(request.getMergedBy()));
			
			return request;
		}
		return super.createChildOnContainmentFeature(element, feature, correspondingChild, typeProvider);
	}

	@Override
	public Object getMatchKey(Object element) {
		// TODO Auto-generated method stub
		return null;
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
	
	protected String getLabel(Object element, String branch) {
		return getRepositoryOwner(element) + ":" + branch;
	}
	
	protected String getRepositoryOwner(Object element) {
		if (element instanceof Repository) {
			return ((Repository) element).getOwner().getLogin();
		} else if (element instanceof RepositoryId) {
			return ((RepositoryId) element).getOwner();
		} 
		return null;
	}
	
	protected IRepositoryIdProvider getRepository(Object element) {
		return (IRepositoryIdProvider) element;
	}

}
