package org.flowerplatform.codesync.as.adapter;

import static org.flowerplatform.codesync.as.CodeSyncAsConstants.VARIABLE_INITIALIZER;

import org.apache.flex.abc.ABCConstants;
import org.apache.flex.compiler.definitions.IVariableDefinition;
import org.flowerplatform.codesync.as.CodeSyncAsConstants;
import org.flowerplatform.codesync.as.feature_provider.AsVariableFeatureProvider;

/**
 * Mapped to {@link IVariableDefinition}.
 * 
 * @see AsVariableFeatureProvider
 * 
 * @author Mariana Gheorghe
 */
public class AsVariableModelAdapter extends AsAbstractAstModelAdapter {

	@Override
	public Object getMatchKey(Object element) {
		return getVariable(element).getBaseName();
	}
	
	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		if (VARIABLE_INITIALIZER.equals(feature)) {
			return resolveInitializer(element);
		}
		return super.getValueFeatureValue(element, feature, correspondingValue);
	}

	protected Object resolveInitializer(Object element) {
		Object value = getVariable(element).resolveInitialValue(getProject(element));
		if (ABCConstants.NULL_VALUE.equals(value)) {
			return CodeSyncAsConstants.PARAMETER_NULL_VALUE;
		}
		return value;
	}
	
	protected IVariableDefinition getVariable(Object element) {
		return (IVariableDefinition) element;
	}
	
}
