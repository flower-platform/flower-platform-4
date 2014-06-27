package org.flowerplatform.codesync.as.adapter;

import static org.flowerplatform.codesync.as.CodeSyncAsConstants.TYPED_ELEMENT_TYPE;

import org.apache.flex.compiler.internal.tree.as.ParameterNode;
import org.flowerplatform.codesync.as.feature_provider.AsParameterFeatureProvider;
import org.flowerplatform.core.CoreConstants;

/**
 * Mapped to {@link ParameterNode}. Children are modifiers ({@link MemberExpressionNode}).
 * 
 * @see AsParameterFeatureProvider
 * 
 * @author Mariana Gheorghe
 */
public class AsParameterModelAdapter extends AsAbstractAstModelAdapter {

	@Override
	public Object getMatchKey(Object element) {
//		return getParameter(element).identifier.name; 
		return null;
	}

	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		if (CoreConstants.NAME.equals(feature)) {
//			return getParameter(element).identifier.name;
		} else if (TYPED_ELEMENT_TYPE.equals(feature)) {
//			TypeExpressionNode type = (TypeExpressionNode) getParameter(element).type;
//			if (type == null) {
//				return null;
//			}
//			return ((MemberExpressionNode) type.expr).selector.getIdentifier().name;
		}
		return super.getValueFeatureValue(element, feature, correspondingValue);
	}
	
}
