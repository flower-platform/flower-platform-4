package org.flowerplatform.codesync.as.adapter;

import static org.flowerplatform.codesync.as.CodeSyncAsConstants.TYPED_ELEMENT_TYPE;

import java.util.Arrays;

import org.apache.flex.compiler.definitions.IFunctionDefinition;
import org.apache.flex.compiler.definitions.IParameterDefinition;
import org.flowerplatform.codesync.as.CodeSyncAsConstants;
import org.flowerplatform.codesync.as.feature_provider.AsFunctionFeatureProvider;
import org.flowerplatform.codesync.type_provider.ITypeProvider;

/**
 * Mapped to {@link IFunctionDefinition}. Children are {@link IParameterDefinition}s.
 * 
 * @see AsFunctionFeatureProvider
 * 
 * @author Mariana Gheorghe
 */
public class AsFunctionModelAdapter extends AsAbstractAstModelAdapter {

	@Override
	public Object getMatchKey(Object element) {
		return getFunction(element).getBaseName();
	}
	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		if (TYPED_ELEMENT_TYPE.equals(feature)) {
			return getFunction(element).getReturnTypeAsDisplayString();
		}
		return super.getValueFeatureValue(element, feature, correspondingValue);
	}

	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element, Object feature, Iterable<?> correspondingIterable) {
		if (CodeSyncAsConstants.FUNCTION_PARAMETERS.equals(feature)) {
			return Arrays.asList(getFunction(element).getParameters());
		}
		return super.getContainmentFeatureIterable(element, feature, correspondingIterable);
	}

	protected IFunctionDefinition getFunction(Object element) {
		return (IFunctionDefinition) element;
	}
	
}
