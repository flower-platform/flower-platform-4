package org.flowerplatform.tests.codesync;

import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.COMMENT;
import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.CONTAINS_COMMENT;
import static org.flowerplatform.core.CoreConstants.TYPE_KEY;
import static org.junit.Assert.assertTrue;

import org.flowerplatform.codesync.CodeSyncPlugin;
import org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.CoreUtils;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.NodeServiceRemote;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.core.node.resource.ResourceService;
import org.junit.Test;

/**
 * @author Elena Posea
 */
public class CodeSyncCommentPropagationTest {

	public static final String NO_COMMENTS_INSIDE = "initial_no_comments_inside/codesyncSdiffPatch.sdiff";
	public static final String ONE_COMMENT_INSIDE = "initial_comments_inside/codesyncSdiffOneCommentPatch.sdiff";
	public static final String ONE_DIRTY_NODE_INSIDE = "initial_comments_inside/codesyncSdiffTwoDirtyChildrenPatch.sdiff";
	public static final String TWO_COMMENTS_INSIDE_REMOVE_COMMENT = "initial_comments_inside/codesyncSdiffTwoCommentsPatch.sdiff";
	public static final String TWO_COMMENTS_INSIDE_REMOVE_DIRTY_CHILD = "initial_comments_inside/codesyncSdiffTwoCommentsDirtyPatch.sdiff";
	private static NodeServiceRemote nodeServiceRemote = new NodeServiceRemote();

	/**
	 * Initial:
	 * 
	 * <pre>
	 * Node1
	 * </pre>
	 * 
	 * Expected after addition
	 * 
	 * <pre>
	 * Node1 (children dirty) - <b>Comment</b>
	 * </pre>
	 */
	@Test
	public void testAddCommentPropagation() {
		// subscribe to sdiff file
		Node root = subscribeToSdiffFile(NO_COMMENTS_INSIDE);

		// get the node that we want to use as parent for our new node
		Node testNode = CodeSyncTestSuite.getChild(root, new String[] { "CodeSyncSdiffConstants.java", "CodeSyncSdiffConstants", "STRUCTURE_DIFFS_FOLDER" });
		String testNodeFullyQualifiedName = testNode.getNodeUri();
		ServiceContext<NodeService> serviceContext = new ServiceContext<NodeService>();
		assertTrue("testNode is supposed to be initially clean", !isComment(testNode) && !isChildrenDirty(testNode));

		// adding a comment child to this node
		serviceContext.add(TYPE_KEY, COMMENT);
		String comment1NodeFullyQualifiedName = nodeServiceRemote.addChild(testNodeFullyQualifiedName, serviceContext);
		Node comment1Node = CorePlugin.getInstance().getResourceService().getNode(comment1NodeFullyQualifiedName);

		// update the values for testNode
		testNode = CorePlugin.getInstance().getResourceService().getNode(testNodeFullyQualifiedName);

		// checking all the parents for this newly added comment (check
		// propagation method)
		assertTrue("all the parents are supposed to have the CONTAINS_COMMENT set after this new child has been added", isChildrenDirtyForAllParents(comment1Node, serviceContext));
	}

