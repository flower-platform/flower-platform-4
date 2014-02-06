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

import java.util.Map;

import org.flowerplatform.codesync.CodeSyncAlgorithm;
import org.flowerplatform.codesync.action.ActionResult;

/**
 * @see AbstractModelAdapter
 */
public interface IModelAdapter extends IModelAdapterUI {

	public static final int FEATURE_TYPE_DONT_PROCESS = 0;
	
	public static final int FEATURE_TYPE_CONTAINMENT = 1;
	
	public static final int FEATURE_TYPE_VALUE = 2;
	
	public static final int FEATURE_TYPE_REFERENCE = 3;
	
	public static final String FLOWER_UID = "@flowerUID";
	
	/**
	 * Same type must be set on a pair of adapters.
	 * 
	 * @author Mariana Gheorghe
	 */
	public String getType();
	
	public void setType(String type);
	
	public ModelAdapterFactorySet getModelAdapterFactorySet();
	
	public IModelAdapter setModelAdapterFactorySet(ModelAdapterFactorySet modelAdapterFactorySet);

	public Iterable<?> getContainmentFeatureIterable(Object element, Object feature, Iterable<?> correspondingIterable);
	
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue);

	public Object getMatchKey(Object element);
	
	public void addToMap(Object element, Map<Object, Object> map);
	
	public Object removeFromMap(Object element, Map<Object, Object> leftOrRightMap, boolean isRight);
	
	public void setValueFeatureValue(Object element, Object feature, Object value);
	
	public Object createChildOnContainmentFeature(Object element, Object feature, Object correspondingChild);
	
	public void removeChildrenOnContainmentFeature(Object parent, Object feature, Object child);
	
	/**
	 * Saves the given <code>element</code> to its underlying resource. Returns <code>true</code> if saving is also required
	 * on this <code>element</code>'s children.
	 * 
	 * @author Mariana
	 */
	public boolean save(Object element);
	
	/**
	 * Discards this element (i.e. for a file, discards the AST created from its content; for an EObject, unload the containing resource, etc).
	 *
	 * @author Mariana
	 */
	public boolean discard(Object element);
	
	/**
	 * Called from {@link CodeSyncAlgorithm} before the features of <code>element</code> have been processed.
	 * 
	 * @author Mariana
	 */
	public void beforeFeaturesProcessed(Object element, Object correspondingElement);
	
	/**
	 * Called from {@link CodeSyncAlgorithm} after all the features of <code>element</code> have been processed.
	 * 
	 * @author Mariana
	 */
	public void featuresProcessed(Object element);
	
	/**
	 * Called after a {@link DiffAction} was performed.
	 * 
	 * @param element the element where the action was performed
	 * @param feature the feature that was changed
	 * @param result the action's result
	 * 
	 * @author Mariana
	 */
	public void actionPerformed(Object element, Object feature, ActionResult result);

	/**
	 * Called after all the {@link DiffAction}s were performed for the <code>element</code>,
	 * on the given <code>feature</code>.
	 * 
	 * @author Mariana
	 */
	public void allActionsPerformedForFeature(Object element, Object correspondingElement, Object feature);
	
	/**
	 * Calls {@link #allActionsPerformedForFeature(Object, Object, Object)} for all the containment features
	 * for the <code>element</code>.
	 * 
	 * @author Mariana
	 */
	public void allActionsPerformed(Object element, Object correspondingElement);
	
}