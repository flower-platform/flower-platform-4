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
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.META_TAGS;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.MODIFIERS;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.STATEMENTS;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.SUPER_INTERFACES;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.VISIBILITY;

import org.flowerplatform.codesync.as.adapter.AsInterfaceModelAdapter;
import org.flowerplatform.codesync.feature_provider.NodeFeatureProvider;

/**
 * @see AsInterfaceModelAdapter
 * 
 * @author Mariana Gheorghe
 */
public class AsInterfaceFeatureProvider extends NodeFeatureProvider {

	public AsInterfaceFeatureProvider() {
		valueFeatures.add(DOCUMENTATION);
		valueFeatures.add(VISIBILITY);
		
		containmentFeatures.add(STATEMENTS);
		containmentFeatures.add(META_TAGS);
		containmentFeatures.add(SUPER_INTERFACES);
		containmentFeatures.add(MODIFIERS);
	}
	
}