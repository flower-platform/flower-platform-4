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

import org.flowerplatform.codesync.Match;
import org.flowerplatform.codesync.adapter.IModelAdapter;

public class MatchActionRemoveRight extends MatchActionRemoveAbstract {

	@Override
	protected Object getThis(Match match) {
		return match.getRight();
	}

	@Override
	protected Object getOpposite(Match match) {
		return match.getLeft();
	}

	@Override
	protected IModelAdapter getModelAdapter(Match match) {
		return match.getCodeSyncAlgorithm().getRightModelAdapter(match.getRight());
	}

	@Override
	protected IModelAdapter getOppositeModelAdapter(Match match) {
		return match.getCodeSyncAlgorithm().getLeftModelAdapter(match.getLeft());
	}
	
	@Override
	protected void unsetThis(Match match) {
		match.setDiffsModifiedRight(false);
		match.setChildrenModifiedRight(false);
		match.setRight(null);
	}

}