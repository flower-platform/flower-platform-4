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

import org.flowerplatform.codesync.Match;
import org.flowerplatform.codesync.adapter.IModelAdapter;


/**
 * 
 */
public class MatchActionAddAncestorToLeft extends MatchActionAddAncestorAbstract {

	public MatchActionAddAncestorToLeft(boolean processDiffs) {
		super(processDiffs);
	}

	protected Object getOpposite(Match match) {
		return match.getLeft();
	}

	protected IModelAdapter getOppositeModelAdapter(Match match) {
		return match.getModelAdapterFactorySet().getLeftFactory().getModelAdapter(getOpposite(match));
	}
	
	protected void setOpposite(Match match, Object elment) {
		match.setLeft(elment);
	}

	@Override
	protected void setChildrenModified(Match match) {
		match.setChildrenModifiedLeft(false);
	}
}