package org.flowerplatform.codesync.as.adapter;

import org.apache.flex.compiler.internal.tree.as.IdentifierNode;
import org.flowerplatform.core.CoreConstants;

/**
 * Mapped to {@link IdentifierNode}.
 * 
 * @author Mariana Gheorghe
 */
public class AsIdentifierModelAdapter extends AsAbstractAstModelAdapter {

	@Override
	public Object getMatchKey(Object element) {
//		return getModifier(element).name;
		return null;
	}

	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		if (CoreConstants.NAME.equals(feature)) {
//			return getModifier(element).name;
		}
		return super.getValueFeatureValue(element, feature, correspondingValue);
	}
	
}
