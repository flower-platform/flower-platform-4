package org.flowerplatform.tests.core;

import static org.flowerplatform.tests.EclipseIndependentTestSuite.nodeService;
import static org.flowerplatform.tests.EclipseIndependentTestSuite.startPlugin;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.RemoteMethodInvocationInfo;
import org.flowerplatform.core.RemoteMethodInvocationListener;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.NodeServiceRemote;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.core.node.resource.IResourceDAO;
import org.flowerplatform.core.node.resource.ResourceService;
import org.flowerplatform.core.node.update.Command;
import org.flowerplatform.core.node.update.remote.ChildrenUpdate;
import org.flowerplatform.core.node.update.remote.PropertyUpdate;
import org.flowerplatform.core.node.update.remote.Update;
import org.flowerplatform.freeplane.FreeplanePlugin;
import org.flowerplatform.mindmap.MindMapConstants;
import org.flowerplatform.mindmap.MindMapPlugin;
import org.flowerplatform.tests.TestUtil;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CommandStackTest {

	public static final String PROJECT = "commandstack";
	public static final String DIR = TestUtil.getResourcesDir(CommandStackTest.class);
	private static final String resourceNodeId = new Node(CoreConstants.FILE_NODE_TYPE, "(fileSystem|self|)", PROJECT + "/FAP-FlowerPlatform4.mm", null).getFullNodeId();
	private static final String commandStackNodeId = new Node(CoreConstants.COMMAND_STACK_TYPE, CoreConstants.SELF_RESOURCE,
			RemoteMethodInvocationListener.escapeFullNodeId(resourceNodeId), null).getFullNodeId();

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private static Node rootNode;
	private static NodeServiceRemote nodeServiceRemote;
	private static RemoteMethodInvocationListener remoteMethodInvocationListener;
	private static ResourceService resourceService;

	private RemoteMethodInvocationInfo remoteMethodInvocationInfo;
	private ServiceContext<NodeService> context;

	@BeforeClass
	public static void beforeClass() throws Exception {
		TestUtil.copyFiles(DIR + TestUtil.INITIAL_TO_BE_COPIED, PROJECT);
		startPlugin(new FreeplanePlugin());
		startPlugin(new MindMapPlugin());

		CorePlugin.getInstance().getResourceService().sessionSubscribedToResource(resourceNodeId, "", new ServiceContext<ResourceService>(CorePlugin.getInstance().getResourceService()));
		List<Node> children = nodeService.getChildren(new Node(resourceNodeId), new ServiceContext<NodeService>(nodeService).add(CoreConstants.POPULATE_WITH_PROPERTIES, true));
		rootNode = children.get(0);
		nodeServiceRemote = new NodeServiceRemote();
		resourceService = CorePlugin.getInstance().getResourceService();

		remoteMethodInvocationListener = spy(CorePlugin.getInstance().getRemoteMethodInvocationListener());
		doReturn("dummy-session").when(remoteMethodInvocationListener).getSessionId();

	}

	@Before
	public void setUp() {
		context = new ServiceContext<NodeService>();
		remoteMethodInvocationInfo = spy(new RemoteMethodInvocationInfo());
		doReturn(new ArrayList<String>()).when(remoteMethodInvocationInfo).getResourceNodeIds();
		doReturn(-1L).when(remoteMethodInvocationInfo).getTimestampOfLastRequest();
	}

	@Test
	public void testNewCommand_SetProperty() {
		// tests are not ordered, thus command stack may already be populated
		resourceService.resetCommandStack(resourceNodeId);

		String property = "fontFamily";
		Node node = nodeService.getChildren(rootNode, context).get(0);

		remoteMethodInvocationListener.preInvoke(remoteMethodInvocationInfo);
		nodeServiceRemote.setProperty(node.getFullNodeId(), property, "Changed");
		remoteMethodInvocationListener.postInvoke(remoteMethodInvocationInfo);

		List<Command> commands = resourceService.getCommands(resourceNodeId);
		assertEquals("There is one command in the scommand stack", 1, commands.size());
		List<Update> updates = resourceService.getCommandUpdates(commands.get(0));
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
		// tests are not ordered, thus command stack may already be populated
		resourceService.resetCommandStack(resourceNodeId);

		remoteMethodInvocationListener.preInvoke(remoteMethodInvocationInfo);
		context.add("type", MindMapConstants.MINDMAP_NODE_TYPE);
		nodeServiceRemote.addChild(rootNode.getFullNodeId(), context);
		remoteMethodInvocationListener.postInvoke(remoteMethodInvocationInfo);

		List<Command> commands = resourceService.getCommands(resourceNodeId);
		assertEquals("There is one command in the command stack", 1, commands.size());
		List<Update> updates = resourceService.getCommandUpdates(commands.get(0));
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
		String oldValue = node.getOrPopulateProperties().get(property).toString();

		remoteMethodInvocationListener.preInvoke(remoteMethodInvocationInfo);
		nodeServiceRemote.setProperty(node.getFullNodeId(), property, "Changed");
		remoteMethodInvocationListener.postInvoke(remoteMethodInvocationInfo);

		node = nodeService.getChildren(rootNode, context).get(0);
		assertEquals("Changed", node.getOrPopulateProperties().get(property));

		List<Command> commands = resourceService.getCommands(resourceNodeId);
		resourceService.undo(resourceNodeId, commands.get(commands.size() - 1).getId());

		node = nodeService.getChildren(rootNode, context).get(0);
		assertEquals(oldValue, node.getOrPopulateProperties().get(property));
	}

	@Test
	public void testUndoCommand_AddChild() {
		Node node = nodeService.getChildren(rootNode, context).get(0);

		context.add("type", MindMapConstants.MINDMAP_NODE_TYPE);
		remoteMethodInvocationListener.preInvoke(remoteMethodInvocationInfo);
		String childId = nodeServiceRemote.addChild(node.getFullNodeId(), context);
		remoteMethodInvocationListener.postInvoke(remoteMethodInvocationInfo);

		List<Node> children = nodeService.getChildren(node, context);
		assertTrue("A new node was created and added as a child", children.contains(new Node(childId)));

		List<Command> commands = resourceService.getCommands(resourceNodeId);
		resourceService.undo(resourceNodeId, commands.get(commands.size() - 1).getId());

		children = nodeService.getChildren(node, context);
		assertFalse("The newly created node was removed", children.contains(new Node(childId)));

	}

	@Test
	public void testRedoCommand_SetProperty() {
		String property = "text", newValue = "Changed 1";
		Node node = nodeService.getChildren(rootNode, context).get(0);

		remoteMethodInvocationListener.preInvoke(remoteMethodInvocationInfo);
		nodeServiceRemote.setProperty(node.getFullNodeId(), property, newValue);
		remoteMethodInvocationListener.postInvoke(remoteMethodInvocationInfo);

		List<Command> commands = resourceService.getCommands(resourceNodeId);
		resourceService.undo(resourceNodeId, commands.get(commands.size() - 1).getId());

		resourceService.redo(resourceNodeId, commands.get(commands.size() - 1).getId());

		node = nodeService.getChildren(rootNode, context).get(0);
		assertEquals(newValue, node.getOrPopulateProperties().get(property));
	}

	@Test
	public void testServiceRedoCommand_SetProperty() {
		String property = "text", newValue = "Changed 1";
		Node node = nodeService.getChildren(rootNode, context).get(0);

		remoteMethodInvocationListener.preInvoke(remoteMethodInvocationInfo);
		nodeServiceRemote.setProperty(node.getFullNodeId(), property, newValue);
		remoteMethodInvocationListener.postInvoke(remoteMethodInvocationInfo);

		List<Command> commands = resourceService.getCommands(resourceNodeId);
		remoteMethodInvocationListener.preInvoke(remoteMethodInvocationInfo);
		nodeServiceRemote.undo(commandStackNodeId, commands.get(commands.size() - 1).getId());
		remoteMethodInvocationListener.postInvoke(remoteMethodInvocationInfo);

		remoteMethodInvocationListener.preInvoke(remoteMethodInvocationInfo);
		nodeServiceRemote.redo(commandStackNodeId, commands.get(commands.size() - 1).getId());
		remoteMethodInvocationListener.postInvoke(remoteMethodInvocationInfo);

		node = nodeService.getChildren(rootNode, context).get(0);
		assertEquals(newValue, node.getOrPopulateProperties().get(property));
	}

	@Test
	public void testRedoCommand_AddChild() {
		Node node = nodeService.getChildren(rootNode, context).get(0);

		context.add("type", MindMapConstants.MINDMAP_NODE_TYPE);
		remoteMethodInvocationListener.preInvoke(remoteMethodInvocationInfo);
		String childId = nodeServiceRemote.addChild(node.getFullNodeId(), context);
		remoteMethodInvocationListener.postInvoke(remoteMethodInvocationInfo);

		List<Command> commands = resourceService.getCommands(resourceNodeId);
		resourceService.undo(resourceNodeId, commands.get(commands.size() - 1).getId());

		resourceService.redo(resourceNodeId, commands.get(commands.size() - 1).getId());

		List<Node> children = nodeService.getChildren(node, context);
		assertTrue("A new node was created and added as a child", children.contains(new Node(childId)));
	}

	@Test
	public void testServiceRedoCommand_AddChild() {
		Node node = nodeService.getChildren(rootNode, context).get(0);

		context.add("type", MindMapConstants.MINDMAP_NODE_TYPE);
		remoteMethodInvocationListener.preInvoke(remoteMethodInvocationInfo);
		String childId = nodeServiceRemote.addChild(node.getFullNodeId(), context);
		remoteMethodInvocationListener.postInvoke(remoteMethodInvocationInfo);

		List<Command> commands = resourceService.getCommands(resourceNodeId);
		remoteMethodInvocationListener.preInvoke(remoteMethodInvocationInfo);
		nodeServiceRemote.undo(commandStackNodeId, commands.get(commands.size() - 1).getId());
		remoteMethodInvocationListener.postInvoke(remoteMethodInvocationInfo);

		remoteMethodInvocationListener.preInvoke(remoteMethodInvocationInfo);
		nodeServiceRemote.redo(commandStackNodeId, commands.get(commands.size() - 1).getId());
		remoteMethodInvocationListener.postInvoke(remoteMethodInvocationInfo);

		List<Node> children = nodeService.getChildren(node, context);
		assertTrue("A new node was created and added as a child", children.contains(new Node(childId)));
	}

	@Test
	public void testUndoRedoCommand_AddChild_SetProperty() {
		String property = "text", newValue = "Test";
		Node node = nodeService.getChildren(rootNode, context).get(0);

		context.add("type", MindMapConstants.MINDMAP_NODE_TYPE);
		remoteMethodInvocationListener.preInvoke(remoteMethodInvocationInfo);
		String childId = nodeServiceRemote.addChild(node.getFullNodeId(), context);
		remoteMethodInvocationListener.postInvoke(remoteMethodInvocationInfo);

		remoteMethodInvocationListener.preInvoke(remoteMethodInvocationInfo);
		nodeServiceRemote.setProperty(childId, property, newValue);
		remoteMethodInvocationListener.postInvoke(remoteMethodInvocationInfo);

		List<Command> commands = resourceService.getCommands(resourceNodeId);
		resourceService.undo(resourceNodeId, commands.get(commands.size() - 2).getId());

		resourceService.redo(resourceNodeId, commands.get(commands.size() - 1).getId());

		// List<Node> children = nodeService.getChildren(node, context);
		// assertTrue("A new node was created and added as a child",
		// children.contains(new Node(childId)));
	}

	@Test
	public void testCommandStack() {
		// tests are not ordered, thus command stack may already be populated
		resourceService.resetCommandStack(resourceNodeId);

		Node node = nodeService.getChildren(rootNode, context).get(0);
		Field f = null;
		IResourceDAO resourceDao = null;
		try {
			f = resourceService.getClass().getDeclaredField("resourceDao");
			f.setAccessible(true);
			resourceDao = (IResourceDAO) f.get(resourceService);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		String newNodeId;
		{
			context.add("type", MindMapConstants.MINDMAP_NODE_TYPE);
			remoteMethodInvocationListener.preInvoke(remoteMethodInvocationInfo);
			newNodeId = nodeServiceRemote.addChild(node.getFullNodeId(), context);
			remoteMethodInvocationListener.postInvoke(remoteMethodInvocationInfo);
			List<Command> commands = resourceService.getCommands(resourceNodeId);

			assertEquals("Command to undo has index 0", resourceDao.getCommandToUndoId(resourceNodeId), commands.get(0).getId());
			assertEquals("Command to redo is null", resourceDao.getCommandToRedoId(resourceNodeId), null);
		}

		{
			String property = "text", newValue = "Test";
			remoteMethodInvocationListener.preInvoke(remoteMethodInvocationInfo);
			nodeServiceRemote.setProperty(newNodeId, property, newValue);
			remoteMethodInvocationListener.postInvoke(remoteMethodInvocationInfo);
			List<Command> commands = resourceService.getCommands(resourceNodeId);

			assertEquals("Command to undo has index 1", resourceDao.getCommandToUndoId(resourceNodeId), commands.get(1).getId());
			assertEquals("Command to redo is null", resourceDao.getCommandToRedoId(resourceNodeId), null);
		}

		// {
		// List<Command> commands = resourceService.getCommands(resourceNodeId);
		// resourceService.undo(resourceNodeId, commands.get(0).getId());
		//
		// assertEquals("Command to undo is null",
		// resourceDao.getCommandToUndoId(resourceNodeId), null);
		// assertEquals("Command to redo has index 0",
		// resourceDao.getCommandToRedoId(resourceNodeId),
		// commands.get(0).getId());
		// }
		//
		// {
		// List<Command> commands = resourceService.getCommands(resourceNodeId);
		// resourceService.redo(resourceNodeId, commands.get(commands.size() -
		// 1).getId());
		//
		// assertEquals("Command to undo has index 1",
		// resourceDao.getCommandToUndoId(resourceNodeId),
		// commands.get(1).getId());
		// assertEquals("Command to redo is null",
		// resourceDao.getCommandToRedoId(resourceNodeId), null);
		// }

		{
			List<Command> commands = resourceService.getCommands(resourceNodeId);

			try {
				resourceService.redo(resourceNodeId, commands.get(1).getId());
				fail("Consistency exception is thrown");
			} catch (IllegalArgumentException e) {
				assertTrue("Consistency exception", e.getMessage().contains("has already been redone"));
			}

			assertEquals("Command to undo has index 1", resourceDao.getCommandToUndoId(resourceNodeId), commands.get(1).getId());
			assertEquals("Command to redo is null", resourceDao.getCommandToRedoId(resourceNodeId), null);
		}

		{
			List<Command> commands = resourceService.getCommands(resourceNodeId);

			try {
				resourceService.redo(resourceNodeId, "000");
				fail("Inexistent command exception is thrown");
			} catch (IllegalArgumentException e) {
				assertTrue("Inexistent command exception", e.getMessage().contains("doesn't exist"));
			}

			assertEquals("Command to undo has index 1", resourceDao.getCommandToUndoId(resourceNodeId), commands.get(1).getId());
			assertEquals("Command to redo is null", resourceDao.getCommandToRedoId(resourceNodeId), null);
		}

		{
			List<Command> commands = resourceService.getCommands(resourceNodeId);
			resourceService.undo(resourceNodeId, commands.get(1).getId());

			assertEquals("Command to undo has index 0", resourceDao.getCommandToUndoId(resourceNodeId), commands.get(0).getId());
			assertEquals("Command to redo has index 1", resourceDao.getCommandToRedoId(resourceNodeId), commands.get(1).getId());
		}

		{
			List<Command> commands = resourceService.getCommands(resourceNodeId);

			try {
				resourceService.undo(resourceNodeId, commands.get(1).getId());
				fail("Consistency exception is thrown");
			} catch (IllegalArgumentException e) {
				assertTrue("Consistency exception", e.getMessage().contains("has already been undone"));
			}

			assertEquals("Command to undo has index 0", resourceDao.getCommandToUndoId(resourceNodeId), commands.get(0).getId());
			assertEquals("Command to redo has index 1", resourceDao.getCommandToRedoId(resourceNodeId), commands.get(1).getId());
		}

		{
			List<Command> commands = resourceService.getCommands(resourceNodeId);

			try {
				resourceService.undo(resourceNodeId, "000");
				fail("Inexistent command exception is thrown");
			} catch (IllegalArgumentException e) {
				assertTrue("Inexistent command exception", e.getMessage().contains("doesn't exist"));
			}

			assertEquals("Command to undo has index 0", resourceDao.getCommandToUndoId(resourceNodeId), commands.get(0).getId());
			assertEquals("Command to redo has index 1", resourceDao.getCommandToRedoId(resourceNodeId), commands.get(1).getId());
		}

		{
			List<Command> commands = resourceService.getCommands(resourceNodeId);
			Command testCommand = commands.get(1);
			String property = "text", newValue = "Test 2";
			remoteMethodInvocationListener.preInvoke(remoteMethodInvocationInfo);
			nodeServiceRemote.setProperty(newNodeId, property, newValue);
			remoteMethodInvocationListener.postInvoke(remoteMethodInvocationInfo);

			commands = resourceService.getCommands(resourceNodeId);
			assertFalse("Command 2 is deleted", commands.contains(testCommand));
			try {
				resourceService.undo(resourceNodeId, testCommand.getId());
				fail("Inexistent command exception is thrown");
			} catch (IllegalArgumentException e) {
				assertTrue("Inexistent command exception", e.getMessage().contains("doesn't exist"));
			}
			assertEquals("Command to undo has index 1", resourceDao.getCommandToUndoId(resourceNodeId), commands.get(1).getId());
			assertEquals("Command to redo is null", resourceDao.getCommandToRedoId(resourceNodeId), null);
		}

		{
			String property = "text", newValue = "Test 3";
			remoteMethodInvocationListener.preInvoke(remoteMethodInvocationInfo);
			nodeServiceRemote.setProperty(newNodeId, property, newValue);
			remoteMethodInvocationListener.postInvoke(remoteMethodInvocationInfo);
			List<Command> commands = resourceService.getCommands(resourceNodeId);

			assertEquals("Command to undo has index 2", resourceDao.getCommandToUndoId(resourceNodeId), commands.get(2).getId());
			assertEquals("Command to redo is null", resourceDao.getCommandToRedoId(resourceNodeId), null);
		}

		{
			String property = "text", newValue = "Test 4";
			remoteMethodInvocationListener.preInvoke(remoteMethodInvocationInfo);
			nodeServiceRemote.setProperty(newNodeId, property, newValue);
			remoteMethodInvocationListener.postInvoke(remoteMethodInvocationInfo);
			List<Command> commands = resourceService.getCommands(resourceNodeId);

			assertEquals("Command to undo has index 3", resourceDao.getCommandToUndoId(resourceNodeId), commands.get(3).getId());
			assertEquals("Command to redo is null", resourceDao.getCommandToRedoId(resourceNodeId), null);
		}

		{
			List<Command> commands = resourceService.getCommands(resourceNodeId);
			resourceService.undo(resourceNodeId, commands.get(1).getId());

			assertEquals("Command to undo has index 0", resourceDao.getCommandToUndoId(resourceNodeId), commands.get(0).getId());
			assertEquals("Command to redo has index 1", resourceDao.getCommandToRedoId(resourceNodeId), commands.get(1).getId());
		}

		{
			List<Command> commands = resourceService.getCommands(resourceNodeId);
			resourceService.redo(resourceNodeId, commands.get(3).getId());

			assertEquals("Command to undo has index 3", resourceDao.getCommandToUndoId(resourceNodeId), commands.get(3).getId());
			assertEquals("Command to redo is null", resourceDao.getCommandToRedoId(resourceNodeId), null);
		}

	}

}
