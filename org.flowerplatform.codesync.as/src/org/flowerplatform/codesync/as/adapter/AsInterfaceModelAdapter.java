package org.flowerplatform.codesync.as.adapter;

import static org.flowerplatform.codesync.as.CodeSyncAsConstants.SUPER_INTERFACES;

import java.util.Arrays;

import org.apache.flex.compiler.definitions.IFunctionDefinition;
import org.apache.flex.compiler.definitions.IInterfaceDefinition;
import org.flowerplatform.codesync.as.feature_provider.AsInterfaceFeatureProvider;

/**
 * Mapped to {@link IInterfaceDefinition}. Children are {@link IFunctionDefinition}s.
 * 
 * @see AsInterfaceFeatureProvider
 * 
 * @author Mariana Gheorghe
 */
public class AsInterfaceModelAdapter extends AsTypeModelAdapter {

	@Override
	public Object getMatchKey(Object element) {
		return getInterface(element).getBaseName();
	}

	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element, Object feature, Iterable<?> correspondingIterable) {
		if (SUPER_INTERFACES.equals(feature)) {
			return Arrays.asList(getInterface(element).getExtendedInterfaceReferences());
		}
		return super.getContainmentFeatureIterable(element, feature, correspondingIterable);
	}
	
	protected IInterfaceDefinition getInterface(Object element) {
		return (IInterfaceDefinition) element;
	}
	
}
