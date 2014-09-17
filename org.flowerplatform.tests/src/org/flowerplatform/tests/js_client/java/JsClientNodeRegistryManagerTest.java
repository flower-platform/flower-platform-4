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
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.flowerplatform.core.node.remote.SubscriptionInfo;
import org.flowerplatform.js_client.java.JsClientJavaUtils;
import org.flowerplatform.js_client.java.node.ClientNode;
import org.flowerplatform.js_client.java.node.INodeChangeListener;
import org.flowerplatform.js_client.java.node.JavaHostInvocator;
import org.flowerplatform.js_client.java.node.JavaHostResourceOperationsHandler;
import org.flowerplatform.tests.js_client.java.JSClientJavaTestUtils.RecordingServiceInvocator;
import org.flowerplatform.util.Pair;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;

/**
 * @author Elena Posea
 */
public class JsClientNodeRegistryManagerTest {
	
	private static Context ctx;
	private static Scriptable scope;
	
	/**
	 * @author see class
	 * @throws Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ctx = Context.enter();
		scope = ctx.initStandardObjects();	
		ctx.evaluateReader(scope, Files.newBufferedReader(Paths.get(FileUtils.getFile("src/")
				.getAbsolutePath() + "/../../org.flowerplatform.js_client.core/WebContent/js/node_registry/NodeRegistry.js"), StandardCharsets.UTF_8), null, 1, null);
		ctx.evaluateReader(scope, Files.newBufferedReader(Paths.get(FileUtils.getFile("src/")
				.getAbsolutePath() + "/../../org.flowerplatform.js_client.core/WebContent/js/node_registry/ResourceOperationsManager.js"), StandardCharsets.UTF_8), null, 1, null);
		ctx.evaluateReader(scope, Files.newBufferedReader(Paths.get(FileUtils.getFile("src/")
				.getAbsolutePath() + "/../../org.flowerplatform.js_client.core/WebContent/js/node_registry/NodeRegistryManager.js"), StandardCharsets.UTF_8), null, 1, null);		
	}
	
	/**
	 * @author see class
	 * @throws Exception
	 */
	@AfterClass
	public static void setUpAfterClass() throws Exception {
		Context.exit();
	}

	/**
	 * test client code that get executed while expanding a node
	 * @throws Exception
	 */
	@Test
	public void expand() throws Exception {
		
		// node null, or node not null and no autoSubscribeOnExpand flag, or node not null and autoSubscribeOnExpand flag on false
		ClientNode resourceNode1 = JSClientJavaTestUtils.createResourceClientNode("resourceNode1");
		NativeObject properties1 = new NativeObject(); // it's something like a Map<,>, but gets translated in JavaScript
		resourceNode1.setProperties(properties1);
		
		ClientNode resourceNode2 = JSClientJavaTestUtils.createResourceClientNode("resourceNode2");
		NativeObject properties2 = new NativeObject(); // it's something like a Map<,>, but gets translated in JavaScript
		properties2.put("autoSubscribeOnExpand", properties2, false);
		resourceNode1.setProperties(properties2);
		
		ClientNode resourceNode3 = JSClientJavaTestUtils.createResourceClientNode("resourceNode3");
		
		ClientNode child1 = JSClientJavaTestUtils.createClientNode("child1");
		ClientNode child2 = JSClientJavaTestUtils.createClientNode("child2");
		ClientNode child3 = JSClientJavaTestUtils.createClientNode("child3");
		
		List<ClientNode> getChildrenResponseList1 = new ArrayList<ClientNode>();
		getChildrenResponseList1.add(child1);
		
		List<ClientNode> getChildrenResponseList2 = new ArrayList<ClientNode>();
		getChildrenResponseList2.add(child2);
		
		List<ClientNode> getChildrenResponseList3 = new ArrayList<ClientNode>();
		getChildrenResponseList3.add(child3);
		
		SubscriptionInfo subcriptionInfoResponse = new SubscriptionInfo();
		ClientNode resourceNodeToSubscribeTo = JSClientJavaTestUtils.createClientNode("clientNodeToSubscribeTo");

		NativeObject propertiesDirty = new NativeObject();
		propertiesDirty.put("isDirty", propertiesDirty, false);
		resourceNodeToSubscribeTo.setProperties(propertiesDirty);
		
		subcriptionInfoResponse.setResourceNode(resourceNodeToSubscribeTo);
		subcriptionInfoResponse.setRootNode(resourceNode3);
		subcriptionInfoResponse.setResourceSet("myTestResourceSet");
		
		// create nodeRegistryManager
		Scriptable nodeRegistryManager = ctx.newObject(scope, "NodeRegistryManager", new Object[] {
				new JavaHostResourceOperationsHandler(), 
				new RecordingServiceInvocator()
				.setExpectedResults(new Object[] {getChildrenResponseList1, getChildrenResponseList2, subcriptionInfoResponse, getChildrenResponseList3}),
				new JavaHostInvocator()});
		scope.put("_nodeRegistryManager", scope, nodeRegistryManager);
		
		// create nodeRegistry
		Scriptable nodeRegistry = (Scriptable) ctx.evaluateString(scope, "_nodeRegistryManager.createNodeRegistry();", null, 1, null);
		INodeChangeListener listener = mock(INodeChangeListener.class);
		// add NodeChangedListener in nodeRegistry
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "addNodeChangeListener", listener);
		
		
		// register all nodes
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "registerNode", resourceNode1, null, -1);
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "registerNode", resourceNode2, null, -1);
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "registerNode", resourceNode3, null, -1);
		
		
		// expand first node
