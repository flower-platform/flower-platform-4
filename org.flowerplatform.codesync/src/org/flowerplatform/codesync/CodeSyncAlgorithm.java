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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowerplatform.codesync.action.ActionSynchronize;
import org.flowerplatform.codesync.action.DiffAction;
import org.flowerplatform.codesync.action.MatchActionAddLeftToRight;
import org.flowerplatform.codesync.action.MatchActionAddRightToLeft;
import org.flowerplatform.codesync.action.MatchActionRemoveLeft;
import org.flowerplatform.codesync.action.MatchActionRemoveRight;
import org.flowerplatform.codesync.adapter.AbstractModelAdapter;
import org.flowerplatform.codesync.adapter.IModelAdapter;
import org.flowerplatform.codesync.feature_provider.FeatureProvider;
import org.flowerplatform.codesync.type_provider.ITypeProvider;
import org.flowerplatform.util.Utils;
import org.flowerplatform.util.controller.TypeDescriptor;
import org.flowerplatform.util.controller.TypeDescriptorRegistry;

/**
 * 
 */
public class CodeSyncAlgorithm {
	
	/**
	 * The value used in case the model adapter does not know about its value.
	 * It is ignored at equality check, i.e. it equals any other value, including null.
	 * 
	 * @author Mariana
	 */
	public static final String UNDEFINED = "UNDEFINED_VALUE";
	
	protected TypeDescriptorRegistry typeDescriptorRegistry;
	
	protected ITypeProvider typeProvider;
	
	
	public CodeSyncAlgorithm(TypeDescriptorRegistry typeDescriptorRegistry, ITypeProvider typeProvider) {
		super();
		this.typeDescriptorRegistry = typeDescriptorRegistry;
		this.typeProvider = typeProvider;
	}

	public void generateDiff(Match match) {
		beforeOrAfterFeaturesProcessed(match, true);
		Object[] delegateAndAdapter = match.getDelegateAndModelAdapter(this);
		if (delegateAndAdapter == null) {
			throw new IllegalArgumentException("A match with no members has been given as parameter.");
		}
		FeatureProvider featureProvider = getFeatureProvider(delegateAndAdapter[0]);
		for (Object feature : featureProvider.getFeatures(delegateAndAdapter[0])) {
			switch (featureProvider.getFeatureType(feature)) {
			case IModelAdapter.FEATURE_TYPE_CONTAINMENT:
				processContainmentFeature(feature, match);
				break;
			case IModelAdapter.FEATURE_TYPE_VALUE:
				processValueFeature(feature, match);
				break;
			}
		}
		beforeOrAfterFeaturesProcessed(match, false);
	}
	
	/**
	 * Calls {@link IModelAdapter#beforeFeaturesProcessed(Object, Object)} or {@link IModelAdapter#featuresProcessed(Object)}
	 * for the ancestor, left and right object of the <code>match</code>.
	 * 
	 * @author Mariana
	 */
	protected void beforeOrAfterFeaturesProcessed(Match match, boolean before) {
		Object right = match.getRight();
		IModelAdapter rightAdapter = null;
		if (right != null) {
			rightAdapter = getRightModelAdapter(right);
		}
		Object ancestor = match.getAncestor();
		IModelAdapter ancestorAdapter = null;
		if (ancestor != null) {
			ancestorAdapter = getAncestorModelAdapter(ancestor);
		}
		Object left = match.getLeft();
		IModelAdapter leftAdapter = null;
		if (left != null) {
			leftAdapter = getLeftModelAdapter(left);
		}
		
		if (before) {
			if (ancestorAdapter != null)
				ancestorAdapter.beforeFeaturesProcessed(ancestor, right);
			if (leftAdapter != null)
				leftAdapter.beforeFeaturesProcessed(left, right);
			if (rightAdapter != null)
				rightAdapter.beforeFeaturesProcessed(right, null);
		} else {
			if (ancestorAdapter != null)
				ancestorAdapter.featuresProcessed(ancestor);
			if (leftAdapter != null)
				leftAdapter.featuresProcessed(left);
			if (rightAdapter != null)
				rightAdapter.featuresProcessed(right);
		}
	}
	
