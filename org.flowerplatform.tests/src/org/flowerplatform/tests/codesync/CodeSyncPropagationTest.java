package org.flowerplatform.tests.codesync;

import static org.flowerplatform.codesync.CodeSyncConstants.REMOVED;
import static org.flowerplatform.codesync.CodeSyncConstants.CHILDREN_SYNC;
import static org.flowerplatform.codesync.CodeSyncConstants.SYNC;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.MODIFIER;
import static org.flowerplatform.core.CoreConstants.NAME;
import static org.flowerplatform.core.CoreConstants.TYPE_KEY;
import static org.junit.Assert.assertTrue;

import org.flowerplatform.codesync.CodeSyncPlugin;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.CoreUtils;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.NodeServiceRemote;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.core.node.resource.ResourceService;
import org.junit.Test;

/**
 * 
 * @author Elena Posea
 *
 */

public class CodeSyncPropagationTest {
	
	private static final String INITIAL_ALL_SYNC = "initial_all_sync/";
	private static final String INITIAL_ONE_NOT_SYNC = "initial_one_not_sync/";
	private static final String test1 = "AllSyncAddNodeTestCodeSync";
	private static final String test2 = "AllSyncSimpleRemoveSync";
	private static final String test3 = "AllSyncChangeOriginalValueCondeSync";
	private static final String test4 = "NotSyncRevertToOriginalValueCodeSync";
	private static final String test5 = "ReverToOriginalValueButNotTheLastCodeSync";
	
	/**
	 * Initial:
	 * <pre>
	 * Node1 (SYNC, CHILDREN_SYNC)
	 * </pre>
	 * 
	 * Expected after addition:
	 * <pre>
	 * Node1 (not CHILDREN_SYNC) - Modifier
	 * </pre>
	 */
	@Test
	public void testAddNodePropagation() {
		// subscribe to sdiff file
		NodeServiceRemote nodeServiceRemote = new NodeServiceRemote();
		String sdiffNodeUri = CoreUtils.createNodeUriWithRepo("fpp", CodeSyncTestSuite.PROJECT, INITIAL_ALL_SYNC + test1);
		Node root = CodeSyncPlugin.getInstance().getResource(sdiffNodeUri);
		CorePlugin.getInstance().getResourceService().subscribeToParentResource("dummySessionId", sdiffNodeUri, new ServiceContext<ResourceService>());
		
		// get testNode (the parent of the node I want to add)
		Node testNode = CodeSyncTestSuite.getChild(root, new String[] {"modified_no_conflicts_perform_sync", "Test.java", "Test", "getTest()"});
		String testNodeFullyQualifiedName = testNode.getNodeUri();;
		ServiceContext<NodeService> serviceContext = new ServiceContext<NodeService>();
		assertTrue("testNode is supposed to be initially sync", hasFlagTrue(testNode, SYNC));

		// add a new child of type MODIFIER
		serviceContext.add(TYPE_KEY, MODIFIER);
		String modifierNodeFullyQualifiedName = nodeServiceRemote.addChild(testNodeFullyQualifiedName, serviceContext);
		Node modifierNode = CorePlugin.getInstance().getResourceService().getNode(modifierNodeFullyQualifiedName);
		
		// update the value of node (its flag should change after this addition)
		testNode = CorePlugin.getInstance().getResourceService().getNode(testNodeFullyQualifiedName); 
		assertTrue("all the parents are supposed to have the SYNC flag unset/set to false after this new child has been added", isChildrenDirtyForAllParents(modifierNode, serviceContext, INITIAL_ALL_SYNC + test1));
	}

