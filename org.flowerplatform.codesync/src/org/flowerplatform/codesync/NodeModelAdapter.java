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
package org.flowerplatform.codesync;

import java.util.List;

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
	
	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element, Object feature, Iterable<?> correspondingIterable) {
		if (NodeFeatureProvider.CHILDREN.equals(feature)) {
			List<Object> children = CodeSyncPlugin.getInstance().getMindMapService()
					.getChildrenForNodeId(getNode(element).getId());
			return (Iterable<?>) children.get(1); // first position holds the id
		}
		return null;
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
		CodeSyncPlugin.getInstance().getMindMapService().setProperty(getNode(element).getId(), (String) feature, (String) newValue);
//		getNode(element).getProperties().put((String) feature, (String) newValue);
	}
	
	/**
	 * For the <code>children</code> feature of {@link CodeSyncElement}, also add the new child to the {@link AstCacheElement}s
	 * resource.
	 */
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
			if (NodeFeatureProvider.CHILDREN.equals(feature)) {
				Node parent = getNode(element);
				return CodeSyncPlugin.getInstance().getMindMapService().addNode(parent.getId(), null);
			}
//		}
//		
		return null;
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