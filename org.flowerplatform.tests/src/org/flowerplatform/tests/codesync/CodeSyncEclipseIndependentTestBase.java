package org.flowerplatform.tests.codesync;

import static org.flowerplatform.mindmap.MindMapConstants.FREEPLANE_PERSISTENCE_RESOURCE_KEY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.List;

import org.flowerplatform.codesync.CodeSyncConstants;
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
import org.flowerplatform.core.node.resource.ResourceService;
import org.flowerplatform.freeplane.FreeplanePlugin;
import org.flowerplatform.mindmap.MindMapPlugin;
import org.flowerplatform.tests.EclipseIndependentTestBase;
import org.flowerplatform.tests.TestUtil;
import org.flowerplatform.util.Utils;
import org.junit.Before;

/**
 * @author Mariana Gheorghe
 */
public class CodeSyncEclipseIndependentTestBase extends EclipseIndependentTestBase {

	static {
		startPlugin(new FreeplanePlugin());
		startPlugin(new MindMapPlugin());
		startPlugin(new CodeSyncPlugin());
		startPlugin(new CodeSyncJavaPlugin());
		startPlugin(new CodeSyncAsPlugin());
		startPlugin(new CodeSyncSdiffPlugin());
	}
	
	public static final String PROJECT = "codesync";
	
	public static final String DIR = TestUtil.getResourcesDir(CodeSyncTestSuite.class);
	
	protected final CodeSyncOperationsService codeSyncOperationsService = new CodeSyncOperationsService();
	
	protected final String resourceNodeId = new Node(Utils.getUri(FREEPLANE_PERSISTENCE_RESOURCE_KEY, PROJECT + "|.codesync"), CodeSyncConstants.CODESYNC).getNodeUri();
	
	/**
	 * Restore the test file before each test.
	 */
	@Before
	public void before() {
		deleteFiles(PROJECT);
		copyFiles(DIR + TestUtil.INITIAL_TO_BE_COPIED, PROJECT);
		
		CorePlugin.getInstance().getResourceService().subscribeToParentResource("dummySessionId",  resourceNodeId,  new ServiceContext<ResourceService>());
		CorePlugin.getInstance().getResourceService().reload(resourceNodeId, new ServiceContext<ResourceService>());
	}
	
	/////////////////////////////
	// Utils
	/////////////////////////////

	/**
	 * @author Mariana Gheorghe
	 **/
	public Node getChild(Node node, String[] names) {
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

	/**
	 * @author Mariana Gheorghe
	 **/
	public void checkTreeType(Match actual, TestMatch expected, boolean checkNoDiffs, boolean checkFlags) {
		// first test current match
		expected.tested = true;
		assertEquals("Wrong match type for " + expected.matchKey, expected.matchType, actual.getMatchType());
		if (checkNoDiffs) {
			assertEquals("No diffs expected", 0, actual.getDiffs().size());
		}
		if (checkFlags) {
			checkMatchConflict(actual, expected);
		}
		
		// then recurse for children
		
		// iterate actual children
		for (Match subMatch : actual.getSubMatches()) {
			boolean found = false;
			Object matchKey = subMatch.getMatchKey();
			for (TestMatch subTestMatch : expected.children) {
				if (subTestMatch.matchKey.equals(matchKey)) {
					checkTreeType(subMatch, subTestMatch, checkNoDiffs, checkFlags);
					found = true;
					break;
				}
			}
			if (!found) {
				fail("Match not expected " + subMatch);
			}
		}
		
		// iterate remaining expected children
		for (TestMatch testSubMatch : expected.children) {
			if (!testSubMatch.tested) {
				fail("Expected match not found " + testSubMatch);
			}
		}
	}

	/**
	 * @author Mariana Gheorghe
	 **/
	public void checkMatchConflict(Match actual, TestMatch expected) {
		if (expected.isConflict != actual.isConflict()) {
			fail();
		}
		assertEquals("Wrong conflict state", expected.isConflict, actual.isConflict());
		assertEquals("Wrong children conflict state", expected.isChildrenConflict, actual.isChildrenConflict());
		assertEquals("Wrong sync state", !expected.isConflict, ((Node) actual.getLeft()).getPropertyValue(CodeSyncConstants.SYNC));
		assertEquals("Wrong children sync state", !expected.isChildrenConflict, ((Node) actual.getLeft()).getPropertyValue(CodeSyncConstants.CHILDREN_SYNC));	
	}

	/**
	 * @author Mariana Gheorghe
	 **/
	public Match testMatchTree(Match actual, TestMatch expected, boolean checkNoDiffs, boolean checkFlags) {
		assertNotNull("Match was not created", actual);
		checkTreeType(actual, expected, checkNoDiffs, checkFlags);
		return actual;
	}
	
}
