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

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.codesync.adapter.IModelAdapter;

/**
 * 
 */
public class Match {
	
	/**
	 * @author Mariana
	 */
	public enum MatchType {
		_3MATCH,
		_2MATCH_ANCESTOR_LEFT,
		_2MATCH_ANCESTOR_RIGHT,
		_2MATCH_LEFT_RIGHT,
		_1MATCH_ANCESTOR,
		_1MATCH_LEFT,
		_1MATCH_RIGHT
	}
	
	/**
	 * 
	 */
	private Match parentMatch;
	
	private List<Match> subMatches;
	
	private Object feature;

	private Object ancestor;
	
	private Object left;
	
	private Object right;

	private List<Diff> diffs;

	/**
	 * This field should be used only in read mode. The add
	 * should be done using {@link #addSubMatch()}.
	 * 
	 * 
	 */
	private boolean diffsConflict;	
	
	private boolean diffsModifiedLeft;
	
	private boolean diffsModifiedRight;
	
	private boolean childrenModifiedLeft;
	
	private boolean childrenModifiedRight;
	
	/**
	 * 
	 */
	private boolean childrenConflict;

	public Match getParentMatch() {
		return parentMatch;
	}
	
	public void setParentMatch(Match parentMatch) {
		this.parentMatch = parentMatch;
	}
	
	@Override
	public String toString() {
		return String.format("Match [%s, ancestor = %s, left = %s, right = %s, %d sub-matches]", 
				getMatchType(), ancestor, left, right, getSubMatches().size());
	}


	// *****************************************************
	// Getters and Setters.
	// *****************************************************
	
	/**
	 * This field should be used only in read mode. The add
	 * should be done using {@link #addSubMatch()}.
	 */
	public List<Match> getSubMatches() {
		if (subMatches == null)
			subMatches = new ArrayList<Match>();
		return subMatches;
	}

	public Object getAncestor() {
		return ancestor;
	}

	public void setAncestor(Object ancestor) {
		this.ancestor = ancestor;
	}

	public Object getLeft() {
		return left;
	}

	public void setLeft(Object left) {
		this.left = left;
	}

	public Object getRight() {
		return right;
	}

	public void setRight(Object right) {
		this.right = right;
	}

	/**
	 * This field should be used only in read mode. The add
	 * should be done using {@link #addDiff()}.
	 */
	public List<Diff> getDiffs() {
		if (diffs == null)
			diffs = new ArrayList<Diff>();
		return diffs;
	}

	public boolean isChildrenModifiedLeft() {
		return childrenModifiedLeft;
	}

	public void setChildrenModifiedLeft(boolean childrenModifiedLeft) {
		this.childrenModifiedLeft = childrenModifiedLeft;
	}

	public boolean isChildrenModifiedRight() {
		return childrenModifiedRight;
	}

	public void setChildrenModifiedRight(boolean childrenModifiedRight) {
		this.childrenModifiedRight = childrenModifiedRight;
	}

	public boolean isChildrenConflict() {
		return childrenConflict;
	}

	public void setChildrenConflict(boolean childrenConflict) {
		this.childrenConflict = childrenConflict;
	}

	public boolean isDiffsConflict() {
		return diffsConflict;
	}

	public void setDiffsConflict(boolean diffsConflict) {
		this.diffsConflict = diffsConflict;
	}

	public Object getFeature() {
		return feature;
	}

	public void setFeature(Object feature) {
		this.feature = feature;
	}

	public boolean isDiffsModifiedLeft() {
		return diffsModifiedLeft;
	}

	public void setDiffsModifiedLeft(boolean diffsModifiedLeft) {
		this.diffsModifiedLeft = diffsModifiedLeft;
	}

	public boolean isDiffsModifiedRight() {
		return diffsModifiedRight;
	}

	public void setDiffsModifiedRight(boolean diffsModifiedRight) {
		this.diffsModifiedRight = diffsModifiedRight;
	}

	// *****************************************************
	// Methods with some logic.
	// *****************************************************

