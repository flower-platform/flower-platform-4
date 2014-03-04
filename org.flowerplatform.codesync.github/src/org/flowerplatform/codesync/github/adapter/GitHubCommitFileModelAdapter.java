package org.flowerplatform.codesync.github.adapter;

import static org.flowerplatform.codesync.CodeSyncPropertiesConstants.NAME;
import static org.flowerplatform.codesync.github.GitHubConstants.COMMIT_FILE_ADDITIONS;
import static org.flowerplatform.codesync.github.GitHubConstants.COMMIT_FILE_BLOB_URL;
import static org.flowerplatform.codesync.github.GitHubConstants.COMMIT_FILE_CHANGES;
import static org.flowerplatform.codesync.github.GitHubConstants.COMMIT_FILE_DELETIONS;
import static org.flowerplatform.codesync.github.GitHubConstants.COMMIT_FILE_NAME;
import static org.flowerplatform.codesync.github.GitHubConstants.COMMIT_FILE_PATCH;
import static org.flowerplatform.codesync.github.GitHubConstants.COMMIT_FILE_STATUS;

import java.util.List;

import org.eclipse.egit.github.core.CommitFile;
import org.flowerplatform.codesync.adapter.AbstractModelAdapter;
import org.flowerplatform.codesync.github.feature_provider.GitHubCommitFileFeatureProvider;

/**
 * Mapped to {@link CommitFile}. No children.
 * 
 * @see GitHubCommitFileFeatureProvider
 * 
 * @author Mariana Gheorghe
 */
public class GitHubCommitFileModelAdapter extends AbstractModelAdapter {

	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		CommitFile file = getCommitFile(element);
		if (COMMIT_FILE_NAME.equals(feature)) {
			return file.getFilename();
		} else if (COMMIT_FILE_STATUS.equals(feature)) {
			return file.getStatus();
		} else if (COMMIT_FILE_BLOB_URL.equals(feature)) {
			return file.getBlobUrl();
		} else if (COMMIT_FILE_PATCH.equals(feature)) {
			return file.getPatch();
		} else if (COMMIT_FILE_CHANGES.equals(feature)) {
			return file.getChanges();
		} else if (COMMIT_FILE_ADDITIONS.equals(feature)) {
			return file.getAdditions();
		} else if (COMMIT_FILE_DELETIONS.equals(feature)) {
			return file.getDeletions();
		} else if (NAME.equals(feature)) {
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
