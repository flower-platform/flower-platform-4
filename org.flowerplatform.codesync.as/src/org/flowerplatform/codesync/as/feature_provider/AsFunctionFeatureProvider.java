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

import static org.flowerplatform.codesync.as.CodeSyncAsConstants.DOCUMENTATION;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.FUNCTION_PARAMETERS;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.META_TAGS;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.MODIFIERS;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.TYPED_ELEMENT_TYPE;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.VISIBILITY;

import org.flowerplatform.codesync.as.adapter.AsFunctionModelAdapter;
import org.flowerplatform.codesync.feature_provider.NodeFeatureProvider;

/**
 * @see AsFunctionModelAdapter
 * 
 * @author Mariana Gheorghe
 */
public class AsFunctionFeatureProvider extends NodeFeatureProvider {

	public AsFunctionFeatureProvider() {
		valueFeatures.add(DOCUMENTATION);
		valueFeatures.add(TYPED_ELEMENT_TYPE);
		valueFeatures.add(VISIBILITY);
		
		containmentFeatures.add(META_TAGS);
		containmentFeatures.add(MODIFIERS);
		containmentFeatures.add(FUNCTION_PARAMETERS);
	}
	
}