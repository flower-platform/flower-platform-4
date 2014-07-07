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

import java.util.List;
import java.util.Map;

import org.flowerplatform.codesync.CodeSyncAlgorithm;
import org.flowerplatform.codesync.Match;
import org.flowerplatform.codesync.action.ActionResult;

/**
 * @author Mariana Gheorghe
 */
public abstract class DelegatingModelAdapter extends AbstractModelAdapter {
	
	protected abstract IModelAdapter getDelegate(Object element);
	
	@Override
	public List<?> getValueFeatures(Object element) {
		return getDelegate(element).getValueFeatures(element);
	}

	@Override
	public List<?> getContainmentFeatures(Object element) {
		return getDelegate(element).getContainmentFeatures(element);
	}

	@Override
	public int getFeatureType(Object element, Object feature) {
		return getDelegate(element).getFeatureType(element, feature);
	}

	@Override
	public String getFeatureName(Object element, Object feature) {
		return getDelegate(element).getFeatureName(element, feature);
	}
	
	@Override
	public Object getMatchKey(Object element) {
		return getDelegate(element).getMatchKey(element);
	}

	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		return getDelegate(element).getValueFeatureValue(element, feature, correspondingValue);
	}

	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element,
			Object feature, Iterable<?> correspondingIterable) {
		return getDelegate(element).getContainmentFeatureIterable(element, feature, correspondingIterable);
	}

	@Override
	public void setValueFeatureValue(Object element, Object feature, Object value) {
		getDelegate(element).setValueFeatureValue(element, feature, value);
	}

	@Override
	public Object createChildOnContainmentFeature(Object parent,
			Object feature, Object correspondingChild,
			IModelAdapterSet modelAdapterSet) {
		return getDelegate(parent).createChildOnContainmentFeature(parent, feature,
				correspondingChild, modelAdapterSet);
	}

	@Override
	public void removeChildrenOnContainmentFeature(Object parent, Object feature, Object child) {
		getDelegate(parent).removeChildrenOnContainmentFeature(parent, feature, child);
	}

	@Override
	public void addToMap(Object element, Map<Object, Object> map) {
		getDelegate(element).addToMap(element, map);
	}

	@Override
	public Object removeFromMap(Object element, Map<Object, Object> leftOrRightMap, boolean isRight) {
		return getDelegate(element).removeFromMap(element, leftOrRightMap, isRight);
	}

	@Override
	public boolean save(Object element) {
		return getDelegate(element).save(element);
	}

	@Override
	public boolean discard(Object element) {
		return getDelegate(element).discard(element);
	}

	@Override
	public void beforeFeaturesProcessed(Object element, Object correspondingElement) {
		getDelegate(element).beforeFeaturesProcessed(element, correspondingElement);
	}

	@Override
	public void featuresProcessed(Object element) {
		getDelegate(element).featuresProcessed(element);
	}

	@Override
	public void actionPerformed(Object element, Object feature, ActionResult result, Match match) {
		getDelegate(element).actionPerformed(element, feature, result, match);
	}

	@Override
	public void allActionsPerformedForFeature(Object element, Object correspondingElement, Object feature) {
		getDelegate(element).allActionsPerformedForFeature(element, correspondingElement, feature);
	}

	@Override
	public void allActionsPerformed(Object element,
			Object correspondingElement, CodeSyncAlgorithm codeSyncAlgorithm) {
		getDelegate(element).allActionsPerformed(element, correspondingElement, codeSyncAlgorithm);
	}

	@Override
	public void setConflict(Object element, Object feature, Object oppositeValue) {
		getDelegate(element).setConflict(element, feature, oppositeValue);
	}

	@Override
	public void unsetConflict(Object element, Object feature) {
		getDelegate(element).unsetConflict(element, feature);
	}
	
}