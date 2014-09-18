package org.flowerplatform.tests.js_client.java;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.NodeWithChildren;
import org.flowerplatform.core.node.update.remote.ChildrenUpdate;
import org.flowerplatform.core.node.update.remote.PropertyUpdate;
import org.flowerplatform.core.node.update.remote.Update;
import org.flowerplatform.js_client.java.JsClientJavaUtils;
import org.flowerplatform.js_client.java.node.ClientNode;
import org.flowerplatform.js_client.java.node.INodeChangeListener;
import org.flowerplatform.js_client.java.node.JavaHostInvocator;
import org.flowerplatform.js_client.java.node.JavaHostResourceOperationsHandler;
import org.flowerplatform.tests.js_client.java.JSClientJavaTestUtils.RecordingServiceInvocator;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;

/**
 * @author Cristina Constantinescu
 */
public class JsClientNodeRegistryTest {
	
	private static Context ctx;
	private static Scriptable scope;
	
	/**
	 * @author Cristina Constantinescu
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ctx = Context.enter();
		scope = ctx.initStandardObjects();	
		
		String pathToJsFolder = FileUtils.getFile("src/").getAbsolutePath() + "/../../org.flowerplatform.js_client.common_js_as/WebContent/js";
		ctx.evaluateReader(scope, Files.newBufferedReader(Paths.get(pathToJsFolder + "/NodeRegistry.js"), StandardCharsets.UTF_8), null, 1, null);
		ctx.evaluateReader(scope, Files.newBufferedReader(Paths.get(pathToJsFolder + "/ResourceOperationsManager.js"), StandardCharsets.UTF_8), null, 1, null);
		ctx.evaluateReader(scope, Files.newBufferedReader(Paths.get(pathToJsFolder + "/NodeRegistryManager.js"), StandardCharsets.UTF_8), null, 1, null);		
	}
	
	/**
	 * @author Cristina Constantinescu
	 */
	@AfterClass
	public static void setUpAfterClass() throws Exception {
		Context.exit();
	}
		
	/**
	 * @author Cristina Constantinescu
	 */
	@Test
	public void expand() throws Exception {
		ClientNode parent = JSClientJavaTestUtils.createClientNode("parent");
		ClientNode child1 = JSClientJavaTestUtils.createClientNode("child1");
		ClientNode child2 = JSClientJavaTestUtils.createClientNode("child2");
		ClientNode child3 = JSClientJavaTestUtils.createClientNode("child3");
				
		List<ClientNode> list = new ArrayList<>();
		list.add(child1);
		list.add(child2);
		list.add(child3);
				
		// create nodeRegistryManager
		Scriptable nodeRegistryManager = ctx.newObject(scope, "NodeRegistryManager", new Object[] {
				new JavaHostResourceOperationsHandler(), 
				new RecordingServiceInvocator().setExpectedResults(new Object[] {list}), 
				new JavaHostInvocator()});
		scope.put("_nodeRegistryManager", scope, nodeRegistryManager);
					
		// create nodeRegistry
		Scriptable nodeRegistry = (Scriptable) ctx.evaluateString(scope, "_nodeRegistryManager.createNodeRegistry();", null, 1, null);

		INodeChangeListener listener = mock(INodeChangeListener.class);					
		// add NodeChangedListener in nodeRegistry
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "addNodeChangeListener", listener);
	
