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

import org.flowerplatform.codesync.Diff;
import org.flowerplatform.codesync.Match;
import org.flowerplatform.codesync.adapter.IModelAdapter;
import org.flowerplatform.codesync.adapter.IModelAdapterSet;


/**
 *@author Mariana Gheorghe
 **/
public abstract class MatchActionAddAncestorAbstract extends MatchActionAddAbstract {

	/**
	 *@author Mariana Gheorghe
	 **/
	public MatchActionAddAncestorAbstract(boolean processDiffs) {
		super(processDiffs);
	}

	/**
	 *@author Mariana Gheorghe
	 **/
	protected Object getThis(Match match) {
		return match.getAncestor();
	}

	/**
	 *@author Mariana Gheorghe
	 **/
	protected IModelAdapter getThisModelAdapter(Match match) {
		return match.getCodeSyncAlgorithm().getAncestorModelAdapter(getThis(match));
	}

	@Override
	protected IModelAdapterSet getThisModelAdapterSet(Match match) {
		return match.getCodeSyncAlgorithm().getModelAdapterSetAncestor();
	}

	@Override
	protected void processDiffs(Match match) {
		for (Diff diff : match.getDiffs()) {
			diff.setConflict(false);
		}
		match.setDiffsConflict(false);
	}
}