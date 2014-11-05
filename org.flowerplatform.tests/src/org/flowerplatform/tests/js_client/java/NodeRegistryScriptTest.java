/* license-start
 * 
 * Copyright (C) 2008 - 2014 Crispico Software, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * license-end
 */
package org.flowerplatform.tests.js_client.java;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.flowerplatform.js_client.java.JsClientJavaPlugin;
import org.flowerplatform.js_client.java.JsClientJavaUtils;
import org.flowerplatform.js_client.java.node.ClientNode;
import org.flowerplatform.js_client.java.node.IFunctionInvoker;
import org.flowerplatform.js_client.java.node.INodeChangeListener;
import org.flowerplatform.js_client.java.node.INodeRegistryManagerListener;
import org.flowerplatform.js_client.java.node.JavaHostInvocator;
import org.flowerplatform.js_client.java.node.JavaHostResourceOperationsHandler;
import org.flowerplatform.js_client.java.node.JavaHostServiceInvocator;
import org.flowerplatform.tests.EclipseIndependentTestBase;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;

/**
 * @author Cristina Constantinescu
 */
public class NodeRegistryScriptTest extends EclipseIndependentTestBase {

	private static final String URL = "http://localhost:8080/org.flowerplatform.host.web_app";
	
	/**
	 * Make sure the plugins are started.
	 * 
	 * @auhor Mariana Gheorghe
	 */
	@BeforeClass
	public static void beforeClass() {
		startPlugin(new JsClientJavaPlugin());
	}
	
	/**
	 * @author Cristina Constantinescu
	 */
	class NodeChangedListener implements INodeChangeListener {
		@Override
		public void nodeRemoved(Object node) {
			System.out.println(String.format("nodeRemoved -> %s", (ClientNode) node));
		}
		@Override
		public void nodeAdded(Object node) {
			System.out.println(String.format("nodeAdded -> %s", (ClientNode) node));
		}
		@Override
		public void nodeUpdated(Object node, String property, Object oldValue, Object newValue) {
			System.out.println(String.format("nodeUpdated -> %s, property=%s, oldValue=%s, newValue=%s", node, property, oldValue, newValue));
		}		
	}
	
	/**
	 * @author Cristina Constantinescu
	 */
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
		URL url = new URL(URL + "/js_client.common_js_as/js/NodeRegistry.js");      
		cx.evaluateReader(scope, new BufferedReader(new InputStreamReader(url.openStream())), null, 1, null);
		
		url = new URL(URL + "/js_client.common_js_as/js/ResourceOperationsManager.js");      
		cx.evaluateReader(scope, new BufferedReader(new InputStreamReader(url.openStream())), null, 1, null);
		
		url = new URL(URL + "/js_client.common_js_as/js/NodeRegistryManager.js");      
		cx.evaluateReader(scope, new BufferedReader(new InputStreamReader(url.openStream())), null, 1, null);		
	}
	
	/**
	 * @author Cristina Constantinescu
	 */
	@Test
	public void runJavaScriptFunction() throws Exception {
		Context cx = Context.enter();
		
		try {				
			Scriptable scope = cx.initStandardObjects();
			
			// load scripts
			load(cx, scope);
			
			// create nodeRegistryManager
			Scriptable nodeRegistryManager = cx.newObject(scope, "NodeRegistryManager", new Object[] {
					new JavaHostResourceOperationsHandler(), 
					new JavaHostServiceInvocator(), 
					new JavaHostInvocator()});
			((Scriptable) scope).put("_nodeRegistryManager", scope, nodeRegistryManager);
			
			JsClientJavaPlugin.getInstance().setNodeRegistryManager(nodeRegistryManager);
			
			// add NodeRegistryManagerListener in nodeRegistryManager
			JsClientJavaUtils.invokeJsFunction(nodeRegistryManager, "addListener", new NodeRegistryManagerListener());
				
			// create nodeRegistry
			final NativeObject nodeRegistry = (NativeObject) cx.evaluateString(scope, "_nodeRegistryManager.createNodeRegistry();", null, 1, null);
		
			// add NodeChangedListener in nodeRegistry
			JsClientJavaUtils.invokeJsFunction(nodeRegistry, "addNodeChangeListener", new NodeChangedListener());
			
			// subscribe
			JsClientJavaUtils.invokeJsFunction(nodeRegistryManager, "subscribe", "fpm1:user1/repo-2|TestMap.mm", nodeRegistry, 
				new IFunctionInvoker() {
					@Override
					public void call(Object instance, Object... params) {
						JsClientJavaUtils.invokeJsFunction(nodeRegistry, "expand", params[0], null);
						
						System.out.println(((ClientNode) ((ClientNode) params[0]).getChildren().get(0)).properties);
					}
				}, 
				new IFunctionInvoker() {
					@Override
					public void call(Object instance, Object... params) {
						System.out.println("subscribeFailedCallback -> " + params[0].toString());						
					}
				});			
		} finally {
		    Context.exit();
		}
	}
	
}
