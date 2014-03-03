package org.flowerplatform.codesync.github.adapter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.eclipse.egit.github.core.IRepositoryIdProvider;
import org.eclipse.egit.github.core.service.PullRequestService;
import org.flowerplatform.codesync.adapter.AbstractModelAdapter;
import org.flowerplatform.codesync.feature_provider.NodeFeatureProvider;
import org.flowerplatform.codesync.github.CodeSyncGitHubPlugin;
import org.flowerplatform.codesync.github.GitHubConstants;

/**
 * @author Mariana Gheorghe
 */
public class GitHubRepositoryModelAdapter extends AbstractModelAdapter {

	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		if (NodeFeatureProvider.NAME.equals(feature)) {
			return "GitHub";
		}
		return super.getValueFeatureValue(element, feature, correspondingValue);
	}

	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element, Object feature, Iterable<?> correspondingIterable) {
		if (GitHubConstants.PULL_REQUESTS.equals(feature)) {
			PullRequestService service = new PullRequestService(CodeSyncGitHubPlugin.getInstance().getClient());
			IRepositoryIdProvider repository = getRepository(element);
			
			try {
				// test with just one
				int id = 73;
				return Arrays.asList(service.getPullRequest(repository, id));
//				List<PullRequest> pullRequests = new ArrayList<PullRequest>();
//				pullRequests.addAll(service.getPullRequests(repository, "open"));
//				pullRequests.addAll(service.getPullRequests(repository, "closed"));
//				return pullRequests;
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return super.getContainmentFeatureIterable(element, feature, correspondingIterable);
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
	
	protected IRepositoryIdProvider getRepository(Object element) {
		return (IRepositoryIdProvider) element;
	}

}
