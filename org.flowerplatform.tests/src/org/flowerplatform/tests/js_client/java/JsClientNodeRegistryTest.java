package org.flowerplatform.tests.js_client.java;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.flowerplatform.core.node.update.remote.PropertyUpdate;
import org.flowerplatform.core.node.update.remote.Update;
import org.flowerplatform.js_client.java.ClientNode;
import org.flowerplatform.js_client.java.ClientNodeWithChildren;
import org.flowerplatform.js_client.java.INodeChangeListener;
import org.flowerplatform.js_client.java.JsClientJavaUtils;
import org.flowerplatform.js_client.java.JsExternalInvocator;
import org.flowerplatform.js_client.java.JsList;
import org.flowerplatform.js_client.java.JsResourceOperationsHandler;
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
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ctx = Context.enter();
		scope = ctx.initStandardObjects();	
		ctx.evaluateReader(scope, Files.newBufferedReader(Paths.get(FileUtils.getFile("src/").getAbsolutePath() + "/../../org.flowerplatform.js_client.core/WebContent/js/node_registry/NodeRegistry.js"), StandardCharsets.UTF_8), null, 1, null);
		ctx.evaluateReader(scope, Files.newBufferedReader(Paths.get(FileUtils.getFile("src/").getAbsolutePath() + "/../../org.flowerplatform.js_client.core/WebContent/js/node_registry/ResourceOperationsManager.js"), StandardCharsets.UTF_8), null, 1, null);
		ctx.evaluateReader(scope, Files.newBufferedReader(Paths.get(FileUtils.getFile("src/").getAbsolutePath() + "/../../org.flowerplatform.js_client.core/WebContent/js/node_registry/NodeRegistryManager.js"), StandardCharsets.UTF_8), null, 1, null);		
	}
	
	@AfterClass
	public static void setUpAfterClass() throws Exception {
		Context.exit();
	}
		
	@Test
	public void expand() throws Exception {
		ClientNode parent = JSClientJavaTestUtils.createClientNode("parent");
		ClientNode child1 = JSClientJavaTestUtils.createClientNode("child1");
		ClientNode child2 = JSClientJavaTestUtils.createClientNode("child2");
		ClientNode child3 = JSClientJavaTestUtils.createClientNode("child3");
				
		JsList<ClientNode> list = new JsList<ClientNode>();
		list.add(child1);
		list.add(child2);
		list.add(child3);
		
		// create nodeRegistryManager
		Scriptable nodeRegistryManager = ctx.newObject(scope, "NodeRegistryManager", new Object[] {
				new JsResourceOperationsHandler(), 
				new RecordingServiceInvocator().setExpectedResults(new Object[] {list}), 
				new JsExternalInvocator()});
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
		assertEquals(3, parent.getChildren().length);
		
		verify(listener).nodeAdded(child1);
		verify(listener).nodeAdded(child2);
		verify(listener).nodeAdded(child3);			
	}
	
	@Test
	public void register() throws Exception {
		ClientNode parent = JSClientJavaTestUtils.createClientNode("parent");
		ClientNode child = JSClientJavaTestUtils.createClientNode("child");
				
		// create nodeRegistryManager
		Scriptable nodeRegistryManager = ctx.newObject(scope, "NodeRegistryManager", new Object[] {
				new JsResourceOperationsHandler(), 
				new RecordingServiceInvocator(), 
				new JsExternalInvocator()});
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
		assertEquals(1, parent.getChildren().length);
		
		// verify getNodeById is not null/ node is registered
		assertNotNull(JsClientJavaUtils.invokeJsFunction(nodeRegistry, "getNodeById", child.getNodeUri()));
	}
	
	@Test
	public void unregister() throws Exception {
		ClientNode grandParent = JSClientJavaTestUtils.createClientNode("grandParent");
		ClientNode parent = JSClientJavaTestUtils.createClientNode("parent");
		ClientNode child1 = JSClientJavaTestUtils.createClientNode("child1");
		ClientNode child2 = JSClientJavaTestUtils.createClientNode("child2");
				
		// create nodeRegistryManager
		Scriptable nodeRegistryManager = ctx.newObject(scope, "NodeRegistryManager", new Object[] {
				new JsResourceOperationsHandler(), 
				new RecordingServiceInvocator(), 
				new JsExternalInvocator()});
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

	@Test
	public void collapse() throws Exception {
		ClientNode grandParent = JSClientJavaTestUtils.createClientNode("grandParent");
		ClientNode parent = JSClientJavaTestUtils.createClientNode("parent");
		ClientNode child1 = JSClientJavaTestUtils.createClientNode("child1");
		ClientNode child2 = JSClientJavaTestUtils.createClientNode("child2");
				
		// create nodeRegistryManager
		Scriptable nodeRegistryManager = ctx.newObject(scope, "NodeRegistryManager", new Object[] {
				new JsResourceOperationsHandler(), 
				new RecordingServiceInvocator(), 
				new JsExternalInvocator()});
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
	 *  new node is only different in what it concerns its properties
	 * @throws Exception
	 */
	@Test
	public void refreshProperties() throws Exception {
		ClientNode node = JSClientJavaTestUtils.createClientNode("node");
		ClientNodeWithChildren newNodeWithChildren = JSClientJavaTestUtils.createClientNodeWithChildren("newNodeWithChildren");
		
		NativeObject oldProperties = new NativeObject(); 
		oldProperties.put("propertyA", oldProperties, "valueA"); // it's something like a Map<,>, but gets translated in JavaScript
		oldProperties.put("propertyB", oldProperties, "valueB");
		node.setProperties(oldProperties);

		NativeObject newProperties = new NativeObject();
		newProperties.put("propertyA", newProperties, "newValueA");
		newProperties.put("propertyC", newProperties, "valueC");
		newNodeWithChildren.getNode().setProperties(newProperties);
		
		// create nodeRegistryManager
		Scriptable nodeRegistryManager = ctx.newObject(scope, "NodeRegistryManager", new Object[] {
				new JsResourceOperationsHandler(), 
				new RecordingServiceInvocator().setExpectedResults(new Object[] {newNodeWithChildren}), 
				new JsExternalInvocator()});
		scope.put("_nodeRegistryManager", scope, nodeRegistryManager);
					
		// create nodeRegistry
		Scriptable nodeRegistry = (Scriptable) ctx.evaluateString(scope, "_nodeRegistryManager.createNodeRegistry();", null, 1, null);

		// register node
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "registerNode", node, null, -1);	
		
		// try to refresh this node
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "refresh", node);
		// assertEquals((NativeObject)node.getProperties(), newProperties);
		
		NativeObject afterRefreshProperties = (NativeObject) node.getProperties();
		assertTrue("properties not changed properly", afterRefreshProperties != null && sameMap(afterRefreshProperties, newProperties));
	}

	/**
	 *  new node doesn't have any children => collapse
	 * @throws Exception
	 */
	@Test
	public void refreshCollapseIfNoChildren() throws Exception {

		ClientNode node = JSClientJavaTestUtils.createClientNode("node");
		ClientNode child1 = JSClientJavaTestUtils.createClientNode("child1");
		ClientNode child2 = JSClientJavaTestUtils.createClientNode("child2");
		ClientNodeWithChildren newNodeWithChildren = JSClientJavaTestUtils.createClientNodeWithChildren("newNodeWithChildren");
		
		
		// create nodeRegistryManager
		Scriptable nodeRegistryManager = ctx.newObject(scope, "NodeRegistryManager", new Object[] {
				new JsResourceOperationsHandler(), 
				new RecordingServiceInvocator().setExpectedResults(new Object[] {newNodeWithChildren}), 
				new JsExternalInvocator()});
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

	private boolean sameMap(Map<String, Object> properties, Map<String, Object> newProperties) {
		for (Object key: properties.keySet()) {
			if (!newProperties.containsKey(key)) {
				return false;
			}else{
				if(!newProperties.get(key).equals(properties.get(key))){
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
	 *  new node has a different list of nodes than node.
	 *  if you find the same node with the same index => keep it
	 *  if you find same node with different index => remove/unregister it and register again with new index
	 *  if you find new node, add it  
	 * @throws Exception
	 */
	@Test
	public void refreshDifferentListOfChildren() throws Exception {

		ClientNode node = JSClientJavaTestUtils.createClientNode("node");
		ClientNode child0 = JSClientJavaTestUtils.createClientNode("child0");
		ClientNode child1 = JSClientJavaTestUtils.createClientNode("child1");
		ClientNode child2 = JSClientJavaTestUtils.createClientNode("child2");
		
		ClientNodeWithChildren newNodeWithChildren = JSClientJavaTestUtils.createClientNodeWithChildren("newNodeWithChildren");
		ClientNodeWithChildren newChild3 = JSClientJavaTestUtils.createClientNodeWithChildren("child3");
		ClientNodeWithChildren newChild0 = JSClientJavaTestUtils.createClientNodeWithChildren("child0");
		ClientNodeWithChildren newChild1 = JSClientJavaTestUtils.createClientNodeWithChildren("child1");

		JsList<ClientNodeWithChildren> children = new JsList<ClientNodeWithChildren>();
		children.add(newChild3);
		children.add(newChild0);
		children.add(newChild1); // different index than before (was 1, now it's 2)
		children.set(1, newChild3);
		children.set(0, newChild0);
		children.set(2, newChild1);
		newNodeWithChildren.setChildren(children); 
		
		
		// create nodeRegistryManager
		Scriptable nodeRegistryManager = ctx.newObject(scope, "NodeRegistryManager", new Object[] {
				new JsResourceOperationsHandler(), 
				new RecordingServiceInvocator().setExpectedResults(new Object[] {newNodeWithChildren}), 
				new JsExternalInvocator()});
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
		verify(listener, times(0)).nodeAdded(newChild1.getNode());

		JsList<ClientNode> expectedChildren = new JsList<ClientNode>();
		expectedChildren.add(child0); // should preserve this one
		expectedChildren.add(newChild3.getNode());
		expectedChildren.add(child1); // it should keep the old instance, just the index inside the list should be different

		assertEquals("doesn't have the right children after refresh", expectedChildren, node.getChildren());
	}

	/**
	 * no updates to process => call refresh for root
	 * @throws Exception
	 */
//	@Test
	public void onUpdateNoUpdateToProcess() throws Exception {

		ClientNode root = JSClientJavaTestUtils.createClientNode("root");
		ClientNode child1 = JSClientJavaTestUtils.createClientNode("child1");
		ClientNode child2 = JSClientJavaTestUtils.createClientNode("child2");
		
		ClientNodeWithChildren rootWithChildren = JSClientJavaTestUtils.createClientNodeWithChildren("root");
		
		JsList<ClientNodeWithChildren> children = new JsList<ClientNodeWithChildren>();
		ClientNodeWithChildren child1WithChildren = JSClientJavaTestUtils.createClientNodeWithChildren("child1");
		ClientNodeWithChildren child2WithChildren = JSClientJavaTestUtils.createClientNodeWithChildren("child2");
		children.add(child1WithChildren);
		children.add(child2WithChildren);
		
		rootWithChildren.setChildren(children);
		
		// create nodeRegistryManager
		Scriptable nodeRegistryManager = ctx.newObject(scope, "NodeRegistryManager", new Object[] {
				new JsResourceOperationsHandler(), 
				new RecordingServiceInvocator().setExpectedResults(new Object[] {rootWithChildren}), // return this as the refreshed version of root
				new JsExternalInvocator()});
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
		
		// check refresh on root
	}

	/**
	 * node not found => don't process the updates
	 * @throws Exception
	 */
//	@Test
	public void onUpdateIfNodeNotFoundDoNotProcessUpdate() throws Exception {

		ClientNode root = JSClientJavaTestUtils.createClientNode("root");
		ClientNode child1 = JSClientJavaTestUtils.createClientNode("child1");
		ClientNode child2 = JSClientJavaTestUtils.createClientNode("child2");

		ClientNodeWithChildren rootWithChildren = JSClientJavaTestUtils.createClientNodeWithChildren("root");
		
		JsList<ClientNodeWithChildren> children = new JsList<ClientNodeWithChildren>();
		ClientNodeWithChildren child1WithChildren = JSClientJavaTestUtils.createClientNodeWithChildren("child1");
		ClientNodeWithChildren child2WithChildren = JSClientJavaTestUtils.createClientNodeWithChildren("child2");
		children.add(child1WithChildren);
		children.add(child2WithChildren);
		
		rootWithChildren.setChildren(children);

		
		// create nodeRegistryManager
		Scriptable nodeRegistryManager = ctx.newObject(scope, "NodeRegistryManager", new Object[] {
				new JsResourceOperationsHandler(), 
				new RecordingServiceInvocator().setExpectedResults(new Object[]{new PropertyUpdate()}),
				new JsExternalInvocator()});
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
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "processUpdates", root); // try to process one update
		//
	}

	/**
	 * node found, update the property, notify listeners
	 * @throws Exception
	 */
	@Test
	public void onUpdateProperty() throws Exception {
		ClientNode node = JSClientJavaTestUtils.createClientNode("node");

		NativeObject oldProperties = new NativeObject(); 
		oldProperties.put("oldPropertyToBeRemoved", oldProperties, "notImportantValue");
		oldProperties.put("oldProperty", oldProperties, "oldValueForOldProperty"); // it's something like a Map<,>, but gets translated in JavaScript
		node.setProperties(oldProperties);

		NativeObject newProperties = new NativeObject();
		newProperties.put("newProperty", newProperties, "valueForNewProperty");
		newProperties.put("oldProperty", newProperties, "newValueForOldProperty");
		
		// create nodeRegistryManager
		Scriptable nodeRegistryManager = ctx.newObject(scope, "NodeRegistryManager", new Object[] {
				new JsResourceOperationsHandler(), 
				new RecordingServiceInvocator(),
				new JsExternalInvocator()});
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
		
		JsList<Update> updates = new JsList<Update>();
		updates.add(puNewProperty);
		updates.add(puNewValueForOldProperty);
		updates.add(puRemoveOldPropertyToBeRemoved);
		
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "processUpdates", updates); // try to process one update

		NativeObject afterUpdateProperties = (NativeObject) node.getProperties();
		// after update the node will have the right properties
		assertTrue(sameMap(afterUpdateProperties, newProperties));

		// verify listeners
		verify(listener).nodeUpdated(node, "oldProperty", "oldValueForOldProperty", "newValueForOldProperty");
		verify(listener).nodeUpdated(node, "newProperty", null, "valueForNewProperty");
		verify(listener).nodeUpdated(node, "oldPropertyToBeRemoved", "notImportantValue", null);
	}

}
