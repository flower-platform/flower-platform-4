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

import org.flowerplatform.codesync.Match;
import org.flowerplatform.codesync.adapter.IModelAdapter;
import org.flowerplatform.codesync.adapter.IModelAdapterSet;

/**
 *@author Mariana Gheorghe
 **/
public class MatchActionAddLeftToRight extends MatchActionAddLateralAbstract {
	
	/**
	 *@author Mariana Gheorghe
	 **/
	public MatchActionAddLeftToRight(boolean processDiffs) {
		super(processDiffs);
		// TODO Auto-generated constructor stub
	}

	/**
	 *@author Mariana Gheorghe
	 **/
	protected Object getThis(Match match) {
		return match.getLeft();
	}
	
	/**
	 *@author Mariana Gheorghe
	 **/
	protected Object getOpposite(Match match) {
		return match.getRight();
	}
	
	/**
	 *@author Mariana Gheorghe
	 **/
	protected IModelAdapter getThisModelAdapter(Match match) {
		return match.getCodeSyncAlgorithm().getLeftModelAdapter(getThis(match));
	}
	
	/**
	 *@author Mariana Gheorghe
	 **/
	protected IModelAdapter getOppositeModelAdapter(Match match) {
		return match.getCodeSyncAlgorithm().getRightModelAdapter(getOpposite(match));
	}
	
	@Override
	protected IModelAdapterSet getThisModelAdapterSet(Match match) {
		return match.getCodeSyncAlgorithm().getModelAdapterSetLeft();
	}

	@Override
	protected IModelAdapterSet getOppositeModelAdapterSet(Match match) {
		return match.getCodeSyncAlgorithm().getModelAdapterSetRight();
	}

	/**
	 *@author Mariana Gheorghe
	 **/	
	protected void setOpposite(Match match, Object elment) {
		match.setRight(elment);
	}
	
	@Override
	protected void setChildrenModified(Match match) {
		match.setChildrenModifiedRight(true);
	}
	
}