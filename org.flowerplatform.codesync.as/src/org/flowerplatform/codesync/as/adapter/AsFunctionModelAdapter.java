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
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.FUNCTION_PARAMETERS;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.META_TAGS;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.MODIFIERS;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.TYPED_ELEMENT_TYPE;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.VISIBILITY;

import java.util.Arrays;
import java.util.List;

import org.apache.flex.compiler.definitions.IFunctionDefinition;
import org.apache.flex.compiler.definitions.IGetterDefinition;
import org.apache.flex.compiler.definitions.IParameterDefinition;
import org.apache.flex.compiler.definitions.ISetterDefinition;
import org.flowerplatform.codesync.CodeSyncAlgorithm;
import org.flowerplatform.codesync.as.CodeSyncAsConstants;

/**
 * Mapped to {@link IFunctionDefinition}. Children are {@link IParameterDefinition}s.
 * 
 * @author Mariana Gheorghe
 */
public class AsFunctionModelAdapter extends AsAbstractAstModelAdapter {

	public AsFunctionModelAdapter() {
		valueFeatures.add(DOCUMENTATION);
		valueFeatures.add(TYPED_ELEMENT_TYPE);
		valueFeatures.add(VISIBILITY);
		
		containmentFeatures.add(META_TAGS);
		containmentFeatures.add(MODIFIERS);
		containmentFeatures.add(FUNCTION_PARAMETERS);
	}
	
	@Override
	public Object getMatchKey(Object element, CodeSyncAlgorithm codeSyncAlgorithm) {
		String name = getFunction(element).getBaseName();
		if (element instanceof ISetterDefinition) {
			name = "set " + name;
		} else if (element instanceof IGetterDefinition) {
			name = "get " + name;
		}
		@SuppressWarnings("rawtypes")
		List paramList = Arrays.asList(getFunction(element).getParameters());
		String formatedParamList = paramList.toString().replace("[", "(").replace("]", ")");
		String type = getFunction(element).getReturnTypeAsDisplayString();
		return name + formatedParamList + (type != null && !type.isEmpty() ? ":" + type : "");
		}
	
	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue, CodeSyncAlgorithm codeSyncAlgorithm) {
		if (TYPED_ELEMENT_TYPE.equals(feature)) {
			return getFunction(element).getReturnTypeAsDisplayString();
		}
		return super.getValueFeatureValue(element, feature, correspondingValue, codeSyncAlgorithm);
	}

	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element, Object feature, Iterable<?> correspondingIterable, CodeSyncAlgorithm codeSyncAlgorithm) {
		if (CodeSyncAsConstants.FUNCTION_PARAMETERS.equals(feature)) {
			return Arrays.asList(getFunction(element).getParameters());
		}
		return super.getContainmentFeatureIterable(element, feature, correspondingIterable, codeSyncAlgorithm);
	}

	protected IFunctionDefinition getFunction(Object element) {
		return (IFunctionDefinition) element;
	}
	
}