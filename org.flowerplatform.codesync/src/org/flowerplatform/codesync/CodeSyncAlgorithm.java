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

import static org.flowerplatform.core.CoreConstants.NAME;

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
import org.flowerplatform.codesync.adapter.ComposedModelAdapterSet;
import org.flowerplatform.codesync.adapter.IModelAdapter;
import org.flowerplatform.codesync.adapter.IModelAdapterSet;
import org.flowerplatform.codesync.adapter.file.FileModelAdapter;
import org.flowerplatform.core.file.IFileAccessController;
import org.flowerplatform.util.Pair;
import org.flowerplatform.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Mariana Gheorghe
 *
 */
public class CodeSyncAlgorithm {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CodeSyncAlgorithm.class);
	
	protected IModelAdapterSet modelAdapterSetLeft;
	protected IModelAdapterSet modelAdapterSetRight;
	protected IModelAdapterSet modelAdapterSetAncestor;
	
	protected Side featureProviderSide;
	
	protected IFileAccessController fileAccessController;
	
	protected Map<Object, String> filesToRename = new HashMap<Object, String>();
	protected List<Object> filesToDelete = new ArrayList<Object>();
	
	public IModelAdapterSet getModelAdapterSetLeft() {
		return modelAdapterSetLeft;
	}

	public IModelAdapterSet getModelAdapterSetRight() {
		return modelAdapterSetRight;
	}

	public IModelAdapterSet getModelAdapterSetAncestor() {
		return modelAdapterSetAncestor;
	}
	
	public IFileAccessController getFileAccessController() {
		return fileAccessController;
	}
	
	public void setFileAccessController(IFileAccessController controller) {
		this.fileAccessController = controller;
	}
	
	public Map<Object, String> getFilesToRename() {
		return filesToRename;
	}
	
	public List<Object> getFilesToDelete() {
		return filesToDelete;
	}
	
	/**
	 *@author Mariana Gheorghe
	 */
	public void initializeModelAdapterSets(List<String> leftTechnologies, List<String> rightTechnologies, List<String> ancestorTechnologies) {
		modelAdapterSetLeft = getModelAdapterSet(leftTechnologies);
		modelAdapterSetRight = getModelAdapterSet(rightTechnologies);
		modelAdapterSetAncestor = getModelAdapterSet(ancestorTechnologies);
	}
	
	/**
	 *@author Mariana Gheorghe
	 */
	public void initializeFeatureProvider(Side side) {
		featureProviderSide = side;
	}
	
	/**
	 * @author Mariana Gheorghe
	 */
	private IModelAdapterSet getModelAdapterSet(List<String> technologies) {
		if (technologies.size() == 1) {
			return CodeSyncPlugin.getInstance().getModelAdapterSet(technologies.get(0));
		} else {
			ComposedModelAdapterSet composedModelAdapterSet = new ComposedModelAdapterSet();
			for (String technology : technologies) {
				composedModelAdapterSet.addModelAdapterSet(CodeSyncPlugin.getInstance().getModelAdapterSet(technology));
			}
			return composedModelAdapterSet;
		}
	}

	/**
	 * @return A pair containing two flags: 
	 * <ul>
	 * <li>isConflict - shows if the match has a conflict
	 * <li>isSync - shows if the match has been syncronized
	 * </ul>
	 */
	public Pair<Boolean, Boolean> generateDiff(Match match, boolean performAction) {
		boolean isChildrenConflict = false;
		boolean isConflict = false;
		boolean isChildrenSync = true;
		boolean isSync = true;

		LOGGER.debug("Generate diff for {}", match);

		beforeOrAfterFeaturesProcessed(match, true);
		FeatureProvider featureProvider = getFeatureProvider(match);

		// first iterate over value features
		for (Object feature : featureProvider.getValueFeatures()) {
			isConflict = isConflict || processValueFeature(feature, match);
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
				isSync = synchronize(match, action);
			}
		}
		
		// iterate over containment features
		Pair<Boolean, Boolean> conflictSyncPair;
		for (Object feature : featureProvider.getContainmentFeatures()) {
			conflictSyncPair = processContainmentFeature(feature, match, !performLater && performAction);
			isChildrenConflict = isChildrenConflict || conflictSyncPair.a;
			isChildrenSync = isChildrenSync && conflictSyncPair.b;
		}

		if (performLater && performAction) {
			isSync = isSync && synchronize(match, action);
		}

		beforeOrAfterFeaturesProcessed(match, false);

		// propagate childrenConflict flag for parents
		if (isChildrenConflict) {
			if (match.getLeft() != null) {
				getLeftModelAdapter(match.getLeft()).setChildrenConflict(match.getLeft());
			}
			if (match.getRight() != null) {
				getRightModelAdapter(match.getRight()).setChildrenConflict(match.getRight());
			}
		} else {
			if (match.getLeft() != null) {
				getLeftModelAdapter(match.getLeft()).unsetChildrenConflict(match.getLeft());
			}
			if (match.getRight() != null) {
				getRightModelAdapter(match.getRight()).unsetChildrenConflict(match.getRight());
			}
		}

		// propagate childrenSync flag for parents
		if (isChildrenSync) {
			if (match.getLeft() != null) {
				getLeftModelAdapter(match.getLeft()).setChildrenSync(match.getLeft());
			}
			if (match.getRight() != null) {
				getRightModelAdapter(match.getRight()).setChildrenSync(match.getRight());
			}
		}

		// after the sub-matches are processed
		if (performAction) {
			save(match, false);
		}

		return new Pair<Boolean, Boolean>(isConflict || isChildrenConflict, isSync && isChildrenSync);
	}
	
	/**
	 * Calls {@link IModelAdapter#beforeFeaturesProcessed(Object, Object, CodeSyncAlgorithm)} or {@link IModelAdapter#featuresProcessed(Object, CodeSyncAlgorithm)}
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
			if (ancestorAdapter != null) {
				ancestorAdapter.beforeFeaturesProcessed(ancestor, right, this);
			}
			if (leftAdapter != null) {
				leftAdapter.beforeFeaturesProcessed(left, right, this);
			}
			if (rightAdapter != null) {
				rightAdapter.beforeFeaturesProcessed(right, null, this);
			}
		} else {
			if (ancestorAdapter != null) {
				ancestorAdapter.featuresProcessed(ancestor, this);
			}
			if (leftAdapter != null) {
				leftAdapter.featuresProcessed(left, this);
			}
			if (rightAdapter != null) {
				rightAdapter.featuresProcessed(right, this);
			}
		}
	}
	
	/**
	 * @author Cristi
	 * @author Mariana
	 * 
	 * 
	 */
	public Pair<Boolean, Boolean> processContainmentFeature(Object feature, Match match, boolean performAction) {
		boolean isChildrenConflict = false;
		boolean isChildrenSync = true;
		Pair<Boolean, Boolean> conflitSyncPair = new Pair<Boolean, Boolean>(false, true);
		
		LOGGER.debug("Process containment feature {} for {}", feature, match);
		
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
			rightList = modelAdapter.getContainmentFeatureIterable(match.getRight(), feature, null, this); 
			for (Object rightChild : rightList) {
				rightChildModelAdapter = getRightModelAdapter(rightChild);
				if (rightChildModelAdapter != null) {
					rightChildModelAdapter.addToMap(rightChild, rightMap, this);
				}
			}
		}
		
		// FILL_LEFT_MAP
		Map<Object, Object> leftMap = new HashMap<Object, Object>();
		if (match.getLeft() != null) {
			IModelAdapter modelAdapter = getLeftModelAdapter(match.getLeft());
			Iterable<?> leftList = modelAdapter.getContainmentFeatureIterable(match.getLeft(), feature, rightList, this); 
			for (Object leftChild : leftList) {
				leftChildModelAdapter = getLeftModelAdapter(leftChild);
				if (leftChildModelAdapter != null) {
					leftChildModelAdapter.addToMap(leftChild, leftMap, this);
				}
			}
		}
		
		// ITERATE_ANCESTOR_LIST
		if (match.getAncestor() != null) {
			IModelAdapter modelAdapter = getAncestorModelAdapter(match.getAncestor());
			Iterable<?> ancestorList = modelAdapter.getContainmentFeatureIterable(match.getAncestor(), feature, rightList, this);
			for (Object ancestorChild : ancestorList) {
				// this will be a 3-match, 2-match or 1-match
				// depending on what we find in the maps
				Match childMatch = new Match();
				childMatch.setAncestor(ancestorChild);
				ancestorChildModelAdapter = getAncestorModelAdapter(ancestorChild);
				if (ancestorChildModelAdapter != null) {
					childMatch.setMatchKey(ancestorChildModelAdapter.getMatchKey(ancestorChild, this));
					childMatch.setLeft(ancestorChildModelAdapter.removeFromMap(ancestorChild, leftMap, false, this));
					childMatch.setRight(ancestorChildModelAdapter.removeFromMap(ancestorChild, rightMap, true, this));
					childMatch.setFeature(feature);
					
					if (!childMatch.isEmptyMatch()) {
						match.addSubMatch(childMatch);

						// recurse
						conflitSyncPair = generateDiff(childMatch, performAction);
						isChildrenConflict = isChildrenConflict || conflitSyncPair.a;
						isChildrenSync = isChildrenSync && conflitSyncPair.b;
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
			childMatch.setMatchKey(leftChildModelAdapter.getMatchKey(leftChild, this));
			childMatch.setRight(leftChildModelAdapter.removeFromMap(leftChild, rightMap, true, this));
			childMatch.setFeature(feature);

			if (!childMatch.isEmptyMatch()) {
				match.addSubMatch(childMatch);

				// recurse
				conflitSyncPair = generateDiff(childMatch, performAction);
				isChildrenConflict = isChildrenConflict || conflitSyncPair.a;
				isChildrenSync = isChildrenSync && conflitSyncPair.b;
			}
		} 
		
		// ITERATE_REMAINING_RIGHT_CHILDREN
		for (Object rightChild : rightMap.values()) {
			// this will be a 1-match-ancestor (i.e. deleted left & right)
			Match childMatch = new Match();
			childMatch.setRight(rightChild);
			childMatch.setMatchKey(getRightModelAdapter(rightChild).getMatchKey(rightChild, this));
			childMatch.setFeature(feature);
			
			if (!childMatch.isEmptyMatch()) {
				match.addSubMatch(childMatch);

				// recurse
				conflitSyncPair = generateDiff(childMatch, performAction);
				isChildrenConflict = isChildrenConflict || conflitSyncPair.a;
				isChildrenSync = isChildrenSync && conflitSyncPair.b;
			}
		}
		
		return new Pair<Boolean, Boolean>(isChildrenConflict, isChildrenSync);
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
	 * @return true - if the match has a conflict, false - otherwise
	 */
	public Boolean processValueFeature(Object feature, Match match) {
		LOGGER.debug("Process value feature {} for {}", feature, match);
		
		Diff diff = null;
		
		Object ancestor = match.getAncestor();
		Object left = match.getLeft();
		Object right = match.getRight();
		
		if (ancestor == null && left == null 
			|| ancestor == null && right == null
			|| left == null && right == null) {
			return false; // for 1-Match, don't do anything
		}
		
		Object ancestorValue = null;
		Object leftValue = null;
		Object rightValue = null;
		
		if (right != null) {
			IModelAdapter modelAdapter = getRightModelAdapter(right);
			rightValue = modelAdapter.getValueFeatureValue(right, feature, null, this);
		}
		
		if (ancestor != null) {
			IModelAdapter modelAdapter = getAncestorModelAdapter(ancestor);
			ancestorValue = modelAdapter.getValueFeatureValue(ancestor, feature, rightValue, this); 
		}
		
		if (left != null) {
			IModelAdapter modelAdapter = getLeftModelAdapter(left);
			leftValue = modelAdapter.getValueFeatureValue(left, feature, rightValue, this);
		}
		
		if (left != null && right != null && safeEquals(leftValue, rightValue)) {
			if (ancestor != null && !safeEquals(leftValue, ancestorValue)) {
				diff = new Diff();
				diff.setLeftModified(true);
				diff.setRightModified(true);
				getLeftModelAdapter(left).unsetConflict(left, feature, this);
				getRightModelAdapter(right).unsetConflict(right, feature, this);
			}
		} else {
			if (ancestor != null && left != null && safeEquals(ancestorValue, leftValue)) {
				// modif on RIGHT
				if (right != null) {
					diff = new Diff();
					diff.setRightModified(true);
					getLeftModelAdapter(left).unsetConflict(left, feature, this);
				}
			} else if (ancestor != null && right != null && safeEquals(ancestorValue, rightValue)) {
				// modif on LEFT
				if (left != null) {
					diff = new Diff();
					diff.setLeftModified(true);
					getRightModelAdapter(right).unsetConflict(right, feature, this);
				}
			} else {
				diff = new Diff();
				if (left != null) {
					diff.setLeftModified(true);
					getLeftModelAdapter(left).setConflict(left, feature, rightValue, this);
				}
				if (right != null) {
					diff.setRightModified(true);
					getRightModelAdapter(right).setConflict(right, feature, leftValue, this);
				}
				diff.setConflict(true);
				
			}
		}
		if (diff != null) {
			diff.setFeature(feature);
			match.addDiff(diff);
			return diff.isConflict();
		}

		return false;
	}
	
	/**
	 *@author Valentina Bojan
	 */
	public boolean synchronize(Match match) {
		return synchronize(match, null);
	}
	
	/**
	 *@author Valentina Bojan
	 */
	public boolean synchronize(Match match, DiffAction action) {
		boolean isSync = true;

		if (match.isConflict() || match.isChildrenConflict()) {
			LOGGER.debug("Conflict for {}", match);
			return false;
		}

		LOGGER.debug("Perform sync for {}", match);

		// sync match

		if (action == null) {
			action = getDiffActionToApplyForMatch(match);
		}

		if (action != null) {
			ActionResult actResult = action.execute(match, -1);
			isSync = !actResult.conflict;
		}
		
		if (action == null) {
			// no action performed; inform the ancestor
			if (match.getParentMatch() != null && match.getParentMatch().getAncestor() != null) {
				Match parentMatch = match.getParentMatch();
				Object matchKey = match.getAncestor() != null ? getAncestorModelAdapter(match.getAncestor()).getMatchKey(match.getAncestor(), this)
						: getLeftModelAdapter(match.getLeft()).getMatchKey(match.getLeft(), this);
				ActionResult result = new ActionResult(false, false, false, matchKey, !(match.getLeft() == null));
				getAncestorModelAdapter(parentMatch.getAncestor()).actionPerformed(parentMatch.getAncestor(), match.getFeature(), result, parentMatch);
			}
		}
		
		ActionResult[] actResult = new ActionSynchronize().execute(match);
		for (ActionResult actionResult : actResult) {
			if (actionResult != null) {
				isSync = isSync && (!actionResult.conflict);
			}
		}
		
		// update sync flags
		if (match.getAncestor() != null) {
			getAncestorModelAdapter(match.getAncestor()).allActionsPerformed(match.getAncestor(), null, this);
		}
		if (match.getLeft() != null) {
			getLeftModelAdapter(match.getLeft()).allActionsPerformed(match.getLeft(), match.getRight(), this);
			getLeftModelAdapter(match.getLeft()).setSync(match.getLeft());
		}
		if (match.getRight() != null) {
			getRightModelAdapter(match.getRight()).allActionsPerformed(match.getRight(), match.getLeft(), this);
			getRightModelAdapter(match.getRight()).setSync(match.getRight());
		}
		
		// recurse
		List<Match> subMatches = new ArrayList<Match>();
		subMatches.addAll(match.getSubMatches());

		for (Match subMatch : subMatches) {
			isSync = isSync && synchronize(subMatch);
		}

		return isSync;
	}
	
	/**
	 * @author Mariana Gheorghe
	 */
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
	
	/**
	 *@author Mariana Gheorghe
	 */
	public void save(Match match, boolean shouldRecurse) {
		boolean saveSubMatches = false;
		
		// save left
		if (match.getLeft() != null) {
			IModelAdapter leftModelAdapter = getLeftModelAdapter(match.getLeft());
			saveSubMatches |= leftModelAdapter.save(match.getLeft(), match.getCodeSyncAlgorithm());
		}
		
		// save right
		if (match.getRight() != null) {
			IModelAdapter rightModelAdapter = getRightModelAdapter(match.getRight());
			saveSubMatches |= rightModelAdapter.save(match.getRight(), match.getCodeSyncAlgorithm());
		}
		
		// save sub-matches
		if (shouldRecurse && saveSubMatches) {
			for (Match subMatch : match.getSubMatches()) {
				save(subMatch, shouldRecurse);
			}
		}
	}
	
	/**
	 *@author Mariana Gheorghe
	 */
	public String getElementTypeForMatch(Match match) {
		if (match.getLeft() != null) {
			return modelAdapterSetLeft.getType(match.getLeft(), this);
		} else if (match.getRight() != null) {
			return modelAdapterSetRight.getType(match.getRight(), this);
		} else {
			return modelAdapterSetAncestor.getType(match.getAncestor(), this);
		}
	}
	
	/**
	 *@author Mariana Gheorghe
	 */
	public FeatureProvider getFeatureProvider(Match match) {
		if (featureProviderSide == null) {
			throw new RuntimeException("No feature provider side registered for algorithm");
		}
		
		// get the type from the first !null element
		String type = null;
		Side delegateSide = null;
		if (match.getLeft() != null) {
			delegateSide = Side.LEFT;
			type = modelAdapterSetLeft.getType(match.getLeft(), this);
		} else if (match.getRight() != null) {
			delegateSide = Side.RIGHT;
			type = modelAdapterSetRight.getType(match.getRight(), this);
		} else {
			delegateSide = Side.ANCESTOR;
			type = modelAdapterSetAncestor.getType(match.getAncestor(), this);
		}
		
		// get the adapter from the set Side for the found type
		IModelAdapter modelAdapter = null;
		Object model = null;
		switch (featureProviderSide) {
		case LEFT: 
			model = match.getLeft();
			modelAdapter = modelAdapterSetLeft.getModelAdapterForType(type);
			break;
		case RIGHT:
			model = match.getRight();
			modelAdapter = modelAdapterSetRight.getModelAdapterForType(type);
			break;
		case ANCESTOR:
			model = match.getAncestor();
			modelAdapter = modelAdapterSetAncestor.getModelAdapterForType(type);
			break;
		default:
			break;
		}
		
		FeatureProvider featureProvider = new FeatureProvider(modelAdapter, this); 
		
		// special case for files that have delegates based on extension
		if (modelAdapter instanceof FileModelAdapter) {
			String name = null;
			if (model != null) {
				delegateSide = featureProviderSide;
			}
			switch (delegateSide) {
			case LEFT: 
				name = (String) getModelAdapterSetLeft().getModelAdapterForType(type)
						.getValueFeatureValue(match.getLeft(), NAME, null, this);
				break;
			case RIGHT:
				name = (String) getModelAdapterSetRight().getModelAdapterForType(type)
						.getValueFeatureValue(match.getRight(), NAME, null, this);
				break;
			case ANCESTOR:
				name = (String) getModelAdapterSetAncestor().getModelAdapterForType(type)
						.getValueFeatureValue(match.getAncestor(), NAME, null, this);
				break;
			default:
				break;
			}
			int index = name.lastIndexOf(".");
			if (index >= 0) {
				featureProvider.setExtension(name.substring(index + 1));
			}
		}
		
		return featureProvider;
	}
	
	/**
	 *@author Mariana Gheorghe 
	 */
	public IModelAdapter getRightModelAdapter(Object right) {
		return modelAdapterSetRight.getModelAdapter(right, this);
	}
	
	/**
	 *@author Mariana Gheorghe
	 */
	public IModelAdapter getAncestorModelAdapter(Object ancestor) {
		return modelAdapterSetAncestor.getModelAdapter(ancestor, this);
	}

	/**
	 *@author Mariana Gheorghe
	 */
	public IModelAdapter getLeftModelAdapter(Object left) {
		return modelAdapterSetLeft.getModelAdapter(left, this);
	}
	
	/**
	 *@author Mariana Gheorghe
	 */
	public enum Side {
		LEFT, 
		RIGHT,
		ANCESTOR
	}
	
}