	/**
	 * Initial:
	 * 
	 * <pre>
	 * Node1 (children dirty) - Node2 (children dirty) - Node3 (children dirty) - <b>Comment</b>  
	 *                        \ Comment
	 * </pre>
	 * 
	 * Expected after removal
	 * 
	 * <pre>
	 * Node1 (children dirty) - Node2  - Node3
	 *                        \ Comment
	 * </pre>
	 */
	@Test
	public void testRemoveCommentButNotTheLastDirtyChild() {
		// subscribe to sdiff file
		Node root = subscribeToSdiffFile(TWO_COMMENTS_INSIDE_REMOVE_COMMENT);

		// get the node that we want to remove
		Node testNode = CodeSyncTestSuite.getChild(root, new String[] { "CodeSyncSdiffPlugin.java", "CodeSyncSdiffPlugin", "registerMessageBundle()", "myMessage" });
		String testNodeFullyQualifiedName = testNode.getNodeUri();
		;
		assertTrue("testNode is supposed to be a comment", isComment(testNode));

		// get the first two parents of testNode
		ServiceContext<NodeService> serviceContext = new ServiceContext<NodeService>();
		serviceContext.setService(CorePlugin.getInstance().getNodeService());
		Node parent1OfTestNode = serviceContext.getService().getParent(testNode, serviceContext);
		String parent1OfTestNodeFullyQualifiedName = parent1OfTestNode.getNodeUri();
		Node parent2OfTestNode = serviceContext.getService().getParent(parent1OfTestNode, serviceContext);

		// remove comment node
		nodeServiceRemote.removeChild(parent1OfTestNodeFullyQualifiedName, testNodeFullyQualifiedName);

		// take the first node that should stay dirty (as explained in this
		// method's description)
		Node firstDirtyNode = serviceContext.getService().getParent(parent2OfTestNode, serviceContext);

		// after this removal, both parent1 and parent 2 should be clean;
		assertTrue("all parents without dirty children should be clean", !isChildrenDirty(parent1OfTestNode) && !isChildrenDirty(parent2OfTestNode));

		// everything above should be dirty
		assertTrue("all the parents are supposed to have the CONTAINS_COMMENT set if there is still any dirty child", isChildrenDirtyForAllParents(firstDirtyNode, serviceContext));
	}

	/**
	 * Initial:
	 * 
	 * <pre>
	 * Node1 (children dirty) - Node2 (children dirty) - <b> Node3 (children dirty) </b> - Comment  
	 *                        \ Comment
	 * </pre>
	 * 
	 * Expected after removal
	 * 
	 * <pre>
	 * Node1 (children dirty) - Node2  
	 *                        \ Comment
	 * </pre>
	 */
	@Test
	public void testRemoveNodeWithCommentButNotTheLastDirtyChild() {
		// subscribe to sdiff file
		Node root = subscribeToSdiffFile(TWO_COMMENTS_INSIDE_REMOVE_DIRTY_CHILD);

		// get the node with comment that we want to erase
		Node testNode = CodeSyncTestSuite.getChild(root, new String[] { "CodeSyncSdiffPlugin.java", "CodeSyncSdiffPlugin", "registerMessageBundle()" });
		String testNodeFullyQualifiedName = testNode.getNodeUri();
		;
		ServiceContext<NodeService> serviceContext = new ServiceContext<NodeService>();
		serviceContext.setService(CorePlugin.getInstance().getNodeService());
		assertTrue("testNode is supposed to be a node with a comment", isChildrenDirty(testNode));

		// get testNode's parent
		Node parentOfTestNode = serviceContext.getService().getParent(testNode, serviceContext);
		String parentOfTestNodeFullyQualifiedName = parentOfTestNode.getNodeUri();
		Node firstDirtyNode = serviceContext.getService().getParent(parentOfTestNode, serviceContext);

		// remove node
		nodeServiceRemote.removeChild(parentOfTestNodeFullyQualifiedName, testNodeFullyQualifiedName);
		assertTrue("all parents without dirty children should be clean", !isChildrenDirty(parentOfTestNode));

		// everything above should be dirty, since there is another comment
		// child to keep them that way
		assertTrue("all the parents are supposed to have the CONTAINS_COMMENT set if there is still any dirty child", isChildrenDirtyForAllParents(firstDirtyNode, serviceContext));
	}

	/**
	 * Initial:
	 * 
	 * <pre>
	 * Node1 (children dirty) - Node2 (children dirty) - <b>Node3 (children dirty)</b> - Comment  
	 *                        \ Comment
	 * </pre>
	 * 
	 * Expected after removal
	 * 
	 * <pre>
	 * Node1 (children dirty) - Node2  
	 *                        \ Comment
	 * </pre>
	 */

