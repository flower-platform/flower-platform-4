package org.flowerplatform.tests.js_client.java;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.flowerplatform.js_client.java.ClientNode;
import org.flowerplatform.js_client.java.ClientSubscriptionInfo;
import org.flowerplatform.js_client.java.INodeChangeListener;
import org.flowerplatform.js_client.java.JsClientJavaUtils;
import org.flowerplatform.js_client.java.JsExternalInvocator;
import org.flowerplatform.js_client.java.JsList;
import org.flowerplatform.js_client.java.JsResourceOperationsHandler;
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
	
//	@Test
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
		
		JsList<ClientNode> getChildrenResponseList1 = new JsList<ClientNode>();
		getChildrenResponseList1.add(child1);
		
		JsList<ClientNode> getChildrenResponseList2 = new JsList<ClientNode>();
		getChildrenResponseList2.add(child2);
		
		JsList<ClientNode> getChildrenResponseList3 = new JsList<ClientNode>();
		getChildrenResponseList3.add(child3);
		
		ClientSubscriptionInfo subcriptionInfoResponse = new ClientSubscriptionInfo();
		ClientNode resourceNodeToSubscribeTo = JSClientJavaTestUtils.createClientNode("clientNodeToSubscribeTo");

		NativeObject propertiesDirty = new NativeObject();
		propertiesDirty.put("isDirty", propertiesDirty, false);
		resourceNodeToSubscribeTo.setProperties(propertiesDirty);
		
		subcriptionInfoResponse.setResourceNode(resourceNodeToSubscribeTo);
		subcriptionInfoResponse.setRootNode(resourceNode3);
		subcriptionInfoResponse.setResourceSet("myTestResourceSet");;
		
		// create nodeRegistryManager
		Scriptable nodeRegistryManager = ctx.newObject(scope, "NodeRegistryManager", new Object[] {
				new JsResourceOperationsHandler(), 
				new RecordingServiceInvocator().setExpectedResults(new Object[] {getChildrenResponseList1, getChildrenResponseList2, subcriptionInfoResponse, getChildrenResponseList3}),
				new JsExternalInvocator()});
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
		assertEquals(1, resourceNode1.getChildren().length);
		
		assertNotNull(resourceNode2.getChildren());
		assertEquals(1, resourceNode2.getChildren().length);
		
		NativeObject properties3 = new NativeObject(); // it's something like a Map<,>, but gets translated in JavaScript
		properties3.put("autoSubscribeOnExpand", properties3, true);
		
		JsList<Pair<String, String>> subscribableResources = new JsList<Pair<String, String>>();
		Pair<String, String> subscribableResource = new Pair<String, String>("fpp:user/repo|resourceFileUri", "mindmap");
		subscribableResources.add(0, subscribableResource);
		properties3.put("subscribableResources", properties3, subscribableResources);
		resourceNode3.setProperties(properties3);
		JsClientJavaUtils.invokeJsFunction(nodeRegistryManager, "expand", nodeRegistry, resourceNode3, null);
		
		// test if the resource node is subscribed; if it has 3 children => is is successfully subscribed 
		assertNotNull(resourceNode3.getChildren());
		assertEquals(child3, resourceNode3.getChildren().getItemAt(0));
		verify(listener).nodeAdded(child1);
		verify(listener).nodeAdded(child2);
		verify(listener).nodeAdded(child3);
		// test if linked to node registry
		NativeArray resourceUris =  (NativeArray)(JsClientJavaUtils.invokeJsFunction(nodeRegistryManager, "getResourceUrisForResourceSet", "myTestResourceSet"));
		assertTrue(resourceUris.get(0).equals("scheme:user/repo|clientNodeToSubscribeTo"));
		
		NativeJavaObject resourceSet =  (NativeJavaObject)(JsClientJavaUtils.invokeJsFunction(nodeRegistryManager, "getResourceSetForResourceUri", "scheme:user/repo|clientNodeToSubscribeTo"));
		String resourceSetAsString = (String)resourceSet.unwrap();
		assertTrue(resourceSetAsString.equals("myTestResourceSet"));
	}
	
	@Test
	public void collapseNotDirtyResourceNode() throws Exception {
		// create resource node and expand it; then collapse it
		ClientNode resourceNode1 = JSClientJavaTestUtils.createResourceClientNode("resourceNode1");
		ClientNode resourceNode2 = JSClientJavaTestUtils.createResourceClientNode("resourceNode2");
		
		ClientNode child1 = JSClientJavaTestUtils.createClientNode("child1");
		ClientNode child2 = JSClientJavaTestUtils.createClientNode("child2");
		ClientNode child3 = JSClientJavaTestUtils.createClientNode("child3");
		
		JsList<ClientNode> getChildrenResponseList1 = new JsList<ClientNode>();
		getChildrenResponseList1.add(child1);
		getChildrenResponseList1.add(child2);

		JsList<ClientNode> getChildrenResponseList2 = new JsList<ClientNode>();
		getChildrenResponseList2.add(child3);
		
		ClientSubscriptionInfo subscriptionInfoResponse1 = new ClientSubscriptionInfo();
		ClientSubscriptionInfo subscriptionInfoResponse2 = new ClientSubscriptionInfo();
		ClientNode resourceFileNode1 = JSClientJavaTestUtils.createClientNode("resourceFileNode1");
		ClientNode resourceFileNode2 = JSClientJavaTestUtils.createClientNode("resourceFileNode2");
		NativeObject propertyDirty = new NativeObject();
		propertyDirty.put("isDirty", propertyDirty, false);
		resourceFileNode1.setProperties(propertyDirty);
		resourceFileNode2.setProperties(propertyDirty);

		subscriptionInfoResponse1.setResourceNode(resourceFileNode1);
		subscriptionInfoResponse1.setRootNode(resourceNode1);
		subscriptionInfoResponse1.setResourceSet("myTestResourceSet");;

		subscriptionInfoResponse2.setResourceNode(resourceFileNode2);
		subscriptionInfoResponse2.setRootNode(resourceNode2);
		subscriptionInfoResponse2.setResourceSet("myTestResourceSet");;

		// create nodeRegistryManager
		Scriptable nodeRegistryManager = ctx.newObject(scope, "NodeRegistryManager", new Object[] {
				new JsResourceOperationsHandler(), 
				new RecordingServiceInvocator().setExpectedResults(new Object[] {subscriptionInfoResponse1, getChildrenResponseList1, subscriptionInfoResponse2, getChildrenResponseList2}),
				new JsExternalInvocator()});
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
		JsList<Pair<String, String>> subscribableResources1 = new JsList<Pair<String, String>>();
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
		JsList<Pair<String, String>> subscribableResources2 = new JsList<Pair<String, String>>();
		Pair<String, String> subscribableResource2 = new Pair<String, String>("scheme:user/repo|resourceFileNode2", "mindmap");
		// register resourceFileNode2
		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "registerNode", resourceFileNode2, null, -1);
		subscribableResources2.add(subscribableResource2);
		resourceNodeProperties2.put("subscribableResources", resourceNodeProperties2, subscribableResources2);
		resourceNode2.setProperties(resourceNodeProperties2);
		JsClientJavaUtils.invokeJsFunction(nodeRegistryManager, "expand", nodeRegistry, resourceNode2, null);
		
		// test if the resource node is subscribed; if it has child 3 a children => is is successfully subscribed 
		assertNotNull(resourceNode1.getChildren());
		assertEquals(child1, resourceNode1.getChildren().getItemAt(0));
		assertEquals(child2, resourceNode1.getChildren().getItemAt(1));
		verify(listener).nodeAdded(child1);
		verify(listener).nodeAdded(child2);
		
		// test if linked to node registry
		NativeArray resourceUris =  (NativeArray)(JsClientJavaUtils.invokeJsFunction(nodeRegistryManager, "getResourceUrisForResourceSet", "myTestResourceSet"));
		assertEquals(2, resourceUris.size());
		assertEquals("scheme:user/repo|resourceFileNode1", resourceUris.get(0));
		assertEquals("scheme:user/repo|resourceFileNode2", resourceUris.get(1));
		
		NativeJavaObject resourceSet =  (NativeJavaObject)(JsClientJavaUtils.invokeJsFunction(nodeRegistryManager, "getResourceSetForResourceUri", "scheme:user/repo|resourceFileNode1"));
		String resourceSetAsString = (String)resourceSet.unwrap();
		assertTrue(resourceSetAsString.equals("myTestResourceSet"));
		
		// everything is successfully subscribed; now let's test if we can unsubscribe/collapse
		
		JsClientJavaUtils.invokeJsFunction(nodeRegistryManager, "collapse", nodeRegistry, resourceNode1);
		// check for: unlink from node registry; you should unlink the first one
		resourceUris =  (NativeArray)(JsClientJavaUtils.invokeJsFunction(nodeRegistryManager, "getResourceUrisForResourceSet", "myTestResourceSet"));
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

