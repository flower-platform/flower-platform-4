package org.flowerplatform.codesync.github.adapter;

import static org.flowerplatform.codesync.github.GitHubPropertiesConstants.BODY;
import static org.flowerplatform.codesync.github.GitHubPropertiesConstants.COMMENT_ID;
import static org.flowerplatform.codesync.github.GitHubPropertiesConstants.CREATED_AT;
import static org.flowerplatform.codesync.github.GitHubPropertiesConstants.UPDATED_AT;
import static org.flowerplatform.codesync.github.GitHubPropertiesConstants.USER;

import java.util.List;

import org.eclipse.egit.github.core.Comment;
import org.flowerplatform.codesync.adapter.AbstractModelAdapter;
import org.flowerplatform.codesync.feature_provider.NodeFeatureProvider;

/**
 * @author Mariana Gheorghe
 */
public class CommentModelAdapter extends AbstractModelAdapter {

	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		Comment comment = getComment(element);
		if (COMMENT_ID.equals(feature)) {
			return comment.getId();
		} else if (BODY.equals(feature)) {
			return comment.getBody();
		} else if (USER.equals(feature)) {
			return comment.getUser() == null ? "" : comment.getUser().getLogin();
		} else if (CREATED_AT.equals(feature)) {
			return comment.getCreatedAt();
		} else if (UPDATED_AT.equals(feature)) {
			return comment.getUpdatedAt();
		} else if (NodeFeatureProvider.NAME.equals(feature)) {
			return comment.getBody();
		}
		return super.getValueFeatureValue(element, feature, correspondingValue);
	}

	@Override
	public void setValueFeatureValue(Object element, Object feature, Object value) {
		// TODO
		
	}

	@Override
	public Object getMatchKey(Object element) {
		return getComment(element).getBody();
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
