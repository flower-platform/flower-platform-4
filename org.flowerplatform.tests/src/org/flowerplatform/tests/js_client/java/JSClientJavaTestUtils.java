package org.flowerplatform.tests.js_client.java;

import org.flowerplatform.js_client.java.ClientNode;
import org.flowerplatform.js_client.java.ServiceInvocator;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

/**
 * @author Cristina Constantinescu
 */
public class JSClientJavaTestUtils {

	public static class RecordingServiceInvocator extends ServiceInvocator {

		private Object[] expectedResults;
		private int indexOfLastInvocationResult = -1;
			
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
	
	public static ClientNode createClientNode(String fragment) {
		ClientNode node = new ClientNode();
		node.setNodeUri(String.format("scheme:user/repo|%s", fragment));
		node.setType("type");
		return node;
	}
	
}
