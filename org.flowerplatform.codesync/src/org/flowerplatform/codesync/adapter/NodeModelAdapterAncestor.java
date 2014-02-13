/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
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
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package org.flowerplatform.codesync.adapter;

import java.util.Iterator;

import org.flowerplatform.codesync.FilteredIterable;
import org.flowerplatform.core.node.remote.Node;

/**
 * @author Mariana
 */
public class NodeModelAdapterAncestor extends NodeModelAdapter {

	/**
	 * Filters out added {@link Node}s. from the containment list for <code>feature</code>.
	 */
	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element, Object feature, Iterable<?> correspondingIterable) {
		// first get the children from the FeatureChange, if it exists
		Iterable<?> children = super.getContainmentFeatureIterable(element, feature, correspondingIterable);
		return new FilteredIterable<Object, Object>((Iterator<Object>) children.iterator()) {
			protected boolean isAccepted(Object candidate) {
				Boolean isAdded = (Boolean) getNode(candidate).getOrPopulateProperties().get(ADDED);
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
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		Object originalValue = super.getValueFeatureValue(element, getOriginalFeatureName(feature), correspondingValue);
		if (originalValue == null) {
			originalValue = super.getValueFeatureValue(element, feature, correspondingValue);
		}
		return originalValue;
	}
	
}