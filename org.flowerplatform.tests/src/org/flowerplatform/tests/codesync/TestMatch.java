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

	public TestMatch(Object matchKey, MatchType matchType) {
		this(matchKey, matchType, false, false);
	}
	
	public TestMatch(Object matchKey, MatchType matchType, boolean isConflict, boolean isChildrenConflict) {
		this.matchKey = matchKey;
		this.matchType = matchType;
		this.isConflict = isConflict;
		this.isChildrenConflict = isChildrenConflict;
	}
	
	public TestMatch addChild(Object matchKey, MatchType matchType) {
		return addChild(matchKey, matchType, false, false);
	}
	
	public TestMatch addChild(Object matchKey, MatchType matchType, boolean isConflict, boolean isChildrenConflict) {
		TestMatch child = new TestMatch(matchKey, matchType, isConflict, isChildrenConflict);
		children.add(child);
		child.parent = this;
		return child;
	}
	
	public TestMatch addSibling(Object matchKey, MatchType matchType) {
		return addSibling(matchKey, matchType, false, false);
	}
	
	public TestMatch addSibling(Object matchKey, MatchType matchType, boolean isConflict, boolean isChildrenConflict) {
		return parent.addChild(matchKey, matchType, isConflict, isChildrenConflict);
	}

	@Override
	public String toString() {
		return String.format("Match [%s, %s, %d sub-matches]", 
				matchKey, matchType, children.size());
	}
}
