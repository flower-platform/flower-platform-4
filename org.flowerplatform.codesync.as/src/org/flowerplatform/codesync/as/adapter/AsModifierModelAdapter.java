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

import org.apache.flex.compiler.common.ASModifier;
import org.flowerplatform.codesync.CodeSyncAlgorithm;
import org.flowerplatform.codesync.adapter.file.AstModelElementAdapter;
import org.flowerplatform.core.CoreConstants;

/**
 * Mapped to {@link ASModifier}.
 * 
 * @author Mariana Gheorghe
 */
public class AsModifierModelAdapter extends AstModelElementAdapter {

	@Override
	public Object getMatchKey(Object element, CodeSyncAlgorithm codeSyncAlgorithm) {
		return getModifier(element).toString();
	}

	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue, CodeSyncAlgorithm codeSyncAlgorithm) {
		if (CoreConstants.NAME.equals(feature)) {
			return getModifier(element).toString();
		}
		return super.getValueFeatureValue(element, feature, correspondingValue, codeSyncAlgorithm);
	}

	/**
	 * @author see class
	 */
	protected ASModifier getModifier(Object element) {
		return (ASModifier) element;
	}
	
	@Override
	protected void updateUID(Object element, Object correspondingElement) {
		// TODO Auto-generated method stub

	}

}