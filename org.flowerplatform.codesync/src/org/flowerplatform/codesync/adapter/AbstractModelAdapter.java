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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.flowerplatform.codesync.CodeSyncAlgorithm;
import org.flowerplatform.codesync.CodeSyncConstants;
import org.flowerplatform.codesync.Match;
import org.flowerplatform.codesync.action.ActionResult;
import org.flowerplatform.core.CoreConstants;

/**
 * Convenience implementation.
 * 
 * @author Mariana Gheorghe
 */
public abstract class AbstractModelAdapter implements IModelAdapter {

	protected List<Object> valueFeatures = new ArrayList<>();
	
	protected List<Object> containmentFeatures = new ArrayList<>();
	
	public AbstractModelAdapter() {
		valueFeatures.add(CoreConstants.NAME);
	}
	
	@Override
	public List<?> getValueFeatures(Object element) {
		return valueFeatures;
	}

	@Override
	public List<?> getContainmentFeatures(Object element) {
		return containmentFeatures;
	}

	@Override
	public int getFeatureType(Object element, Object feature) {
		if (valueFeatures.contains(feature)) {
			return CodeSyncConstants.FEATURE_TYPE_VALUE;
		} else if (containmentFeatures.contains(feature)) {
			return CodeSyncConstants.FEATURE_TYPE_CONTAINMENT;
		}
		throw new RuntimeException("Feature " + getFeatureName(element, feature) + " is not registered");
	}

	@Override
	public String getFeatureName(Object element, Object feature) {
		if (feature != null) {
			return feature.toString();
		}
		throw new RuntimeException("Feature is null");
	}
	
	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		throw new IllegalArgumentException("Attempted to acces value feature " + feature + " for element " + element);
	}

	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element, Object feature, Iterable<?> correspondingIterable) {
		throw new IllegalArgumentException("Attempted to acces containment feature " + feature + " for element " + element);
	}
	
	@Override
	public void setValueFeatureValue(Object element, Object feature, Object value) {
		throw new IllegalArgumentException("Attempted to set value feature " + feature + " for element " + element);
	}

	@Override
	public Object createChildOnContainmentFeature(Object parent, Object feature, Object correspondingChild, IModelAdapterSet modelAdapterSet) {
		throw new IllegalArgumentException("Attempted to create child on containment feature " + feature + " for element " + parent);
	}
	
	@Override
	public void removeChildrenOnContainmentFeature(Object parent, Object feature, Object child) {
		throw new IllegalArgumentException("Attempted to remove child on containment feature " + feature + " for element " + parent);
	}

	@Override
	public void addToMap(Object element, Map<Object, Object> map) {
		map.put(getMatchKey(element), element);
	}

	@Override
	public Object removeFromMap(Object element, Map<Object, Object> leftOrRightMap, boolean isRight) {
		return leftOrRightMap.remove(getMatchKey(element));
	}

	@Override
	public boolean save(Object element) {
		return false;
	}

	@Override
	public boolean discard(Object element) {
		return false;
	}

	@Override
	public void beforeFeaturesProcessed(Object element, Object correspondingElement) {
		// nothing to do
	}

	@Override
	public void featuresProcessed(Object element) {
		// nothing to do
	}

	@Override
	public void actionPerformed(Object element, Object feature, ActionResult result, Match match) {
		// nothing to do
	}

	@Override
	public void allActionsPerformedForFeature(Object element, Object correspondingElement, Object feature) {
		// nothing to do
	}

	@Override
	public void allActionsPerformed(Object element, Object correspondingElement, CodeSyncAlgorithm codeSyncAlgorithm) {
		// nothing to do
	}
	
	@Override
	public void setConflict(Object element, Object feature, Object oppositeValue) {
		// nothing to do
	}
	
	@Override
	public void unsetConflict(Object element, Object feature) {
		// nothing to do
	}
}