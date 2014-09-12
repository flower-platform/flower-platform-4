package org.flowerplatform.tests.js_client.java;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.flowerplatform.core.node.remote.SubscriptionInfo;
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

		JsList<ClientNode> getChildrenResponseList1 = new JsList<ClientNode>();
		getChildrenResponseList1.add(child1);

		JsList<ClientNode> getChildrenResponseList2 = new JsList<ClientNode>();
		getChildrenResponseList2.add(child2);

		JsList<ClientNode> getChildrenResponseList3 = new JsList<ClientNode>();
		getChildrenResponseList3.add(child3);

		ClientSubscriptionInfo subcriptionInfoResponse = new ClientSubscriptionInfo();
		ClientNode resourceNodeToSubscribeTo = JSClientJavaTestUtils.createClientNode("clientNodeToSubscribeTo");
		// this should be marked as dirty
		NativeObject propertiesDirty = new NativeObject();
		propertiesDirty.put("isDirty", propertiesDirty, false);
		resourceNodeToSubscribeTo.setProperties(propertiesDirty);
		
		subcriptionInfoResponse.setResourceNode(resourceNodeToSubscribeTo);
		subcriptionInfoResponse.setRootNode(resourceNode3);
		
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
		
		// test if the resource node is subscribed; if it has child 3 a children => is is successfully subscribed 
		assertNotNull(resourceNode3.getChildren());
		assertEquals(child3, resourceNode3.getChildren().getItemAt(0));
		verify(listener).nodeAdded(child1);
		verify(listener).nodeAdded(child2);
		verify(listener).nodeAdded(child3);
	}
	
	@Test
	public void expandResourceNodeNotSavedEarlier() throws Exception {
		
	}
}
