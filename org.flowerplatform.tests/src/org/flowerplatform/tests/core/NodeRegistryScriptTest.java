package org.flowerplatform.tests.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.flowerplatform.js_client.java.INodeChangeListener;
import org.flowerplatform.js_client.java.INodeRegistryManagerListener;
import org.flowerplatform.js_client.java.JsExternalInvocator;
import org.flowerplatform.js_client.java.JsNode;
import org.flowerplatform.js_client.java.JsResourceOperationsHandler;
import org.flowerplatform.js_client.java.JsServiceInvocator;
import org.flowerplatform.tests.TestUtil;
import org.flowerplatform.util.RunnableWithParam;
import org.junit.Test;

/**
 * @author Cristina Constantinescu
 */
public class NodeRegistryScriptTest {
	
	public static final String DIR = TestUtil.getResourcesDir(NodeRegistryScriptTest.class);
	
	ScriptEngine engine;
	
	class NodeChangedListener implements INodeChangeListener {

		@Override
		public void nodeRemoved(JsNode node) {
			System.out.println("nodeRemoved");
		}

		@Override
		public void nodeAdded(JsNode node) {
			System.out.println("nodeAdded");
		}

		@Override
		public void nodeUpdated(JsNode node, String property, Object oldValue, Object newValue) {
			System.out.println("nodeUpdated");
		}
		
	}
	
	class NodeRegistryManagerListener implements INodeRegistryManagerListener {

		@Override
		public void nodeRegistryRemoved(Object nodeRegistry) {
			System.out.println("nodeRegistryRemoved");	
		}

		@Override
		public void resourceNodeRemoved(String resourceNodeUri,	Object nodeRegistry) {
			System.out.println("resourceNodeRemoved");			
		}		
	}
	
	@Test
	public void runJavaScriptFunction() {
		try {						    
			ScriptEngineManager manager = new ScriptEngineManager();
			engine = manager.getEngineByName("JavaScript");
			
			// read script files
			engine.eval(Files.newBufferedReader(Paths.get("D:/data/flower-platform-4/org.flowerplatform.js_client.core/js/NodeRegistry.js"), StandardCharsets.UTF_8));
			engine.eval(Files.newBufferedReader(Paths.get("D:/data/flower-platform-4/org.flowerplatform.js_client.core/js/ResourceOperationsManager.js"), StandardCharsets.UTF_8));
			engine.eval(Files.newBufferedReader(Paths.get("D:/data/flower-platform-4/org.flowerplatform.js_client.core/js/NodeRegistryManager.js"), StandardCharsets.UTF_8));
			
			engine.put("n1", new JsResourceOperationsHandler());
			engine.put("n2", new JsServiceInvocator());
			engine.put("n3", new JsExternalInvocator());
			engine.eval("nodeRegistryManager = new NodeRegistryManager(n1, n2, n3);");
			
			Object nodeRegistryManager = engine.get("nodeRegistryManager");
			nodeRegistryManager.toString();
				
			Object nodeRegistry = ((Invocable) engine).invokeMethod(nodeRegistryManager, "createNodeRegistry");
			((Invocable) engine).invokeMethod(nodeRegistryManager, "addListener", new NodeRegistryManagerListener());
			
			((Invocable) engine).invokeMethod(nodeRegistryManager, "subscribe", "root:user/repo", nodeRegistry, 
					new RunnableWithParam<Object, Object>() {
						@Override
						public Object run(Object param) {
							System.out.println("subscribeResultCallback");
							return null;
						}
				
					}, 
					new RunnableWithParam<Object, Object>() {
						@Override
						public Object run(Object param) {
							System.out.println("subscribeFaultCallback");
							return null;
						}
				
					});
//			CorePlugin.getInstance().nodeRegistryManager.addListener(this);
			
//			CorePlugin.getInstance().nodeRegistryManager.subscribe(editorInput, nodeRegistry, subscribeResultCallback, subscribeFaultCallback);
//			Node node = new Node("myNodeUri", "myType");	
//			Assert.assertEquals(((Invocable) engine).invokeMethod(nodeRegistry, "registerNode", node), "Node " + "[" + node.getType() + ", " + node.getNodeUri() + "] registered!");
//			
//			// this will trigger INodeChangedListener.nodeChanged
//			((Invocable) engine).invokeMethod(nodeRegistry, "setType", node, "newType");
									
		} catch (Exception e) {			
			throw new RuntimeException(e);
		}
	}
	
}