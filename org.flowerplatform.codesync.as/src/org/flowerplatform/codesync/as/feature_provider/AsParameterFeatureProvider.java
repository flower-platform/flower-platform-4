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
package org.flowerplatform.codesync.as.feature_provider;

import static org.flowerplatform.codesync.as.CodeSyncAsConstants.MODIFIERS;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.PARAMETER_DEFAULT_VALUE;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.PARAMETER_IS_REST;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.TYPED_ELEMENT_TYPE;

import org.flowerplatform.codesync.as.adapter.AsParameterModelAdapter;
import org.flowerplatform.codesync.feature_provider.NodeFeatureProvider;

/**
 * @see AsParameterModelAdapter
 * 
 * @author Mariana Gheorghe
 */
public class AsParameterFeatureProvider extends NodeFeatureProvider {

	public AsParameterFeatureProvider() {
		valueFeatures.add(TYPED_ELEMENT_TYPE);
		valueFeatures.add(PARAMETER_IS_REST);
		valueFeatures.add(PARAMETER_DEFAULT_VALUE);
		
		containmentFeatures.add(MODIFIERS);
	}
	
}