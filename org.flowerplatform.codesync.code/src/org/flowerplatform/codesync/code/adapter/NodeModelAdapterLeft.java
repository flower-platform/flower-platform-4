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

import java.util.Iterator;
import java.util.List;

import org.flowerplatform.codesync.CodeSyncPlugin;
import org.flowerplatform.codesync.FilteredIterable;
import org.flowerplatform.codesync.action.ActionResult;
import org.flowerplatform.codesync.adapter.IModelAdapter;
import org.flowerplatform.codesync.adapter.NodeModelAdapter;
import org.flowerplatform.core.node.remote.Node;

/**
 * @author Mariana
 */
public class NodeModelAdapterLeft extends NodeModelAdapter {

	/**
	 * Filters out deleted {@link Node}s from the containment list for <code>feature</code>.
	 */
	@Override
	public Iterable<?> getContainmentFeatureIterable(final Object element, Object feature, Iterable<?> correspondingIterable) {
		Iterable<?> children = super.getContainmentFeatureIterable(element, feature, correspondingIterable);
		// filter out deleted elements
		return new FilteredIterable<Object, Object>((Iterator<Object>) children.iterator()) {
			protected boolean isAccepted(Object candidate) {
				if (candidate instanceof Node && Boolean.parseBoolean((String) ((Node) candidate).getProperties().get(REMOVED))) {
					return false;
				}
				return true;
			}
		
		};
	}
	
	/**
	 * Before the features are processed for <code>element</code>, checks if the AST cache was deleted, and 
	 * recreates it. Note: we cannot do this while the features are processed because upon requesting the 
	 * value for a 2nd feature, the AST cache will be refreshed again, thus losing the value for a previously
	 * processed feature.
	 */
	@Override
	public void beforeFeaturesProcessed(Object element, Object correspondingElement) {
//		CodeSyncElement cse = getCodeSyncElement(element);
//		if (cse != null) {
//			if (cse.getAstCacheElement() == null || cse.getAstCacheElement().eResource() == null) {
//				AstCacheElement ace = (AstCacheElement) createCorrespondingModelElement(correspondingElement);
//				cse.setAstCacheElement(ace);
//			}
//		}
	}

	/**
	 * Adds the {@link AstCacheElement} to the AST cache resource.
	 */
	@Override
	public void featuresProcessed(Object element) {
//		CodeSyncElement cse = getCodeSyncElement(element);
//		if (cse != null) {
//			AstCacheElement ace = cse.getAstCacheElement();
//			if (ace != null && ace.eResource() == null) {
//				addToResource(ace);
//			}
//		}
	}

