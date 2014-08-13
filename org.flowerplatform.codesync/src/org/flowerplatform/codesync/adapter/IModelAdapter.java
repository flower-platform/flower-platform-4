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
 * @see AbstractModelAdapter
 */
public interface IModelAdapter {
	
	List<?> getValueFeatures(Object element, CodeSyncAlgorithm codeSyncAlgorithm);
	
	List<?> getContainmentFeatures(Object element, CodeSyncAlgorithm codeSyncAlgorithm);
	
	int getFeatureType(Object element, Object feature, CodeSyncAlgorithm codeSyncAlgorithm);
	
	String getFeatureName(Object element, Object feature, CodeSyncAlgorithm codeSyncAlgorithm);

	Iterable<?> getContainmentFeatureIterable(Object element, Object feature, Iterable<?> correspondingIterable, CodeSyncAlgorithm codeSyncAlgorithm);
	
	Object getValueFeatureValue(Object element, Object feature, Object correspondingValue, CodeSyncAlgorithm codeSyncAlgorithm);

	Object getMatchKey(Object element, CodeSyncAlgorithm codeSyncAlgorithm);
	
	void addToMap(Object element, Map<Object, Object> map, CodeSyncAlgorithm codeSyncAlgorithm);
	
	Object removeFromMap(Object element, Map<Object, Object> leftOrRightMap, boolean isRight, CodeSyncAlgorithm codeSyncAlgorithm);
	
	void setValueFeatureValue(Object element, Object feature, Object value, CodeSyncAlgorithm codeSyncAlgorithm);
	
	Object createChildOnContainmentFeature(Object parent, Object feature, Object correspondingChild, IModelAdapterSet modelAdapterSet, CodeSyncAlgorithm codeSyncAlgorithm);
	
	void removeChildrenOnContainmentFeature(Object parent, Object feature, Object child, CodeSyncAlgorithm codeSyncAlgorithm);
	
	/**
	 * Saves the given <code>element</code> to its underlying resource. Returns <code>true</code> if saving is also required
	 * on this <code>element</code>'s children.
	 * 
	 * @author Mariana
	 */
	boolean save(Object element, CodeSyncAlgorithm codeSyncAlgorithm);
	
	/**
	 * Discards this element (i.e. for a file, discards the AST created from its content; for an EObject, unload the containing resource, etc).
	 *
	 * @author Mariana
	 */
	boolean discard(Object element, CodeSyncAlgorithm codeSyncAlgorithm);
	
	/**
	 * Called from {@link CodeSyncAlgorithm} before the features of <code>element</code> have been processed.
	 * 
	 * @author Mariana
	 */
	void beforeFeaturesProcessed(Object element, Object correspondingElement, CodeSyncAlgorithm codeSyncAlgorithm);
	
	/**
	 * Called from {@link CodeSyncAlgorithm} after all the features of <code>element</code> have been processed.
	 * 
	 * @author Mariana
	 */
	void featuresProcessed(Object element, CodeSyncAlgorithm codeSyncAlgorithm);
	
	/**
	 * Called after a {@link DiffAction} was performed.
	 * 
	 * @param element the element where the action was performed
	 * @param feature the feature that was changed
	 * @param result the action's result
	 * @author Mariana
	 */
	void actionPerformed(Object element, Object feature, ActionResult result, Match match);

	/**
	 * Called after all the {@link DiffAction}s were performed for the <code>element</code>,
	 * on the given <code>feature</code>.
	 * 
	 * @author Mariana
	 */
	void allActionsPerformedForFeature(Object element, Object correspondingElement, Object feature, CodeSyncAlgorithm codeSyncAlgorithm);
	
	/**
	 * Calls {@link #allActionsPerformedForFeature(Object, Object, Object, CodeSyncAlgorithm)} for all the containment features
	 * for the <code>element</code>.
	 * 
	 * @author Mariana
	 */
	void allActionsPerformed(Object element, Object correspondingElement, CodeSyncAlgorithm codeSyncAlgorithm);
	
	void setConflict(Object element, Object feature, Object oppositeValue, CodeSyncAlgorithm codeSyncAlgorithm);

	void unsetConflict(Object element, Object feature, CodeSyncAlgorithm codeSyncAlgorithm);

	void setChildrenConflict(Object element);

	void unsetChildrenConflict(Object element);

	void setSync(Object element);

	void setChildrenSync(Object element);
	
}