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

import static org.flowerplatform.codesync.CodeSyncAlgorithm.UNDEFINED;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.flowerplatform.codesync.AbstractModelAdapter;

/**
 * @author Mariana
 */
public class StringModelAdapter extends AbstractModelAdapter {
	
	@Override
	public boolean hasChildren(Object modelElement) {
		return false;
	}

	@Override
	public List<?> getChildren(Object modelElement) {
		return Collections.emptyList();
	}

	@Override
	public String getLabel(Object modelElement) {
		return (String) modelElement;
	}

	@Override
	public List<String> getIconUrls(Object modelElement) {
		return null;
	}

	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element, Object feature, Iterable<?> correspondingIterable) {
		if (UNDEFINED.equals(element)) {
			return Collections.singletonList(UNDEFINED);
		}
		throw new UnsupportedOperationException("String does not have children for feature " + feature);
	}

	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		if (UNDEFINED.equals(element)) {
			return UNDEFINED;
		}
		throw new UnsupportedOperationException("String does not have feature " + feature);
	}

	@Override
	public Object getMatchKey(Object element) {
		return element;
	}

	@Override
	public Object removeFromMap(Object element, Map<Object, Object> leftOrRightMap, boolean isRight) {
		if (UNDEFINED.equals(element)) {
			if (leftOrRightMap.isEmpty() || leftOrRightMap.containsKey(UNDEFINED)) {
				return UNDEFINED;
			} else {
				return leftOrRightMap.remove(leftOrRightMap.keySet().iterator().next());
			}
		}
		return leftOrRightMap.remove(getMatchKey(element));
	}

	@Override
	public void setValueFeatureValue(Object element, Object feature, Object value) {
		throw new UnsupportedOperationException("String does not have feature " + feature);
	}

	@Override
	public Object createChildOnContainmentFeature(Object element, Object feature, Object correspondingChild) {
		throw new UnsupportedOperationException("String does not have children for feature " + feature);
	}

	@Override
	public void removeChildrenOnContainmentFeature(Object parent, Object feature, Object child) {
		throw new UnsupportedOperationException("String does not have children for feature " + feature);
	}

	@Override
	public Object createCorrespondingModelElement(Object element) {
		return element;
	}

}