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
import org.flowerplatform.codesync.IModelAdapter;
import org.flowerplatform.codesync.Match;


/**
 * 
 */
public abstract class MatchActionAddAncestorAbstract extends MatchActionAddAbstract {

	public MatchActionAddAncestorAbstract(boolean processDiffs) {
		super(processDiffs);
	}

	protected Object getThis(Match match) {
		return match.getAncestor();
	}

	protected IModelAdapter getThisModelAdapter(Match match) {
		return match.getModelAdapterFactorySet().getAncestorFactory().getModelAdapter(getThis(match));
	}

	@Override
	protected void processDiffs(Match match) {
		for (Diff diff : match.getDiffs()) {
			diff.setConflict(false);
		}
		match.setDiffsConflict(false);
	}
}