/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
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
package org.flowerplatform.tests.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.tests.TestUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Cristina Constantinescu
 */
public class NodeRegistryScriptTest {
	
	public static final String DIR = TestUtil.getResourcesDir(NodeRegistryScriptTest.class);
	
	ScriptEngine engine;
	
	/**
	 * @author see class
	 */
	class NodeChangedListener implements INodeChangedListener {
		
		@Override
		public void nodeChanged(Node node) {
			Assert.assertEquals(node.getType(), "newType");
		}		
	}
	
	/**
	 * @author see class
	 */
	@Test
	public void runJavaScriptFunction() {
		try {
			ScriptEngineManager manager = new ScriptEngineManager();
			engine = manager.getEngineByName("JavaScript");
			// read script file
			engine.eval(Files.newBufferedReader(Paths.get(DIR + "/jsFile.js"), StandardCharsets.UTF_8));

			Object nodeRegistry = engine.get("nodeRegistry");

			((Invocable) engine).invokeMethod(nodeRegistry, "addListener", new NodeChangedListener());
	
			Node node = new Node("myNodeUri", "myType");	
			Assert.assertEquals(((Invocable) engine).invokeMethod(nodeRegistry, "registerNode", node), "Node " + "[" + node.getType() + ", " + node.getNodeUri() + "] registered!");
			
			// this will trigger INodeChangedListener.nodeChanged
			((Invocable) engine).invokeMethod(nodeRegistry, "setType", node, "newType");
									
		} catch (Exception e) {			
			throw new RuntimeException(e);
		}
	}
	
}