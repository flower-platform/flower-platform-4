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

import static org.flowerplatform.codesync.as.CodeSyncAsConstants.DOCUMENTATION;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.META_TAGS;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.MODIFIERS;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.TYPED_ELEMENT_TYPE;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.VISIBILITY;

import java.util.Arrays;

import org.apache.flex.compiler.asdoc.IASDocComment;
import org.apache.flex.compiler.definitions.IDefinition;
import org.apache.flex.compiler.definitions.IDocumentableDefinition;
import org.apache.flex.compiler.internal.scopes.ASFileScope;
import org.apache.flex.compiler.scopes.IASScope;
import org.apache.flex.compiler.units.ICompilationUnit;
import org.flowerplatform.codesync.as.asdoc.AsDocComment;
import org.flowerplatform.codesync.code.adapter.AstModelElementAdapter;
import org.flowerplatform.core.CoreConstants;

/**
 * Mapped to {@link IDefinition}.
 * 
 * @author Mariana Gheorghe
 */
public abstract class AsAbstractAstModelAdapter extends AstModelElementAdapter {

	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		if (CoreConstants.NAME.equals(feature)) {
			return getDefinition(element).getBaseName();
		} else if (TYPED_ELEMENT_TYPE.equals(feature)) {
			return getDefinition(element).getTypeAsDisplayString();
		} else if (VISIBILITY.equals(feature)) {
			return getDefinition(element).getNamespaceReference().getBaseName();
		} else if (DOCUMENTATION.equals(feature)) {
			if (element instanceof IDocumentableDefinition) {
				IASDocComment comment = ((IDocumentableDefinition) element).getExplicitSourceComment();
				if (comment == null) {
					return null;
				}
				return ((AsDocComment) comment).getText();
			}
		} 
		return super.getValueFeatureValue(element, feature, correspondingValue);
	}

	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element, Object feature, Iterable<?> correspondingIterable) {
		if (MODIFIERS.equals(feature)) {
			return Arrays.asList(getDefinition(element).getModifiers().getAllModifiers());
		} else if (META_TAGS.equals(feature)) {
			return Arrays.asList(getDefinition(element).getAllMetaTags());
		}
		return super.getContainmentFeatureIterable(element, feature, correspondingIterable);
	}
	
	protected ICompilationUnit getCompilationUnit(IDefinition definition) {
		IASScope scope = definition.getContainingScope();
		while (!(scope instanceof ASFileScope)) {
			scope = scope.getContainingScope();
		}
		return ((ASFileScope) scope).getCompilationUnit();
	}
	
	protected IDefinition getDefinition(Object element) {
		return (IDefinition) element;
	}
	
	@Override
	protected void updateUID(Object element, Object correspondingElement) {
		// TODO Auto-generated method stub

	}

}