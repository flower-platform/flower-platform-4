package org.flowerplatform.codesync.as.adapter;

import static org.flowerplatform.codesync.as.CodeSyncAsConstants.SUPER_CLASS;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.SUPER_INTERFACES;

import org.apache.flex.compiler.definitions.IClassDefinition;
import org.flowerplatform.codesync.as.CodeSyncAsConstants;
import org.flowerplatform.codesync.as.feature_provider.AsClassFeatureProvider;
import org.flowerplatform.core.CoreConstants;

/**
 * Mapped to {@link IClassDefinition}.
 * 
 * @see AsClassFeatureProvider
 * 
 * @author Mariana Gheorghe
 */
public class AsClassModelAdapter extends AsAbstractAstModelAdapter {

	@Override
	public Object getMatchKey(Object element) {
		return getClassDefinition(element).getBaseName();
	}
	
	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element, Object feature, Iterable<?> correspondingIterable) {
		if (CodeSyncAsConstants.STATEMENTS.equals(feature)) {
			return getClassDefinition(element).getContainedScope().getAllLocalDefinitions();
		} else if (SUPER_INTERFACES.equals(feature)) {
//			return Arrays.asList(getClassDefinition(element).getImplementedInterfaceReferences());
			return null;
		}
		return super.getContainmentFeatureIterable(element, feature, correspondingIterable);
	}

	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		if (CoreConstants.NAME.equals(feature)) {
			return getClassDefinition(element).getBaseName();
		} else if (SUPER_CLASS.equals(feature)) {
			return getClassDefinition(element).getBaseClassAsDisplayString();
		}
		return super.getValueFeatureValue(element, feature, correspondingValue);
	}
	
	protected IClassDefinition getClassDefinition(Object element) {
		return (IClassDefinition) element;
	}
	
}
