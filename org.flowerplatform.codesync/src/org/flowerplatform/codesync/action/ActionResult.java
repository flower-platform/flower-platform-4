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

/**
 * @author Mariana Gheorghe
 */
public class ActionResult {
	
	public boolean conflict;
	
	public boolean modifiedLeft;
	
	public boolean modifiedRight;
	
	/**
	 * The match key of the child object that was added or removed.
	 * 
	 * @author Mariana
	 */
	public Object childMatchKey;
	
	/**
	 * @author Mariana
	 */
	public boolean childAdded;

	/**
	 *@author Mariana Gheorghe
	 **/
	public ActionResult(boolean conflict, boolean modifiedLeft, boolean modifiedRight) {
		super();
		this.conflict = conflict;
		this.modifiedLeft = modifiedLeft;
		this.modifiedRight = modifiedRight;
	}
	
	/**
	 * @author Mariana
	 */
	public ActionResult(boolean conflict, boolean modifiedLeft, boolean modifiedRight, Object childMatchKey, boolean childAdded) {
		this(conflict, modifiedLeft, modifiedRight);
		this.childMatchKey = childMatchKey;
		this.childAdded = childAdded;
	}
	
}