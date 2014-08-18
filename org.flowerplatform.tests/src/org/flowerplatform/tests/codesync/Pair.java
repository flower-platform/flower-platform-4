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
package org.flowerplatform.tests.codesync;

import org.flowerplatform.codesync.Match.MatchType;

/**
 * @author Mariana
 */
class Pair {
	public MatchType type;
	public int level;
	
	/**
	 * @author see class
	 */
	public Pair(MatchType type, int level) {
		this.type = type;
		this.level = level;
	}

	@Override
	public String toString() {
		String indent = "";
		for (int i = 0; i < level; i++) {
			indent += "\t";
		}
		return String.format("%s%s @lv %d\n", indent, type, level);
	}
}