	public Object getDelegate() {
		if (getAncestor() != null)
			return getAncestor();
		else if (getLeft() != null)
			return getLeft();
		else
			return getRight();
	}
	
	/**
	 * @author Cristi
	 * @author Mariana
	 */
	public Object[] getDelegateAndModelAdapter(CodeSyncAlgorithm algorithm) {
		Object delegate = null;
		IModelAdapter modelAdapter = null;
		if (getAncestor() != null && !getAncestor().equals(CodeSyncConstants.UNDEFINED)) {
			delegate = getAncestor();
			modelAdapter = algorithm.getAncestorModelAdapter(this, delegate);
		} else if (getLeft() != null && !getLeft().equals(CodeSyncConstants.UNDEFINED)) {
			delegate = getLeft();
			modelAdapter = algorithm.getLeftModelAdapter(this, delegate);
		} else if (getRight() != null && !getRight().equals(CodeSyncConstants.UNDEFINED)) {
			delegate = getRight();
			modelAdapter = algorithm.getRightModelAdapter(this, delegate);
		}
		
		if (delegate == null)
			return null;
		else 
			return new Object[] { delegate, modelAdapter };
	}
	

	
	/**
	 * Calculated.
	 * 
	 * 
	 */
	public boolean isLeftAdd() {
		return getAncestor() == null && getLeft() != null;
	}
	
	public boolean isRightAdd() {
		return getAncestor() == null && getRight() != null;
	}
	
	public boolean isLeftRemove() {
		return getAncestor() != null && getLeft() == null;
	}
	
	public boolean isRightRemove() {
		return getAncestor() != null && getRight() == null;
	}

	/**
	 * Calculated. Returns <code>true</code> if:
	 * 
	 * <ul>
	 * 	<li>at least one of the {@link #diffs} has a conflict (fast
	 * 		access using {@link #diffsConflict}; OR
	 * 	<li>this is a 1-match-l/r and parent is a 2-match-a-l/r
	 * </ul>
	 * 
	 * 
	 */
	public boolean isConflict() {
		return isDiffsConflict() ||
			getAncestor() == null && (getLeft() == null || getRight() == null) && // this is 1-match-l/r AND
			getParentMatch() != null && 
					(getParentMatch().getAncestor() != null && (getParentMatch().getLeft() == null || getParentMatch().getRight() == null)); // parent match is 2-match
		
	}
	
	public boolean isModifiedLeft() {
		return diffsModifiedLeft || 
			getAncestor() != null && getLeft() == null ||
			getAncestor() == null && getLeft() != null;
	}

	public boolean isModifiedRight() {
		return diffsModifiedRight || 
			getAncestor() != null && getRight() == null ||
			getAncestor() == null && getRight() != null;
	}

	/**
	 * Called by the CodeSync algorithm after a new
	 * {@link Match} has been created and initialized. Adds the
	 * submatch to the list and sets the parent.
	 * 
	 * <ul>
	 * 	<li>Propagates {@link #childrenModifiedLeft} and {@link #childrenModifiedRight}
	 * 		if the submatch is not a 3-match.
	 * 	<li>Propagates {@link #childrenConflict} if this is a 2-match
	 * 		and the submatch is a 1-match-left/right.
	 * </ul>
	 * 
	 * @param subMatch A fully initialized {@link Match}, without submatches
	 * 		or diffs.
	 * 
	 */
	public void addSubMatch(Match subMatch) {
		getSubMatches().add(subMatch);
		subMatch.parentMatch = this;
		subMatch.codeSyncAlgorithm = codeSyncAlgorithm;
		
		int nMatch = 0;
		if (getAncestor() != null)
			nMatch++;
		if (getLeft() != null)
			nMatch++;
		if (getRight() != null)
			nMatch++;
		
		boolean conflict = nMatch == 2 && // this is a 2-match 
			subMatch.getAncestor() == null && // and new child is 1-match
			(subMatch.getLeft() == null || subMatch.getRight() == null); // left or right
		
		boolean modifiedLeft = false, modifiedRight = false;
		
		if (subMatch.getAncestor() != null) {
			if (subMatch.getLeft() == null)
				modifiedLeft = true; 
			if (subMatch.getRight() == null)
				modifiedRight = true;
		} else {
			if (subMatch.getLeft() != null)
				modifiedLeft = true;
			if (subMatch.getRight() != null)
				modifiedRight = true;
		}
		
		propagateConflictAndModified(this, conflict, modifiedLeft, modifiedRight);
	}
	
