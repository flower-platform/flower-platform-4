package org.flowerplatform.tests.js_client.java;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.flowerplatform.js_client.java.ClientNode;
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
				
		JsList<ClientNode> list = new JsList<>();
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
		// TODO CS: dc. modificam in loc de 1, 2, 3: 1, 2, 2: ne da exceptie? nu trebuie sa facem si ceva sa ne asiguram ca nu s-a apelat altceva apoi?
	}
	
}
