package org.flowerplatform.tests.core;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.js_client.java.IFunctionInvoker;
import org.flowerplatform.js_client.java.INodeChangeListener;
import org.flowerplatform.js_client.java.INodeRegistryManagerListener;
import org.flowerplatform.js_client.java.JsClientJavaUtils;
import org.flowerplatform.js_client.java.JsExternalInvocator;
import org.flowerplatform.js_client.java.JsResourceOperationsHandler;
import org.flowerplatform.js_client.java.JsServiceInvocator;
import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;

/**
 * @author Cristina Constantinescu
 */
public class NodeRegistryScriptTest {
		
	private static String BASE_URL = "http://localhost:8080/org.flowerplatform.host.web_app";
	
	class NodeChangedListener implements INodeChangeListener {
		@Override
		public void nodeRemoved(Object node) {
			System.out.println(String.format("nodeRemoved -> %s", ((Node) node).getProperties()));
		}
		@Override
		public void nodeAdded(Object node) {
			System.out.println(String.format("nodeAdded -> %s", ((Node) node).getChildren()));
		}
		@Override
		public void nodeUpdated(Object node, String property, Object oldValue, Object newValue) {
			System.out.println(String.format("nodeUpdated -> %s, property=%s, oldValue=%s, newValue=%s", node, property, oldValue, newValue));
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
	
	private void load(Context cx, Scriptable scope) throws Exception {
		URL url = new URL(BASE_URL + "/js_client.core/js/node_registry/NodeRegistry.js");      
		cx.evaluateReader(scope, new BufferedReader(new InputStreamReader(url.openStream())), null, 1, null);
		
		url = new URL(BASE_URL + "/js_client.core/js/node_registry/ResourceOperationsManager.js");      
		cx.evaluateReader(scope, new BufferedReader(new InputStreamReader(url.openStream())), null, 1, null);
		
		url = new URL(BASE_URL + "/js_client.core/js/node_registry/NodeRegistryManager.js");      
		cx.evaluateReader(scope, new BufferedReader(new InputStreamReader(url.openStream())), null, 1, null);		
	}
	
	@Test
	public void runJavaScriptFunction() throws Exception {
		Context cx = Context.enter();
		
		try {				
			Scriptable scope = cx.initStandardObjects();
			
			// load scripts
			load(cx, scope);
			
			// create nodeRegistryManager
			Scriptable nodeRegistryManager = cx.newObject(scope, "NodeRegistryManager", new Object[] {new JsResourceOperationsHandler(), new JsServiceInvocator(), new JsExternalInvocator()});
			((Scriptable) scope).put("_nodeRegistryManager", scope, nodeRegistryManager);
					
			// add NodeRegistryManagerListener in nodeRegistryManager
			JsClientJavaUtils.invokeJsFunction(nodeRegistryManager, "addListener", new NodeRegistryManagerListener());
				
			// create nodeRegistry
			NativeObject nodeRegistry = (NativeObject) cx.evaluateString(scope, "_nodeRegistryManager.createNodeRegistry();", null, 1, null);
		
			// add NodeChangedListener in nodeRegistry
			JsClientJavaUtils.invokeJsFunction(nodeRegistry, "addNodeChangeListener", new NodeChangedListener());
			
			// subscribe
			JsClientJavaUtils.invokeJsFunction(nodeRegistryManager, "subscribe", "fpm:user1/repo-1|tt.mm", nodeRegistry, 
				new IFunctionInvoker() {
					@Override
					public void call(Object instance, Object... params) {
						System.out.println("subscribeOKCallback -> " + ((Node)params[0]).getProperties());						
					}
				}, 
				new IFunctionInvoker() {
					@Override
					public void call(Object instance, Object... params) {
						System.out.println("subscribeFailedCallback -> " + params[0].toString());						
					}
				});

			// get root node
			Object node = (Object) JsClientJavaUtils.invokeJsFunction(nodeRegistry, "getNodeById", "fpm:user1/repo-1|tt.mm");
			
			// expand root node
			JsClientJavaUtils.invokeJsFunction(nodeRegistry, "expand", node, null);
			
			System.out.println(node);
		} finally {
		    Context.exit();
		}
	}
	
}