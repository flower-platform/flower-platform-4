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
package org.flowerplatform.codesync.regex;

/**
 * @author Elena Posea
 * 
 */
public class State {

	public int level;

	public Object node;

	/**
	 * @param level
	 *            nesting level
	 * @param node
	 *            currentNode for stack (it is needed to know where to attach
	 *            info as children)
	 */
	public State(int level, Object node) {
		this.level = level;
		this.node = node;
	}
}
