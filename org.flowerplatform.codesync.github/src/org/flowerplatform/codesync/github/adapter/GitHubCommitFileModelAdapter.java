package org.flowerplatform.codesync.github.adapter;

import static org.flowerplatform.codesync.github.GitHubConstants.ADDITIONS;
import static org.flowerplatform.codesync.github.GitHubConstants.BLOB_URL;
import static org.flowerplatform.codesync.github.GitHubConstants.CHANGES;
import static org.flowerplatform.codesync.github.GitHubConstants.DELETIONS;
import static org.flowerplatform.codesync.github.GitHubConstants.FILENAME;
import static org.flowerplatform.codesync.github.GitHubConstants.PATCH;
import static org.flowerplatform.codesync.github.GitHubConstants.STATUS;

import java.util.List;

import org.eclipse.egit.github.core.CommitFile;
import org.flowerplatform.codesync.adapter.AbstractModelAdapter;
import org.flowerplatform.codesync.feature_provider.NodeFeatureProvider;

/**
 * Mapped to {@link CommitFile}. No children.
 * 
 * @author Mariana Gheorghe
 */
public class GitHubCommitFileModelAdapter extends AbstractModelAdapter {

	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		CommitFile file = getCommitFile(element);
		if (FILENAME.equals(feature)) {
			return file.getFilename();
		} else if (STATUS.equals(feature)) {
			return file.getStatus();
		} else if (BLOB_URL.equals(feature)) {
			return file.getBlobUrl();
		} else if (PATCH.equals(feature)) {
			return file.getPatch();
		} else if (CHANGES.equals(feature)) {
			return file.getChanges();
		} else if (ADDITIONS.equals(feature)) {
			return file.getAdditions();
		} else if (DELETIONS.equals(feature)) {
			return file.getDeletions();
		} else if (NodeFeatureProvider.NAME.equals(feature)) {
			return file.getFilename();
		}
		return super.getValueFeatureValue(element, feature, correspondingValue);
	}

	@Override
	public void setValueFeatureValue(Object element, Object feature, Object value) {
		// TODO
	}

	@Override
	public Object getMatchKey(Object element) {
		return getCommitFile(element).getFilename();
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

	protected CommitFile getCommitFile(Object element) {
		return (CommitFile) element;
	}
	
}
