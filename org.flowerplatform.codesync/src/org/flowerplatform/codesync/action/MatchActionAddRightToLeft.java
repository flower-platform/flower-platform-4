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
import org.flowerplatform.codesync.adapter.IModelAdapterSet;


/**
 * @author Mariana Gheorghe
 */
public class MatchActionAddRightToLeft extends MatchActionAddLateralAbstract {
	
	/**
	 *@author Mariana Gheorghe
	 */
	public MatchActionAddRightToLeft(boolean processDiffs) {
		super(processDiffs);
	}

	/**
	 * @author see class
	 */
	protected Object getThis(Match match) {
		return match.getRight();
	}
	
	/**
	 * @author see class
	 */
	protected Object getOpposite(Match match) {
		return match.getLeft();
	}
	
	/**
	 * @author see class
	 */
	protected IModelAdapter getThisModelAdapter(Match match) {
		return match.getCodeSyncAlgorithm().getRightModelAdapter(getThis(match));
	}
	
	/**
	 * @author see class
	 */
	protected IModelAdapter getOppositeModelAdapter(Match match) {
		return match.getCodeSyncAlgorithm().getLeftModelAdapter(getOpposite(match));
	}
	
	@Override
	protected IModelAdapterSet getThisModelAdapterSet(Match match) {
		return match.getCodeSyncAlgorithm().getModelAdapterSetRight();
	}

	@Override
	protected IModelAdapterSet getOppositeModelAdapterSet(Match match) {
		return match.getCodeSyncAlgorithm().getModelAdapterSetLeft();
	}
	
	/**
	 * @author see class
	 */
	protected void setOpposite(Match match, Object elment) {
		match.setLeft(elment);
	}
	
	@Override
	protected void setChildrenModified(Match match) {
		match.setChildrenModifiedLeft(true);
	}
	
}