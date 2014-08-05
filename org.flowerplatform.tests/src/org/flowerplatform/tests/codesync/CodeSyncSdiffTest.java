package org.flowerplatform.tests.codesync;

import static org.flowerplatform.codesync.CodeSyncConstants.MATCH_BODY_MODIFIED;
import static org.flowerplatform.codesync.CodeSyncConstants.MATCH_CHILDREN_MODIFIED_RIGHT;
import static org.flowerplatform.codesync.CodeSyncConstants.MATCH_DIFFS_MODIFIED_RIGHT;
import static org.flowerplatform.codesync.CodeSyncConstants.MATCH_TYPE;
import static org.flowerplatform.tests.codesync.CodeSyncTestSuite.DIR;
import static org.flowerplatform.tests.TestUtil.INITIAL_TO_BE_COPIED;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowerplatform.codesync.Match.MatchType;
import org.flowerplatform.codesync.sdiff.CodeSyncSdiffPlugin;
import org.flowerplatform.codesync.sdiff.WorkspaceAndPatchFileContentProvider;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.tests.TestUtil;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Alexandra Topoloaga
 */
public class CodeSyncSdiffTest {

	public static final String INITIAL_SDIFF = "initial_sdiff";
	
	public static final String PROJECT = "codesync";
	
	public static final String PATCH_FILE_ADDED = "added/file-added.patch";
	
	public static final String PATCH_FILE_REMOVED = "removed/file-removed.patch"; 
	
	public static final String PATCH_FILE_MODIFIED = "modified/file-modified.patch";
	
	public static List<Node> children;
	
	public static Map<String, Object> propertiesMap;
		
	public static ArrayList<Map<String, Object>> properties;
	
	public static int i = 0;
	
	@BeforeClass 
	public static void beforeClass() {
		 String from = DIR + INITIAL_TO_BE_COPIED + "/" + INITIAL_SDIFF;
		 TestUtil.copyFilesAndCreateProject(from, PROJECT);
	}
	
	public static void assertTest(Node node) {
		i++;
		assertTrue("The node has too many descendants! Expected: " + properties.size() + ", found: " + i , i < properties.size());
		for (Map.Entry<String, Object> entry: properties.get(i).entrySet()) {
			assertEquals("Different " + entry.getKey(), entry.getValue(), node.getPropertyValue(entry.getKey()));
		}
		ServiceContext<NodeService> context = new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService());
		children = CorePlugin.getInstance().getNodeService().getChildren(node, context);
		for (Node child: children) {
			assertTest(child);
		}
	}
	
	private static void test(String filePatch) {
		String patchName = PROJECT + "/" + filePatch;
		File patch = CodeSyncTestSuite.getFile(patchName);
		String sdiffOutputPath = "sdiffs/test.sdiff";
		Node node = CodeSyncSdiffPlugin.getInstance().getSDiffService().createStructureDiff(TestUtil.readFile(patch.toString()), PROJECT, sdiffOutputPath, new WorkspaceAndPatchFileContentProvider());
		i = -1;
		ServiceContext<NodeService> context = new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService());
		children = CorePlugin.getInstance().getNodeService().getChildren(node, context);
		assertTrue("The node has too many descendants! Expected: 1, found:" + children.size(), children.size() == 1);
		assertTest(children.get(0));	
	}
		
	@Test
	public void testFileShouldBeAdded() {
		properties = new ArrayList<Map<String, Object>>();
		propertiesMap = new HashMap<String, Object>();
		propertiesMap.put(MATCH_TYPE, MatchType._1MATCH_RIGHT.toString());
		propertiesMap.put(MATCH_CHILDREN_MODIFIED_RIGHT, true);
		for (int i = 1; i <= 3; i++) {		
			properties.add(propertiesMap);
		}
		test(PATCH_FILE_ADDED);
	}
	
	@Test
	public void testFileShouldBeRemoved() {
		properties = new ArrayList<Map<String, Object>>();
		propertiesMap = new HashMap<String, Object>();
		propertiesMap.put(MATCH_TYPE, MatchType._2MATCH_ANCESTOR_LEFT.toString());
		propertiesMap.put(MATCH_CHILDREN_MODIFIED_RIGHT, true);
		for (int i = 1; i <= 3; i++) {		
			properties.add(propertiesMap);
		}
		test(PATCH_FILE_REMOVED);
	}
	
	@Test
	public void testFileShouldBeModified() {
		properties = new ArrayList<Map<String, Object>>();
		propertiesMap = new HashMap<String, Object>();
		propertiesMap.put(MATCH_TYPE, MatchType._3MATCH.toString());
		propertiesMap.put(MATCH_CHILDREN_MODIFIED_RIGHT, true);
		for (int i = 1; i <= 2; i++) {		
			properties.add(propertiesMap);
		}
		
		//no change method
		propertiesMap = new HashMap<String, Object>();
		propertiesMap.put(MATCH_TYPE, MatchType._3MATCH.toString());
		propertiesMap.put(MATCH_CHILDREN_MODIFIED_RIGHT, false);
		propertiesMap.put(MATCH_DIFFS_MODIFIED_RIGHT, false);
		propertiesMap.put(MATCH_BODY_MODIFIED, false);
		properties.add(propertiesMap);
		
		//removed method
		propertiesMap = new HashMap<String, Object>();
		propertiesMap.put(MATCH_TYPE, MatchType._2MATCH_ANCESTOR_LEFT.toString());
		propertiesMap.put(MATCH_CHILDREN_MODIFIED_RIGHT, true);
		properties.add(propertiesMap);
		
		//changed doc method
		propertiesMap = new HashMap<String, Object>();
		propertiesMap.put(MATCH_TYPE, MatchType._3MATCH.toString());
		propertiesMap.put(MATCH_DIFFS_MODIFIED_RIGHT, true);
		properties.add(propertiesMap);
		
		//modified body
		propertiesMap = new HashMap<String, Object>();
		propertiesMap.put(MATCH_TYPE, MatchType._3MATCH.toString());
		propertiesMap.put(MATCH_BODY_MODIFIED, true);
		properties.add(propertiesMap);
		
		//added method
		propertiesMap = new HashMap<String, Object>();
		propertiesMap.put(MATCH_TYPE, MatchType._1MATCH_RIGHT.toString());
		propertiesMap.put(MATCH_CHILDREN_MODIFIED_RIGHT, true);
		properties.add(propertiesMap);
		
		test(PATCH_FILE_MODIFIED);
	}
}