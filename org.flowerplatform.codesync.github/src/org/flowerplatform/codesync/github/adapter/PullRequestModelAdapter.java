package org.flowerplatform.codesync.github.adapter;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.egit.github.core.Comment;
import org.eclipse.egit.github.core.CommitFile;
import org.eclipse.egit.github.core.IRepositoryIdProvider;
import org.eclipse.egit.github.core.PullRequest;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.service.IssueService;
import org.eclipse.egit.github.core.service.PullRequestService;
import org.flowerplatform.codesync.adapter.AbstractModelAdapter;
import org.flowerplatform.codesync.feature_provider.NodeFeatureProvider;
import org.flowerplatform.codesync.github.feature_provider.PullRequestFeatureProvider;
import org.flowerplatform.codesync.type_provider.ITypeProvider;

/**
 * Mapped to {@link PullRequest}. Children are {@link CommitFile}s and {@link Comment}s.
 * 
 * @see PullRequestFeatureProvider
 * 
 * @author Mariana Gheorghe
 */
public class PullRequestModelAdapter extends AbstractModelAdapter {

	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		PullRequest pull = getPullRequest(element);
		if (NUMBER.equals(feature)) {
			return pull.getNumber();
		} else if (BODY.equals(feature)) {
			return pull.getBody();
		} else if (HTML_URL.equals(feature)) {
			return pull.getHtmlUrl();
		} else if (STATE.equals(feature)) {
			return pull.getState();
		} else if (CLOSED_AT.equals(feature)) {
			return pull.getClosedAt();
		} else if (MERGED_AT.equals(feature)) {
			return pull.getMergedAt();
		} else if (UPDATED_AT.equals(feature)) {
			return pull.getUpdatedAt();
		} else if (ADDITIONS.equals(feature)) {
			return pull.getAdditions();
		} else if (DELETIONS.equals(feature)) {
			return pull.getDeletions();
		} else if (CHANGED_FILES.equals(feature)) {
			return pull.getChangedFiles();
		} else if (COMMITS.equals(feature)) {
			return pull.getCommits();
		} else if (COMMENTS.equals(feature)) {
			return pull.getComments();
		} else if (USER.equals(feature)) {
			return getLogin(pull.getUser());
		} else if (ASSIGNEE.equals(feature)) {
			return getLogin(pull.getAssignee());
		} else if (MERGED_BY.equals(feature)) {
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
		PullRequestService service = new PullRequestService(/* client ??? */);
		IRepositoryIdProvider repository = new RepositoryId("flower-platform", "flower-platform-4");
		int id = getPullRequest(element).getNumber();
		if (COMMIT_FILES.equals(feature)) {
			try {
				return service.getFiles(repository, id);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		} else if (COMMIT_COMMENTS.equals(feature)) {
			List<Comment> comments = new ArrayList<Comment>();
			try {
				comments.addAll(service.getComments(repository, id));
				comments.addAll(new IssueService().getComments(repository, id));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			return comments;
		}
		return super.getContainmentFeatureIterable(element, feature,
				correspondingIterable);
	}

	@Override
	public Object createChildOnContainmentFeature(Object element, Object feature, Object correspondingChild, ITypeProvider typeProvider) {
		// TODO
		return null;
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

	protected PullRequest getPullRequest(Object element) {
		return (PullRequest) element;
	}
	
}
