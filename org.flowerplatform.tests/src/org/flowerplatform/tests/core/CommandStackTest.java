package org.flowerplatform.tests.core;

import static org.flowerplatform.tests.EclipseIndependentTestSuite.nodeService;
import static org.flowerplatform.tests.EclipseIndependentTestSuite.startPlugin;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.RemoteMethodInvocationInfo;
import org.flowerplatform.core.RemoteMethodInvocationListener;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.NodeServiceRemote;
import org.flowerplatform.core.node.remote.ResourceServiceRemote;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.core.node.resource.ResourceService;
import org.flowerplatform.core.node.resource.ResourceSetService;
import org.flowerplatform.core.node.update.Command;
import org.flowerplatform.core.node.update.remote.ChildrenUpdate;
import org.flowerplatform.core.node.update.remote.PropertyUpdate;
import org.flowerplatform.core.node.update.remote.Update;
import org.flowerplatform.freeplane.FreeplanePlugin;
import org.flowerplatform.mindmap.MindMapConstants;
import org.flowerplatform.mindmap.MindMapPlugin;
import org.flowerplatform.tests.TestUtil;
import org.flowerplatform.util.Utils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CommandStackTest {

	public static final String PROJECT = "commandstack";
	public static final String DIR = TestUtil.getResourcesDir(CommandStackTest.class);
	private static final String resourceNodeUri = Utils.getUri("fpm", PROJECT + "/repository|FAP-FlowerPlatform4.mm");
	private static final String commandStackNodeUri = CorePlugin.getInstance().getCommandStackResourceHandler().createCommandStackNode(resourceNodeUri).getNodeUri();

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private static Node rootNode;
	private static NodeServiceRemote nodeServiceRemote;
	private static RemoteMethodInvocationListener remoteMethodInvocationListener;
	private static ResourceService resourceService;
	private static ResourceServiceRemote resourceServiceRemote;
	private static ResourceSetService resourceSetService;

	private RemoteMethodInvocationInfo remoteMethodInvocationInfo;
	private ServiceContext<NodeService> context;

	@BeforeClass
	public static void beforeClass() throws Exception {
		TestUtil.copyFiles(DIR + TestUtil.INITIAL_TO_BE_COPIED, PROJECT);
		startPlugin(new FreeplanePlugin());
		startPlugin(new MindMapPlugin());

		CorePlugin.getInstance().getResourceService().subscribeToParentResource("dummy-session", resourceNodeUri, new ServiceContext<ResourceService>(CorePlugin.getInstance().getResourceService()));
		CorePlugin.getInstance().getResourceService().subscribeToParentResource("dummy-session", commandStackNodeUri, new ServiceContext<ResourceService>(CorePlugin.getInstance().getResourceService()));
		resourceService = CorePlugin.getInstance().getResourceService();
		resourceServiceRemote = new ResourceServiceRemote();
		resourceSetService = CorePlugin.getInstance().getResourceSetService();
		rootNode = resourceService.getNode(resourceNodeUri);
		nodeServiceRemote = new NodeServiceRemote();

		remoteMethodInvocationListener = spy(CorePlugin.getInstance().getRemoteMethodInvocationListener());
		doReturn("dummy-session").when(remoteMethodInvocationListener).getSessionId();

	}

	@Before
	public void setUp() {
		context = new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService());
		remoteMethodInvocationInfo = spy(new RemoteMethodInvocationInfo());
		doReturn(new ArrayList<String>()).when(remoteMethodInvocationInfo).getResourceUris();
		doReturn(new ArrayList<String>()).when(remoteMethodInvocationInfo).getResourceSets();
		doReturn(-1L).when(remoteMethodInvocationInfo).getTimestampOfLastRequest();
		remoteMethodInvocationInfo.setMethodName("test");
	}

	@Test
	public void testNewCommand_SetProperty() {
		resourceSetService.resetCommandStack(resourceNodeUri);

		String property = "fontFamily";
		Node node = nodeService.getChildren(rootNode, context).get(0);

		remoteMethodInvocationListener.preInvoke(remoteMethodInvocationInfo);
		nodeServiceRemote.setProperty(node.getNodeUri(), property, "Changed");
		remoteMethodInvocationListener.postInvoke(remoteMethodInvocationInfo);

		List<Command> commands = resourceSetService.getCommands(resourceNodeUri);
		assertEquals("There is one command in the command stack", 1, commands.size());

		List<Update> updates = resourceSetService.getCommandUpdates(commands.get(0));
		assertEquals("There are 2 updates for the command", 2, updates.size());

		boolean foundPropertyUpdate = false;
		for (int i = 0; i < updates.size(); i++) {
			PropertyUpdate prop = (PropertyUpdate) updates.get(i);
			if (prop.getKey().equals(property)) {
				foundPropertyUpdate = true;
				break;
			}
		}
		assertTrue("An instance of PropertyUpdate for key \"" + property + "\" was added to the updates list", foundPropertyUpdate);
	}

	@Test
	public void testNewCommand_AddChild() {
		resourceSetService.resetCommandStack(resourceNodeUri);

		remoteMethodInvocationListener.preInvoke(remoteMethodInvocationInfo);
		context.add("type", MindMapConstants.MINDMAP_NODE_TYPE);
		nodeServiceRemote.addChild(rootNode.getNodeUri(), context);
		remoteMethodInvocationListener.postInvoke(remoteMethodInvocationInfo);

		List<Command> commands = resourceSetService.getCommands(resourceNodeUri);
		assertEquals("There is one command in the command stack", 1, commands.size());
		List<Update> updates = resourceSetService.getCommandUpdates(commands.get(0));
		assertEquals("There are 3 updates for the command", 3, updates.size());
		int childrenUpdateIndex = -1;
		for (int i = 0; i < updates.size(); i++) {
			if (updates.get(i) instanceof ChildrenUpdate) {
				childrenUpdateIndex = i;
				break;
			}
		}
		assertTrue("An instance of ChildrenUpdate was added to the updates list", childrenUpdateIndex >= 0);
		assertEquals("The ChildrenUpdate type is UPDATE_CHILD_ADDED", CoreConstants.UPDATE_CHILD_ADDED, ((ChildrenUpdate) updates.get(childrenUpdateIndex)).getType());
	}

	@Test
	public void testUndoCommand_SetProperty() {
		String property = "text";
		Node node = nodeService.getChildren(rootNode, context).get(0);
		String oldValue = node.getOrPopulateProperties(context).get(property).toString();

		remoteMethodInvocationListener.preInvoke(remoteMethodInvocationInfo);
		nodeServiceRemote.setProperty(node.getNodeUri(), property, "Changed");
		remoteMethodInvocationListener.postInvoke(remoteMethodInvocationInfo);

		node = nodeService.getChildren(rootNode, context).get(0);
		assertEquals("Changed", node.getOrPopulateProperties(context).get(property));

		List<Command> commands = resourceSetService.getCommands(resourceNodeUri);
		resourceSetService.undo(resourceNodeUri, commands.get(commands.size() - 1).getId());

		node = nodeService.getChildren(rootNode, context).get(0);
		assertEquals(oldValue, node.getOrPopulateProperties(context).get(property));
	}

	@Test
	public void testUndoCommand_AddChild() {
		Node node = nodeService.getChildren(rootNode, context).get(0);

		context.add("type", MindMapConstants.MINDMAP_NODE_TYPE);
		remoteMethodInvocationListener.preInvoke(remoteMethodInvocationInfo);
		String childUri = nodeServiceRemote.addChild(node.getNodeUri(), context);
		remoteMethodInvocationListener.postInvoke(remoteMethodInvocationInfo);

		List<Node> children = nodeService.getChildren(node, context);
		assertTrue("A new node was created and added as a child", children.contains(new Node(childUri, MindMapConstants.MINDMAP_NODE_TYPE)));

		List<Command> commands = resourceSetService.getCommands(resourceNodeUri);
		resourceSetService.undo(resourceNodeUri, commands.get(commands.size() - 1).getId());

		children = nodeService.getChildren(node, context);
		assertFalse("The newly created node was removed", children.contains(new Node(childUri, MindMapConstants.MINDMAP_NODE_TYPE)));

	}

	@Test
	public void testUndoCommand_RemoveChild() {
		Node node = nodeService.getChildren(rootNode, context).get(5);

		Map<String, Object> nodeProperties = node.getOrPopulateProperties(context);
		List<Node> nodeChildren = nodeService.getChildren(node, context);
		
		remoteMethodInvocationListener.preInvoke(remoteMethodInvocationInfo);
		nodeServiceRemote.removeChild(rootNode.getNodeUri(), node.getNodeUri());
		remoteMethodInvocationListener.postInvoke(remoteMethodInvocationInfo);

		List<Node> rootChildren = nodeService.getChildren(rootNode, context);
		assertTrue("Child node is removed from parent", !rootChildren.contains(node));

		List<Command> commands = resourceSetService.getCommands(resourceNodeUri);
		resourceSetService.undo(resourceNodeUri, commands.get(commands.size() - 1).getId());

		node = nodeService.getChildren(rootNode, context).get(5);

		rootChildren = nodeService.getChildren(rootNode, context);
		assertTrue("Child node is added", rootChildren.contains(node));

		Map<String, Object> properties = node.getOrPopulateProperties(context);
		assertTrue("Properties are repopulated.", nodeProperties.keySet().equals(properties.keySet()));
		
		List<Node> nodeChildrenUndo = nodeService.getChildren(node, context);
		
		assertTrue("Grandchildren nodes are added", nodeChildren.equals(nodeChildrenUndo));

	}
	
	@Test
	public void testRedoCommand_SetProperty() {
		String property = "text", newValue = "Changed 1";
		Node node = nodeService.getChildren(rootNode, context).get(0);

		remoteMethodInvocationListener.preInvoke(remoteMethodInvocationInfo);
		nodeServiceRemote.setProperty(node.getNodeUri(), property, newValue);
		remoteMethodInvocationListener.postInvoke(remoteMethodInvocationInfo);

		List<Command> commands = resourceSetService.getCommands(resourceNodeUri);
		resourceSetService.undo(resourceNodeUri, commands.get(commands.size() - 1).getId());

		resourceSetService.redo(resourceNodeUri, commands.get(commands.size() - 1).getId());

		node = nodeService.getChildren(rootNode, context).get(0);
		assertEquals(newValue, node.getOrPopulateProperties(context).get(property));
	}

	@Test
	public void testServiceRedoCommand_SetProperty() {
		String property = "text", newValue = "Changed 1";
		Node node = nodeService.getChildren(rootNode, context).get(0);

		remoteMethodInvocationListener.preInvoke(remoteMethodInvocationInfo);
		nodeServiceRemote.setProperty(node.getNodeUri(), property, newValue);
		remoteMethodInvocationListener.postInvoke(remoteMethodInvocationInfo);

		List<Command> commands = resourceSetService.getCommands(resourceNodeUri);
		remoteMethodInvocationListener.preInvoke(remoteMethodInvocationInfo);
		resourceServiceRemote.undo(commandStackNodeUri + "#" + commands.get(commands.size() - 1).getId());
		remoteMethodInvocationListener.postInvoke(remoteMethodInvocationInfo);

		remoteMethodInvocationListener.preInvoke(remoteMethodInvocationInfo);
		resourceServiceRemote.redo(commandStackNodeUri + "#" + commands.get(commands.size() - 1).getId());
		remoteMethodInvocationListener.postInvoke(remoteMethodInvocationInfo);

		node = nodeService.getChildren(rootNode, context).get(0);
		assertEquals(newValue, node.getOrPopulateProperties(context).get(property));
	}

	@Test
	public void testRedoCommand_AddChild() {
		Node node = nodeService.getChildren(rootNode, context).get(0);

		context.add("type", MindMapConstants.MINDMAP_NODE_TYPE);
		remoteMethodInvocationListener.preInvoke(remoteMethodInvocationInfo);
		String childUri = nodeServiceRemote.addChild(node.getNodeUri(), context);
		remoteMethodInvocationListener.postInvoke(remoteMethodInvocationInfo);

		List<Command> commands = resourceSetService.getCommands(resourceNodeUri);
		resourceSetService.undo(resourceNodeUri, commands.get(commands.size() - 1).getId());

		resourceSetService.redo(resourceNodeUri, commands.get(commands.size() - 1).getId());

		List<Node> children = nodeService.getChildren(node, context);
		assertTrue("A new node was created and added as a child", children.contains(new Node(childUri, MindMapConstants.MINDMAP_NODE_TYPE)));
	}

	@Test
	public void testRedoCommand_RemoveChild() {
		Node node = nodeService.getChildren(rootNode, context).get(5);

		remoteMethodInvocationListener.preInvoke(remoteMethodInvocationInfo);
		nodeServiceRemote.removeChild(rootNode.getNodeUri(), node.getNodeUri());
		remoteMethodInvocationListener.postInvoke(remoteMethodInvocationInfo);

		List<Node> children = nodeService.getChildren(rootNode, context);
		assertTrue("Child node is removed from parent's children", !children.contains(node));

		List<Command> commands = resourceSetService.getCommands(resourceNodeUri);
		resourceSetService.undo(resourceNodeUri, commands.get(commands.size() - 1).getId());
		children = nodeService.getChildren(rootNode, context);
		assertTrue("Child node was added back", children.contains(node));

		resourceSetService.redo(resourceNodeUri, commands.get(commands.size() - 1).getId());
		children = nodeService.getChildren(rootNode, context);
		assertTrue("Child node is removed from parent's children", !children.contains(node));
	}

	@Test
	public void testServiceRedoCommand_AddChild() {
		Node node = nodeService.getChildren(rootNode, context).get(0);

		context.add("type", MindMapConstants.MINDMAP_NODE_TYPE);
		remoteMethodInvocationListener.preInvoke(remoteMethodInvocationInfo);
		String childUri = nodeServiceRemote.addChild(node.getNodeUri(), context);
		remoteMethodInvocationListener.postInvoke(remoteMethodInvocationInfo);

		List<Command> commands = resourceSetService.getCommands(resourceNodeUri);
		remoteMethodInvocationListener.preInvoke(remoteMethodInvocationInfo);
		resourceServiceRemote.undo(commandStackNodeUri + "#" + commands.get(commands.size() - 1).getId());
		remoteMethodInvocationListener.postInvoke(remoteMethodInvocationInfo);

		remoteMethodInvocationListener.preInvoke(remoteMethodInvocationInfo);
		resourceServiceRemote.redo(commandStackNodeUri + "#" + commands.get(commands.size() - 1).getId());
		remoteMethodInvocationListener.postInvoke(remoteMethodInvocationInfo);

		List<Node> children = nodeService.getChildren(node, context);
		assertTrue("A new node was created and added as a child", children.contains(new Node(childUri, MindMapConstants.MINDMAP_NODE_TYPE)));
	}

	@Test
	public void testUndoRedoCommand_AddChild_SetProperty() {
		String property = "text", newValue = "Test";
		Node node = nodeService.getChildren(rootNode, context).get(0);

		context.add("type", MindMapConstants.MINDMAP_NODE_TYPE);
		remoteMethodInvocationListener.preInvoke(remoteMethodInvocationInfo);
		String childUri = nodeServiceRemote.addChild(node.getNodeUri(), context);
		remoteMethodInvocationListener.postInvoke(remoteMethodInvocationInfo);

		remoteMethodInvocationListener.preInvoke(remoteMethodInvocationInfo);
		nodeServiceRemote.setProperty(childUri, property, newValue);
		remoteMethodInvocationListener.postInvoke(remoteMethodInvocationInfo);

		List<Command> commands = resourceSetService.getCommands(resourceNodeUri);
		resourceSetService.undo(resourceNodeUri, commands.get(commands.size() - 2).getId());

		resourceSetService.redo(resourceNodeUri, commands.get(commands.size() - 1).getId());

		// List<Node> children = nodeService.getChildren(node, context);
		// assertTrue("A new node was created and added as a child",
		// children.contains(new Node(childUri)));
	}

	@Test
	public void testCommandStack() {
		resourceSetService.resetCommandStack(resourceNodeUri);

		Node node = nodeService.getChildren(rootNode, context).get(0);

		String newNodeId;
		{
			context.add("type", MindMapConstants.MINDMAP_NODE_TYPE);
			remoteMethodInvocationListener.preInvoke(remoteMethodInvocationInfo);
			newNodeId = nodeServiceRemote.addChild(node.getNodeUri(), context);
			remoteMethodInvocationListener.postInvoke(remoteMethodInvocationInfo);
			List<Command> commands = resourceSetService.getCommands(resourceNodeUri);

			assertEquals("Command to undo has index 0", resourceSetService.getCommandToUndoId(resourceNodeUri), commands.get(0).getId());
			assertEquals("Command to redo is null", resourceSetService.getCommandToRedoId(resourceNodeUri), null);
		}

		{
			String property = "text", newValue = "Test";
			remoteMethodInvocationListener.preInvoke(remoteMethodInvocationInfo);
			nodeServiceRemote.setProperty(newNodeId, property, newValue);
			remoteMethodInvocationListener.postInvoke(remoteMethodInvocationInfo);
			List<Command> commands = resourceSetService.getCommands(resourceNodeUri);

			assertEquals("Command to undo has index 1", resourceSetService.getCommandToUndoId(resourceNodeUri), commands.get(1).getId());
			assertEquals("Command to redo is null", resourceSetService.getCommandToRedoId(resourceNodeUri), null);
		}

		 {
			 List<Command> commands = resourceSetService.getCommands(resourceNodeUri);
			 resourceSetService.undo(resourceNodeUri, commands.get(0).getId());
			
			 assertEquals("Command to undo is null",
			 resourceSetService.getCommandToUndoId(resourceNodeUri), null);
			 assertEquals("Command to redo has index 0",
			 resourceSetService.getCommandToRedoId(resourceNodeUri),
			 commands.get(0).getId());
		 }
		
		 {
			 List<Command> commands = resourceSetService.getCommands(resourceNodeUri);
			 resourceSetService.redo(resourceNodeUri, commands.get(commands.size() - 1).getId());
			
			 assertEquals("Command to undo has index 1",
			 resourceSetService.getCommandToUndoId(resourceNodeUri),
			 commands.get(1).getId());
			 assertEquals("Command to redo is null",
			 resourceSetService.getCommandToRedoId(resourceNodeUri), null);
		 }

		{
			List<Command> commands = resourceSetService.getCommands(resourceNodeUri);

			try {
				resourceSetService.redo(resourceNodeUri, commands.get(1).getId());
				fail("Consistency exception is thrown");
			} catch (IllegalArgumentException e) {
				assertTrue("Consistency exception", e.getMessage().contains("has already been redone"));
			}

			assertEquals("Command to undo has index 1", resourceSetService.getCommandToUndoId(resourceNodeUri), commands.get(1).getId());
			assertEquals("Command to redo is null", resourceSetService.getCommandToRedoId(resourceNodeUri), null);
		}

		{
			List<Command> commands = resourceSetService.getCommands(resourceNodeUri);

			try {
				resourceSetService.redo(resourceNodeUri, "000");
				fail("Inexistent command exception is thrown");
			} catch (IllegalArgumentException e) {
				assertTrue("Inexistent command exception", e.getMessage().contains("doesn't exist"));
			}

			assertEquals("Command to undo has index 1", resourceSetService.getCommandToUndoId(resourceNodeUri), commands.get(1).getId());
			assertEquals("Command to redo is null", resourceSetService.getCommandToRedoId(resourceNodeUri), null);
		}

		{
			List<Command> commands = resourceSetService.getCommands(resourceNodeUri);
			resourceSetService.undo(resourceNodeUri, commands.get(1).getId());

			assertEquals("Command to undo has index 0", resourceSetService.getCommandToUndoId(resourceNodeUri), commands.get(0).getId());
			assertEquals("Command to redo has index 1", resourceSetService.getCommandToRedoId(resourceNodeUri), commands.get(1).getId());
		}

		{
			List<Command> commands = resourceSetService.getCommands(resourceNodeUri);

			try {
				resourceSetService.undo(resourceNodeUri, commands.get(1).getId());
				fail("Consistency exception is thrown");
			} catch (IllegalArgumentException e) {
				assertTrue("Consistency exception", e.getMessage().contains("has already been undone"));
			}

			assertEquals("Command to undo has index 0", resourceSetService.getCommandToUndoId(resourceNodeUri), commands.get(0).getId());
			assertEquals("Command to redo has index 1", resourceSetService.getCommandToRedoId(resourceNodeUri), commands.get(1).getId());
		}

		{
			List<Command> commands = resourceSetService.getCommands(resourceNodeUri);

			try {
				resourceSetService.undo(resourceNodeUri, "000");
				fail("Inexistent command exception is thrown");
			} catch (IllegalArgumentException e) {
				assertTrue("Inexistent command exception", e.getMessage().contains("doesn't exist"));
			}

			assertEquals("Command to undo has index 0", resourceSetService.getCommandToUndoId(resourceNodeUri), commands.get(0).getId());
			assertEquals("Command to redo has index 1", resourceSetService.getCommandToRedoId(resourceNodeUri), commands.get(1).getId());
		}

		{
			List<Command> commands = resourceSetService.getCommands(resourceNodeUri);
			Command testCommand = commands.get(1);
			String property = "text", newValue = "Test 2";
			remoteMethodInvocationListener.preInvoke(remoteMethodInvocationInfo);
			nodeServiceRemote.setProperty(newNodeId, property, newValue);
			remoteMethodInvocationListener.postInvoke(remoteMethodInvocationInfo);

			commands = resourceSetService.getCommands(resourceNodeUri);
			assertFalse("Command 2 is deleted", commands.contains(testCommand));
			try {
				resourceSetService.undo(resourceNodeUri, testCommand.getId());
				fail("Inexistent command exception is thrown");
			} catch (IllegalArgumentException e) {
				assertTrue("Inexistent command exception", e.getMessage().contains("doesn't exist"));
			}
			assertEquals("Command to undo has index 1", resourceSetService.getCommandToUndoId(resourceNodeUri), commands.get(1).getId());
			assertEquals("Command to redo is null", resourceSetService.getCommandToRedoId(resourceNodeUri), null);
		}

		{
			String property = "text", newValue = "Test 3";
			remoteMethodInvocationListener.preInvoke(remoteMethodInvocationInfo);
			nodeServiceRemote.setProperty(newNodeId, property, newValue);
			remoteMethodInvocationListener.postInvoke(remoteMethodInvocationInfo);
			List<Command> commands = resourceSetService.getCommands(resourceNodeUri);

			assertEquals("Command to undo has index 2", resourceSetService.getCommandToUndoId(resourceNodeUri), commands.get(2).getId());
			assertEquals("Command to redo is null", resourceSetService.getCommandToRedoId(resourceNodeUri), null);
		}

		{
			String property = "text", newValue = "Test 4";
			remoteMethodInvocationListener.preInvoke(remoteMethodInvocationInfo);
			nodeServiceRemote.setProperty(newNodeId, property, newValue);
			remoteMethodInvocationListener.postInvoke(remoteMethodInvocationInfo);
			List<Command> commands = resourceSetService.getCommands(resourceNodeUri);

			assertEquals("Command to undo has index 3", resourceSetService.getCommandToUndoId(resourceNodeUri), commands.get(3).getId());
			assertEquals("Command to redo is null", resourceSetService.getCommandToRedoId(resourceNodeUri), null);
		}

		{
			List<Command> commands = resourceSetService.getCommands(resourceNodeUri);
			resourceSetService.undo(resourceNodeUri, commands.get(1).getId());

			assertEquals("Command to undo has index 0", resourceSetService.getCommandToUndoId(resourceNodeUri), commands.get(0).getId());
			assertEquals("Command to redo has index 1", resourceSetService.getCommandToRedoId(resourceNodeUri), commands.get(1).getId());
		}

		{
			List<Command> commands = resourceSetService.getCommands(resourceNodeUri);
			resourceSetService.redo(resourceNodeUri, commands.get(3).getId());

			assertEquals("Command to undo has index 3", resourceSetService.getCommandToUndoId(resourceNodeUri), commands.get(3).getId());
			assertEquals("Command to redo is null", resourceSetService.getCommandToRedoId(resourceNodeUri), null);
		}

	}

}
