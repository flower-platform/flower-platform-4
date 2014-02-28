package org.flowerplatform.codesync.github.adapter;

import static org.flowerplatform.codesync.github.GitHubPropertiesConstants.LINE;
import static org.flowerplatform.codesync.github.GitHubPropertiesConstants.PATH;
import static org.flowerplatform.codesync.github.GitHubPropertiesConstants.POSITION;

import java.util.List;

import org.eclipse.egit.github.core.CommitComment;

public class CommitCommentModelAdapter extends CommentModelAdapter {

	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		CommitComment comment = getCommitComment(element);
		if (PATH.equals(feature)) {
			return comment.getPath();
		} else if (LINE.equals(feature)) {
			return comment.getLine();
		} else if (POSITION.equals(feature)) {
			return comment.getPosition();
		}
		return super.getValueFeatureValue(element, feature, correspondingValue);
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
