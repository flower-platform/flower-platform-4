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

import java.util.List;
import java.util.Map;

import org.flowerplatform.codesync.CodeSyncPlugin;
import org.flowerplatform.codesync.action.ActionResult;
import org.flowerplatform.core.mindmap.remote.Node;

/**
 * Convenience implementation.
 * 
 * @author Mariana Gheorghe
 */
public abstract class AbstractModelAdapter implements IModelAdapter {

	protected String type;
	
	private ModelAdapterFactorySet modelAdapterFactorySet;
	
	@Override
	public String getType() {
		return type;
	}

	@Override
	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public ModelAdapterFactorySet getModelAdapterFactorySet() {
		return modelAdapterFactorySet;
	}
	
	@Override
	public IModelAdapter setModelAdapterFactorySet(ModelAdapterFactorySet modelAdapterFactorySet) {
		this.modelAdapterFactorySet = modelAdapterFactorySet;
		return this;
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
	public Object createChildOnContainmentFeature(Object element, Object feature, Object correspondingChild) {
		throw new IllegalArgumentException("Attempted to create child on containment feature " + feature + " for element " + element);
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
	public void actionPerformed(Object element, Object feature, ActionResult result) {
		// nothing to do
	}

	@Override
	public void allActionsPerformedForFeature(Object element, Object correspondingElement, Object feature) {
		// nothing to do
	}

	@Override
	public void allActionsPerformed(Object element, Object correspondingElement) {
		// nothing to do
	}
	
	//////////////////////////////////////////////////////////
	// Node utils
	//////////////////////////////////////////////////////////
	
	/**
	 * Gets the category node from this node's children list, or create a new category node if it does not exist.
	 */
	protected Node getChildrenCategoryForNode(Node node, Object feature) {
		for (Node category : getChildrenForNode(node)) {
			if (category.getBody().equals(feature)) {
				return category;
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	protected List<Node> getChildrenForNode(Node node) {
		return (List<Node>) CodeSyncPlugin.getInstance().getMindMapService()
				.getChildrenForNodeId(node.getId()).get(1); // first position holds the id
	}
}
