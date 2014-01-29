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

import org.flowerplatform.codesync.FilteredIterable;
import org.flowerplatform.codesync.NodeModelAdapter;
import org.flowerplatform.codesync.action.ActionResult;

/**
 * @author Mariana
 */
public class NodeModelAdapterLeft extends NodeModelAdapter {

	public NodeModelAdapterLeft() {
		super();
	}
	
	/**
	 * Filters out deleted {@link CodeSyncElement}s. Returns the new containment list from the {@link FeatureChange}s map for
	 * the <code>feature</code>, if it exists. Also recreates the children from <code>correspondingIterable</code> from the 
	 * right side, in case the AST cache was deleted.
	 */
	@Override
	public Iterable<?> getContainmentFeatureIterable(final Object element, Object feature, Iterable<?> correspondingIterable) {
		Iterable<?> list = super.getContainmentFeatureIterable(element, feature, correspondingIterable);
		List result = (List) list;
//		// if the AST cache was deleted, recreate the children using the corresponding iterable from the right side
//		if (isUndefinedList(list)) {
//			result = new ArrayList<Object>();
//			for (Object correspondingElement : correspondingIterable) {
//				result.add(createChildOnContainmentFeature(element, feature, correspondingElement));
//			}
//		}
//		// get the children from the FeatureChange, if it exists
//		FeatureChange change = getFeatureChange(element, feature);
//		if (change != null) {
//			result = (List) change.getNewValue();
//		}
		
		// filter out deleted elements
		return new FilteredIterable<Object, Object>((Iterator<Object>) result.iterator()) {
			protected boolean isAccepted(Object candidate) {
//				if (candidate instanceof Node && ((Node) candidate).isDeleted())
//					return false;
				return true;
			}
		
		};
	}
	
	/**
	 * Returns the new value from the {@link FeatureChange}s map for the <code>feature</code>, if it exists.
	 * Also sets the <code>correspondingValue</code> from the right side, in case the AST cache was deleted.
	 */
	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		// if the AST cache was deleted, recreate the value using the corresponding value from the right side
//		Object value = astCacheElementModelAdapter != null 
//				? astCacheElementModelAdapter.getValueFeatureValue(element, feature, correspondingValue)
//				: super.getValueFeatureValue(element, feature, correspondingValue);
//		if (CodeSyncAlgorithm.UNDEFINED.equals(value)) {
//			setValueFeatureValue(element, feature, correspondingValue);
//		}
//		// get the value from the FeatureChange, if it exists
//		FeatureChange change = getFeatureChange(element, feature);
//		if (change != null) {
//			return change.getNewValue();
//		}
//		return value;
		return super.getValueFeatureValue(element, feature, correspondingValue);
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
//		if (result != null && !result.conflict) {
//			CodeSyncElement cse = getCodeSyncElement(element);
//			if (cse != null) {
//				FeatureChange change = cse.getFeatureChanges().get(feature);
//				if (change != null) {
//					int featureType = getModelAdapterFactorySet().getFeatureProvider(cse).getFeatureType(feature);
//					if (featureType == IModelAdapter.FEATURE_TYPE_VALUE) {
//						setValueFeatureValue(element, feature, change.getNewValue());
//						cse.getFeatureChanges().removeKey(feature);
//					} else {
//						if (featureType == IModelAdapter.FEATURE_TYPE_CONTAINMENT) {
//							List<Object> children = (List<Object>) super.getContainmentFeatureIterable(element, feature, null);
//							List<Object> newValues = (List<Object>) change.getNewValue();
//							if (result.childAdded) {
//								Object existingChild = findChild(children, result.childMatchKey);
//								if (existingChild == null) {
//									// child added to source from new children => add it to the actual children as well
//									Object newChild = findChild(newValues, result.childMatchKey);
//									if (newChild != null) {
//										children.add(newChild instanceof EObject ? EcoreUtil.copy((EObject) newChild) : newChild);
//									}
//								} else {
//									// child added to model from source => add it to the new values as well
//									Object newChild = findChild(newValues, result.childMatchKey);
//									if (newChild == null) {
//										newValues.add(existingChild instanceof EObject ? EcoreUtil.copy((EObject) existingChild) : existingChild);
//									}
//								}
//							} else {
//								Object existingChild = findChild(children, result.childMatchKey);
//								if (existingChild != null) {
//									// child removed from source => remove it from the actual children as well
//									children.remove(existingChild);
//								} else {
//									Object newChild = findChild(newValues, result.childMatchKey);
//									if (newChild != null) {
//										// child removed from model => remove it from the new children as well
//										newValues.remove(newChild);
//									}
//								}
//							}
//							if (newValues.size() == 0) {
//								cse.getFeatureChanges().removeKey(feature);
//							}
//						}
//					}
//				} else {
//					List<Object> children = (List<Object>) super.getContainmentFeatureIterable(element, feature, null);
//					Object child = findChild(children, result.childMatchKey);
//					if (child != null && child instanceof CodeSyncElement) {
//						if (result.childAdded) {
//							((CodeSyncElement) child).setAdded(false);
//						} else {
//							children.remove(child);
//						}
//					}
//				}
//			}
//		}
	}
	
	private boolean elementContainsChildWithMatchKey(Object element, Object feature, Object matchKey) {
		if (element == null || matchKey == null) {
			return false;
		}
		Iterable<?> children = getEObjectConverter().getModelAdapter(element).getContainmentFeatureIterable(element, feature, null);
		for (Object child : children) {
			if (matchKey.equals(getEObjectConverter().getModelAdapter(child).getMatchKey(child))) {
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