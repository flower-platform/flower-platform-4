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
import org.flowerplatform.core.mindmap.remote.Node;

/**
 * @author Mariana Gheorghe
 */
public class NodeModelAdapter extends AbstractModelAdapter {

	protected ModelAdapterFactory modelAdapterFactory;
	
	protected ModelAdapterFactory codeSyncElementConverter;
	
	public NodeModelAdapter setModelAdapterFactory(ModelAdapterFactory modelAdapterFactory) {
		this.modelAdapterFactory = modelAdapterFactory;
		return this;
	}
	
	public ModelAdapterFactory getModelAdapterFactory() {
		return modelAdapterFactory;
	}
	
	/**
	 * @see IModelAdapter#createCorrespondingModelElement(Object)
	 */
	public NodeModelAdapter setEObjectConverter(ModelAdapterFactory codeSyncElementConverter) {
		this.codeSyncElementConverter = codeSyncElementConverter;
		return this;
	}
	
	public ModelAdapterFactory getEObjectConverter() {
		return codeSyncElementConverter;
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
	
	@Override
	public Object createCorrespondingModelElement(Object element) {
		IModelAdapter adapter = null;
		if (element == null) {
			adapter = codeSyncElementConverter.getModelAdapterByType(getType());
		} else {
			adapter = codeSyncElementConverter.getModelAdapter(element);
		}
		if (adapter != null) {
			return adapter.createCorrespondingModelElement(element);
		}
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
	
	/**
	 * Gets the category node from this node's children list, or create a new category node if it does not exist.
	 */
	private Node getChildrenCategoryForNode(Node node, Object feature) {
		for (Node category : getChildrenForNode(node)) {
			if (category.getBody().equals(feature)) {
				return category;
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private Iterable<Node> getChildrenForNode(Node node) {
		return (Iterable<Node>) CodeSyncPlugin.getInstance().getMindMapService()
				.getChildrenForNodeId(node.getId()).get(1); // first position holds the id
	}

	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		return getNode(element).getProperties().get(feature);
	}
	
	@Override
	public Object getMatchKey(Object element) {
		return getNode(element).getBody();
	}
	
	@Override
	public void setValueFeatureValue(Object element, Object feature, Object newValue) {
		CodeSyncPlugin.getInstance().getMindMapService().setProperty(getNode(element).getId(), (String) feature, newValue.toString());
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
					category = CodeSyncPlugin.getInstance().getMindMapService().addNode(parent.getId(), "category");
					CodeSyncPlugin.getInstance().getMindMapService().setProperty(category.getId(), Node.NAME, feature.toString());
				}
				return CodeSyncPlugin.getInstance().getMindMapService().addNode(category.getId(), null);
//		}
//		
//		return null;
	}
	
	@Override
	public void removeChildrenOnContainmentFeature(Object parent, Object feature, Object child) {
//		EObject eObject = getContainingEObjectForFeature(parent, feature);
//		if (eObject != null) {
//			super.removeChildrenOnContainmentFeature(eObject, feature, child);
//		}
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

	protected Node getNode(Object element) {
		return (Node) element;
	}
	
}