	/**
	 * Iterative propagation in parallel for conflict and modified. 
	 * 
	 */
	private void propagateConflictAndModified(Match currentMatch, boolean conflict, boolean modifiedLeft, boolean modifiedRight) {
		while (currentMatch != null && 
				(conflict && !currentMatch.isChildrenConflict() || // conflict not yet propagated on this node or
				modifiedLeft && !currentMatch.isChildrenModifiedLeft() ||
				modifiedRight && !currentMatch.isChildrenModifiedRight())) { // modified not yet propagated
			if (conflict && !currentMatch.isChildrenConflict())
				currentMatch.childrenConflict = true;
			if (modifiedLeft && !currentMatch.isChildrenModifiedLeft())
				currentMatch.childrenModifiedLeft = true;
			if (modifiedRight && !currentMatch.isChildrenModifiedRight())
				currentMatch.childrenModifiedRight = true;
			currentMatch = currentMatch.getParentMatch();
		}
	}

	public boolean refreshDiffFlags(boolean conflict, boolean modifiedLeft, boolean modifiedRight) {
		boolean modified = false;
		if (!diffsConflict && conflict) {
			diffsConflict = true;
			modified = true;
		} else if (diffsConflict && !conflict) {
			boolean ok = true;
			for (Diff childDiff : getDiffs()) 
				if (childDiff.isConflict()) {
					ok = false;
					break;
				}
			if (ok) {
				diffsConflict = false;
				modified = true;
			}
		}
		if (!diffsModifiedLeft && modifiedLeft) {
			diffsModifiedLeft = true;
			modified = true;
		} else if (diffsModifiedLeft && !modifiedLeft) {
			boolean ok = true;
			for (Diff childDiff : getDiffs()) 
				if (childDiff.isLeftModified()) {
					ok = false;
					break;
				}
			if (ok) {
				diffsModifiedLeft = false;
				modified = true;
			}
		}
		if (!diffsModifiedRight && modifiedRight) {
			diffsModifiedRight = true;
			modified = true;
		} else if (diffsModifiedRight && !modifiedRight) {
			boolean ok = true;
			for (Diff childDiff : getDiffs()) 
				if (childDiff.isRightModified()) {
					ok = false;
					break;
				}
			if (ok) {
				diffsModifiedRight = false;
				modified = true;
			}
		}
		return modified;
	}
	
