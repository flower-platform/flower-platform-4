/* license-start
 * 
 * Copyright (C) 2008 - 2014 Crispico Software, <http://www.crispico.com/>.
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

import static org.flowerplatform.codesync.as.CodeSyncAsConstants.MODIFIERS;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.PARAMETER_DEFAULT_VALUE;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.PARAMETER_IS_REST;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.TYPED_ELEMENT_TYPE;

import org.apache.flex.compiler.definitions.IParameterDefinition;
import org.flowerplatform.codesync.CodeSyncAlgorithm;

/**
 * Mapped to {@link IParameterDefinition}.
 * 
 * @author Mariana Gheorghe
 */
public class AsParameterModelAdapter extends AsVariableModelAdapter {

	/**
	 *@author Mariana Gheorghe
	 **/
	public AsParameterModelAdapter() {
		valueFeatures.add(TYPED_ELEMENT_TYPE);
		valueFeatures.add(PARAMETER_IS_REST);
		valueFeatures.add(PARAMETER_DEFAULT_VALUE);
		
		containmentFeatures.add(MODIFIERS);
	}
	
	@Override
	public Object getMatchKey(Object element, CodeSyncAlgorithm codeSyncAlgorithm) {
		return getParameter(element).getBaseName();
	}

	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue, CodeSyncAlgorithm codeSyncAlgorithm) {
		if (PARAMETER_IS_REST.equals(feature)) {
			return getParameter(element).isRest();
		} else if (PARAMETER_DEFAULT_VALUE.equals(feature)) {
			Object value = resolveInitializer(element);
			return value;
		}
		return super.getValueFeatureValue(element, feature, correspondingValue, codeSyncAlgorithm);
	}
	
	/**
	 *@author Mariana Gheorghe
	 **/
	protected IParameterDefinition getParameter(Object element) {
		return (IParameterDefinition) element;
	}
}