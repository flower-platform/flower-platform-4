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
package org.flowerplatform.codesync;

import static org.flowerplatform.codesync.CodeSyncConstants.FEATURE_PROVIDER;
import static org.flowerplatform.codesync.CodeSyncConstants.MODEL_ADAPTER_ANCESTOR;
import static org.flowerplatform.codesync.CodeSyncConstants.MODEL_ADAPTER_LEFT;
import static org.flowerplatform.codesync.CodeSyncConstants.MODEL_ADAPTER_RIGHT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowerplatform.codesync.action.ActionResult;
import org.flowerplatform.codesync.action.ActionSynchronize;
import org.flowerplatform.codesync.action.DiffAction;
import org.flowerplatform.codesync.action.MatchActionAddLeftToRight;
import org.flowerplatform.codesync.action.MatchActionAddRightToLeft;
import org.flowerplatform.codesync.action.MatchActionRemoveAbstract;
import org.flowerplatform.codesync.action.MatchActionRemoveLeft;
import org.flowerplatform.codesync.action.MatchActionRemoveRight;
import org.flowerplatform.codesync.adapter.AbstractModelAdapter;
import org.flowerplatform.codesync.adapter.IModelAdapter;
import org.flowerplatform.codesync.feature_provider.FeatureProvider;
import org.flowerplatform.codesync.type_provider.ITypeProvider;
import org.flowerplatform.util.Utils;
import org.flowerplatform.util.controller.TypeDescriptor;
import org.flowerplatform.util.controller.TypeDescriptorRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 */
public class CodeSyncAlgorithm {
	
	private final static Logger logger = LoggerFactory.getLogger(CodeSyncAlgorithm.class);
	
	protected TypeDescriptorRegistry typeDescriptorRegistry;
	
	protected ITypeProvider typeProvider;
	
	
	public CodeSyncAlgorithm(TypeDescriptorRegistry typeDescriptorRegistry, ITypeProvider typeProvider) {
		super();
		this.typeDescriptorRegistry = typeDescriptorRegistry;
		this.typeProvider = typeProvider;
	}

