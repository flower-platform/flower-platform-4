package org.flowerplatform.codesync.as.adapter;

import static org.flowerplatform.codesync.as.CodeSyncAsConstants.PARAMETER_DEFAULT_VALUE;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.PARAMETER_IS_REST;

import org.apache.flex.compiler.definitions.IParameterDefinition;
import org.flowerplatform.codesync.as.feature_provider.AsParameterFeatureProvider;

/**
 * Mapped to {@link IParameterDefinition}.
 * 
 * @see AsParameterFeatureProvider
 * 
 * @author Mariana Gheorghe
 */
public class AsParameterModelAdapter extends AsVariableModelAdapter {

	@Override
	public Object getMatchKey(Object element) {
		return getParameter(element).getBaseName();
	}

	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		if (PARAMETER_IS_REST.equals(feature)) {
			return getParameter(element).isRest();
		} else if (PARAMETER_DEFAULT_VALUE.equals(feature)) {
			Object value = resolveInitializer(element);
			return value;
		}
		return super.getValueFeatureValue(element, feature, correspondingValue);
	}
	
	protected IParameterDefinition getParameter(Object element) {
		return (IParameterDefinition) element;
	}
}
