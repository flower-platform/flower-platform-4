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

import static org.flowerplatform.tests.EclipseIndependentTestSuite.nodeService;
import static org.flowerplatform.tests.EclipseIndependentTestSuite.startPlugin;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.List;

import org.flowerplatform.codesync.CodeSyncPlugin;
import org.flowerplatform.codesync.Match;
import org.flowerplatform.codesync.as.CodeSyncAsPlugin;
import org.flowerplatform.codesync.code.java.CodeSyncJavaPlugin;
import org.flowerplatform.codesync.remote.CodeSyncOperationsService;
import org.flowerplatform.codesync.sdiff.CodeSyncSdiffPlugin;
import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.freeplane.FreeplanePlugin;
import org.flowerplatform.mindmap.MindMapPlugin;
import org.flowerplatform.tests.EclipseDependentTestSuiteBase;
import org.flowerplatform.tests.EclipseIndependentTestSuite;
import org.flowerplatform.tests.TestUtil;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author Mariana
 */
@RunWith(Suite.class)
@SuiteClasses({ 
	CodeSyncJavaTest.class, 
	CodeSyncAsTest.class,
	CodeSyncCommentPropagationTest.class,
	CodeSyncPropagationTest.class
//	CodeSyncJavascriptTest.class,
//	CodeSyncWikiTest.class 
})
public class CodeSyncTestSuite extends EclipseDependentTestSuiteBase {
	
	public static final String PROJECT = "codesync";
	
	public static final String DIR = TestUtil.getResourcesDir(CodeSyncTestSuite.class);
	
	public static final CodeSyncOperationsService codeSyncService = new CodeSyncOperationsService();
	
	@BeforeClass
	public static void beforeClass() throws Exception {
		EclipseIndependentTestSuite.deleteFiles(PROJECT);
		EclipseIndependentTestSuite.copyFiles(DIR + TestUtil.INITIAL_TO_BE_COPIED, PROJECT);
		
		startPlugin(new FreeplanePlugin());
		startPlugin(new MindMapPlugin());
		startPlugin(new CodeSyncPlugin());
		startPlugin(new CodeSyncJavaPlugin());
		startPlugin(new CodeSyncAsPlugin());
		startPlugin(new CodeSyncSdiffPlugin());
	}
	
	public static File getFile(String path) {
		String absolutePath = /*"/org/ws_trunk/" +*/ path;
		try {
			return (File) CorePlugin.getInstance().getFileAccessController().getFile(absolutePath);
		} catch (Exception e) {			
			throw new RuntimeException(String.format("Error while getting resource %s", absolutePath), e);
		}
	}
	
	// ///////////////////////////
	// Utils
	// ///////////////////////////

	public static Node getChild(Node node, String[] names) {
		Node parent = node;
		for (String name : names) {
			List<Node> children = nodeService.getChildren(parent,
					new ServiceContext<NodeService>(nodeService).add(
							CoreConstants.POPULATE_WITH_PROPERTIES, true));
			for (Node child : children) {
				if (name.equals(child.getOrPopulateProperties(new ServiceContext<NodeService>(nodeService)).get(
						CoreConstants.NAME))) {
					parent = child;
					break;
				}
			}
		}
		return parent;
	}

	private static Pair[] typeList;
	private static boolean[] conflicts;
	private static int i;

	public static void testConflicts(Match match, boolean[] _conflicts) {
		i = 0;
		conflicts = _conflicts;
		checkTree_conflict(match);
	}

	public static void checkTree_type(Match parentMatch, boolean checkNoDiffs, int level) {
		checkMatch_type(parentMatch, level);
		if (checkNoDiffs) {
			assertEquals("No diffs expected", 0, parentMatch.getDiffs().size());
		}
		for (Match subMatch : parentMatch.getSubMatches()) {
			i++;
			checkTree_type(subMatch, checkNoDiffs, level + 1);
		}
	}

	public static void checkMatch_type(Match match, int level) {
		assertEquals("Wrong match at index " + i, typeList[i].type, match.getMatchType());
		assertEquals("Wrong level at index " + i, typeList[i].level, level);
	}

	public static void checkTree_conflict(Match parentMatch) {
		checkMatch_conflict(parentMatch);
		for (Match subMatch : parentMatch.getSubMatches()) {
			i++;
			checkTree_conflict(subMatch);
		}
	}

	public static void checkMatch_conflict(Match match) {
		assertEquals("Wrong conflict state at index " + i, conflicts[i],
				match.isConflict());
	}

	public static Match testMatchTree(Match match, Pair[] _typeList, boolean checkNoDiffs) {
		assertNotNull("Match was not created", match);
		i = 0;
		typeList = _typeList;
		checkTree_type(match, checkNoDiffs, 0);
		assertEquals(typeList.length, i + 1);
		return match;
	}

}
