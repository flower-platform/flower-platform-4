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
package org.flowerplatform.codesync.action;

import org.flowerplatform.codesync.Diff;
import org.flowerplatform.codesync.Match;

public class ActionSynchronize {

	public static ActionSynchronize instance = new ActionSynchronize();
	
	/**
	 * @author Cristi
	 * @author Mariana
	 */
	public ActionResult[] execute(Match match) {
		if (match.isConflict() || match.isChildrenConflict()) {
			throw new IllegalArgumentException("The match (or one of its children) are conflicted.");
		}
		boolean childrenConflict = false;
		boolean childrenModifiedLeft = false;
		boolean childrenModifiedRight = false;
		
		boolean diffsInConflict = false;
		boolean diffsModifiedLeft = false;
		boolean diffsModifiedRight = false;
		
		ActionResult[] result = new ActionResult[match.getDiffs().size()];
		
		int i = 0;
		for (Diff diff : match.getDiffs()) {
			int defaultAction = DiffActionRegistry.INSTANCE.getActionEntriesForUI(match, diff, true).defaultAction;
			
			// If the changes on the LEFT are the same as the ones on the RIGHT, then no action
			// will be performed. In this case, we must announce the adaptors manually so they can
			// do their actions (i.e. cleaning, ...).
			if (defaultAction != -1) {
				result[i] = DiffActionRegistry.ActionType.values()[defaultAction].diffAction.execute(match, i);
//				diffsInConflict = diffsInConflict
			} else {
				ActionResult actionResult = new ActionResult(diff.isConflict(), diff.isLeftModified(), diff.isRightModified());
				if (match.getLeft() != null) {
					match.getCodeSyncAlgorithm().getLeftModelAdapter(match.getLeft()).actionPerformed(match.getLeft(), diff.getFeature(), actionResult, match);
				}
				if (match.getRight() != null) {
					match.getCodeSyncAlgorithm().getRightModelAdapter(match.getRight()).actionPerformed(match.getRight(), diff.getFeature(), actionResult, match);
				}				
			}
			i++;
		}
		return result;
	}
	
}