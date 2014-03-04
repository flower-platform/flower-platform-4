package org.flowerplatform.codesync.github.adapter;

import static org.flowerplatform.codesync.CodeSyncPropertiesConstants.NAME;
import static org.flowerplatform.codesync.github.GitHubConstants.COMMENT_BODY;
import static org.flowerplatform.codesync.github.GitHubConstants.COMMIT_COMMENT_COMMIT_ID;
import static org.flowerplatform.codesync.github.GitHubConstants.COMMIT_COMMENT_PATH;
import static org.flowerplatform.codesync.github.GitHubConstants.COMMIT_COMMENT_POSITION;

import java.io.IOException;
import java.util.List;

import org.eclipse.egit.github.core.CommitComment;
import org.eclipse.egit.github.core.IRepositoryIdProvider;
import org.eclipse.egit.github.core.RepositoryId;
import org.flowerplatform.codesync.github.feature_provider.GitHubCommitCommentFeatureProvider;

/**
 * @see GitHubCommitCommentFeatureProvider
 * 
 * @author Mariana Gheorghe
 */
public class GitHubCommitCommentModelAdapter extends GitHubCommentModelAdapter {

	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		CommitComment comment = getCommitComment(element);
		if (COMMIT_COMMENT_COMMIT_ID.equals(feature)) {
			return comment.getCommitId();
		} else if (COMMIT_COMMENT_PATH.equals(feature)) {
			return comment.getPath();
		} else if (COMMIT_COMMENT_POSITION.equals(feature)) {
			return comment.getPosition();
		}
		return super.getValueFeatureValue(element, feature, correspondingValue);
	}

	@Override
	public void setValueFeatureValue(Object element, Object feature, Object value) {
		if (COMMENT_BODY.equals(feature) || NAME.equals(feature)) {
			CommitComment commitComment = getCommitComment(element);
			commitComment.setBody((String) value);
			IRepositoryIdProvider repository = RepositoryId.createFromId(getRepositoryIdFromURL(commitComment.getUrl()));;
			try {
				getPullRequestService().editComment(repository, commitComment);
				return;
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		super.setValueFeatureValue(element, feature, value);
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
	
	protected CommitComment getCommitComment(Object element) {
		return (CommitComment) element;
	}

}
