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

/**
 * @author Mariana Gheorghe
 */
public abstract class DiffAction {
	
	/**
	 *@author see class
	 **/
	public abstract ActionResult execute(Match match, int diffIndex);
	
	/**
	 * @author Mariana Gheorghe
	 */
	public void actionPerformed(
			Match match,
			IModelAdapter leftModelAdapter, Object left, 
			IModelAdapter rightModelAdapter, Object right,
			Object feature, ActionResult result) {
		
		if (leftModelAdapter != null) {
			leftModelAdapter.actionPerformed(left, feature, result, match);
		}
		if (rightModelAdapter != null) {
			rightModelAdapter.actionPerformed(right, feature, result, match);
		}
	}
	
}