//		TODO: daca nodul e null, desi e luat cazul in nodeRegistryManager.expand, nu e luat ºi în nodeRegistry; acolo acceseaza direct NodeUri si crapa 
//		JsClientJavaUtils.invokeJsFunction(nodeRegistryManager, "expand", nodeRegistry, null, null); // invoke on null node
		JsClientJavaUtils.invokeJsFunction(nodeRegistryManager, "expand", nodeRegistry, resourceNode1, null);
		JsClientJavaUtils.invokeJsFunction(nodeRegistryManager, "expand", nodeRegistry, resourceNode2, null);
		
		// it should just expand
		assertNotNull(resourceNode1.getChildren());
		assertEquals(1, resourceNode1.getChildren().size());
		
		assertNotNull(resourceNode2.getChildren());
		assertEquals(1, resourceNode2.getChildren().size());
		
		NativeObject properties3 = new NativeObject(); // it's something like a Map<,>, but gets translated in JavaScript
		properties3.put("autoSubscribeOnExpand", properties3, true);
		
		List<Pair<String, String>> subscribableResources = new ArrayList<Pair<String, String>>();
		Pair<String, String> subscribableResource = new Pair<String, String>("fpp:user/repo|resourceFileUri", "mindmap");
		subscribableResources.add(0, subscribableResource);
		properties3.put("subscribableResources", properties3, subscribableResources);
		resourceNode3.setProperties(properties3);
		JsClientJavaUtils.invokeJsFunction(nodeRegistryManager, "expand", nodeRegistry, resourceNode3, null);
		
		// test if the resource node is subscribed; if it has 3 children => is is successfully subscribed 
		assertNotNull(resourceNode3.getChildren());
		assertEquals(child3, resourceNode3.getChildren().get(0));
		verify(listener).nodeAdded(child1);
		verify(listener).nodeAdded(child2);
		verify(listener).nodeAdded(child3);
		// test if linked to node registry
		NativeArray resourceUris =  (NativeArray) (JsClientJavaUtils.invokeJsFunction(nodeRegistryManager, "getResourceUrisForResourceSet", "myTestResourceSet"));
		assertTrue(resourceUris.get(0).equals("scheme:user/repo|clientNodeToSubscribeTo"));
		
		NativeJavaObject resourceSet =  (NativeJavaObject) 
				(JsClientJavaUtils.invokeJsFunction(nodeRegistryManager, "getResourceSetForResourceUri", "scheme:user/repo|clientNodeToSubscribeTo"));
		String resourceSetAsString = (String) resourceSet.unwrap();
		assertTrue(resourceSetAsString.equals("myTestResourceSet"));
	}
	
	/**
	 * tests client code that gets executed when collapsing a node that doesn't have any change 
	 * @throws Exception
	 */
	@Test
	public void collapseNotDirtyResourceNode() throws Exception {
		// create resource node and expand it; then collapse it
		ClientNode resourceNode1 = JSClientJavaTestUtils.createResourceClientNode("resourceNode1");
		ClientNode resourceNode2 = JSClientJavaTestUtils.createResourceClientNode("resourceNode2");
		
		ClientNode child1 = JSClientJavaTestUtils.createClientNode("child1");
		ClientNode child2 = JSClientJavaTestUtils.createClientNode("child2");
		ClientNode child3 = JSClientJavaTestUtils.createClientNode("child3");
		
		List<ClientNode> getChildrenResponseList1 = new ArrayList<ClientNode>();
		getChildrenResponseList1.add(child1);
		getChildrenResponseList1.add(child2);

		List<ClientNode> getChildrenResponseList2 = new ArrayList<ClientNode>();
		getChildrenResponseList2.add(child3);
		
		SubscriptionInfo subscriptionInfoResponse1 = new SubscriptionInfo();
		SubscriptionInfo subscriptionInfoResponse2 = new SubscriptionInfo();
		ClientNode resourceFileNode1 = JSClientJavaTestUtils.createClientNode("resourceFileNode1");
		ClientNode resourceFileNode2 = JSClientJavaTestUtils.createClientNode("resourceFileNode2");
		NativeObject propertyDirty = new NativeObject();
		propertyDirty.put("isDirty", propertyDirty, false);
		resourceFileNode1.setProperties(propertyDirty);
		resourceFileNode2.setProperties(propertyDirty);

		subscriptionInfoResponse1.setResourceNode(resourceFileNode1);
		subscriptionInfoResponse1.setRootNode(resourceNode1);
		subscriptionInfoResponse1.setResourceSet("myTestResourceSet");

		subscriptionInfoResponse2.setResourceNode(resourceFileNode2);
		subscriptionInfoResponse2.setRootNode(resourceNode2);
		subscriptionInfoResponse2.setResourceSet("myTestResourceSet");

		// create nodeRegistryManager
		Scriptable nodeRegistryManager = ctx.newObject(scope, "NodeRegistryManager", new Object[] {
				new JavaHostResourceOperationsHandler(), 
				new RecordingServiceInvocator()
				.setExpectedResults(new Object[] {subscriptionInfoResponse1, getChildrenResponseList1, subscriptionInfoResponse2, getChildrenResponseList2}),
				new JavaHostInvocator()});
		scope.put("_nodeRegistryManager", scope, nodeRegistryManager);
		
		// create nodeRegistry
		Scriptable nodeRegistry = (Scriptable) ctx.evaluateString(scope, "_nodeRegistryManager.createNodeRegistry();", null, 1, null);
		INodeChangeListener listener = mock(INodeChangeListener.class);
		// add NodeChangedListener in nodeRegistry
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "addNodeChangeListener", listener);
		
		// register resourceNode1 and 2 
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "registerNode", resourceNode1, null, -1);
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "registerNode", resourceNode2, null, -1);
		
		NativeObject resourceNodeProperties1 = new NativeObject(); // it's something like a Map<,>, but gets translated in JavaScript
		resourceNodeProperties1.put("autoSubscribeOnExpand", resourceNodeProperties1, true);
		List<Pair<String, String>> subscribableResources1 = new ArrayList<Pair<String, String>>();
		Pair<String, String> subscribableResource1 = new Pair<String, String>("scheme:user/repo|resourceFileNode1", "mindmap");
		// register resourceFileNode1
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "registerNode", resourceFileNode1, null, -1);
		subscribableResources1.add(subscribableResource1);
		resourceNodeProperties1.put("subscribableResources", resourceNodeProperties1, subscribableResources1);
		resourceNode1.setProperties(resourceNodeProperties1);
		JsClientJavaUtils.invokeJsFunction(nodeRegistryManager, "expand", nodeRegistry, resourceNode1, null);

		// same thing for resourceNode2
		NativeObject resourceNodeProperties2 = new NativeObject();
		resourceNodeProperties2.put("autoSubscribeOnExpand", resourceNodeProperties2, true);
		List<Pair<String, String>> subscribableResources2 = new ArrayList<Pair<String, String>>();
		Pair<String, String> subscribableResource2 = new Pair<String, String>("scheme:user/repo|resourceFileNode2", "mindmap");
		// register resourceFileNode2
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "registerNode", resourceFileNode2, null, -1);
		subscribableResources2.add(subscribableResource2);
		resourceNodeProperties2.put("subscribableResources", resourceNodeProperties2, subscribableResources2);
		resourceNode2.setProperties(resourceNodeProperties2);
		JsClientJavaUtils.invokeJsFunction(nodeRegistryManager, "expand", nodeRegistry, resourceNode2, null);
		
		// test if the resource node is subscribed; if it has child 3 a children => is is successfully subscribed 
		assertNotNull(resourceNode1.getChildren());
		assertEquals(child1, resourceNode1.getChildren().get(0));
		assertEquals(child2, resourceNode1.getChildren().get(1));
		verify(listener).nodeAdded(child1);
		verify(listener).nodeAdded(child2);
		
		// test if linked to node registry
		NativeArray resourceUris =  (NativeArray) (JsClientJavaUtils.invokeJsFunction(nodeRegistryManager, "getResourceUrisForResourceSet", "myTestResourceSet"));
		assertEquals(2, resourceUris.size());
		assertEquals("scheme:user/repo|resourceFileNode1", resourceUris.get(0));
		assertEquals("scheme:user/repo|resourceFileNode2", resourceUris.get(1));
		
		NativeJavaObject resourceSet =  (NativeJavaObject) 
				(JsClientJavaUtils.invokeJsFunction(nodeRegistryManager, "getResourceSetForResourceUri", "scheme:user/repo|resourceFileNode1"));
		String resourceSetAsString = (String) resourceSet.unwrap();
		assertTrue(resourceSetAsString.equals("myTestResourceSet"));
		
		// everything is successfully subscribed; now let's test if we can unsubscribe/collapse
		
		JsClientJavaUtils.invokeJsFunction(nodeRegistryManager, "collapse", nodeRegistry, resourceNode1);
		// check for: unlink from node registry; you should unlink the first one
		resourceUris =  (NativeArray) (JsClientJavaUtils.invokeJsFunction(nodeRegistryManager, "getResourceUrisForResourceSet", "myTestResourceSet"));
		assertEquals(1, resourceUris.size());
		assertEquals("scheme:user/repo|resourceFileNode2", resourceUris.get(0));
		// assert the nodes were removed from map
		verify(listener).nodeRemoved(child1);
		verify(listener).nodeRemoved(child2);
		
		// after you unlink the last one, the whole set should be removed
		verify(listener, never()).nodeRemoved(child3);
		JsClientJavaUtils.invokeJsFunction(nodeRegistryManager, "collapse", nodeRegistry, resourceNode2);
		// check for: unlink from node registry; you should unlink the last one
		Object nullResourceUris =  JsClientJavaUtils.invokeJsFunction(nodeRegistryManager, "getResourceUrisForResourceSet", "myTestResourceSet");
		assertNull(nullResourceUris);
		verify(listener).nodeRemoved(child3);
	}

	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void getAllDirtyResourceSetsTest() throws Exception {
		// create a dirty and a clean resource node
		// try to run getAllDirtyResourceSets to see if it returns the right resourceSet
		ClientNode dirtyResourceNode = JSClientJavaTestUtils.createResourceClientNode("dirtyResourceNode");
		ClientNode cleanResourceNode = JSClientJavaTestUtils.createResourceClientNode("cleanResourceNode");
		
		SubscriptionInfo dirtyResourceSubscriptionInfoResponse = new SubscriptionInfo();
		ClientNode dirtyResourceFileNode = JSClientJavaTestUtils.createClientNode("dirtyResourceFileNode");
		NativeObject propertyDirty = new NativeObject();
		propertyDirty.put("isDirty", propertyDirty, true);
		dirtyResourceFileNode.setProperties(propertyDirty);
		dirtyResourceSubscriptionInfoResponse.setResourceNode(dirtyResourceFileNode);
		dirtyResourceSubscriptionInfoResponse.setRootNode(dirtyResourceNode);
		dirtyResourceSubscriptionInfoResponse.setResourceSet("dirtyResourceSet");

		SubscriptionInfo cleanResourceSubscriptionInfoResponse = new SubscriptionInfo();
		ClientNode cleanResourceFileNode = JSClientJavaTestUtils.createClientNode("cleanResourceFileNode");
		NativeObject propertyClean = new NativeObject();
		propertyClean.put("isDirty", propertyClean, false);
		cleanResourceFileNode.setProperties(propertyClean);
		cleanResourceSubscriptionInfoResponse.setResourceNode(cleanResourceFileNode);
		cleanResourceSubscriptionInfoResponse.setRootNode(cleanResourceNode);
		cleanResourceSubscriptionInfoResponse.setResourceSet("cleanResourceSet");
		
		// create nodeRegistryManager
		Scriptable nodeRegistryManager = ctx.newObject(scope, "NodeRegistryManager", new Object[] {
				new JavaHostResourceOperationsHandler(), 
				new RecordingServiceInvocator().setExpectedResults(new Object[] {dirtyResourceSubscriptionInfoResponse, null, cleanResourceSubscriptionInfoResponse, null}),
				new JavaHostInvocator()});
		scope.put("_nodeRegistryManager", scope, nodeRegistryManager);
		
		// create nodeRegistry
		Scriptable nodeRegistry = (Scriptable) ctx.evaluateString(scope, "_nodeRegistryManager.createNodeRegistry();", null, 1, null);
		
		// register resourceNode 
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "registerNode", dirtyResourceNode, null, -1);
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "registerNode", cleanResourceNode, null, -1);
		
		NativeObject dirtyResourceNodeProperties = new NativeObject(); // it's something like a Map<,>, but gets translated in JavaScript
		dirtyResourceNodeProperties.put("autoSubscribeOnExpand", dirtyResourceNodeProperties, true);
		List<Pair<String, String>> dirtySubscribableResources = new ArrayList<Pair<String, String>>();
		Pair<String, String> dirtySubscribableResource = new Pair<String, String>("scheme:user/repo|dirtyResourceFileNode", "mindmap");
		// register dirtyResourceFileNode
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "registerNode", dirtyResourceFileNode, null, -1);
		dirtySubscribableResources.add(dirtySubscribableResource);
		dirtyResourceNodeProperties.put("subscribableResources", dirtyResourceNodeProperties, dirtySubscribableResources);
		dirtyResourceNode.setProperties(dirtyResourceNodeProperties);
		JsClientJavaUtils.invokeJsFunction(nodeRegistryManager, "expand", nodeRegistry, dirtyResourceNode, null);

		NativeObject cleanResourceNodeProperties = new NativeObject(); // it's something like a Map<,>, but gets translated in JavaScript
		cleanResourceNodeProperties.put("autoSubscribeOnExpand", cleanResourceNodeProperties, true);
		List<Pair<String, String>> cleanSubscribableResources = new ArrayList<Pair<String, String>>();
		Pair<String, String> cleanSubscribableResource = new Pair<String, String>("scheme:user/repo|cleanResourceFileNode", "mindmap");
		// register cleanResourceFileNode
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "registerNode", cleanResourceFileNode, null, -1);
		cleanSubscribableResources.add(cleanSubscribableResource);
		cleanResourceNodeProperties.put("subscribableResources", cleanResourceNodeProperties, cleanSubscribableResources);
		cleanResourceNode.setProperties(cleanResourceNodeProperties);
		JsClientJavaUtils.invokeJsFunction(nodeRegistryManager, "expand", nodeRegistry, cleanResourceNode, null);
		
		NativeJavaObject resourceSet =  (NativeJavaObject)
				(JsClientJavaUtils.invokeJsFunction(nodeRegistryManager, "getResourceSetForResourceUri", "scheme:user/repo|dirtyResourceFileNode"));
		String resourceSetAsString = (String) resourceSet.unwrap();
		assertEquals("dirtyResourceSet", resourceSetAsString);
		
		resourceSet =  (NativeJavaObject) 
				(JsClientJavaUtils.invokeJsFunction(nodeRegistryManager, "getResourceSetForResourceUri", "scheme:user/repo|cleanResourceFileNode"));
		resourceSetAsString = (String) resourceSet.unwrap();
		assertEquals("cleanResourceSet", resourceSetAsString);
		
		// test if one the dirty one is returned 
		NativeArray resourceUris =  (NativeArray) (JsClientJavaUtils.invokeJsFunction(nodeRegistryManager, "getAllDirtyResourceSets", true, null));
		assertEquals(1, resourceUris.size());
		assertEquals("dirtyResourceSet", resourceUris.get(0));
		
	}

}
