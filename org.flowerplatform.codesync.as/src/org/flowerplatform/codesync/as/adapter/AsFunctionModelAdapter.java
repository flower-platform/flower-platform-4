package org.flowerplatform.codesync.as.adapter;

import static org.flowerplatform.codesync.as.CodeSyncAsConstants.TYPED_ELEMENT_TYPE;

import org.flowerplatform.codesync.as.CodeSyncAsConstants;
import org.flowerplatform.codesync.as.feature_provider.AsFunctionFeatureProvider;
import org.flowerplatform.core.CoreConstants;

/**
 * Mapped to {@link FunctionDefinitionNode}.
 * 
 * @see AsFunctionFeatureProvider
 * 
 * @author Mariana Gheorghe
 */
public class AsFunctionModelAdapter extends AsAbstractAstModelAdapter {

	@Override
	public Object getMatchKey(Object element) {
//		return getFunction(element).name.identifier.name;
		return null;
	}

	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		if (CoreConstants.NAME.equals(feature)) {
//			return getFunction(element).name.identifier.name;
		} else if (TYPED_ELEMENT_TYPE.equals(feature)) {
//			return NodeMagic.getFunctionTypeName(getFunction(element));
		}
		return super.getValueFeatureValue(element, feature, correspondingValue);
	}
	
	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element, Object feature, Iterable<?> correspondingIterable) {
		if (CodeSyncAsConstants.FUNCTION_PARAMETERS.equals(feature)) {
//			ParameterListNode parameters = getFunction(element).fexpr.signature.parameter;
//			if (parameters == null) {
//				return Collections.emptyList();
//			}
//			return parameters.items;
		}
		return super.getContainmentFeatureIterable(element, feature, correspondingIterable);
	}

}
