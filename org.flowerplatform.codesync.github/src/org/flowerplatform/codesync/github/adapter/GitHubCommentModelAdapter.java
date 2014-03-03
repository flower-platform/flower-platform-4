package org.flowerplatform.codesync.github.adapter;

import static org.flowerplatform.codesync.github.GitHubConstants.COMMENT_BODY;
import static org.flowerplatform.codesync.github.GitHubConstants.COMMENT_CREATED_AT;
import static org.flowerplatform.codesync.github.GitHubConstants.COMMENT_ID;
import static org.flowerplatform.codesync.github.GitHubConstants.COMMENT_UPDATED_AT;
import static org.flowerplatform.codesync.github.GitHubConstants.COMMENT_USER;

import java.util.List;

import org.eclipse.egit.github.core.Comment;
import org.flowerplatform.codesync.adapter.AbstractModelAdapter;
import org.flowerplatform.codesync.feature_provider.NodeFeatureProvider;
import org.flowerplatform.codesync.github.feature_provider.GitHubCommentFeatureProvider;

/**
 * @see GitHubCommentFeatureProvider
 * 
 * @author Mariana Gheorghe
 */
public class GitHubCommentModelAdapter extends AbstractModelAdapter {

	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		Comment comment = getComment(element);
		if (COMMENT_ID.equals(feature)) {
			return comment.getId();
		} else if (COMMENT_BODY.equals(feature)) {
			return comment.getBody();
		} else if (COMMENT_USER.equals(feature)) {
			return comment.getUser() == null ? "" : comment.getUser().getLogin();
		} else if (COMMENT_CREATED_AT.equals(feature)) {
			return comment.getCreatedAt();
		} else if (COMMENT_UPDATED_AT.equals(feature)) {
			return comment.getUpdatedAt();
		} else if (NodeFeatureProvider.NAME.equals(feature)) {
			return String.valueOf(getComment(element).getId());
		}
		return super.getValueFeatureValue(element, feature, correspondingValue);
	}

	@Override
	public void setValueFeatureValue(Object element, Object feature, Object value) {
		// TODO
		
	}

	@Override
	public Object getMatchKey(Object element) {
		return String.valueOf(getComment(element).getId());
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

	protected Comment getComment(Object element) {
		return (Comment) element;
	}
	
}
