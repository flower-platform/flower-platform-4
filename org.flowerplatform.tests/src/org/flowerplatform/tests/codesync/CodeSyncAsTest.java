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

import static org.flowerplatform.codesync.Match.MatchType._2MATCH_LEFT_RIGHT;
import static org.flowerplatform.codesync.Match.MatchType._3MATCH;
import static org.flowerplatform.mindmap.MindMapConstants.FREEPLANE_PERSISTENCE_RESOURCE_KEY;
import static org.flowerplatform.tests.codesync.CodeSyncTestSuite.PROJECT;
import static org.flowerplatform.tests.codesync.CodeSyncTestSuite.CODE_SYNC_SERVICE;
import static org.junit.Assert.assertEquals;

import org.flowerplatform.codesync.CodeSyncConstants;
import org.flowerplatform.codesync.CodeSyncPlugin;
import org.flowerplatform.codesync.Match;
import org.flowerplatform.codesync.as.CodeSyncAsConstants;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.util.Utils;
import org.junit.Test;

/**
 * @author Mariana Gheorghe
 */
public class CodeSyncAsTest {

	public static final String INITIAL_AS = "initial_as";
	
	private static final String RESOURCE_NODE_ID = new Node(Utils.getUri(FREEPLANE_PERSISTENCE_RESOURCE_KEY, PROJECT + "|.codesync"), CodeSyncConstants.CODESYNC).getNodeUri();
	
	/**
	 * @author see class
	 */
	@Test
	public void test() {
		CodeSyncPlugin.getInstance().addSrcDir(INITIAL_AS);
		String fullyQualifiedName = PROJECT + "/" + INITIAL_AS;
		
		Match match = CODE_SYNC_SERVICE.synchronize(RESOURCE_NODE_ID, CodeSyncTestSuite.getFile(fullyQualifiedName), CodeSyncAsConstants.ACTIONSCRIPT, true);
		
		assertEquals(2, match.getSubMatches().size()); // found 2 files
		
		Pair[] typeList = {
			new Pair(_3MATCH, 0), 						// src: initial_as
				new Pair(_2MATCH_LEFT_RIGHT, 1),				// Test.as
					new Pair(_2MATCH_LEFT_RIGHT, 2),				// public class Test
						new Pair(_2MATCH_LEFT_RIGHT, 3),				// public function set orderIndex(value:int):void
							new Pair(_2MATCH_LEFT_RIGHT, 4),				// value:int 
						new Pair(_2MATCH_LEFT_RIGHT, 3),				// public function Test(orderIndex:int = 0)
							new Pair(_2MATCH_LEFT_RIGHT, 4),				// orderIndex:int = 0
						new Pair(_2MATCH_LEFT_RIGHT, 3),				// public function get orderIndex():int
						new Pair(_2MATCH_LEFT_RIGHT, 3),				// public var test:int
							new Pair(_2MATCH_LEFT_RIGHT, 4),				// [Bindable]
						new Pair(_2MATCH_LEFT_RIGHT, 3),				// implements ITest
				new Pair(_2MATCH_LEFT_RIGHT, 1),				// Test.mxml
					new Pair(_2MATCH_LEFT_RIGHT, 2),				// s:ItemRenderer
						new Pair(_2MATCH_LEFT_RIGHT, 3),				// <s:BitmapImage id="hasSubActions"/>
							new Pair(_2MATCH_LEFT_RIGHT, 4),				// [Bindable]
						new Pair(_2MATCH_LEFT_RIGHT, 3),				// protected function clickHandler(event:MouseEvent):void
							new Pair(_2MATCH_LEFT_RIGHT, 4),				// event:MouseEvent 
						new Pair(_2MATCH_LEFT_RIGHT, 3),				// public static const HEIGHT:int = 22
							new Pair(_2MATCH_LEFT_RIGHT, 4)					// static
		};
		
		CodeSyncTestSuite.testMatchTree(match, typeList, true);
	}
	
}