		// register parent node
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "registerNode", parent, null, -1);		
		verify(listener).nodeAdded(parent);
		
		// expand parent node
		JsClientJavaUtils.invokeJsFunction(nodeRegistryManager, "expand", nodeRegistry, parent, null);
				
		// verify children
		assertNotNull(parent.getChildren());	
		assertEquals(3, parent.getChildren().size());
		
		verify(listener).nodeAdded(child1);
		verify(listener).nodeAdded(child2);
		verify(listener).nodeAdded(child3);			
	}
	
	/**
	 * @author Elena Posea
	 * @throws Exception
	 */
	@Test
	public void register() throws Exception {
		ClientNode parent = JSClientJavaTestUtils.createClientNode("parent");
		ClientNode child = JSClientJavaTestUtils.createClientNode("child");
				
		// create nodeRegistryManager
		Scriptable nodeRegistryManager = ctx.newObject(scope, "NodeRegistryManager", new Object[] {
				new JavaHostResourceOperationsHandler(), 
				new RecordingServiceInvocator(), 
				new JavaHostInvocator()});
		scope.put("_nodeRegistryManager", scope, nodeRegistryManager);
		
		// create nodeRegistry
		Scriptable nodeRegistry = (Scriptable) ctx.evaluateString(scope, "_nodeRegistryManager.createNodeRegistry();", null, 1, null);
		
		INodeChangeListener listener = mock(INodeChangeListener.class);
		// add NodeChangedListener in nodeRegistry
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "addNodeChangeListener", listener);
		
		// register parent node
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "registerNode", parent, null, -1);
		verify(listener).nodeAdded(parent);
		
		// register child node
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "registerNode", child, parent, -1);
		verify(listener).nodeAdded(child);
		
		// verify parent
		assertNotNull(child.getParent());
		
		// verify children
		assertNotNull(parent.getChildren());
		assertEquals(1, parent.getChildren().size());
		
		// verify getNodeById is not null/ node is registered
		assertNotNull(JsClientJavaUtils.invokeJsFunction(nodeRegistry, "getNodeById", child.getNodeUri()));
	}
	
	
	/**
	 * @author Elena Posea
	 * @throws Exception
	 */
	@Test
	public void unregister() throws Exception {
		ClientNode grandParent = JSClientJavaTestUtils.createClientNode("grandParent");
		ClientNode parent = JSClientJavaTestUtils.createClientNode("parent");
		ClientNode child1 = JSClientJavaTestUtils.createClientNode("child1");
		ClientNode child2 = JSClientJavaTestUtils.createClientNode("child2");
				
		// create nodeRegistryManager
		Scriptable nodeRegistryManager = ctx.newObject(scope, "NodeRegistryManager", new Object[] {
				new JavaHostResourceOperationsHandler(), 
				new RecordingServiceInvocator(), 
				new JavaHostInvocator()});
		scope.put("_nodeRegistryManager", scope, nodeRegistryManager);
					
		// create nodeRegistry
		Scriptable nodeRegistry = (Scriptable) ctx.evaluateString(scope, "_nodeRegistryManager.createNodeRegistry();", null, 1, null);
		
		INodeChangeListener listener = mock(INodeChangeListener.class);
		// add NodeChangedListener in nodeRegistry
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "addNodeChangeListener", listener);
		
		// register grandParent node
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "registerNode", grandParent, null, -1);
		
		// register parent node
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "registerNode", parent, grandParent, -1);
		
		// register child1 & child2 node
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "registerNode", child1, parent, -1);
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "registerNode", child2, parent, -1);

		// remove parent
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "unregisterNode", parent, grandParent, -1);
		verify(listener).nodeRemoved(parent);
		verify(listener).nodeRemoved(child1);
		verify(listener).nodeRemoved(child2);
		
		// verify grandParent's list of children
		assertNull(grandParent.getChildren());
		
		// verify getNodeById is null for removed nodes
		assertNull(JsClientJavaUtils.invokeJsFunction(nodeRegistry, "getNodeById", parent.getNodeUri()));
		assertNull(JsClientJavaUtils.invokeJsFunction(nodeRegistry, "getNodeById", child1.getNodeUri()));
		assertNull(JsClientJavaUtils.invokeJsFunction(nodeRegistry, "getNodeById", child2.getNodeUri()));
		
	}
	
	
	/**
	 * @author Elena Posea
	 * @throws Exception
	 */
	@Test
	public void collapse() throws Exception {
		ClientNode grandParent = JSClientJavaTestUtils.createClientNode("grandParent");
		ClientNode parent = JSClientJavaTestUtils.createClientNode("parent");
		ClientNode child1 = JSClientJavaTestUtils.createClientNode("child1");
		ClientNode child2 = JSClientJavaTestUtils.createClientNode("child2");
				
		// create nodeRegistryManager
		Scriptable nodeRegistryManager = ctx.newObject(scope, "NodeRegistryManager", new Object[] {
				new JavaHostResourceOperationsHandler(), 
				new RecordingServiceInvocator(), 
				new JavaHostInvocator()});
		scope.put("_nodeRegistryManager", scope, nodeRegistryManager);
		
		// create nodeRegistry
		Scriptable nodeRegistry = (Scriptable) ctx.evaluateString(scope, "_nodeRegistryManager.createNodeRegistry();", null, 1, null);
		
		INodeChangeListener listener = mock(INodeChangeListener.class);
		// add NodeChangedListener in nodeRegistry
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "addNodeChangeListener", listener);
		
		// register grandParent node
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "registerNode", grandParent, null, -1);
		
		// register parent node
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "registerNode", parent, grandParent, -1);
		
		// register child1 & child2 node
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "registerNode", child1, parent, -1);
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "registerNode", child2, parent, -1);
		
		// collapse grandParent
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "collapse", grandParent);
		
		// verify getNodeById is null for removed nodes
		assertNull(JsClientJavaUtils.invokeJsFunction(nodeRegistry, "getNodeById", parent.getNodeUri()));
		assertNull(JsClientJavaUtils.invokeJsFunction(nodeRegistry, "getNodeById", child1.getNodeUri()));
		assertNull(JsClientJavaUtils.invokeJsFunction(nodeRegistry, "getNodeById", child2.getNodeUri()));
		
		verify(listener).nodeRemoved(parent);
		verify(listener).nodeRemoved(child1);
		verify(listener).nodeRemoved(child2);
		
	}
	
	/**
	 * @author Elena Posea
	 * new node is only different in what it concerns its properties
	 * @throws Exception
	 */
	@Test
	public void refreshProperties() throws Exception {
		ClientNode node = JSClientJavaTestUtils.createClientNode("node");
		NodeWithChildren newNodeWithChildren = JSClientJavaTestUtils.createClientNodeWithChildren("newNodeWithChildren");
		
		Map<String, Object> oldProperties = new HashMap<String, Object>(); 
		oldProperties.put("propertyA", "valueA");
		oldProperties.put("propertyB", "valueB");
		node.setProperties(oldProperties);
		
		Map<String, Object> newProperties = new HashMap<String, Object>();
		newProperties.put("propertyA", "newValueA");
		newProperties.put("propertyC", "valueC");
		newNodeWithChildren.getNode().setProperties(newProperties);
		
		// create nodeRegistryManager
		Scriptable nodeRegistryManager = ctx.newObject(scope, "NodeRegistryManager", new Object[] {
				new JavaHostResourceOperationsHandler(), 
				new RecordingServiceInvocator().setExpectedResults(new Object[] {newNodeWithChildren}), 
				new JavaHostInvocator()});
		scope.put("_nodeRegistryManager", scope, nodeRegistryManager);
					
		// create nodeRegistry
		Scriptable nodeRegistry = (Scriptable) ctx.evaluateString(scope, "_nodeRegistryManager.createNodeRegistry();", null, 1, null);
		
		// register node
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "registerNode", node, null, -1);	
		
		// try to refresh this node
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "refresh", node);
		// assertEquals((NativeObject)node.getProperties(), newProperties);
		
		NativeObject afterRefreshProperties = node.getProperties();
		assertTrue("properties not changed properly", afterRefreshProperties != null && sameMap(afterRefreshProperties, newProperties));
	}
	
	/**
	 * @author Elena Posea
	 * new node doesn't have any children => collapse
	 * @throws Exception
	 */
	@Test
	public void refreshCollapseIfNoChildren() throws Exception {
		
		ClientNode node = JSClientJavaTestUtils.createClientNode("node");
		ClientNode child1 = JSClientJavaTestUtils.createClientNode("child1");
		ClientNode child2 = JSClientJavaTestUtils.createClientNode("child2");
		NodeWithChildren newNodeWithChildren = JSClientJavaTestUtils.createClientNodeWithChildren("newNodeWithChildren");
		
		
		// create nodeRegistryManager
		Scriptable nodeRegistryManager = ctx.newObject(scope, "NodeRegistryManager", new Object[] {
				new JavaHostResourceOperationsHandler(), 
				new RecordingServiceInvocator().setExpectedResults(new Object[] {newNodeWithChildren}), 
				new JavaHostInvocator()});
		scope.put("_nodeRegistryManager", scope, nodeRegistryManager);
					
		// create nodeRegistry
		Scriptable nodeRegistry = (Scriptable) ctx.evaluateString(scope, "_nodeRegistryManager.createNodeRegistry();", null, 1, null);
		
		// register node
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "registerNode", node, null, -1);	
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "registerNode", child1, node, -1);	
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "registerNode", child2, node, -1);	
		
		INodeChangeListener listener = mock(INodeChangeListener.class);
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "addNodeChangeListener", listener);
		// try to refresh this node
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "refresh", node);
		
		// add NodeChangedListener in nodeRegistry
		verify(listener).nodeRemoved(child1);
		verify(listener).nodeRemoved(child2);
		
		assertNull("list of children for updated node should be null", node.getChildren());
	}
	
	/**
	 * @author Elena Posea
	 * @throws Exception
	 */
	private boolean sameMap(Map<String, Object> properties, Map<String, Object> newProperties) {
		for (Object key: properties.keySet()) {
			if (!newProperties.containsKey(key)) {
				return false;
			} else {
				if (!newProperties.get(key).equals(properties.get(key))) {
					return false;
				}
			}
		}
		
		for (Object key: newProperties.keySet()) {
			if (!properties.containsKey(key)) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * @author Elena Posea
	 * new node has a different list of nodes than node.
	 * if you find the same node with the same index => keep it
	 * if you find same node with different index => remove/unregister it and register again with new index
	 * if you find new node, add it  
	 * @throws Exception
	 */
	@Test
	public void refreshDifferentListOfChildren() throws Exception {
		
		ClientNode node = JSClientJavaTestUtils.createClientNode("node");
		ClientNode child0 = JSClientJavaTestUtils.createClientNode("child0");
		ClientNode child1 = JSClientJavaTestUtils.createClientNode("child1");
		ClientNode child2 = JSClientJavaTestUtils.createClientNode("child2");
		
		NodeWithChildren newNodeWithChildren = JSClientJavaTestUtils.createClientNodeWithChildren("newNodeWithChildren");
		NodeWithChildren newChild3 = JSClientJavaTestUtils.createClientNodeWithChildren("child3");
		NodeWithChildren newChild0 = JSClientJavaTestUtils.createClientNodeWithChildren("child0");
		NodeWithChildren newChild1 = JSClientJavaTestUtils.createClientNodeWithChildren("child1");
		
		List<NodeWithChildren> children = new ArrayList<NodeWithChildren>();
		children.add(newChild3);
		children.add(newChild0);
		children.add(newChild1); // different index than before (was 1, now it's 2)
		children.set(1, newChild3);
		children.set(0, newChild0);
		children.set(2, newChild1);
		newNodeWithChildren.setChildren(children); 
		
		
		// create nodeRegistryManager
		Scriptable nodeRegistryManager = ctx.newObject(scope, "NodeRegistryManager", new Object[] {
				new JavaHostResourceOperationsHandler(), 
				new RecordingServiceInvocator().setExpectedResults(new Object[] {newNodeWithChildren}), 
				new JavaHostInvocator()});
		scope.put("_nodeRegistryManager", scope, nodeRegistryManager);
					
		// create nodeRegistry
		Scriptable nodeRegistry = (Scriptable) ctx.evaluateString(scope, "_nodeRegistryManager.createNodeRegistry();", null, 1, null);
		
		// register node
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "registerNode", node, null, -1);	
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "registerNode", child0, node, -1);	
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "registerNode", child1, node, -1);	
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "registerNode", child2, node, -1); // -1 means just add them, one after another	
		
		INodeChangeListener listener = mock(INodeChangeListener.class);
		// add NodeChangedListener in nodeRegistry
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "addNodeChangeListener", listener);
		// try to refresh this node
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "refresh", node);
		
		verify(listener).nodeRemoved(child2); // because it is no longer a child of node
		verify(listener).nodeRemoved(child1); // because its index changed, so it has to be removed and added with the new index
		verify(listener).nodeAdded(newChild3.getNode()); // new child
		verify(listener).nodeAdded(child1); // same child, added on a different place
		
		// child1 should be preserved/ neither added nor removed
		 verify(listener, never()).nodeAdded(newChild1.getNode());

		// in order to avoid overriding equals for ClientNode
		// MyMatcher<Node> baseMatcher = new MyMatcher<Node>();
		// baseMatcher.setChild(child1);
		// verify(listener, never()).nodeAdded(argThat(baseMatcher));		
		
		List<Node> expectedChildren = new ArrayList<Node>();
		expectedChildren.add(child0); // should preserve this one
		expectedChildren.add(newChild3.getNode());
		expectedChildren.add(child1); // it should keep the old instance, just the index inside the list should be different
		
		assertEquals("doesn't have the right children after refresh", expectedChildren, node.getChildren());
	}

	/** 
	 * @author Elena Posea
	 * no updates to process => call refresh for root
	 * @throws Exception
	 */
	@Test
	public void onUpdateNoUpdateToProcess() throws Exception {
		
		ClientNode root = JSClientJavaTestUtils.createClientNode("root");
		ClientNode child1 = JSClientJavaTestUtils.createClientNode("child1");
		ClientNode child2 = JSClientJavaTestUtils.createClientNode("child2");
		
		NodeWithChildren rootWithChildren = JSClientJavaTestUtils.createClientNodeWithChildren("root");
		
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put("flagToCheckRefreshGotExecuted", true); 
		rootWithChildren.getNode().setProperties(properties);
		
		List<NodeWithChildren> children = new ArrayList<NodeWithChildren>();
		NodeWithChildren child1WithChildren = JSClientJavaTestUtils.createClientNodeWithChildren("child1");
		NodeWithChildren child2WithChildren = JSClientJavaTestUtils.createClientNodeWithChildren("child2");
		children.add(child1WithChildren);
		children.add(child2WithChildren);
		
		rootWithChildren.setChildren(children);
		
		// create nodeRegistryManager
		Scriptable nodeRegistryManager = ctx.newObject(scope, "NodeRegistryManager", new Object[] {
				new JavaHostResourceOperationsHandler(), 
				new RecordingServiceInvocator().setExpectedResults(new Object[] {rootWithChildren}), // return this as the refreshed version of root
				new JavaHostInvocator()});
		scope.put("_nodeRegistryManager", scope, nodeRegistryManager);
		
		// create nodeRegistry
		Scriptable nodeRegistry = (Scriptable) ctx.evaluateString(scope, "_nodeRegistryManager.createNodeRegistry();", null, 1, null);
		
		// register node
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "registerNode", root, null, -1);	
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "registerNode", child1, root, -1);	
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "registerNode", child2, root, -1);	
		
		INodeChangeListener listener = mock(INodeChangeListener.class);
		// add NodeChangedListener in nodeRegistry
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "addNodeChangeListener", listener);
		// try to update this node
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "processUpdates", new Object[] {null}); // try to process no updates
		
		Boolean refreshGotExecuted = (Boolean) ((NativeObject) root.getProperties()).get("flagToCheckRefreshGotExecuted");
		assertNotNull(refreshGotExecuted);
	}
	
	/** 
	 * @author Elena Posea
	 * node not found => don't process the updates
	 * @throws Exception
	 */
	@Test
	public void onUpdateIfNodeNotFoundDoNotProcessUpdate() throws Exception {
		
		ClientNode root = JSClientJavaTestUtils.createClientNode("root");
		ClientNode child1 = JSClientJavaTestUtils.createClientNode("child1");
		ClientNode child2 = JSClientJavaTestUtils.createClientNode("child2");
		
		NodeWithChildren rootWithChildren = JSClientJavaTestUtils.createClientNodeWithChildren("root");
		
		List<NodeWithChildren> children = new ArrayList<NodeWithChildren>();
		NodeWithChildren child1WithChildren = JSClientJavaTestUtils.createClientNodeWithChildren("child1");
		NodeWithChildren child2WithChildren = JSClientJavaTestUtils.createClientNodeWithChildren("child2");
		children.add(child1WithChildren);
		children.add(child2WithChildren);
		
		rootWithChildren.setChildren(children);
		
		
		// create nodeRegistryManager
		Scriptable nodeRegistryManager = ctx.newObject(scope, "NodeRegistryManager", new Object[] {
				new JavaHostResourceOperationsHandler(), 
				new RecordingServiceInvocator(), //.setExpectedResults(new Object[]{new PropertyUpdate()}),
				new JavaHostInvocator()});
		scope.put("_nodeRegistryManager", scope, nodeRegistryManager);
		
		// create nodeRegistry
		Scriptable nodeRegistry = (Scriptable) ctx.evaluateString(scope, "_nodeRegistryManager.createNodeRegistry();", null, 1, null);
		
		// register nodes
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "registerNode", root, null, -1);
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "registerNode", child1, root, -1);
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "registerNode", child2, root, -1);
		
		INodeChangeListener listener = mock(INodeChangeListener.class);
		// add NodeChangedListener in nodeRegistry
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "addNodeChangeListener", listener);
		// try to update this node
		PropertyUpdate puNewProperty = new PropertyUpdate();
		puNewProperty.setFullNodeId(root.getNodeUri());
		puNewProperty.setType("UPDATED");
		puNewProperty.setIsUnset(false);
		puNewProperty.setKey("newProperty");
		puNewProperty.setValue("valueForNewProperty");

		// node not found TODO: should I check for anything? if nothing throws any exception, it should be fine
		// verific ca nu s-a adaugat in map
	}

	/** 
	 * @author Elena Posea
	 * node found, update or remove the property, notify listeners
	 * @throws Exception
	 */
	@Test
	public void onUpdateProperty() throws Exception {
		ClientNode node = JSClientJavaTestUtils.createClientNode("node");
		
		Map<String, Object> oldProperties = new HashMap<String, Object>(); 
		oldProperties.put("oldPropertyToBeRemoved", "notImportantValue");
		oldProperties.put("oldProperty", "oldValueForOldProperty");
		node.setProperties(oldProperties);
		
		Map<String, Object> newProperties = new HashMap<String, Object>();
		newProperties.put("newProperty", "valueForNewProperty");
		newProperties.put("oldProperty", "newValueForOldProperty");
		
		// create nodeRegistryManager
		Scriptable nodeRegistryManager = ctx.newObject(scope, "NodeRegistryManager", new Object[] {
				new JavaHostResourceOperationsHandler(), 
				new RecordingServiceInvocator(),
				new JavaHostInvocator()});
		scope.put("_nodeRegistryManager", scope, nodeRegistryManager);
		
		// create nodeRegistry
		Scriptable nodeRegistry = (Scriptable) ctx.evaluateString(scope, "_nodeRegistryManager.createNodeRegistry();", null, 1, null);
		// register node
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "registerNode", node, null, -1);
		
		INodeChangeListener listener = mock(INodeChangeListener.class);
		// add NodeChangedListener in nodeRegistry
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "addNodeChangeListener", listener);
		
		// try to apply updates to this node (propertyUpdates)
		PropertyUpdate puNewProperty = new PropertyUpdate();
		puNewProperty.setFullNodeId(node.getNodeUri());
		puNewProperty.setType("UPDATED");
		puNewProperty.setIsUnset(false);
		puNewProperty.setKey("newProperty");
		puNewProperty.setValue("valueForNewProperty");
		
		PropertyUpdate puNewValueForOldProperty = new PropertyUpdate();
		puNewValueForOldProperty.setFullNodeId(node.getNodeUri());
		puNewValueForOldProperty.setType("UPDATED");
		puNewValueForOldProperty.setIsUnset(false);
		puNewValueForOldProperty.setKey("oldProperty");
		puNewValueForOldProperty.setValue("newValueForOldProperty");
		
		PropertyUpdate puRemoveOldPropertyToBeRemoved = new PropertyUpdate();
		puRemoveOldPropertyToBeRemoved.setFullNodeId(node.getNodeUri());
		puRemoveOldPropertyToBeRemoved.setType("UPDATED");
		puRemoveOldPropertyToBeRemoved.setIsUnset(true);
		puRemoveOldPropertyToBeRemoved.setKey("oldPropertyToBeRemoved");
		
		List<Update> updates = new ArrayList<Update>();
		updates.add(puNewProperty);
		updates.add(puNewValueForOldProperty);
		updates.add(puRemoveOldPropertyToBeRemoved);
		
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "processUpdates", updates);
		
		NativeObject afterUpdateProperties = (NativeObject) node.getProperties();
		// after update the node will have the right properties
		assertTrue(sameMap(afterUpdateProperties, newProperties));
		
		// verify listeners
		verify(listener).nodeUpdated(node, "oldProperty", "oldValueForOldProperty", "newValueForOldProperty");
		verify(listener).nodeUpdated(node, "newProperty", null, "valueForNewProperty");
	}
	
	/** 
	 * @author Elena Posea
	 * node found, child added
	 */
	@Test
	public void onUpdateAddChild() throws Exception {
		ClientNode node = JSClientJavaTestUtils.createClientNode("node");
		
		// create nodeRegistryManager
		Scriptable nodeRegistryManager = ctx.newObject(scope, "NodeRegistryManager", new Object[] {
				new JavaHostResourceOperationsHandler(), 
				new RecordingServiceInvocator(),
				new JavaHostInvocator()});
		scope.put("_nodeRegistryManager", scope, nodeRegistryManager);
		
		// create nodeRegistry
		Scriptable nodeRegistry = (Scriptable) ctx.evaluateString(scope, "_nodeRegistryManager.createNodeRegistry();", null, 1, null);
		// register node
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "registerNode", node, null, -1);
		
		INodeChangeListener listener = mock(INodeChangeListener.class);
		// add NodeChangedListener in nodeRegistry
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "addNodeChangeListener", listener);
		
		// try to apply updates to this node (propertyUpdates)
		ChildrenUpdate childrenUpdateAdd = new ChildrenUpdate();
		childrenUpdateAdd.setFullNodeId(node.getNodeUri());
		childrenUpdateAdd.setType("ADDED");
		ClientNode targetNodeFirstChild = JSClientJavaTestUtils.createClientNode("targetNode1");
		childrenUpdateAdd.setTargetNode(targetNodeFirstChild);
		
		PropertyUpdate sendChildrenFlag = new PropertyUpdate();
		sendChildrenFlag.setFullNodeId(node.getNodeUri());
		sendChildrenFlag.setType("UPDATED");
		sendChildrenFlag.setIsUnset(false);
		sendChildrenFlag.setValue(true);
		sendChildrenFlag.setKey("hasChildren");
		
		List<Update> updates = new ArrayList<Update>();
		updates.add(sendChildrenFlag);
		updates.add(childrenUpdateAdd);
		
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "processUpdates", updates);
		
		// verify listeners: for first child it should be added on server, but not expanded
		verify(listener, never()).nodeAdded(targetNodeFirstChild);
		// it should also set hasChildren on true/ send updates 
		verify(listener).nodeUpdated(node, "hasChildren", null, true);
		
		// register that node, and then add another one (which won't be the first)
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "registerNode", targetNodeFirstChild, node, -1);
		
		ClientNode targetNodeSecondChild = JSClientJavaTestUtils.createClientNode("targetNode2");
		childrenUpdateAdd.setTargetNode(targetNodeSecondChild);
		
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "processUpdates", updates);
		
		// this time, this second node should be added on client
		verify(listener).nodeAdded(targetNodeSecondChild);
	}
	
	/**
	 * @author Elena Posea
	 * node found, child removed
	 * one node, 2 registered children on it; when the first one is removed, we expect nodeRemoved to be called once, and we expect no nodeUpdated
	 * on the other hand, when the second one is removed, we expect also a nodeUpdated for hasChildren and false to be sent 
	 */
	@Test
	public void onUpdateRemoveChild() throws Exception {
		ClientNode node = JSClientJavaTestUtils.createClientNode("node");
		ClientNode firstChild = JSClientJavaTestUtils.createClientNode("firstChild");
		ClientNode secondChild = JSClientJavaTestUtils.createClientNode("secondChild");
		
		// create nodeRegistryManager
		Scriptable nodeRegistryManager = ctx.newObject(scope, "NodeRegistryManager", new Object[] {
				new JavaHostResourceOperationsHandler(), 
				new RecordingServiceInvocator(),
				new JavaHostInvocator()});
		scope.put("_nodeRegistryManager", scope, nodeRegistryManager);
		
		// create nodeRegistry
		Scriptable nodeRegistry = (Scriptable) ctx.evaluateString(scope, "_nodeRegistryManager.createNodeRegistry();", null, 1, null);
		// register node
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "registerNode", node, null, -1);
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "registerNode", firstChild, node, -1);
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "registerNode", secondChild, node, -1);
		
		INodeChangeListener listener = mock(INodeChangeListener.class);
		// add NodeChangedListener in nodeRegistry
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "addNodeChangeListener", listener);
		
		// try to apply updates to this node (propertyUpdates)
		ChildrenUpdate childrenUpdateRemove1 = new ChildrenUpdate();
		childrenUpdateRemove1.setFullNodeId(node.getNodeUri());
		childrenUpdateRemove1.setType("REMOVED");
		childrenUpdateRemove1.setTargetNode(firstChild);
		
		ChildrenUpdate childrenUpdateRemove2 = new ChildrenUpdate();
		childrenUpdateRemove2.setFullNodeId(node.getNodeUri());
		childrenUpdateRemove2.setType("REMOVED");
		childrenUpdateRemove2.setTargetNode(secondChild);
		
		PropertyUpdate sendChildrenFlag = new PropertyUpdate();
		sendChildrenFlag.setFullNodeId(node.getNodeUri());
		sendChildrenFlag.setType("UPDATED");
		sendChildrenFlag.setIsUnset(false);
		sendChildrenFlag.setValue(false);
		sendChildrenFlag.setKey("hasChildren"); 
		// TODO: cum facem legat de flag? in mod normal serverul are grija ca daca trimite un update pt sters ultimul/adaugat primul copil,
		// sa mai trimita un update pt flag; il mai trimit e artificial in teste, cat sa subliniez faptul ca ar trebui trimis separat de catre server?
		
		List<Update> updates = new ArrayList<Update>();
		// this update we send here in tests by hand; in real code, this update is automatically send by server when it send an remove update for the last child
		updates.add(sendChildrenFlag);
		updates.add(childrenUpdateRemove2);
		updates.add(childrenUpdateRemove1);
		
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "processUpdates", updates);
		
		// expected behavior for the first child: - remove child
		verify(listener).nodeRemoved(firstChild);
		// - don't send any updates for hasChildren flag; it was not the last child
		
		// expected behavior for the last child: - remove the last child
		verify(listener).nodeRemoved(secondChild);
		
		// send an update for hasChildren flag: it should be now on false
		verify(listener).nodeUpdated(node, "hasChildren", null, false);
		
	}
}

/**
 * 
 * @author Elena Posea
 *
 * @param <T>
 */
//class MyMatcher<T> extends BaseMatcher<T> {
//		public Node child1;
//		
//		public void setChild(Node child) {
//			child1 = child;
//		}
//		
//		@Override
//		public void describeTo(Description description) {
//			// nothing to do
//		}
//		
//		@Override
//		public boolean matches(Object item) {
//			// an instance of Node that is not the same instance as child1 but has the same uri
//			if (item instanceof Node) {
//				Node realItem = (Node) item;
//				return child1 != realItem && child1.getNodeUri().equals(realItem.getNodeUri());
//			 }
//			 return false;
//		};
//}