//	@Test
//	public void collapseDirtyResourceNode() throws Exception {
//		// create resource node and expand it; then collapse it
//		ClientNode resourceNode = JSClientJavaTestUtils.createResourceClientNode("resourceNode");
//		
//		ClientNode child1 = JSClientJavaTestUtils.createClientNode("child1");
//		ClientNode child2 = JSClientJavaTestUtils.createClientNode("child2");
//		ClientNode child3 = JSClientJavaTestUtils.createClientNode("child3");
//		
//		JsList<ClientNode> getChildrenResponseList1 = new JsList<ClientNode>();
//		getChildrenResponseList1.add(child1);
//		getChildrenResponseList1.add(child2);
//		getChildrenResponseList1.add(child3);
//		
//		ClientSubscriptionInfo subscriptionInfoResponse = new ClientSubscriptionInfo();
//		ClientNode resourceFileNode = JSClientJavaTestUtils.createClientNode("resourceFileNode");
//		NativeObject propertyDirty = new NativeObject();
//		propertyDirty.put("isDirty", propertyDirty, true);
//		resourceFileNode.setProperties(propertyDirty);
//		
//		subscriptionInfoResponse.setResourceNode(resourceFileNode);
//		subscriptionInfoResponse.setRootNode(resourceNode);
//		subscriptionInfoResponse.setResourceSet("myTestResourceSet");;
//		
//		// create nodeRegistryManager
//		Scriptable nodeRegistryManager = ctx.newObject(scope, "NodeRegistryManager", new Object[] {
//				new JsResourceOperationsHandler(), 
//				new RecordingServiceInvocator().setExpectedResults(new Object[] {subscriptionInfoResponse, getChildrenResponseList1}),
//				new JsExternalInvocator()});
//		scope.put("_nodeRegistryManager", scope, nodeRegistryManager);
//		
//		// create nodeRegistry
//		Scriptable nodeRegistry = (Scriptable) ctx.evaluateString(scope, "_nodeRegistryManager.createNodeRegistry();", null, 1, null);
//		INodeChangeListener listener = mock(INodeChangeListener.class);
//		// add NodeChangedListener in nodeRegistry
//		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "addNodeChangeListener", listener);
//		
//		// register resourceNode1 and 2 
//		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "registerNode", resourceNode, null, -1);
//		
//		NativeObject resourceNodeProperties = new NativeObject(); // it's something like a Map<,>, but gets translated in JavaScript
//		resourceNodeProperties.put("autoSubscribeOnExpand", resourceNodeProperties, true);
//		JsList<Pair<String, String>> subscribableResources1 = new JsList<Pair<String, String>>();
//		Pair<String, String> subscribableResource1 = new Pair<String, String>("scheme:user/repo|resourceFileNode1", "mindmap");
//		// register resourceFileNode1
//		JsClientJavaUtils.invokeJsFunction(nodeRegistry, "registerNode", resourceFileNode, null, -1);
//		subscribableResources1.add(subscribableResource1);
//		resourceNodeProperties.put("subscribableResources", resourceNodeProperties, subscribableResources1);
//		resourceNode.setProperties(resourceNodeProperties);
//		JsClientJavaUtils.invokeJsFunction(nodeRegistryManager, "expand", nodeRegistry, resourceNode, null);
//		
//		// test if the resource node is subscribed; if it has child 3 a children => is is successfully subscribed 
//		assertNotNull(resourceNode.getChildren());
//		assertEquals(child1, resourceNode.getChildren().getItemAt(0));
//		assertEquals(child2, resourceNode.getChildren().getItemAt(1));
//		assertEquals(child3, resourceNode.getChildren().getItemAt(2));
//		verify(listener).nodeAdded(child1);
//		verify(listener).nodeAdded(child2);
//		verify(listener).nodeAdded(child3);
//		
//		// test if linked to node registry
//		NativeArray resourceUris =  (NativeArray)(JsClientJavaUtils.invokeJsFunction(nodeRegistryManager, "getResourceUrisForResourceSet", "myTestResourceSet"));
//		assertEquals(1, resourceUris.size());
//		assertEquals("scheme:user/repo|resourceFileNode", resourceUris.get(0));
//		
//		NativeJavaObject resourceSet =  (NativeJavaObject)(JsClientJavaUtils.invokeJsFunction(nodeRegistryManager, "getResourceSetForResourceUri", "scheme:user/repo|resourceFileNode"));
//		String resourceSetAsString = (String)resourceSet.unwrap();
//		assertTrue(resourceSetAsString.equals("myTestResourceSet"));
//		
//		// everything is successfully subscribed; now let's test if we can unsubscribe/collapse
//		
//		JsClientJavaUtils.invokeJsFunction(nodeRegistryManager, "collapse", nodeRegistry, resourceNode);
//		// check for: unlink from node registry; you should unlink the first one
//		resourceUris =  (NativeArray)(JsClientJavaUtils.invokeJsFunction(nodeRegistryManager, "getResourceUrisForResourceSet", "myTestResourceSet"));
//		assertEquals(1, resourceUris.size());
//		assertEquals("scheme:user/repo|resourceFileNode", resourceUris.get(0));
//		// assert the nodes were removed from map
//		verify(listener).nodeRemoved(child1);
//		verify(listener).nodeRemoved(child2);
//		verify(listener).nodeRemoved(child3);
//		
//	}

}
