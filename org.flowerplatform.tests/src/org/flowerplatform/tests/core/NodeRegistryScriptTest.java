package org.flowerplatform.tests.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Observable;

import javax.script.ScriptEngine;

import org.flowerplatform.js_client.java.IFunctionInvoker;
import org.flowerplatform.js_client.java.INodeChangeListener;
import org.flowerplatform.js_client.java.INodeRegistryManagerListener;
import org.flowerplatform.js_client.java.JsExternalInvocator;
import org.flowerplatform.js_client.java.JsNode;
import org.flowerplatform.js_client.java.JsResourceOperationsHandler;
import org.flowerplatform.js_client.java.JsServiceInvocator;
import org.flowerplatform.tests.TestUtil;
import org.flowerplatform.util.RunnableWithParam;
import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

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
	public void runJavaScriptFunction() throws Exception {
		Context cx = Context.enter();
		try {			
			Scriptable scope = cx.initStandardObjects();
					
			cx.evaluateReader(scope, Files.newBufferedReader(Paths.get("D:/data/flower-platform-4/org.flowerplatform.js_client.core/WebContent/js/node_registry/NodeRegistry.js"), StandardCharsets.UTF_8), null, 1, null);
			cx.evaluateReader(scope, Files.newBufferedReader(Paths.get("D:/data/flower-platform-4/org.flowerplatform.js_client.core/WebContent/js/node_registry/ResourceOperationsManager.js"), StandardCharsets.UTF_8), null, 1, null);
			cx.evaluateReader(scope, Files.newBufferedReader(Paths.get("D:/data/flower-platform-4/org.flowerplatform.js_client.core/WebContent/js/node_registry/NodeRegistryManager.js"), StandardCharsets.UTF_8), null, 1, null);
					
			JsResourceOperationsHandler n1 = new JsResourceOperationsHandler();
			ScriptableObject.putProperty(scope, "n1", Context.javaToJS(n1, scope));
			  
			JsServiceInvocator n2 = new JsServiceInvocator();
			ScriptableObject.putProperty(scope, "n2", Context.javaToJS(n2, scope));
			
			JsExternalInvocator n3 = new JsExternalInvocator();
			ScriptableObject.putProperty(scope, "n3", Context.javaToJS(n3, scope));
			
			cx.evaluateString(scope, "_nodeRegistryManager = new NodeRegistryManager(n1, n2, n3);", null, 1, null);
			
			Scriptable nodeRegistryManager = (Scriptable) scope.get("_nodeRegistryManager", scope);
						
			Object nodeRegistry = cx.evaluateString(scope, "_nodeRegistryManager.createNodeRegistry();", null, 1, null);
		
			Function fct = (Function) nodeRegistryManager.getPrototype().get("subscribe", nodeRegistryManager.getPrototype());
			fct.call(cx, scope, nodeRegistryManager, new Object[] {"fpm:user1/repo-1|tt.mm", nodeRegistry, 
				new IFunctionInvoker<Object, Object>() {
					@Override
					public Object call(Object instance, Object... params) {
						System.out.println("subscribeOKCallback -> " + params);
						return null;
					}
				}, 
				new IFunctionInvoker<Object, Object>() {
					@Override
					public Object call(Object instance, Object... params) {
						System.out.println("subscribeFailedCallback -> " + params[0].toString());
						return null;
					}
				}});
									
			fct = (Function) nodeRegistryManager.getPrototype().get("getResourceUris", nodeRegistryManager.getPrototype());
			Object result = fct.call(cx, scope, nodeRegistryManager, new Object[] {});
			System.out.println(result);
		} finally {
		    Context.exit();
		}
	}
	
}