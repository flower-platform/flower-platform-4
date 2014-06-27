package org.flowerplatform.codesync.as.adapter;

import static org.flowerplatform.codesync.as.CodeSyncAsConstants.TYPED_ELEMENT_TYPE;

import org.flowerplatform.codesync.as.feature_provider.AsVariableFeatureProvider;
import org.flowerplatform.core.CoreConstants;

/**
 * Mapped to {@link VariableDefinitionNode}.
 * 
 * @see AsVariableFeatureProvider
 * 
 * @author Mariana Gheorghe
 */
public class AsVariableModelAdapter extends AsAbstractAstModelAdapter {

	@Override
	public Object getMatchKey(Object element) {
//		return NodeMagic.getVariableName(getVariable(element));
		return null;
	}
	
	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		if (CoreConstants.NAME.equals(feature)) {
//			return NodeMagic.getVariableName(getVariable(element));
		} else if (TYPED_ELEMENT_TYPE.equals(feature)) {
//			return NodeMagic.getVariableTypeName(getVariable(element));
		}
		return super.getValueFeatureValue(element, feature, correspondingValue);
	}
	
}
