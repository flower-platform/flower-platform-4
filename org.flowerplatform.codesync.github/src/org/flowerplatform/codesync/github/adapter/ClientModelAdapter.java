package org.flowerplatform.codesync.github.adapter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.eclipse.egit.github.core.IRepositoryIdProvider;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.PullRequestService;
import org.flowerplatform.codesync.adapter.AbstractModelAdapter;
import org.flowerplatform.codesync.feature_provider.NodeFeatureProvider;
import org.flowerplatform.codesync.github.GitHubPropertiesConstants;

/**
 * @author Mariana Gheorghe
 */
public class ClientModelAdapter extends AbstractModelAdapter {

	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		if (NodeFeatureProvider.NAME.equals(feature)) {
			return "GitHub";
		}
		return super.getValueFeatureValue(element, feature, correspondingValue);
	}

	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element, Object feature, Iterable<?> correspondingIterable) {
		if (GitHubPropertiesConstants.PULL_REQUESTS.equals(feature)) {
			PullRequestService service = new PullRequestService(getClient(element));
			IRepositoryIdProvider repository = new RepositoryId("flower-platform", "flower-platform-4");
			// test with just one
			int id = 82;
			try {
				return Arrays.asList(service.getPullRequest(repository, id));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return super.getContainmentFeatureIterable(element, feature,
				correspondingIterable);
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
	
	protected GitHubClient getClient(Object object) {
		return (GitHubClient) object;
	}

}