	/**
	 * @author Cristi
	 * @author Mariana
	 * 
	 * 
	 */
	public void processContainmentFeature(Object feature, Match match) {
		// cache the model adapters for children to avoid
		// a lot of calls to the model adapter factory; we are
		// assuming that all the children of an object, for a certain
		// feature, are similar (i.e. same type and same model adapter)
		IModelAdapter leftChildModelAdapter = null;
		IModelAdapter rightChildModelAdapter = null;
		IModelAdapter ancestorChildModelAdapter = null;
		
		// FILL_RIGHT_MAP
		Map<Object, Object> rightMap = new HashMap<Object, Object>();
		Iterable<?> rightList = null;
		if (match.getRight() != null) {
			IModelAdapter modelAdapter = getRightModelAdapter(match.getRight());
			rightList = modelAdapter.getContainmentFeatureIterable(match.getRight(), feature, null); 
			for (Object rightChild : rightList) {
				rightChildModelAdapter = getRightModelAdapter(rightChild);
				if (rightChildModelAdapter != null) {
					rightChildModelAdapter.addToMap(rightChild, rightMap);
				}
			}
		}
		
		// FILL_LEFT_MAP
		Map<Object, Object> leftMap = new HashMap<Object, Object>();
		if (match.getLeft() != null) {
			IModelAdapter modelAdapter = getLeftModelAdapter(match.getLeft());
			Iterable<?> leftList = modelAdapter.getContainmentFeatureIterable(match.getLeft(), feature, rightList); 
			for (Object leftChild : leftList) {
					leftChildModelAdapter = getLeftModelAdapter(leftChild);
				if (leftChildModelAdapter != null) {
					leftChildModelAdapter.addToMap(leftChild, leftMap);
				}
			}
		}
		
		// ITERATE_ANCESTOR_LIST
		if (match.getAncestor() != null) {
			IModelAdapter modelAdapter = getAncestorModelAdapter(match.getAncestor());
			Iterable<?> ancestorList = modelAdapter.getContainmentFeatureIterable(match.getAncestor(), feature, rightList);
			for (Object ancestorChild : ancestorList) {
					ancestorChildModelAdapter = getAncestorModelAdapter(ancestorChild);
				if (ancestorChildModelAdapter != null) {
					// this will be a 3-match, 2-match or 1-match
					// depending on what we find in the maps
					Match childMatch = new Match();
					childMatch.setAncestor(ancestorChild);
					childMatch.setLeft(ancestorChildModelAdapter.removeFromMap(ancestorChild, leftMap, false));
					childMatch.setRight(ancestorChildModelAdapter.removeFromMap(ancestorChild, rightMap, true));
					childMatch.setFeature(feature);
					
					if (!childMatch.isEmptyMatch()) {
						match.addSubMatch(childMatch);
	
						// recurse
						generateDiff(childMatch);
					}
				}
			}
		}
		
		// ITERATE_REMAINING_LEFT_CHILDREN
		for (Object leftChild : leftMap.values()) {
			// this will be a 2-match or 1-match
			// depending on what we find in the maps
			Match childMatch = new Match();
			childMatch.setLeft(leftChild);
			if (leftChildModelAdapter == null) {
				// might be null for CodeSync/code, because the leftMap iteration doesn't happen
				// or if there are no ancestor children
				leftChildModelAdapter = getLeftModelAdapter(leftChild);
			}
			childMatch.setRight(leftChildModelAdapter.removeFromMap(leftChild, rightMap, true));
			childMatch.setFeature(feature);

			if (!childMatch.isEmptyMatch()) {
				match.addSubMatch(childMatch);

				// recurse
				generateDiff(childMatch);
			}
		} 
		
		// ITERATE_REMAINING_RIGHT_CHILDREN
		for (Object rightChild : rightMap.values()) {
			// this will be a 1-match-ancestor (i.e. deleted left & right)
			Match childMatch = new Match();
			childMatch.setRight(rightChild);
			childMatch.setFeature(feature);
			
			if (!childMatch.isEmptyMatch()) {
				match.addSubMatch(childMatch);

				// recurse
				generateDiff(childMatch);
			}
		}
	}
	
	/**
	 * @author Cristi
	 * @author Mariana
	 */
	public static boolean safeEquals(Object a, Object b) {
		if (UNDEFINED.equals(a) || UNDEFINED.equals(b)) {
			return true;
		}
		return Utils.safeEquals(a, b);
	}
	
