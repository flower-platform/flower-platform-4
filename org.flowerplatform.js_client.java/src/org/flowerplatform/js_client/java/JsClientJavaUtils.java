package org.flowerplatform.js_client.java;

import java.util.Arrays;

import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * @author Cristina Constantinescu
 */
public class JsClientJavaUtils {

	public static Object invokeJsFunction(Scriptable instance, String functionName, Object... parameters) {
		return ScriptableObject.callMethod(instance, functionName, Arrays.asList(parameters).toArray());
	}
	
}
