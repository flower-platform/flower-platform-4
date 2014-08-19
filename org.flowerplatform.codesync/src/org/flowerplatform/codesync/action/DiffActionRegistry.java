/* license-start
 * 
 * Copyright (C) 2008 - 2014 Crispico Software, <http://www.crispico.com/>.
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
package org.flowerplatform.codesync.action;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.codesync.Diff;
import org.flowerplatform.codesync.Match;

/**
 * @author Mariana Gheorghe
 */
public class DiffActionRegistry {
	
	/**
	 *@author see class
	 */
	public enum ActionType {
		
		ACTION_TYPE_COPY_LEFT_RIGHT(new DiffActionCopyLeftToRight()),
		ACTION_TYPE_COPY_RIGHT_LEFT(new DiffActionCopyRightToLeft()),
		ACTION_TYPE_REVERT(new DiffActionRevert()),
		ACTION_TYPE_1_MATCH_LEFT_ADD_SAME_ON_OPPOSITE(new MatchActionAddLeftToRight(false)),
		ACTION_TYPE_1_MATCH_LEFT_REMOVE_THIS(new MatchActionRemoveLeft()),
		ACTION_TYPE_1_MATCH_RIGHT_ADD_SAME_ON_OPPOSITE(new MatchActionAddRightToLeft(false)),
		ACTION_TYPE_1_MATCH_RIGHT_REMOVE_THIS(new MatchActionRemoveRight()),
		ACTION_TYPE_1_MATCH_ANCESTOR_ADD_ANCESTOR_ON_LEFT_RIGHT(new MatchActionAddAncestorToLeftRight()),
		ACTION_TYPE_2_MATCH_LEFT_RIGHT_REMOVE_LEFT_RIGHT(new MatchActionRemoveLeftRight()),
		ACTION_TYPE_2_MATCH_LEFT_REMOVE_OPPOSITE(new MatchActionRemoveLeft()),
		ACTION_TYPE_2_MATCH_LEFT_ADD_FROM_OPPOSITE(new MatchActionAddLeftToRight(true)),
		ACTION_TYPE_2_MATCH_LEFT_ADD_FROM_ANCESTOR(new MatchActionAddAncestorToRight(true)),
		ACTION_TYPE_2_MATCH_RIGHT_REMOVE_OPPOSITE(new MatchActionRemoveRight()),
		ACTION_TYPE_2_MATCH_RIGHT_ADD_FROM_OPPOSITE(new MatchActionAddRightToLeft(true)),
		ACTION_TYPE_2_MATCH_RIGHT_ADD_FROM_ANCESTOR(new MatchActionAddAncestorToLeft(true));
		
		public DiffAction diffAction;
		
		private ActionType(DiffAction action) {
			this.diffAction = action;
		}
	}
	
	/**
	 *@author see class
	 */
	public class ActionEntries {
		public List<DiffActionEntry> entries;
		public int defaultAction;
		
		/**
		 *@author see class 
		 */
		public ActionEntries(List<DiffActionEntry> entries, int defaultAction) {
			super();
			this.entries = entries;
			this.defaultAction = defaultAction;
		}
	}
	
	public static final DiffActionRegistry INSTANCE = new DiffActionRegistry();