	/**
	 * @author Cristi
	 * @author Mariana
	 * 
	 * 
	 */
	public void processValueFeature(Object feature, Match match) {
		Diff diff = null;
		
		Object ancestor = match.getAncestor();
		Object left = match.getLeft();
		Object right = match.getRight();
		
		if (ancestor == null && left == null ||
				ancestor == null && right == null ||
				left == null && right == null)
			return; // for 1-Match, don't do anything
		
		Object ancestorValue = null;
		Object leftValue = null;
		Object rightValue = null;
		
		if (right != null) {
			IModelAdapter modelAdapter = getRightModelAdapter(right);
			rightValue = modelAdapter.getValueFeatureValue(right, feature, null);
		}
		
		if (ancestor != null) {
			IModelAdapter modelAdapter = getAncestorModelAdapter(ancestor);
			ancestorValue = modelAdapter.getValueFeatureValue(ancestor, feature, rightValue); 
		}
		
		if (left != null) {
			IModelAdapter modelAdapter = getLeftModelAdapter(left);
			leftValue = modelAdapter.getValueFeatureValue(left, feature, rightValue);
		}
		
		if (left != null && right != null && safeEquals(leftValue, rightValue)) {
			if (ancestor != null && !safeEquals(leftValue, ancestorValue)) {
				diff = new Diff();
				diff.setLeftModified(true);
				diff.setRightModified(true);
			}
		} else {
			if (ancestor != null && left != null && safeEquals(ancestorValue, leftValue)) {
				// modif on RIGHT
				if (right != null) {
					diff = new Diff();
					diff.setRightModified(true);
				}
			} else if (ancestor != null && right != null && safeEquals(ancestorValue, rightValue)) {
				// modif on LEFT
				if (left != null) {
					diff = new Diff();
					diff.setLeftModified(true);
				}
			} else {
				diff = new Diff();
				if (left != null)
					diff.setLeftModified(true);
				if (right != null)
					diff.setRightModified(true);
				diff.setConflict(true);
			}
		}
		if (diff != null) {
			diff.setFeature(feature);
			match.addDiff(diff);
		}
	}
	
	public void synchronize(Match match) {
		DiffAction action = null;
		
		if (Match.MatchType._1MATCH_LEFT.equals(match.getMatchType())) {
			action = new MatchActionAddLeftToRight(false);
		} else if (Match.MatchType._1MATCH_RIGHT.equals(match.getMatchType())) {
			action = new MatchActionAddRightToLeft(false);
//			action = new MatchActionRemoveRight(); // TODO test
		} else if (Match.MatchType._2MATCH_ANCESTOR_LEFT.equals(match.getMatchType())) {
			action = new MatchActionRemoveLeft();
//			action = new MatchActionAddLeftToRight(false); // TODO test
		} else if (Match.MatchType._2MATCH_ANCESTOR_RIGHT.equals(match.getMatchType())) {
			action = new MatchActionRemoveRight();
		}
		
		if (action != null) {
			action.execute(match, -1);
		}
		
		List<Match> subMatches = new ArrayList<Match>();
		subMatches.addAll(match.getSubMatches());
		
		for (Match subMatch : subMatches) {
			synchronize(subMatch);
		}
		
		ActionSynchronize syncAction = new ActionSynchronize();
		syncAction.execute(match);
	}
	
	public ITypeProvider getTypeProvider() {
		return typeProvider;
	}
	
	public FeatureProvider getFeatureProvider(Object object) {
		return getDescriptor(object).getSingleController(FeatureProvider.FEATURE_PROVIDER, object);
	}

	public AbstractModelAdapter getRightModelAdapter(Object right) {
		return getDescriptor(right).getSingleController(AbstractModelAdapter.MODEL_ADAPTER_RIGHT, right);
	}

	public AbstractModelAdapter getAncestorModelAdapter(Object ancestor) {
		return getDescriptor(ancestor).getSingleController(AbstractModelAdapter.MODEL_ADAPTER_ANCESTOR, ancestor);
	}

	public AbstractModelAdapter getLeftModelAdapter(Object left) {
		return getDescriptor(left).getSingleController(AbstractModelAdapter.MODEL_ADAPTER_LEFT, left);
	}
	
	private TypeDescriptor getDescriptor(Object object) {
		String type = typeProvider.getType(object);
		return typeDescriptorRegistry.getExpectedTypeDescriptor(type);
	}
	
}