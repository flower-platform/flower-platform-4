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
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.VARIABLE_INITIALIZER;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.VISIBILITY;

import org.apache.flex.abc.ABCConstants;
import org.apache.flex.compiler.definitions.IVariableDefinition;
import org.flowerplatform.codesync.as.CodeSyncAsConstants;

/**
 * Mapped to {@link IVariableDefinition}.
 * 
 * @author Mariana Gheorghe
 */
public class AsVariableModelAdapter extends AsAbstractAstModelAdapter {

	public AsVariableModelAdapter() {
		valueFeatures.add(DOCUMENTATION);
		valueFeatures.add(TYPED_ELEMENT_TYPE);
		valueFeatures.add(VISIBILITY);
		valueFeatures.add(VARIABLE_INITIALIZER);
		
		containmentFeatures.add(META_TAGS);
		containmentFeatures.add(MODIFIERS);
	}
	
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
		IVariableDefinition var = getVariable(element);
		Object value = var.resolveInitialValue(getCompilationUnit(var).getProject());
		if (ABCConstants.NULL_VALUE.equals(value)) {
			return CodeSyncAsConstants.PARAMETER_NULL_VALUE;
		}
		return value;
	}
	
	protected IVariableDefinition getVariable(Object element) {
		return (IVariableDefinition) element;
	}
	
}