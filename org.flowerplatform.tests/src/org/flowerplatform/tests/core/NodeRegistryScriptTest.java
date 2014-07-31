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
	
	class NodeChangedListener implements INodeChangedListener {
		
		@Override
		public void nodeChanged(Node node) {
			Assert.assertEquals(node.getType(), "newType");
		}		
	}
	
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