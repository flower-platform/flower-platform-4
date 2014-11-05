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

import org.flowerplatform.core.node.remote.NodeWithChildren;
import org.flowerplatform.js_client.java.node.AbstractServiceInvocator;
import org.flowerplatform.js_client.java.node.ClientNode;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

/**
 * @author Cristina Constantinescu
 */
public final class JSClientJavaTestUtils {

	private JSClientJavaTestUtils() {
		
	};
	
	/**
	 * @author Cristina Constantinescu
	 */
	public static class RecordingServiceInvocator extends AbstractServiceInvocator {

		private Object[] expectedResults;
		private int indexOfLastInvocationResult = -1;
			
		/**
		 * @author Cristina Constantinescu
		 */
		public RecordingServiceInvocator setExpectedResults(Object[] result) {
			this.expectedResults = result;
			indexOfLastInvocationResult = -1;
			return this;
		}

		@Override
		public void invoke(String serviceIdAndMethodName, Object[] parameters, Function resultCallback, Function faultCallback) throws Exception {
			Context cx = Context.enter();			
			Scriptable scope = cx.initStandardObjects();	
			
			resultCallback.call(cx, scope, scope, new Object[] {expectedResults[++indexOfLastInvocationResult]});
		}		
	}
	
	/**
	 * @author Cristina Constantinescu
	 */
	public static ClientNode createClientNode(String fragment) {
		ClientNode node = new ClientNode();
		node.setNodeUri(String.format("scheme:user/repo|%s", fragment));
		node.setType("type");
		return node;
	}
	
	/**
	 * 
	 * @param fragment
	 * @return
	 */
	public static NodeWithChildren createClientNodeWithChildren(String fragment) {
		NodeWithChildren node = new NodeWithChildren();
		ClientNode cn = new ClientNode();
		cn.setNodeUri(String.format("scheme:user/repo|%s", fragment));
		cn.setType("type");
		node.setNode(cn);
		return node;
	}

	/**
	 * 
	 * @param fragment
	 * @return
	 */
	public static ClientNode createResourceClientNode(String fragment) {
		ClientNode node = new ClientNode();
		node.setNodeUri(String.format("file:user/repo|%s", fragment));
		node.setType("type");
		return node;
	}

}