	@Test
	public void testRemoveLastChildWithComment() {
		// subscribe to sdiff file
		Node root = subscribeToSdiffFile(ONE_DIRTY_NODE_INSIDE);

		// get testNode
		Node testNode = CodeSyncTestSuite.getChild(root, new String[] { "CodeSyncSdiffPlugin.java", "CodeSyncSdiffPlugin", "registerMessageBundle()" });
		String testNodeFullyQualifiedName = testNode.getNodeUri();
		;

		assertTrue("testNode is supposed to be a node with comment", isChildrenDirty(testNode));

		// get the parent of testNode
		ServiceContext<NodeService> serviceContext = new ServiceContext<NodeService>();
		serviceContext.setService(CorePlugin.getInstance().getNodeService());
		Node parentOfTestNode = serviceContext.getService().getParent(testNode, serviceContext);
		String parentOfTestNodeFullyQualifiedName = parentOfTestNode.getNodeUri();

		// remove node
		nodeServiceRemote.removeChild(parentOfTestNodeFullyQualifiedName, testNodeFullyQualifiedName);

		// update the values of parentOfTestNode
		parentOfTestNode = CorePlugin.getInstance().getResourceService().getNode(parentOfTestNodeFullyQualifiedName);

		assertTrue("if no dirty child is left, all parents (that don't have dirty children) should loose the CONTAINS_COMMENT flag",
				isChildrenNotDirtyForAllParents(parentOfTestNode, serviceContext));
	}

	/**
	 * Initial:
	 * 
	 * <pre>
	 * Node1 (children dirty) - <b>Comment</b>
	 * </pre>
	 * 
	 * Expected after removal
	 * 
	 * <pre>
	 * Node1
	 * </pre>
	 */
	@Test
	public void testRemoveLastComment() {
		// subscribe to sdiffile
		Node root = subscribeToSdiffFile(ONE_COMMENT_INSIDE);

		// get testNode
		Node testNode = CodeSyncTestSuite.getChild(root, new String[] { "CodeSyncSdiffPlugin.java", "CodeSyncSdiffPlugin", "registerMessageBundle()", "myComment" });
		String testNodeFullyQualifiedName = testNode.getNodeUri();
		;
		assertTrue("testNode is supposed to be a comment", isComment(testNode));
		ServiceContext<NodeService> serviceContext = new ServiceContext<NodeService>();
		serviceContext.setService(CorePlugin.getInstance().getNodeService());
		// get the parent of node
		Node parentOfTestNode = serviceContext.getService().getParent(testNode, serviceContext);
		String parentOfTestNodeFullyQualifiedName = parentOfTestNode.getNodeUri();

		// remove node
		nodeServiceRemote.removeChild(parentOfTestNodeFullyQualifiedName, testNodeFullyQualifiedName);

		assertTrue("if no dirty child is left, all parents (that don't have dirty children) should loose the CONTAINS_COMMENT flag",
				isChildrenNotDirtyForAllParents(parentOfTestNode, serviceContext));
	}

	private boolean isComment(Node node) {
		return node.getType().equals(CodeSyncSdiffConstants.COMMENT);
	}

	private boolean isChildrenDirty(Node node) {
		Boolean b = (Boolean) node.getPropertyValue(CONTAINS_COMMENT);
		// is something that has a child comment
		return b != null && b;
	}

	private boolean isChildrenDirtyForAllParents(Node node, ServiceContext<NodeService> serviceContext) {
		return flagChildrenDirtyForAllParents(node, serviceContext, false);
	}

	private boolean isChildrenNotDirtyForAllParents(Node node, ServiceContext<NodeService> serviceContext) {
		return flagChildrenDirtyForAllParents(node, serviceContext, true);
	}

	private boolean flagChildrenDirtyForAllParents(Node node, ServiceContext<NodeService> serviceContext, boolean flag) {
		Node parent = null;
		while ((parent = serviceContext.getService().getParent(node, serviceContext)) != null) {
			if (isChildrenDirty(parent) == flag) {
				// the parentSync flag has already been propagated
				return false;
			}
			node = parent;
		}
		return true;

	}

	private Node subscribeToSdiffFile(String sdiffFileName) {
		String sdiffNodeUri = CoreUtils.createNodeUriWithRepo("fpp", CodeSyncTestSuite.PROJECT, sdiffFileName);
		Node root = CodeSyncPlugin.getInstance().getResource(sdiffNodeUri);
		CorePlugin.getInstance().getResourceService().subscribeToParentResource("dummySessionId", sdiffNodeUri, new ServiceContext<ResourceService>());
		return root;
	}
}
