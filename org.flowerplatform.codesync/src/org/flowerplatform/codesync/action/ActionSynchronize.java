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
package org.flowerplatform.codesync.action;

import org.flowerplatform.codesync.Diff;
import org.flowerplatform.codesync.Match;

public class ActionSynchronize {

	public static ActionSynchronize INSTANCE = new ActionSynchronize();
	
	/**
	 * @author Cristi
	 * @author Mariana
	 */
	public ActionResult[] execute(Match match) {
		if (match.isConflict() || match.isChildrenConflict())
			throw new IllegalArgumentException("The match (or one of its children) are conflicted.");
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
			if (defaultAction != -1) {
				result[i] = DiffActionRegistry.ActionType.values()[defaultAction].diffAction.execute(match, i);
//				diffsInConflict = diffsInConflict
			}
			i++;
		}
		return result;
	}
	
}