	/**
	 *@author see class 
	 */
	public ActionEntries getActionEntriesForUI(Match match, Diff diff, boolean returnOnlyDefaultActions) {
		List<DiffActionEntry> result = null;
		if (!returnOnlyDefaultActions) {
			result = new ArrayList<DiffActionEntry>();
		}
		int defaultAction = -1;

		DiffActionEntry ae;
		if (diff != null) {
			
			if (diff.isLeftModified() && !diff.isRightModified()) {
				defaultAction = ActionType.ACTION_TYPE_COPY_LEFT_RIGHT.ordinal();
			} else if (!diff.isLeftModified() && diff.isRightModified()) {
				defaultAction = ActionType.ACTION_TYPE_COPY_RIGHT_LEFT.ordinal();
			}
			
			if (!returnOnlyDefaultActions) {
				ae = new DiffActionEntry();
				ae.setLabel("Copy Left -> Right");
				ae.setActionType(ActionType.ACTION_TYPE_COPY_LEFT_RIGHT.ordinal());
				ae.setDiffIndex(match.getDiffs().indexOf(diff));
				ae.setEnabled(match.getRight() != null
						&& (diff.isLeftModified() && !diff.isRightModified()
						|| diff.isLeftModified() && diff.isRightModified() && diff.isConflict()));
	//			ae.setDefault1(diff.isLeftModified() && !diff.isRightModified());
				result.add(ae);
				
				ae = new DiffActionEntry();
				ae.setLabel("Copy Left <- Right");
				ae.setActionType(ActionType.ACTION_TYPE_COPY_RIGHT_LEFT.ordinal());
				ae.setDiffIndex(match.getDiffs().indexOf(diff));
				ae.setEnabled(match.getLeft() != null
						&& (!diff.isLeftModified() && diff.isRightModified()
						|| diff.isLeftModified() && diff.isRightModified() && diff.isConflict()));
	//			ae.setDefault1(!diff.isLeftModified() && diff.isRightModified());
				result.add(ae);
				
				ae = new DiffActionEntry();
				ae.setLabel("Revert");
				ae.setActionType(ActionType.ACTION_TYPE_REVERT.ordinal());
				ae.setDiffIndex(match.getDiffs().indexOf(diff));
				ae.setEnabled(diff.getParentMatch().getAncestor() != null);
				result.add(ae);
			}
		} else {
			// match actions
			if (match.getAncestor() == null && match.getRight() == null) {
				if (!(match.getParentMatch().getAncestor() == null && match.getParentMatch().getRight() == null)) {
					// 1-match-left; only the "top" one
					if (match.getParentMatch().getRight() != null) {
						defaultAction = ActionType.ACTION_TYPE_1_MATCH_LEFT_ADD_SAME_ON_OPPOSITE.ordinal();
					}
					if (!returnOnlyDefaultActions) {
					
						if (match.getParentMatch().getRight() != null) {
							ae = new DiffActionEntry();
							ae.setLabel("Copy Left -> Right (Add Left on Right)");
							ae.setActionType(ActionType.ACTION_TYPE_1_MATCH_LEFT_ADD_SAME_ON_OPPOSITE.ordinal());
							ae.setEnabled(true);
//							ae.setDefault1(true);
							result.add(ae);
						} else {
							ae = new DiffActionEntry();
							ae.setLabel("Copy Left -> Right (Nothing)");
							result.add(ae);
						}
						
						ae = new DiffActionEntry();
						ae.setLabel("Copy Left <- Right (Nothing)");
						result.add(ae);
		
						ae = new DiffActionEntry();
						ae.setLabel("Revert (Remove Left)");
						ae.setActionType(ActionType.ACTION_TYPE_1_MATCH_LEFT_REMOVE_THIS.ordinal());
						ae.setEnabled(true);
						result.add(ae);
					}
				}
			} else if (match.getAncestor() == null && match.getLeft() == null) {
				if (!(match.getParentMatch().getAncestor() == null && match.getParentMatch().getLeft() == null)) {
					// 1-match-right; only the "top" one
					if (match.getParentMatch().getLeft() != null) {
						defaultAction = ActionType.ACTION_TYPE_1_MATCH_RIGHT_ADD_SAME_ON_OPPOSITE.ordinal();
					}
					
					if (!returnOnlyDefaultActions) {
						ae = new DiffActionEntry();
						ae.setLabel("Copy Left -> Right (Nothing)");
						result.add(ae);
		
						if (match.getParentMatch().getLeft() != null) {
							ae = new DiffActionEntry();
							ae.setLabel("Copy Left <- Right (Add Right on Left)");
							ae.setActionType(ActionType.ACTION_TYPE_1_MATCH_RIGHT_ADD_SAME_ON_OPPOSITE.ordinal());
							ae.setEnabled(true);
	//						ae.setDefault1(true);
							result.add(ae);
						} else {
							ae = new DiffActionEntry();
							ae.setLabel("Copy Left <- Right (Nothing)");
							result.add(ae);
						}
						
						ae = new DiffActionEntry();
						ae.setLabel("Revert (Remove Right)");
						ae.setActionType(ActionType.ACTION_TYPE_1_MATCH_RIGHT_REMOVE_THIS.ordinal());
						ae.setEnabled(true);
						result.add(ae);
					}
				}
			} else if (match.getAncestor() != null && match.getLeft() == null && match.getRight() == null) {
				if (!(match.getParentMatch().getAncestor() != null && match.getParentMatch().getLeft() == null && match.getParentMatch().getRight() == null)) {
					// 1-match-ancestor; only the "top" one
					ae = new DiffActionEntry();
					ae.setLabel("Copy Left -> Right (Nothing)");
					result.add(ae);

					ae = new DiffActionEntry();
					ae.setLabel("Copy Left <- Right (Nothing)");
					result.add(ae);

					if (match.getParentMatch().getLeft() != null && match.getParentMatch().getRight() != null) {
						ae = new DiffActionEntry();
						ae.setLabel("Revert (Add Ancestor on Left and Right)");
						ae.setActionType(ActionType.ACTION_TYPE_1_MATCH_ANCESTOR_ADD_ANCESTOR_ON_LEFT_RIGHT.ordinal());
						ae.setEnabled(true);
						result.add(ae);
					} else {
						ae = new DiffActionEntry();
						ae.setLabel("Revert (Nothing)");
						result.add(ae);
					}
				}
			} else if (match.getAncestor() == null && match.getLeft() != null && match.getRight() != null) {
				if (!(match.getParentMatch().getAncestor() == null && match.getParentMatch().getLeft() != null && match.getParentMatch().getRight() != null)) {
					// 2-match-left-right; only the "top" one
					ae = new DiffActionEntry();
					ae.setLabel("Copy Left -> Right (Nothing)");
					result.add(ae);

					ae = new DiffActionEntry();
					ae.setLabel("Copy Left <- Right (Nothing)");
					result.add(ae);

					ae = new DiffActionEntry();
					ae.setLabel("Revert (Remove Left and Right)");
					ae.setActionType(ActionType.ACTION_TYPE_2_MATCH_LEFT_RIGHT_REMOVE_LEFT_RIGHT.ordinal());
					ae.setEnabled(true);
					result.add(ae);
				}
			} else if (match.getAncestor() != null && match.getRight() == null) {
				if (!(match.getParentMatch().getAncestor() != null && match.getParentMatch().getRight() == null)) {
					// 2-match-ancestor-left; deleted on right
					defaultAction = ActionType.ACTION_TYPE_2_MATCH_LEFT_REMOVE_OPPOSITE.ordinal();
					
					if (!returnOnlyDefaultActions) {
						ae = new DiffActionEntry();
						ae.setLabel("Copy Left -> Right (Add Left on Right)");
						ae.setActionType(ActionType.ACTION_TYPE_2_MATCH_LEFT_ADD_FROM_OPPOSITE.ordinal());
						ae.setEnabled(true);
						result.add(ae);
						
						ae = new DiffActionEntry();
						ae.setLabel("Copy Left <- Right (Remove Left)");
						ae.setActionType(ActionType.ACTION_TYPE_2_MATCH_LEFT_REMOVE_OPPOSITE.ordinal());
	//					ae.setDefault1(true);
						ae.setEnabled(true);
						result.add(ae);
						
						ae = new DiffActionEntry();
						ae.setLabel("Revert (Add Ancestor on Right)");
						ae.setActionType(ActionType.ACTION_TYPE_2_MATCH_LEFT_ADD_FROM_ANCESTOR.ordinal());
						ae.setEnabled(true);
						result.add(ae);
					}
				}
			} else if (match.getAncestor() != null && match.getLeft() == null) {
				if (!(match.getParentMatch().getAncestor() != null && match.getParentMatch().getLeft() == null)) {
					// 2-match-ancestor-right; deleted on left
					defaultAction = ActionType.ACTION_TYPE_2_MATCH_RIGHT_REMOVE_OPPOSITE.ordinal();
					
					if (!returnOnlyDefaultActions) {
						ae = new DiffActionEntry();
						ae.setLabel("Copy Left -> Right (Remove Right)");
						ae.setActionType(ActionType.ACTION_TYPE_2_MATCH_RIGHT_REMOVE_OPPOSITE.ordinal());
//						ae.setDefault1(true);
						ae.setEnabled(true);
						result.add(ae);
						
						ae = new DiffActionEntry();
						ae.setLabel("Copy Left <- Right (Add Right on Left)");
						ae.setActionType(ActionType.ACTION_TYPE_2_MATCH_RIGHT_ADD_FROM_OPPOSITE.ordinal());
						ae.setEnabled(true);
						result.add(ae);
						
						ae = new DiffActionEntry();
						ae.setLabel("Revert (Add Ancestor on Left)");
						ae.setActionType(ActionType.ACTION_TYPE_2_MATCH_RIGHT_ADD_FROM_ANCESTOR.ordinal());
						ae.setEnabled(true);
						result.add(ae);
					}
				}
			}
		}
		
		if (!returnOnlyDefaultActions && defaultAction != -1) {
			for (DiffActionEntry entry : result) {
				if (entry.getActionType() == defaultAction) {
					entry.setDefault1(true);
				}
			}
		}
		
		return new ActionEntries(result, defaultAction);
	}
	
}