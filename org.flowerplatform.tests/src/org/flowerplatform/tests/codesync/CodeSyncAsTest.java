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
package org.flowerplatform.tests.codesync;

import static org.flowerplatform.codesync.Match.MatchType._2MATCH_LEFT_RIGHT;
import static org.flowerplatform.codesync.Match.MatchType._3MATCH;
import static org.flowerplatform.mindmap.MindMapConstants.FREEPLANE_PERSISTENCE_RESOURCE_KEY;
import static org.flowerplatform.tests.codesync.CodeSyncTestSuite.CODE_SYNC_SERVICE;
import static org.flowerplatform.tests.codesync.CodeSyncTestSuite.PROJECT;
import static org.junit.Assert.assertEquals;

import org.flowerplatform.codesync.CodeSyncConstants;
import org.flowerplatform.codesync.Match;
import org.flowerplatform.codesync.as.CodeSyncAsConstants;
import org.flowerplatform.core.CorePlugin;
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
	 *@author see class
	 */
	@Test
	public void test() {
		String fullyQualifiedName = PROJECT + "/" + INITIAL_AS;
		Node node = CorePlugin.getInstance().getResourceService().getNode(RESOURCE_NODE_ID);
		String nodeUri = CodeSyncTestSuite.getChild(node, new String[] { INITIAL_AS }).getNodeUri();
		
		Match match = CODE_SYNC_SERVICE.synchronize(nodeUri, CodeSyncTestSuite.getFile(fullyQualifiedName), CodeSyncAsConstants.ACTIONSCRIPT, true);
		
		assertEquals(2, match.getSubMatches().size()); // found 2 files
		
		TestMatch expected = new TestMatch(INITIAL_AS, _3MATCH);
		
		TestMatch testAs = expected.addChild("Test.as", _2MATCH_LEFT_RIGHT).addChild("Test", _2MATCH_LEFT_RIGHT);
		testAs.addChild("ITest", _2MATCH_LEFT_RIGHT);
		testAs.addChild("Test(orderIndex:int)", _2MATCH_LEFT_RIGHT).addChild("orderIndex", _2MATCH_LEFT_RIGHT);
		testAs.addChild("set orderIndex(value:int):void", _2MATCH_LEFT_RIGHT).addChild("value", _2MATCH_LEFT_RIGHT);
		testAs.addChild("get orderIndex():int", _2MATCH_LEFT_RIGHT);
		testAs.addChild("test", _2MATCH_LEFT_RIGHT).addChild("Bindable", _2MATCH_LEFT_RIGHT);
			
		TestMatch testMxml = expected.addChild("Test.mxml", _2MATCH_LEFT_RIGHT).addChild("Test", _2MATCH_LEFT_RIGHT);	
		testMxml.addChild("hasSubActions", _2MATCH_LEFT_RIGHT).addChild("Bindable", _2MATCH_LEFT_RIGHT);
		testMxml.addChild("clickHandler(event:MouseEvent):void", _2MATCH_LEFT_RIGHT).addChild("event", _2MATCH_LEFT_RIGHT);
		testMxml.addChild("HEIGHT", _2MATCH_LEFT_RIGHT).addChild("static", _2MATCH_LEFT_RIGHT);
		
		CodeSyncTestSuite.testMatchTree(match, expected, true, false);
	}	
}