	@Override
	public void allActionsPerformedForFeature(Object element, Object correspondingElement, Object feature) {
//		CodeSyncElement cse = getCodeSyncElement(element);
//		if (cse != null) {
//			FeatureChange change = cse.getFeatureChanges().get(feature);
//			if (change != null) {
//				if (getModelAdapterFactorySet().getFeatureProvider(cse).getFeatureType(feature) == FEATURE_TYPE_CONTAINMENT) {
//					List<Object> children = (List<Object>) super.getContainmentFeatureIterable(element, feature, null);
//					List<Object> newValues = (List<Object>) change.getNewValue();
//					
//					// iterate through the children list; for each child not found in the new children, see if it exists in the corresponding element
//					for (Iterator it  = children.iterator(); it.hasNext();) {
//						Object existingChild = (Object) it .next();
//						Object matchKey = getModelAdapterFactory().getModelAdapter(existingChild).getMatchKey(existingChild);
//						Object newChild = findChild(newValues, matchKey);
//						if (newChild == null) {
//							if (!elementContainsChildWithMatchKey(correspondingElement, feature, matchKey)) {
//								// the child doesn't exist in the corresponding element either => delete it
//								it.remove();
//							}
//						}
//					}
//					
//					// iterate through the new values; for each child not found in the children list, see if it exists in the corresponding element
//					for (Object newChild : newValues) {
//						Object matchKey = getModelAdapterFactory().getModelAdapter(newChild).getMatchKey(newChild);
//						Object existingChild = findChild(children, matchKey);
//						if (existingChild == null) {
//							if (elementContainsChildWithMatchKey(correspondingElement, feature, matchKey)) {
//								// the child exists in the corresponding element as well => add it to the children
//								children.add(newChild instanceof EObject ? EcoreUtil.copy((EObject) newChild) : newChild);
//							}
//						}
//					}
//					
//					// compare the new values with the existing children
//					if (newValues.size() == children.size()) {
//						int matches = 0;
//						for (Object newChild : newValues) {
//							Object existingChild = findChild(children, getModelAdapterFactory().getModelAdapter(newChild).getMatchKey(newChild));
//							if (existingChild != null) {
//								children.remove(existingChild);
//								children.add(newChild instanceof EObject ? EcoreUtil.copy((EObject) newChild) : newChild);
//								matches++;
//							}
//						}
//						
//						if (newValues.size() == matches) {
//							// the new values list and the existing list have the same children => remove the feature change
//							cse.getFeatureChanges().removeKey(feature);
//						} else {
//							// the new values list and the existing list are not identical => update the old values
//							change.setOldValue(children);
//						}
//					}
//				} else {
//					setValueFeatureValue(element, feature, change.getNewValue());
//					cse.getFeatureChanges().removeKey(feature);
//				}
//			}
//			if (AstCacheCodePackage.eINSTANCE.getOperation_Parameters().equals(feature)) {
//				AstCacheElement ace = cse.getAstCacheElement();
//				if (ace != null && ace instanceof Operation) {
//					String newName = cse.getName().substring(0, cse.getName().indexOf("("));
//					newName += "(";
//					for (Parameter parameter : ((Operation) ace).getParameters()) {
//						newName += parameter.getType() + ",";
//					}
//					if (newName.endsWith(",")) {
//						newName = newName.substring(0, newName.length() - 1);
//					}
//					newName += ")";
//					cse.setName(newName);
//				}
//			}
//		}
	}

	@Override
	public void actionPerformed(Object element, Object feature, ActionResult result) {
		if (result == null || result.conflict) {
			return;
		}

		Node node = getNode(element);
		int featureType = getModelAdapterFactorySet().getFeatureProvider(node).getFeatureType(feature);
		switch (featureType) {
		case IModelAdapter.FEATURE_TYPE_VALUE:
//			CodeSyncPlugin.getInstance().getNodeService().unsetProperty(node.getId(), getOriginalFeatureName(feature).toString());
			break;
		case IModelAdapter.FEATURE_TYPE_CONTAINMENT:
			List<Object> children = (List<Object>) super.getContainmentFeatureIterable(element, feature, null);
			Object child = findChild(children, result.childMatchKey);
			if (child != null && child instanceof Node) {
				Node childNode = (Node) child;
				if (result.childAdded) {
//					CodeSyncPlugin.getInstance().getNodeService().unsetProperty(childNode.getId(), ADDED);
				} else {
//					CodeSyncPlugin.getInstance().getNodeService().removeChild(childNode.getId(), true);
				}
			}
			break;
		default:
			break;
		}
	}
	
	private boolean elementContainsChildWithMatchKey(Object element, Object feature, Object matchKey) {
		if (element == null || matchKey == null) {
			return false;
		}
		Iterable<?> children = getOppositeModelAdapterFactory().getModelAdapter(element).getContainmentFeatureIterable(element, feature, null);
		for (Object child : children) {
			if (matchKey.equals(getOppositeModelAdapterFactory().getModelAdapter(child).getMatchKey(child))) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks if the <code>list</code> contains the <code>child</code> based on its match key.
	 */
	private Object findChild(List list, Object matchKey) {
		if (matchKey == null)
			return null;
		for (Object existingChild : list) {
			if (matchKey.equals(getModelAdapterFactory().getModelAdapter(existingChild).getMatchKey(existingChild))) {
				return existingChild;
			}
		}
		return null;
	}
	
}