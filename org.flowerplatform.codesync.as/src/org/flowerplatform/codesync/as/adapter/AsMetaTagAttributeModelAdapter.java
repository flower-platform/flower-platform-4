package org.flowerplatform.codesync.as.adapter;

import static org.flowerplatform.codesync.as.CodeSyncAsConstants.META_TAG_ATTRIBUTE_VALUE;

import java.util.List;

import org.apache.flex.compiler.definitions.metadata.IMetaTagAttribute;
import org.flowerplatform.codesync.code.adapter.AstModelElementAdapter;
import org.flowerplatform.core.CoreConstants;

/**
 * Mapped to {@link IMetaTagAttribute}.
 * 
 * @author Mariana Gheorghe
 */
public class AsMetaTagAttributeModelAdapter extends AstModelElementAdapter {

	@Override
	public Object getMatchKey(Object element) {
		return getMetaTagAttribute(element).getKey();
	}

	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		if (CoreConstants.NAME.equals(feature)) {
			return getMetaTagAttribute(element).getKey();
		} else if (META_TAG_ATTRIBUTE_VALUE.equals(feature)) {
			return getMetaTagAttribute(element).getValue();
		}
		return super.getValueFeatureValue(element, feature, correspondingValue);
	}
	
	protected IMetaTagAttribute getMetaTagAttribute(Object element) {
		return (IMetaTagAttribute) element;
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
