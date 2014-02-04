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

import java.util.Collections;
import java.util.List;

import org.flowerplatform.codesync.CodeSyncPlugin;
import org.flowerplatform.codesync.feature_provider.NodeFeatureProvider;
import org.flowerplatform.core.node.remote.Node;

/**
 * @author Mariana Gheorghe
 */
public class NodeModelAdapter extends AbstractModelAdapter {

	public static final String ADDED = "added";
	public static final String REMOVED = "removed";
	
	protected ModelAdapterFactory modelAdapterFactory;
	
	protected ModelAdapterFactory oppositeModelAdapterFactory;
	
	public NodeModelAdapter setModelAdapterFactory(ModelAdapterFactory modelAdapterFactory) {
		this.modelAdapterFactory = modelAdapterFactory;
		return this;
	}
	
	public ModelAdapterFactory getModelAdapterFactory() {
		return modelAdapterFactory;
	}
	
	/**
	 * @see #createChildOnContainmentFeature(Object, Object, Object)
	 */
	public NodeModelAdapter setOppositeModelAdapterFactory(ModelAdapterFactory codeSyncElementConverter) {
		this.oppositeModelAdapterFactory = codeSyncElementConverter;
		return this;
	}
	
	public ModelAdapterFactory getOppositeModelAdapterFactory() {
		return oppositeModelAdapterFactory;
	}
	
	/**
	 * Checks for a {@link FeatureChange} on the name feature first.
	 */
	@Override
	public String getLabel(Object modelElement) {
		return (String) getMatchKey(modelElement);
	}

	@Override
	public List<String> getIconUrls(Object modelElement) {
		return null;
	}
	
	/**
	 * Get the children categories for this {@link Node}, and then return the children for the required category.
	 */
	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element, Object feature, Iterable<?> correspondingIterable) {
		Node category = getChildrenCategoryForNode(getNode(element), feature);
		if (category == null) {
			return Collections.emptyList();
		}
		return getChildrenForNode(category);
	}
	
	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		return getNode(element).getProperties().get(feature);
	}
	
	@Override
	public Object getMatchKey(Object element) {
		return getNode(element).getProperties().get("body");
	}
	
	@Override
	public void setValueFeatureValue(Object element, Object feature, Object newValue) {
		CodeSyncPlugin.getInstance().getNodeService().setProperty(getNode(element), (String) feature, newValue.toString());
	}
	
	@Override
	public Object createChildOnContainmentFeature(Object element, Object feature, Object correspondingChild) {
		// first check if the child already exists
//		Iterable<?> children = super.getContainmentFeatureIterable(eObject, feature, null);
//		IModelAdapter adapter = codeSyncElementConverter.getModelAdapter(correspondingChild);
//		Object matchKey = null;
//		if (adapter != null) {
//			matchKey = adapter.getMatchKey(correspondingChild);
//		} else {
//			matchKey = getMatchKey(correspondingChild);
//		}
//		if (matchKey != null) {
//			for (Object child : children) {
//				if (matchKey.equals(modelAdapterFactory.getModelAdapter(child).getMatchKey(child))) {
//					return child;
//				}
//			}
//		}
//		
//		if (eObject != null) {
				Node parent = getNode(element);
				Node category = getChildrenCategoryForNode(parent, feature);
				if (category == null) {
					category = new Node();
					category.setType("category");
					CodeSyncPlugin.getInstance().getNodeService().addChild(parent, category);
					CodeSyncPlugin.getInstance().getNodeService().setProperty(category, NodeFeatureProvider.NAME, feature.toString());
				}
				// set the type for the new node; needed by the action performed handler
				String type = getOppositeModelAdapterFactory().getModelAdapter(correspondingChild).getType();
				Node child = new Node();
				child.setType(type);
				CodeSyncPlugin.getInstance().getNodeService().addChild(category, child);
				return child;
//		}
//		
//		return null;
	}
	
	@Override
	public void removeChildrenOnContainmentFeature(Object parent, Object feature, Object child) {
		CodeSyncPlugin.getInstance().getNodeService().removeChild(getNode(parent), getNode(child), true);
	}

	@Override
	public boolean hasChildren(Object modelElement) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<?> getChildren(Object modelElement) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean save(Object element) {
		CodeSyncPlugin.getInstance().getNodeService().save();
		return false;
	}

	protected Node getNode(Object element) {
		return (Node) element;
	}

	protected Object getOriginalFeatureName(Object feature) {
		return feature.toString() + ".original";
	}
	
}