	/**
	 * Initial
	 * <pre>
	 * Node1 (CHILDREN_SYNC) - Node2 (SYNC)
	 * </pre>
	 * 
	 * Expected after removal
	 * <pre>
	 * Node1 (not CHILDREN_SYNC) - Node2 (marked as removed)
	 * </pre>
	 */
	@Test
	public void testRemoveNodePropagation() {
		// subscribe to sdiff file
		NodeServiceRemote nodeServiceRemote = new NodeServiceRemote();
		String sdiffNodeUri = CoreUtils.createNodeUriWithRepo("fpp", CodeSyncTestSuite.PROJECT, INITIAL_ALL_SYNC + test2);
		Node root = CodeSyncPlugin.getInstance().getResource(sdiffNodeUri);
		CorePlugin.getInstance().getResourceService().subscribeToParentResource("dummySessionId", sdiffNodeUri, new ServiceContext<ResourceService>());
		
		// get the testNode
		Node testNode = CodeSyncTestSuite.getChild(root, new String[] {"initial", "Test.java", "Test", "test(String)", "st"});
		String testNodeFullyQualifiedName = testNode.getNodeUri();;
		ServiceContext<NodeService> serviceContext = new ServiceContext<NodeService>();
		serviceContext.setService(CorePlugin.getInstance().getNodeService());
		assertTrue("testNode is supposed to be initially sync", hasFlagTrue(testNode, SYNC));
		assertTrue("all the parents are supposed to be clean, initially", isChildrenNotDirtyForAllParents(testNode, serviceContext, INITIAL_ALL_SYNC + test2));

		// get the parent of testNode
		Node parentOfTestNode = serviceContext.getService().getParent(testNode, serviceContext);
		String parentOfTestNodeFullyQualifiedName = parentOfTestNode.getNodeUri();
		
		// remove node
		nodeServiceRemote.removeChild(parentOfTestNodeFullyQualifiedName, testNodeFullyQualifiedName);
		// update the value of testNode
		testNode = CorePlugin.getInstance().getResourceService().getNode(testNodeFullyQualifiedName);
		assertTrue("after removal, testNode should be marked as REMOVED", hasFlagTrue(testNode, REMOVED));

		// everything above should be dirty, including the "removed" node
		assertTrue("all the parents are supposed to have the SYNC flag unset/set to false after this removal", isChildrenDirtyForAllParents(testNode, serviceContext, INITIAL_ALL_SYNC + test2));
	}
	
	/**
	 * Initial
	 * <pre>
	 * Node1 (CHILDREN_SYNC) - Node2 (SYNC)
	 * </pre>
	 * 
	 * Expected after modification of value
	 * <pre>
	 * Node1 (not CHILDREN_SYNC) - Node2 (not SYNC)
	 * </pre>
	 */