	public List<Match> propagateConflictAndModifiedTrueOrFalse(Match currentMatch, boolean conflict, boolean modifiedLeft, boolean modifiedRight) {
		List<Match> modifiedMatches = new ArrayList<Match>();
		while (currentMatch != null && 
				(conflict != currentMatch.isChildrenConflict() || // conflict not yet propagated on this node or
				modifiedLeft != currentMatch.isChildrenModifiedLeft() ||
				modifiedRight != currentMatch.isChildrenModifiedRight())) { // modified not yet propagated
			boolean modified = false;
			if (conflict && !currentMatch.isChildrenConflict()) {
				currentMatch.childrenConflict = true;
				modified = true;
			} else if (!conflict && currentMatch.isChildrenConflict()) {
				boolean ok = true;
				for (Match childMatch : currentMatch.getSubMatches())
					if (childMatch.isConflict() || childMatch.isChildrenConflict()) {
						ok = false;
						break;
					}
				if (ok) {
					currentMatch.childrenConflict = false;
					modified = true;
				}
			}
			if (modifiedLeft && !currentMatch.isChildrenModifiedLeft()) {
				currentMatch.childrenModifiedLeft = true;
				modified = true;
			} else if (!modifiedLeft && currentMatch.isChildrenModifiedLeft()) {
				boolean ok = true;
				for (Match childMatch : currentMatch.getSubMatches())
					if (childMatch.isModifiedLeft() || childMatch.isChildrenModifiedLeft()) {
						ok = false;
						break;
					}
				if (ok) {
					currentMatch.childrenModifiedLeft = false;
					modified = true;
				}
			}
			if (modifiedRight && !currentMatch.isChildrenModifiedRight()) {
				currentMatch.childrenModifiedRight = true;
				modified = true;
			} else if (!modifiedRight && currentMatch.isChildrenModifiedRight()) {
				boolean ok = true;
				for (Match childMatch : currentMatch.getSubMatches())
					if (childMatch.isModifiedRight() || childMatch.isChildrenModifiedRight()) {
						ok = false;
						break;
					}
				if (ok) {
					currentMatch.childrenModifiedRight = false;
					modified = true;
				}
			}
			if (modified)
				modifiedMatches.add(currentMatch);
			currentMatch = currentMatch.getParentMatch();
		}
		return modifiedMatches;
	}

	
	
	/**
	 * Called by the CodeSync algorithm after a new
	 * {@link Diff} has been created and initialized. Adds the
	 * diff to the list and sets the parent.
	 * 
	 * <ul>
	 * 	<li>Sets the {@link #diffsConflict}.
	 * 	<li>Propagates {@link #childrenModifiedLeft} and {@link #childrenModifiedRight}.
	 * 	<li>Propagates {@link #childrenConflict} if the
	 * 		diff has a conflict.
	 * </ul>
	 * 
	 * @param diff A fully initialized {@link Diff}.
	 * 
	 */
	public void addDiff(Diff diff) {
		getDiffs().add(diff);
		diff.setParentMatch(this);
		if (diff.isConflict())
			diffsConflict = true;
		if (diff.isLeftModified())
			diffsModifiedLeft = true;
		if (diff.isRightModified())
			diffsModifiedRight = true;
		if (getParentMatch() != null)
			propagateConflictAndModified(getParentMatch(), diffsConflict, diffsModifiedLeft, diffsModifiedRight);
	}
	
	/**
	 * @author Mariana
	 */
	public MatchType getMatchType() {
		if (getAncestor() != null) 
			if (getLeft() != null)
				if (getRight() != null)
					return MatchType._3MATCH;
				else
					return MatchType._2MATCH_ANCESTOR_LEFT;
			else
				if (getRight() != null)
					return MatchType._2MATCH_ANCESTOR_RIGHT;
				else 
					return MatchType._1MATCH_ANCESTOR;
		else
			if (getLeft() != null)
				if (getRight() != null)
					return MatchType._2MATCH_LEFT_RIGHT;
				else
					return MatchType._1MATCH_LEFT;
			else
				if (getRight() != null)
					return MatchType._1MATCH_RIGHT;
				else 
					return null;
	}
	
	/**
	 * @author Mariana
	 */
	public boolean isEmptyMatch() {
		if (getAncestor() != null && !getAncestor().equals(CodeSyncConstants.UNDEFINED)) {
			return false;
		}
		if (getLeft() != null && !getLeft().equals(CodeSyncConstants.UNDEFINED)) {
			return false;
		}
		if (getRight() != null && !getRight().equals(CodeSyncConstants.UNDEFINED)) {
			return false;
		}
		return true;
	}
	
	private CodeSyncAlgorithm codeSyncAlgorithm;
	
	public CodeSyncAlgorithm getCodeSyncAlgorithm() {
		return codeSyncAlgorithm;
	}

	public void setCodeSyncAlgorithm(CodeSyncAlgorithm codeSyncAlgorithm) {
		this.codeSyncAlgorithm = codeSyncAlgorithm;
	}
	
}