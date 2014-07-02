package org.flowerplatform.codesync.as.adapter;

import static org.flowerplatform.codesync.as.CodeSyncAsConstants.SUPER_CLASS;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.SUPER_INTERFACES;

import java.util.Arrays;

import org.apache.flex.compiler.definitions.IClassDefinition;
import org.apache.flex.compiler.definitions.IFunctionDefinition;
import org.apache.flex.compiler.definitions.IVariableDefinition;
import org.flowerplatform.codesync.as.feature_provider.AsClassFeatureProvider;

/**
 * Mapped to {@link IClassDefinition}. Children are {@link IFunctionDefinition}s
 * and {@link IVariableDefinition}s.
 * 
 * @see AsClassFeatureProvider
 * 
 * @author Mariana Gheorghe
 */
public class AsClassModelAdapter extends AsTypeModelAdapter {

	@Override
	public Object getMatchKey(Object element) {
		return getClassDefinition(element).getBaseName();
	}
	
	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element, Object feature, Iterable<?> correspondingIterable) {
		if (SUPER_INTERFACES.equals(feature)) {
			return Arrays.asList(getClassDefinition(element).getImplementedInterfaceReferences());
		}
		return super.getContainmentFeatureIterable(element, feature, correspondingIterable);
	}

	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		if (SUPER_CLASS.equals(feature)) {
			return getClassDefinition(element).getBaseClassAsDisplayString();
		}
		return super.getValueFeatureValue(element, feature, correspondingValue);
	}
	
	protected IClassDefinition getClassDefinition(Object element) {
		return (IClassDefinition) element;
	}
	
}
