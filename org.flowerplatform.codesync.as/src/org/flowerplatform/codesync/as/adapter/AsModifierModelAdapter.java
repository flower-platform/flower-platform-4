package org.flowerplatform.codesync.as.adapter;

import org.flowerplatform.core.CoreConstants;

import macromedia.asc.parser.MemberExpressionNode;
import macromedia.asc.util.Context;

/**
 * Mapped to {@link MemberExpressionNode}.
 * 
 * @author Mariana Gheorghe
 */
public class AsModifierModelAdapter extends AsAbstractAstModelAdapter {

	@Override
	public Object getMatchKey(Object element) {
		return getModifier(element).selector.getIdentifier().name;
	}

	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		if (CoreConstants.NAME.equals(feature)) {
			return getModifier(element).selector.getIdentifier().name;
		}
		return super.getValueFeatureValue(element, feature, correspondingValue);
	}
	
	protected MemberExpressionNode getModifier(Object element) {
		return (MemberExpressionNode) element;
	}

	@Override
	protected Context getContext(Object element) {
		throw new UnsupportedOperationException();
	}
	
}
