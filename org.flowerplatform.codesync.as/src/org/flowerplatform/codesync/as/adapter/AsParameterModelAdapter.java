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

import static org.flowerplatform.codesync.as.CodeSyncAsConstants.PARAMETER_DEFAULT_VALUE;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.PARAMETER_IS_REST;

import org.apache.flex.compiler.definitions.IParameterDefinition;
import org.flowerplatform.codesync.as.feature_provider.AsParameterFeatureProvider;

/**
 * Mapped to {@link IParameterDefinition}.
 * 
 * @see AsParameterFeatureProvider
 * 
 * @author Mariana Gheorghe
 */
public class AsParameterModelAdapter extends AsVariableModelAdapter {

	@Override
	public Object getMatchKey(Object element) {
		return getParameter(element).getBaseName();
	}

	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		if (PARAMETER_IS_REST.equals(feature)) {
			return getParameter(element).isRest();
		} else if (PARAMETER_DEFAULT_VALUE.equals(feature)) {
			Object value = resolveInitializer(element);
			return value;
		}
		return super.getValueFeatureValue(element, feature, correspondingValue);
	}
	
	protected IParameterDefinition getParameter(Object element) {
		return (IParameterDefinition) element;
	}
}