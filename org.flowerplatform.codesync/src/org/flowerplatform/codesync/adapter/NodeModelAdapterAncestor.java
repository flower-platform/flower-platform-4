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
package org.flowerplatform.codesync.adapter;

import java.util.Iterator;

import org.flowerplatform.codesync.CodeSyncAlgorithm;
import org.flowerplatform.codesync.CodeSyncConstants;
import org.flowerplatform.codesync.FilteredIterable;
import org.flowerplatform.codesync.Match;
import org.flowerplatform.codesync.action.ActionResult;
import org.flowerplatform.codesync.controller.CodeSyncControllerUtils;

/**
 * @author Mariana
 */
public class NodeModelAdapterAncestor extends NodeModelAdapter {

	/**
	 * Filters out added {@link Node}s. from the containment list for <code>feature</code>.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element, Object feature, Iterable<?> correspondingIterable, CodeSyncAlgorithm codeSyncAlgorithm) {
		// first get the children from the FeatureChange, if it exists
		Iterable<?> children = super.getContainmentFeatureIterable(element, feature, correspondingIterable, codeSyncAlgorithm);
		return new FilteredIterable<Object, Object>((Iterator<Object>) children.iterator()) {
			protected boolean isAccepted(Object candidate) {
				Boolean isAdded = (Boolean) getNode(candidate).getPropertyValue(CodeSyncConstants.ADDED);
				if (isAdded != null && isAdded) {
					return false;
				}
				return true;
			}
		};
	}

	/**
	 * Returns the original value for <code>feature</code>.
	 */
	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue, CodeSyncAlgorithm codeSyncAlgorithm) {
		Object originalValue = super.getValueFeatureValue(element, CodeSyncControllerUtils.getOriginalPropertyName(feature.toString()), correspondingValue, codeSyncAlgorithm);
		if (originalValue == null) {
			originalValue = super.getValueFeatureValue(element, feature, correspondingValue, codeSyncAlgorithm);
		}
		return originalValue;
	}

	@Override
	public void actionPerformed(Object element, Object feature, ActionResult result, Match match) {
		if (result == null || result.conflict) {
			return;
		}
		processContainmentFeatureAfterActionPerformed(getNode(element), feature, result, match);
	}

}
