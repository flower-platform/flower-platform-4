/* license-start
 * 
 * Copyright (C) 2008 - 2014 Crispico Software, <http://www.crispico.com/>.
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
	
	/**
	 * @author see class
	 */
	protected abstract IModelAdapter getDelegate(Object element, CodeSyncAlgorithm codeSyncAlgorithm);
	
	@Override
	public List<?> getValueFeatures(Object element, CodeSyncAlgorithm codeSyncAlgorithm) {
		return getDelegate(element, codeSyncAlgorithm).getValueFeatures(element, codeSyncAlgorithm);
	}

	@Override
	public List<?> getContainmentFeatures(Object element, CodeSyncAlgorithm codeSyncAlgorithm) {
		return getDelegate(element, codeSyncAlgorithm).getContainmentFeatures(element, codeSyncAlgorithm);
	}

	@Override
	public int getFeatureType(Object element, Object feature, CodeSyncAlgorithm codeSyncAlgorithm) {
		return getDelegate(element, codeSyncAlgorithm).getFeatureType(element, feature, codeSyncAlgorithm);
	}

	@Override
	public String getFeatureName(Object element, Object feature, CodeSyncAlgorithm codeSyncAlgorithm) {
		return getDelegate(element, codeSyncAlgorithm).getFeatureName(element, feature, codeSyncAlgorithm);
	}
	
	@Override
	public Object getMatchKey(Object element, CodeSyncAlgorithm codeSyncAlgorithm) {
		return getDelegate(element, codeSyncAlgorithm).getMatchKey(element, codeSyncAlgorithm);
	}

	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue, CodeSyncAlgorithm codeSyncAlgorithm) {
		return getDelegate(element, codeSyncAlgorithm).getValueFeatureValue(element, feature, correspondingValue, codeSyncAlgorithm);
	}

	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element,
			Object feature, Iterable<?> correspondingIterable, CodeSyncAlgorithm codeSyncAlgorithm) {
		return getDelegate(element, codeSyncAlgorithm).getContainmentFeatureIterable(element, feature, correspondingIterable, codeSyncAlgorithm);
	}

	@Override
	public void setValueFeatureValue(Object element, Object feature, Object value, CodeSyncAlgorithm codeSyncAlgorithm) {
		getDelegate(element, codeSyncAlgorithm).setValueFeatureValue(element, feature, value, codeSyncAlgorithm);
	}

	@Override
	public Object createChildOnContainmentFeature(Object parent, Object feature, Object correspondingChild,
			IModelAdapterSet modelAdapterSet, CodeSyncAlgorithm codeSyncAlgorithm) {
		return getDelegate(parent, codeSyncAlgorithm).createChildOnContainmentFeature(parent, feature,
				correspondingChild, modelAdapterSet, codeSyncAlgorithm);
	}

	@Override
	public void removeChildrenOnContainmentFeature(Object parent, Object feature, Object child, CodeSyncAlgorithm codeSyncAlgorithm) {
		getDelegate(parent, codeSyncAlgorithm).removeChildrenOnContainmentFeature(parent, feature, child, codeSyncAlgorithm);
	}

	@Override
	public void addToMap(Object element, Map<Object, Object> map, CodeSyncAlgorithm codeSyncAlgorithm) {
		getDelegate(element, codeSyncAlgorithm).addToMap(element, map, codeSyncAlgorithm);
	}

	@Override
	public Object removeFromMap(Object element, Map<Object, Object> leftOrRightMap, boolean isRight, CodeSyncAlgorithm codeSyncAlgorithm) {
		return getDelegate(element, codeSyncAlgorithm).removeFromMap(element, leftOrRightMap, isRight, codeSyncAlgorithm);
	}

	@Override
	public boolean save(Object element, CodeSyncAlgorithm codeSyncAlgorithm) {
		return getDelegate(element, codeSyncAlgorithm).save(element, codeSyncAlgorithm);
	}

	@Override
	public boolean discard(Object element, CodeSyncAlgorithm codeSyncAlgorithm) {
		return getDelegate(element, codeSyncAlgorithm).discard(element, codeSyncAlgorithm);
	}

	@Override
	public void beforeFeaturesProcessed(Object element, Object correspondingElement, CodeSyncAlgorithm codeSyncAlgorithm) {
		getDelegate(element, codeSyncAlgorithm).beforeFeaturesProcessed(element, correspondingElement, codeSyncAlgorithm);
	}

	@Override
	public void featuresProcessed(Object element, CodeSyncAlgorithm codeSyncAlgorithm) {
		getDelegate(element, codeSyncAlgorithm).featuresProcessed(element, codeSyncAlgorithm);
	}

	@Override
	public void actionPerformed(Object element, Object feature, ActionResult result, Match match) {
		getDelegate(element, match.getCodeSyncAlgorithm()).actionPerformed(element, feature, result, match);
	}

	@Override
	public void allActionsPerformedForFeature(Object element, Object correspondingElement, Object feature, CodeSyncAlgorithm codeSyncAlgorithm) {
		getDelegate(element, codeSyncAlgorithm).allActionsPerformedForFeature(element, correspondingElement, feature, codeSyncAlgorithm);
	}

	@Override
	public void allActionsPerformed(Object element, Object correspondingElement, CodeSyncAlgorithm codeSyncAlgorithm) {
		getDelegate(element, codeSyncAlgorithm).allActionsPerformed(element, correspondingElement, codeSyncAlgorithm);
	}

	@Override
	public void setConflict(Object element, Object feature, Object oppositeValue, CodeSyncAlgorithm codeSyncAlgorithm) {
		getDelegate(element, codeSyncAlgorithm).setConflict(element, feature, oppositeValue, codeSyncAlgorithm);
	}

	@Override
	public void unsetConflict(Object element, Object feature, CodeSyncAlgorithm codeSyncAlgorithm) {
		getDelegate(element, codeSyncAlgorithm).unsetConflict(element, feature, codeSyncAlgorithm);
	}
	
}