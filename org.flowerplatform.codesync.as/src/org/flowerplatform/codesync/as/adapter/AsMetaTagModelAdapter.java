package org.flowerplatform.codesync.as.adapter;

import static org.flowerplatform.codesync.as.CodeSyncAsConstants.META_TAG_ATTRIBUTES;

import java.util.Arrays;
import java.util.List;

import org.apache.flex.compiler.definitions.metadata.IMetaTag;
import org.apache.flex.compiler.definitions.metadata.IMetaTagAttribute;
import org.flowerplatform.codesync.as.feature_provider.AsMetaTagFeatureProvider;
import org.flowerplatform.codesync.code.adapter.AstModelElementAdapter;
import org.flowerplatform.core.CoreConstants;

/**
 * Mapped to {@link IMetaTag}. Children are {@link IMetaTagAttribute}s.
 * 
 * @see AsMetaTagFeatureProvider
 * 
 * @author Mariana Gheorghe
 */
public class AsMetaTagModelAdapter extends AstModelElementAdapter {

	@Override
	public Object getMatchKey(Object element) {
		return getMetaTag(element).getTagName();
	}
	
	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		if (CoreConstants.NAME.equals(feature)) {
			return getMetaTag(element).getTagName();
		}
		return super.getValueFeatureValue(element, feature, correspondingValue);
	}

	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element, Object feature, Iterable<?> correspondingIterable) {
		if (META_TAG_ATTRIBUTES.equals(feature)) {
			return Arrays.asList(getMetaTag(element).getAllAttributes());
		}
		return super.getContainmentFeatureIterable(element, feature, correspondingIterable);
	}

	protected IMetaTag getMetaTag(Object element) {
		return (IMetaTag) element;
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

	@Override
	protected void updateUID(Object element, Object correspondingElement) {
		// TODO Auto-generated method stub

	}

}
