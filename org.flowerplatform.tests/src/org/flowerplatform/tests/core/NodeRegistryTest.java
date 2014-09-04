package org.flowerplatform.tests.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.js_client.java.INodeChangeListener;
import org.flowerplatform.js_client.java.IServiceInvocator;
import org.flowerplatform.js_client.java.JsExternalInvocator;
import org.flowerplatform.js_client.java.JsList;
import org.flowerplatform.js_client.java.JsResourceOperationsHandler;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mozilla.javascript.Callable;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeJSON;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Cristina Constantinescu
 */
public class NodeRegistryTest {
	
	class ServiceInvocator implements IServiceInvocator {

		private Object[] result;
		private int i = 0;
		
		public ServiceInvocator setResult(Object[] result) {
			this.result = result;
			return this;
		}

		public void invoke(String serviceIdAndMethodName, Object[] parameters) throws Exception {	
			this.invoke(serviceIdAndMethodName, parameters, null, null);
		}
		
		public void invoke(String serviceIdAndMethodName, Object[] parameters, Function resultCallback) throws Exception {	
			this.invoke(serviceIdAndMethodName, parameters, resultCallback, null);
		}
		
		@Override
		public void invoke(String serviceIdAndMethodName, Object[] parameters, Function resultCallback, Function faultCallback) throws Exception {
			Context cx = Context.enter();			
			Scriptable scope = cx.initStandardObjects();	
			
			resultCallback.call(cx, scope, scope, new Object[] {toJSONObject(cx, scope, result[i++])});
		}		
	}
	
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
			
	private Object toJSONObject(Context ctx, Scriptable scope, Object obj) throws Exception {		
		// convert result to json
		String json = new ObjectMapper().writeValueAsString(obj);
		
		// convert json to js compatible object
		return NativeJSON.parse(ctx, scope, json, new Callable() {					
			@Override
			public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {					
				return args[1];
			}
		});
	}
	
	private Node getNode(String fragment) {
		return new Node(String.format("fpm:user/repo|%s", fragment), "type");
	}
	
	private NativeObject getNode(NativeObject nodeRegistry, String nodeUri) {		
		Function fct = (Function) nodeRegistry.getPrototype().get("getNodeById", nodeRegistry.getPrototype());
		return (NativeObject) fct.call(ctx, scope, nodeRegistry, new Object[] {nodeUri});			
	}
	
	@Test
	public void expand() throws Exception {		
		Function fct;
	
		Node parent = getNode("root");
		Node child1 = getNode("node1");
		Node child2 = getNode("node2");
		Node child3 = getNode("node3");
		
		// create nodeRegistryManager
		Scriptable nodeRegistryManager = ctx.newObject(scope, "NodeRegistryManager", new Object[] {
				new JsResourceOperationsHandler(), 
				new ServiceInvocator().setResult(new Object[] {Arrays.asList(child1, child2, child3)}), 
				new JsExternalInvocator()});
		((Scriptable) scope).put("_nodeRegistryManager", scope, nodeRegistryManager);
					
		// create nodeRegistry
		NativeObject nodeRegistry = (NativeObject) ctx.evaluateString(scope, "_nodeRegistryManager.createNodeRegistry();", null, 1, null);

		INodeChangeListener listener = mock(INodeChangeListener.class);
			
		// add NodeChangedListener in nodeRegistry
		fct = (Function) nodeRegistry.getPrototype().get("addNodeChangeListener", nodeRegistry.getPrototype());
		fct.call(ctx, scope, nodeRegistry, new Object[] {listener});
		
		// register parent node
		fct = (Function) nodeRegistry.getPrototype().get("registerNode", nodeRegistry.getPrototype());
		fct.call(ctx, scope, nodeRegistry, new Object[] {toJSONObject(ctx, scope, parent), null, -1});
										
		// get parent node
		NativeObject node = getNode(nodeRegistry, parent.getNodeUri());
		verify(listener).nodeAdded(node);
		
		// expand parent node
		fct = (Function) nodeRegistryManager.getPrototype().get("expand", nodeRegistryManager.getPrototype());
		fct.call(ctx, scope, nodeRegistryManager, new Object[] {nodeRegistry, node, null});
					
		// verify children
		Object children = node.get("children");
		assertNotNull(children);
		assertTrue(children instanceof JsList);
		assertEquals(3, ((JsList<?>) children).length());
				
		verify(listener).nodeAdded(getNode(nodeRegistry, child1.getNodeUri()));
		verify(listener).nodeAdded(getNode(nodeRegistry, child2.getNodeUri()));
		verify(listener).nodeAdded(getNode(nodeRegistry, child3.getNodeUri()));			
	}
	
}
