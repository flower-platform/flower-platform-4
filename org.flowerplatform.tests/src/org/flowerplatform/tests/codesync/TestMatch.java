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

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.codesync.Match.MatchType;

/**
 * @author Mariana Gheorghe
 */
public class TestMatch {

	public Object matchKey;
	public MatchType matchType;
	
	public boolean isConflict;
	public boolean isChildrenConflict;
	
	public TestMatch parent;
	public List<TestMatch> children = new ArrayList<TestMatch>();
	
	public boolean tested;
	/**
	 *@author Mariana Gheorghe
	 **/
	public TestMatch(Object matchKey, MatchType matchType) {
		this(matchKey, matchType, false, false);
	}
	/**
	 *@author Mariana Gheorghe
	 **/
	public TestMatch(Object matchKey, MatchType matchType, boolean isConflict, boolean isChildrenConflict) {
		this.matchKey = matchKey;
		this.matchType = matchType;
		this.isConflict = isConflict;
		this.isChildrenConflict = isChildrenConflict;
	}
	/**
	 *@author Mariana Gheorghe
	 **/
	public TestMatch addChild(Object matchKeyParameter, MatchType matchTypeParameter) {
		return addChild(matchKeyParameter, matchTypeParameter, false, false);
	}
	/**
	 *@author Mariana Gheorghe
	 **/
	public TestMatch addChild(Object matchKeyParameter, MatchType matchTypeParameter, boolean isConflictParameter, boolean isChildrenConflictParameter) {
		TestMatch child = new TestMatch(matchKeyParameter, matchTypeParameter, isConflictParameter, isChildrenConflictParameter);
		children.add(child);
		child.parent = this;
		return child;
	}
	/**
	 *@author Mariana Gheorghe
	 **/
	public TestMatch addSibling(Object matchKeyParameter, MatchType matchTypeParameter) {
		return addSibling(matchKeyParameter, matchTypeParameter, false, false);
	}
	/**
	 *@author Mariana Gheorghe
	 **/
	public TestMatch addSibling(Object matchKeyParameter, MatchType matchTypeParameter, boolean isConflictParameter, boolean isChildrenConflictParameter) {
		return parent.addChild(matchKeyParameter, matchTypeParameter, isConflictParameter, isChildrenConflictParameter);
	}

	@Override
	public String toString() {
		return String.format("Match [%s, %s, %d sub-matches]", 
				matchKey, matchType, children.size());
	}
}