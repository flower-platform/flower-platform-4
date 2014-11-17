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
package org.flowerplatform.freeplane.client;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.freeplane.FreeplanePlugin;
import org.flowerplatform.freeplane.controller.xml_parser.XmlWritter;
import org.flowerplatform.js_client.java.JsClientJavaPlugin;
import org.flowerplatform.js_client.java.JsClientJavaUtils;
import org.flowerplatform.js_client.java.node.ClientNode;
import org.flowerplatform.js_client.java.node.IFunctionInvoker;
import org.flowerplatform.js_client.java.node.INodeChangeListener;
import org.flowerplatform.js_client.java.node.INodeRegistryManagerListener;
import org.flowerplatform.js_client.java.node.JavaHostInvocator;
import org.flowerplatform.js_client.java.node.JavaHostResourceOperationsHandler;
import org.flowerplatform.js_client.java.node.JavaHostServiceInvocator;
import org.freeplane.core.ui.AFreeplaneAction;
import org.freeplane.features.map.MapModel;
import org.freeplane.features.map.NodeModel;
import org.freeplane.features.map.mindmapmode.MMapController;
import org.freeplane.features.map.mindmapmode.MMapModel;
import org.freeplane.features.mode.Controller;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;

/**
 * @author Valentina Bojan
 * @author Cristina Constantinescu
 */
class FlowerPlatformSubscribe extends AFreeplaneAction {
	private static final long serialVersionUID = 1L;
	
	private static final String URL = "http://localhost:8080/org.flowerplatform.host.web_app";

	public static NativeObject nodeRegistry;
	
	/**
	 * @author Valentina Bojan
	 */
	public FlowerPlatformSubscribe() {
		super("FlowerPlatformSubscribe");
	}

	/**
	 * Method which is called when the button Flower Platform Subscribe was pressed.
	 * @author Valentina Bojan
	 */
	public void actionPerformed(final ActionEvent e) {
		try {
			runJavaScriptFunction();
		} catch (Exception ex) {
			ex.printStackTrace();
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
	 * @author Valentina Bojan
	 * @author Cristina Constantinescu
	 */
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
			nodeRegistry = (NativeObject) cx.evaluateString(scope, "_nodeRegistryManager.createNodeRegistry();", null, 1, null);
		
			// add NodeChangedListener in nodeRegistry
			JsClientJavaUtils.invokeJsFunction(nodeRegistry, "addNodeChangeListener", new NodeChangedListener());
			
			// subscribe
			JsClientJavaUtils.invokeJsFunction(nodeRegistryManager, "subscribe", "fpm1:user1/repo-2|TestMap.mm", nodeRegistry, 
				new IFunctionInvoker() {
					public void call(Object instance, Object... params) {
						// create the XML content for the root node
						ClientNode root = (ClientNode) params[0];
						XmlWritter xmlCreator = new XmlWritter(FreeplanePlugin.getInstance().getXmlConfiguration(), root.getProperties());
						
						// create a new map containing the root node from the XML content
						MMapController mapController = (MMapController) Controller.getCurrentModeController().getMapController();
						FlowerPlatformManager flowerManager = FlowerPlatformManager.getController();
						MapModel map = new MMapModel();
						NodeModel rootNode = flowerManager.loadNodeFromXmlContent(map, xmlCreator.getXmlContent());
						ClientNodeModel rootClientNodeModel = new ClientNodeModel();
						rootClientNodeModel.setNode(root);
						rootNode.addExtension(rootClientNodeModel);

						// expand root node
						JsClientJavaUtils.invokeJsFunction(nodeRegistry, "expand", root, null);
						List<Node> children = root.getChildren();
						flowerManager.addChildrenToParent(children, rootNode);
						
						// display the new map
						mapController.setSaved(map, false);
						mapController.fireMapCreated(map);
						mapController.newMapView(map);
						flowerManager.addViewerToChildren(rootNode, new FlowerPlatformView());
					}
				},
				new IFunctionInvoker() {
					public void call(Object instance, Object... params) {
						System.out.println("subscribeFailedCallback -> " + params[0].toString());						
					}
				});			
			
		} finally {
		    Context.exit();
		}
	}
	
	/**
	 * @author Cristina Constantinescu
	 */
	class NodeChangedListener implements INodeChangeListener {
		
		/**
		 *@author see class
		 **/
		public void nodeRemoved(Object node) {
			System.out.println(String.format("nodeRemoved -> %s", (ClientNode) node));
		}
		
		/**
		 *@author see class
		 **/
		public void nodeAdded(Object node) {
			System.out.println(String.format("nodeAdded -> %s", (ClientNode) node));
		}
		
		/**
		 *@author see class
		 **/
		public void nodeUpdated(Object node, String property, Object oldValue, Object newValue) {
			System.out.println(String.format("nodeUpdated -> %s, property=%s, oldValue=%s, newValue=%s", node, property, oldValue, newValue));
		}		
	}
	
	/**
	 * @author Cristina Constantinescu
	 */
	class NodeRegistryManagerListener implements INodeRegistryManagerListener {
		
		/**
		 *@author see class
		 **/
		public void nodeRegistryRemoved(Object nodeRegistryObject) {
			System.out.println("nodeRegistryRemoved");	
		}
		
		/**
		 *@author see class
		 **/
		public void resourceNodeRemoved(String resourceNodeUri,	Object nodeRegistryObject) {
			System.out.println("resourceNodeRemoved");			
		}		
	}
}