	public void generateDiff(Match match, boolean performAction) {
		logger.debug("Generate diff for {}", match);
		
		beforeOrAfterFeaturesProcessed(match, true);
		Object[] delegateAndAdapter = match.getDelegateAndModelAdapter(this);
		if (delegateAndAdapter == null) {
			throw new IllegalArgumentException("A match with no members has been given as parameter.");
		}
		FeatureProvider featureProvider = getFeatureProvider(match);
		
		// first iterate over value features
		for (Object feature : featureProvider.getValueFeatures(delegateAndAdapter[0])) {
			processValueFeature(feature, match);
		}
		
		// sync
		boolean performLater = false;
		DiffAction action = null;
		if (performAction) {
			action = getDiffActionToApplyForMatch(match);
			if (action instanceof MatchActionRemoveAbstract) {
				// remove actions must be performed after the sub-matches are computed
				performLater = true;
			} else {
				synchronize(match, action);
			}
		}
		
		// iterate over containment features
		for (Object feature : featureProvider.getContainmentFeatures(delegateAndAdapter[0])) {
			processContainmentFeature(feature, match, !performLater && performAction);
		}

		if (performLater && performAction) {
			synchronize(match, action);
		}
		
		beforeOrAfterFeaturesProcessed(match, false);
		
		// after the sub-matches are processed
		if (performAction) {
			save(match, false);
		}
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
			rightAdapter = getRightModelAdapter(match, right);
		}
		Object ancestor = match.getAncestor();
		IModelAdapter ancestorAdapter = null;
		if (ancestor != null) {
			ancestorAdapter = getAncestorModelAdapter(match, ancestor);
		}
		Object left = match.getLeft();
		IModelAdapter leftAdapter = null;
		if (left != null) {
			leftAdapter = getLeftModelAdapter(match, left);
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
	public void processContainmentFeature(Object feature, Match match, boolean performAction) {
		logger.debug("Process containment feature {} for {}", feature, match);
		
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
			IModelAdapter modelAdapter = getRightModelAdapter(match, match.getRight());
			rightList = modelAdapter.getContainmentFeatureIterable(match.getRight(), feature, null); 
			for (Object rightChild : rightList) {
				rightChildModelAdapter = getRightModelAdapter(null, rightChild);
				if (rightChildModelAdapter != null) {
					rightChildModelAdapter.addToMap(rightChild, rightMap);
				}
			}
		}
		
		// FILL_LEFT_MAP
		Map<Object, Object> leftMap = new HashMap<Object, Object>();
		if (match.getLeft() != null) {
			IModelAdapter modelAdapter = getLeftModelAdapter(match, match.getLeft());
			Iterable<?> leftList = modelAdapter.getContainmentFeatureIterable(match.getLeft(), feature, rightList); 
			for (Object leftChild : leftList) {
				leftChildModelAdapter = getLeftModelAdapter(null, leftChild);
				if (leftChildModelAdapter != null) {
					leftChildModelAdapter.addToMap(leftChild, leftMap);
				}
			}
		}
		
		// ITERATE_ANCESTOR_LIST
		if (match.getAncestor() != null) {
			IModelAdapter modelAdapter = getAncestorModelAdapter(match, match.getAncestor());
			Iterable<?> ancestorList = modelAdapter.getContainmentFeatureIterable(match.getAncestor(), feature, rightList);
			for (Object ancestorChild : ancestorList) {
				// this will be a 3-match, 2-match or 1-match
				// depending on what we find in the maps
				Match childMatch = new Match();
				childMatch.setAncestor(ancestorChild);
				ancestorChildModelAdapter = getAncestorModelAdapter(childMatch, ancestorChild);
				if (ancestorChildModelAdapter != null) {
					childMatch.setMatchKey(ancestorChildModelAdapter.getMatchKey(ancestorChild));
					childMatch.setLeft(ancestorChildModelAdapter.removeFromMap(ancestorChild, leftMap, false));
					childMatch.setRight(ancestorChildModelAdapter.removeFromMap(ancestorChild, rightMap, true));
					childMatch.setFeature(feature);
					
					if (!childMatch.isEmptyMatch()) {
						match.addSubMatch(childMatch);
	
						// recurse
						generateDiff(childMatch, performAction);
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
				leftChildModelAdapter = getLeftModelAdapter(childMatch, leftChild);
			}
			childMatch.setMatchKey(leftChildModelAdapter.getMatchKey(leftChild));
			childMatch.setRight(leftChildModelAdapter.removeFromMap(leftChild, rightMap, true));
			childMatch.setFeature(feature);

			if (!childMatch.isEmptyMatch()) {
				match.addSubMatch(childMatch);

				// recurse
				generateDiff(childMatch, performAction);
			}
		} 
		
		// ITERATE_REMAINING_RIGHT_CHILDREN
		for (Object rightChild : rightMap.values()) {
			// this will be a 1-match-ancestor (i.e. deleted left & right)
			Match childMatch = new Match();
			childMatch.setRight(rightChild);
			childMatch.setMatchKey(getRightModelAdapter(childMatch, rightChild).getMatchKey(rightChild));
			childMatch.setFeature(feature);
			
			if (!childMatch.isEmptyMatch()) {
				match.addSubMatch(childMatch);

				// recurse
				generateDiff(childMatch, performAction);
			}
		}
	}
	
	/**
	 * @author Cristi
	 * @author Mariana
	 */
	public static boolean safeEquals(Object a, Object b) {
		if (CodeSyncConstants.UNDEFINED.equals(a) || CodeSyncConstants.UNDEFINED.equals(b)) {
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
		logger.debug("Process value feature {} for {}", feature, match);
		
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
			IModelAdapter modelAdapter = getRightModelAdapter(match, right);
			rightValue = modelAdapter.getValueFeatureValue(right, feature, null);
		}
		
		if (ancestor != null) {
			IModelAdapter modelAdapter = getAncestorModelAdapter(match, ancestor);
			ancestorValue = modelAdapter.getValueFeatureValue(ancestor, feature, rightValue); 
		}
		
		if (left != null) {
			IModelAdapter modelAdapter = getLeftModelAdapter(match, left);
			leftValue = modelAdapter.getValueFeatureValue(left, feature, rightValue);
		}
		
		if (left != null && right != null && safeEquals(leftValue, rightValue)) {
			if (ancestor != null && !safeEquals(leftValue, ancestorValue)) {
				diff = new Diff();
				diff.setLeftModified(true);
				diff.setRightModified(true);
				getLeftModelAdapter(match, left).unsetConflict(left, feature);
				getRightModelAdapter(match, right).unsetConflict(right, feature);
			}
		} else {
			if (ancestor != null && left != null && safeEquals(ancestorValue, leftValue)) {
				// modif on RIGHT
				if (right != null) {
					diff = new Diff();
					diff.setRightModified(true);
					getLeftModelAdapter(match, left).unsetConflict(left, feature);
				}
			} else if (ancestor != null && right != null && safeEquals(ancestorValue, rightValue)) {
				// modif on LEFT
				if (left != null) {
					diff = new Diff();
					diff.setLeftModified(true);
					getRightModelAdapter(match, right).unsetConflict(right, feature);
				}
			} else {
				diff = new Diff();
				if (left != null) {
					diff.setLeftModified(true);
					getLeftModelAdapter(match, left).setConflict(left, feature, rightValue);
				}
				if (right != null) {
					diff.setRightModified(true);
					getRightModelAdapter(match, right).setConflict(right, feature, leftValue);
				}
				diff.setConflict(true);
				
			}
		}
		if (diff != null) {
			diff.setFeature(feature);
			match.addDiff(diff);
			if (match.getLeft() != null) {
				getLeftModelAdapter(match, left).unsetConflict(left, feature);
			}
			if (match.getRight() != null) {
				getRightModelAdapter(match, right).unsetConflict(right, feature);
			}
		}
	}
	
	public void synchronize(Match match) {
		synchronize(match, null);
	}
	
	public void synchronize(Match match, DiffAction action) {
		if (match.isConflict() || match.isChildrenConflict()) {
			logger.debug("Conflict for {}", match);
			return;
		}
		
		logger.debug("Perform sync for {}", match);
		
		// sync match
		
		if (action == null) {
			action = getDiffActionToApplyForMatch(match);
		}
		
		if (action != null) {
			action.execute(match, -1);
		}
		
		if (action == null) {
			// no action performed; inform the ancestor
			if (match.getParentMatch() != null && match.getParentMatch().getAncestor() != null) {
				Match parentMatch = match.getParentMatch();
				Object matchKey = match.getAncestor() != null ? getAncestorModelAdapter(match, match.getAncestor()).getMatchKey(match.getAncestor())
						: getLeftModelAdapter(match, match.getLeft()).getMatchKey(match.getLeft());
				ActionResult result = new ActionResult(false, false, false, matchKey, !(match.getLeft() == null));
				getAncestorModelAdapter(match, parentMatch.getAncestor()).actionPerformed(parentMatch.getAncestor(), match.getFeature(), result, parentMatch);
			}
		}
		
		ActionSynchronize syncAction = new ActionSynchronize();
		syncAction.execute(match);
		
		// update sync flags
		
		if (match.getAncestor() != null) {
			getAncestorModelAdapter(match, match.getAncestor()).allActionsPerformed(match.getAncestor(), null, this);
		}
		if (match.getLeft() != null) {
			getLeftModelAdapter(match, match.getLeft()).allActionsPerformed(match.getLeft(), match.getRight(), this);
		}
		if (match.getRight() != null) {
			getRightModelAdapter(match, match.getRight()).allActionsPerformed(match.getRight(), match.getLeft(), this);
		}
		
		// recurse
		
		List<Match> subMatches = new ArrayList<Match>();
		subMatches.addAll(match.getSubMatches());
		
		for (Match subMatch : subMatches) {
			synchronize(subMatch);
		}
		
	}
	
	protected DiffAction getDiffActionToApplyForMatch(Match match) {
		if (Match.MatchType._1MATCH_LEFT.equals(match.getMatchType())) {
			return new MatchActionAddLeftToRight(false);
		} else if (Match.MatchType._1MATCH_RIGHT.equals(match.getMatchType())) {
			return new MatchActionAddRightToLeft(false);
//			return new MatchActionRemoveRight(); // TODO test
		} else if (Match.MatchType._2MATCH_ANCESTOR_LEFT.equals(match.getMatchType())) {
			return new MatchActionRemoveLeft();
//			return new MatchActionAddLeftToRight(false); // TODO test
		} else if (Match.MatchType._2MATCH_ANCESTOR_RIGHT.equals(match.getMatchType())) {
			return new MatchActionRemoveRight();
		}
		return null;
	}
	
	public void save(Match match, boolean shouldRecurse) {
		boolean saveSubMatches = false;
		
		// save left
		if (match.getLeft() != null) {
			AbstractModelAdapter leftModelAdapter = getLeftModelAdapter(match, match.getLeft());
			saveSubMatches |= leftModelAdapter.save(match.getLeft());
		}
		
		// save right
		if (match.getRight() != null) {
			AbstractModelAdapter rightModelAdapter = getRightModelAdapter(match, match.getRight());
			saveSubMatches |= rightModelAdapter.save(match.getRight());
		}
		
		// save sub-matches
		if (shouldRecurse && saveSubMatches) {
			for (Match subMatch : match.getSubMatches()) {
				save(subMatch, shouldRecurse);
			}
		}
	}
	
	public ITypeProvider getTypeProvider() {
		return typeProvider;
	}
	
	/**
	 * Use the delegate's descriptor to find the controller, because otherwise we risk getting the wrong 
	 * descriptor for an element that does not exist (e.g. the type for a newly created folder will be 
	 * File (because it was not yet written to disk).
	 * 
	 * <p>
	 * 
	 * Same applies for all the methods below.
	 */
	public FeatureProvider getFeatureProvider(Match match) {
		return getDescriptor(match.getDelegate()).getSingleController(FEATURE_PROVIDER, match.getDelegate());
	}

	public AbstractModelAdapter getRightModelAdapter(Match match, Object right) {
		return getDescriptor(match != null ? match.getDelegate() : right).getSingleController(MODEL_ADAPTER_RIGHT, right);
	}
	
	public AbstractModelAdapter getAncestorModelAdapter(Match match, Object ancestor) {
		return getDescriptor(match != null ? match.getDelegate() : ancestor).getSingleController(MODEL_ADAPTER_ANCESTOR, ancestor);
	}

	public AbstractModelAdapter getLeftModelAdapter(Match match, Object left) {
		return getDescriptor(match != null ? match.getDelegate() : left).getSingleController(MODEL_ADAPTER_LEFT, left);
	}
	
	private TypeDescriptor getDescriptor(Object object) {
		String type = typeProvider.getType(object);
		return typeDescriptorRegistry.getExpectedTypeDescriptor(type);
	}
	
}