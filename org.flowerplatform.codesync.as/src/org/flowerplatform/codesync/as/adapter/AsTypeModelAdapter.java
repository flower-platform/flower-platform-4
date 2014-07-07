/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * license-end
 */
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
					if (candidate.isImplicit() || candidate.getNode() == null) {
						return false;
					}
					return true;
				}
			};
		}
		return super.getContainmentFeatureIterable(element, feature, correspondingIterable);
	}

}