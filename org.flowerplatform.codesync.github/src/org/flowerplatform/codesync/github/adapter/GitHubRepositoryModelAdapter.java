package org.flowerplatform.codesync.github.adapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.egit.github.core.IRepositoryIdProvider;
import org.eclipse.egit.github.core.PullRequest;
import org.eclipse.egit.github.core.PullRequestMarker;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.service.PullRequestService;
import org.flowerplatform.codesync.adapter.AbstractModelAdapter;
import org.flowerplatform.codesync.feature_provider.NodeFeatureProvider;
import org.flowerplatform.codesync.github.CodeSyncGitHubPlugin;
import org.flowerplatform.codesync.github.GitHubConstants;
import org.flowerplatform.codesync.github.feature_provider.GitHubRepositoryFeatureProvider;
import org.flowerplatform.codesync.type_provider.ITypeProvider;
import org.flowerplatform.core.node.remote.Node;

/**
 * @see GitHubRepositoryFeatureProvider
 * 
 * @author Mariana Gheorghe
 */
public class GitHubRepositoryModelAdapter extends AbstractModelAdapter {

	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		if (NodeFeatureProvider.NAME.equals(feature)) {
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
				pullRequests.addAll(service.getPullRequests(repository, "open"));
//				pullRequests.addAll(service.getPullRequests(repository, "closed"));
				return pullRequests;
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return super.getContainmentFeatureIterable(element, feature, correspondingIterable);
	}

	@Override
	public void setValueFeatureValue(Object element, Object feature, Object value) {
		// TODO Auto-generated method stub
		super.setValueFeatureValue(element, feature, value);
	}

	@Override
	public Object createChildOnContainmentFeature(Object element, Object feature, Object correspondingChild, ITypeProvider typeProvider) {
		IRepositoryIdProvider repository = getRepository(element);
		PullRequest request = new PullRequest();
		Node node = (Node) correspondingChild;
		request.setTitle((String) node.getOrPopulateProperties().get("text"));
		PullRequestMarker base = new PullRequestMarker();
		base.setLabel(getLabel(element, "master"));
		request.setBase(base);
		PullRequestMarker head = new PullRequestMarker();
		head.setLabel(getLabel(element, "test"));
		request.setHead(head);
		try {
			return getPullRequestService().createPullRequest(repository, request);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void removeChildrenOnContainmentFeature(Object parent, Object feature, Object child) {
		// TODO Auto-generated method stub
		super.removeChildrenOnContainmentFeature(parent, feature, child);
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
	
	protected PullRequestService getPullRequestService() {
		return new PullRequestService(CodeSyncGitHubPlugin.getInstance().getClient());
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
