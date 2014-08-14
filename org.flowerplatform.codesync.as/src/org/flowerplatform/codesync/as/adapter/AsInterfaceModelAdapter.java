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
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.STATEMENTS;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.SUPER_INTERFACES;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.VISIBILITY;

import java.util.Arrays;

import org.apache.flex.compiler.definitions.IInterfaceDefinition;
import org.flowerplatform.codesync.CodeSyncAlgorithm;

/**
 * Mapped to {@link IInterfaceDefinition}. Children are {@link IFunctionDefinition}s.
 * 
 * @author Mariana Gheorghe
 */
public class AsInterfaceModelAdapter extends AsTypeModelAdapter {

	/**
	 * @author see class
	 */
	public AsInterfaceModelAdapter() {
		valueFeatures.add(DOCUMENTATION);
		valueFeatures.add(VISIBILITY);
		
		containmentFeatures.add(STATEMENTS);
		containmentFeatures.add(META_TAGS);
		containmentFeatures.add(SUPER_INTERFACES);
		containmentFeatures.add(MODIFIERS);
	}
	
	@Override
	public Object getMatchKey(Object element, CodeSyncAlgorithm codeSyncAlgorithm) {
		return getInterface(element).getBaseName();
	}

	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element, Object feature, Iterable<?> correspondingIterable, CodeSyncAlgorithm codeSyncAlgorithm) {
		if (SUPER_INTERFACES.equals(feature)) {
			return Arrays.asList(getInterface(element).getExtendedInterfaceReferences());
		}
		return super.getContainmentFeatureIterable(element, feature, correspondingIterable, codeSyncAlgorithm);
	}
	
	/**
	 * @author see class
	 */
	protected IInterfaceDefinition getInterface(Object element) {
		return (IInterfaceDefinition) element;
	}
	
}