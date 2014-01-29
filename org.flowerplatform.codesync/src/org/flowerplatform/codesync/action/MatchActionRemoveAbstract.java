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

import java.util.Iterator;

import org.flowerplatform.codesync.IModelAdapter;
import org.flowerplatform.codesync.Match;

public abstract class MatchActionRemoveAbstract extends DiffAction {

	protected abstract Object getThis(Match match);
	protected abstract Object getOpposite(Match match);
	protected abstract IModelAdapter getModelAdapter(Match match);
	protected abstract void unsetThis(Match match);
	
	/**
	 * @author Cristi
	 * @author Mariana
	 */
	@Override
	public ActionResult execute(Match match, int diffIndex) {
		IModelAdapter modelAdapter = getModelAdapter(match.getParentMatch() != null ? match.getParentMatch() : match);
		Object child = getThis(match);
		IModelAdapter childModelAdapter = getModelAdapter(match);
		modelAdapter.removeChildrenOnContainmentFeature(
				match.getParentMatch() != null ? getThis(match.getParentMatch()) : null, 
				match.getFeature(), 
				child);
		
		if (match.getAncestor() == null) {
			if (getOpposite(match) != null) {
				// for the RemoveLeft + RemoveRight case; this invocation will be followed
				// by an invocation of the *RemoveRight; the result is ignored
				unsetThis(match);
				return null;
			} else {
				// 0-match => remove the match
				match.getParentMatch().getSubMatches().remove(match);
				match.setParentMatch(null);
				return new ActionResult(false, false, false, childModelAdapter.getMatchKey(child), false);
			}
		} else {
			// submatches (and possible diffs) still exist; they need to be updated
			recurseUpdateFieldsAndFlags(match);
			return new ActionResult(false, true, true, childModelAdapter.getMatchKey(child), false);
		}
	}
	
	protected void recurseUpdateFieldsAndFlags(Match match) {
		// clear diffs and flags
		match.getDiffs().clear();
		match.setDiffsConflict(false);
		
		// update children flags (for this side only; other side is not "changed")
		match.setChildrenConflict(false);
	
		// update other fields and flags
		unsetThis(match);
		
		for (Iterator<Match> iter = match.getSubMatches().iterator(); iter.hasNext();) {
			Match currentMatch = iter.next();
			if (currentMatch.getAncestor() == null) {
				currentMatch.setParentMatch(null);
				iter.remove();
			}
			recurseUpdateFieldsAndFlags(currentMatch);
		}
	}

}