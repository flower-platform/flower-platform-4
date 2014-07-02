package org.flowerplatform.codesync.as.adapter;

import java.util.Collection;

import org.apache.flex.compiler.definitions.IDefinition;
import org.apache.flex.compiler.definitions.ITypeDefinition;
import org.flowerplatform.codesync.FilteredIterable;
import org.flowerplatform.codesync.as.CodeSyncAsConstants;

/**
 * @author Mariana Gheorghe
 */
public abstract class AsTypeModelAdapter extends AsAbstractAstModelAdapter {

	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element, Object feature, Iterable<?> correspondingIterable) {
		if (CodeSyncAsConstants.STATEMENTS.equals(feature)) {
			ITypeDefinition type = (ITypeDefinition) element;
			Collection<IDefinition> statements = type.getContainedScope().getAllLocalDefinitions();
			return new FilteredIterable<IDefinition, IDefinition>(statements.iterator()) {

				@Override
				protected boolean isAccepted(IDefinition candidate) {
					if (candidate.isImplicit()) {
						return false;
					}
					return true;
				}
			};
		}
		return super.getContainmentFeatureIterable(element, feature, correspondingIterable);
	}

}
