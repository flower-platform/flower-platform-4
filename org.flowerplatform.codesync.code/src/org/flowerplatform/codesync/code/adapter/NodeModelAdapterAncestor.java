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
package org.flowerplatform.codesync.code.adapter;

import java.util.Iterator;

import org.flowerplatform.codesync.FilteredIterable;
import org.flowerplatform.codesync.adapter.NodeModelAdapter;

/**
 * @author Mariana
 */
public class NodeModelAdapterAncestor extends NodeModelAdapter {

	public NodeModelAdapterAncestor() {
		super();
	}
	
	/**
	 * Filters out added {@link CodeSyncElement}s. Returns the new containment list from the {@link FeatureChange}s map for
	 * the <code>feature</code>, if it exists. 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element, Object feature, Iterable<?> correspondingIterable) {
		// first get the children from the FeatureChange, if it exists
		Iterable<?> result = super.getContainmentFeatureIterable(element, feature, correspondingIterable);
//		FeatureChange change = getFeatureChange(element, feature);
//		if (change != null) {
//			result = (Iterable<?>) change.getOldValue();
//		} else {
//			result = astCacheElementModelAdapter != null 
//					? astCacheElementModelAdapter.getContainmentFeatureIterable(element, feature, correspondingIterable) 
//					: super.getContainmentFeatureIterable(element, feature, correspondingIterable);
//		}
		return new FilteredIterable<Object, Object>((Iterator<Object>) result.iterator()) {
			protected boolean isAccepted(Object candidate) {
//				if (candidate instanceof Node && ((Node) candidate).isAdded())
//					return false;
				return true;
			}
		
		};
	}

	/**
	 * Returns the old value from the {@link FeatureChange}s map for the <code>feature</code>, if it exists.
	 */
	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		// first get the value from the FeatureChange, if it exists
//		FeatureChange change = getFeatureChange(element, feature);
//		if (change != null) {
//			return change.getOldValue();
//		}
//		return astCacheElementModelAdapter != null 
//				? astCacheElementModelAdapter.getValueFeatureValue(element, feature, correspondingValue)
//				: super.getValueFeatureValue(element, feature, correspondingValue);
		return super.getValueFeatureValue(element, feature, correspondingValue);
	}
}