	@Test
	public void testChangeOriginalValueNodePropagation() {
		// subscribe to sdiff file
		NodeServiceRemote nodeServiceRemote = new NodeServiceRemote();
		String sdiffNodeUri = CoreUtils.createNodeUriWithRepo("fpp", CodeSyncTestSuite.PROJECT, INITIAL_ALL_SYNC + test3);
		Node root = CodeSyncPlugin.getInstance().getResource(sdiffNodeUri);
		CorePlugin.getInstance().getResourceService().subscribeToParentResource("dummySessionId", sdiffNodeUri, new ServiceContext<ResourceService>());

		// get testNode
		Node testNode = CodeSyncTestSuite.getChild(root, new String[] {"modified_no_conflicts_perform_sync", "Test.java", "Test", "x"});
		String testNodeFullyQualifiedName = testNode.getNodeUri();;
		ServiceContext<NodeService> serviceContext = new ServiceContext<NodeService>();
		serviceContext.setService(CorePlugin.getInstance().getNodeService());
		assertTrue("testNode is supposed to be initially sync", hasFlagTrue(testNode, SYNC));
		assertTrue("all the parents are supposed to be clean, initially", isChildrenNotDirtyForAllParents(testNode, serviceContext, INITIAL_ALL_SYNC + test3));
		
		// change a property
		nodeServiceRemote.setProperty(testNodeFullyQualifiedName, NAME, "xx");
		
		// everything above should be dirty
		assertTrue("all the parents are supposed to have the SYNC flag unset/set to false after this change", isChildrenDirtyForAllParents(testNode, serviceContext, INITIAL_ALL_SYNC + test3));
		
	}

	
	/**
	 * Initial 
	 * Node (not CHILDREN_SYNC) - Node (not SYNC because x)
	 * 
	 * Expected after rever on x but change on y
	 * Node (not CHILDREN_SYNC) - Node (not SYNC because y)
	 */
	@Test
	public void testRevertToOriginalValueButNotTheLastOneNodePropagation() {
		// subscribe to sdiff file
		NodeServiceRemote nodeServiceRemote = new NodeServiceRemote();
		String sdiffNodeUri = CoreUtils.createNodeUriWithRepo("fpp", CodeSyncTestSuite.PROJECT, INITIAL_ONE_NOT_SYNC + test5);
		Node root = CodeSyncPlugin.getInstance().getResource(sdiffNodeUri);
		CorePlugin.getInstance().getResourceService().subscribeToParentResource("dummySessionId", sdiffNodeUri, new ServiceContext<ResourceService>());

		// get testNode
		Node testNodeX = CodeSyncTestSuite.getChild(root, new String[] {"modified_no_conflicts_perform_sync", "Test.java", "Test", "xx"});
		String testNodeXFullyQualifiedName = testNodeX.getNodeUri();;
		Node testNodeY = CodeSyncTestSuite.getChild(root, new String[] {"modified_no_conflicts_perform_sync", "Test.java", "Test", "y"});
		String testNodeYFullyQualifiedName = testNodeY.getNodeUri();;

		ServiceContext<NodeService> serviceContext = new ServiceContext<NodeService>();
		serviceContext.setService(CorePlugin.getInstance().getNodeService());
		assertTrue("testNodeX is supposed to be initially not SYNC", !hasFlagTrue(testNodeX, SYNC));
		assertTrue("testNodeY is supposed to be initially SYNC", hasFlagTrue(testNodeY, SYNC));
		assertTrue("all the parents are supposed to be dirty, initially", isChildrenDirtyForAllParents(testNodeX, serviceContext, INITIAL_ONE_NOT_SYNC + test5));
		
		// change a property
		nodeServiceRemote.setProperty(testNodeYFullyQualifiedName, NAME, "yy");
		// revert a property
		nodeServiceRemote.setProperty(testNodeXFullyQualifiedName, NAME, "x");
		
		// everything above should be dirty
		assertTrue("all the parents are supposed to still have the SYNC flag unset/set to false after this change", isChildrenDirtyForAllParents(testNodeX, serviceContext, INITIAL_ONE_NOT_SYNC + test5));
	}

	
	@Test
	public void testRevertToOriginalValueNodePropagation() {
		// subscribe to sdiff file
		NodeServiceRemote nodeServiceRemote = new NodeServiceRemote();
		String sdiffNodeUri = CoreUtils.createNodeUriWithRepo("fpp", CodeSyncTestSuite.PROJECT, INITIAL_ONE_NOT_SYNC + test4);
		Node root = CodeSyncPlugin.getInstance().getResource(sdiffNodeUri);
		CorePlugin.getInstance().getResourceService().subscribeToParentResource("dummySessionId", sdiffNodeUri, new ServiceContext<ResourceService>());
		
		// get test node
		Node testNode = CodeSyncTestSuite.getChild(root, new String[] {"modified_no_conflicts_perform_sync", "Test.java", "Test", "xx"});
		String testNodeFullyQualifiedName = testNode.getNodeUri();;
		ServiceContext<NodeService> serviceContext = new ServiceContext<NodeService>();
		serviceContext.setService(CorePlugin.getInstance().getNodeService());
		assertTrue("testNode is supposed to be initially not sync", !hasFlagTrue(testNode, SYNC));
		assertTrue("all the parents are supposed to be dirty, initially", isChildrenDirtyForAllParents(testNode, serviceContext, INITIAL_ONE_NOT_SYNC + test4));
		
		// change a property back to its original value
		nodeServiceRemote.setProperty(testNodeFullyQualifiedName, NAME, "x");
		
		// everything above should be clean again
		assertTrue("all the parents are supposed to be clean, after I changed back this property", isChildrenNotDirtyForAllParents(testNode, serviceContext, INITIAL_ONE_NOT_SYNC + test4));
	}
	
	private static boolean hasFlagTrue(Node node, String flag) {
		Boolean b = (Boolean) node.getPropertyValue(flag);
		return b != null && b;
	}
	
	private boolean isChildrenDirty(Node node) {
		Boolean b = (Boolean) node.getPropertyValue(CHILDREN_SYNC);
		// is something that has a child comment
		return b == null || b == false;
	}

	private boolean isChildrenDirtyForAllParents(Node node, ServiceContext<NodeService> serviceContext, String stop) {
			return isChildrenFlagForAllParents(node, serviceContext, stop, false);
	}

	private boolean isChildrenFlagForAllParents(Node node, ServiceContext<NodeService> serviceContext, String stop, boolean flag) {
		Node parent = null;
		while ((parent = serviceContext.getService().getParent(node, serviceContext)) != null && !parent.getNodeUri().endsWith(stop)) {
			if (isChildrenDirty(parent) == flag) {
				// the parentSync flag has already been propagated
				return false;
			}
			node = parent;
		}
		return true;
	}	

	private boolean isChildrenNotDirtyForAllParents(Node node, ServiceContext<NodeService> serviceContext, String stop) {
		return isChildrenFlagForAllParents(node, serviceContext, stop, true);
	}


}