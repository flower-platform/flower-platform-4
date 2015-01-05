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
package org.flowerplatform.js_client.java;

import java.util.Arrays;

import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * @author Cristina Constantinescu
 */
public class JsClientJavaUtils {

	public static Object invokeJsFunction(Scriptable instance, String functionName, Object... parameters) {		
		Object result  = ScriptableObject.callMethod(instance, functionName, Arrays.asList(parameters).toArray());
		if (result instanceof NativeJavaObject) {
			result = ((NativeJavaObject) result).unwrap();
		}
		return result;
	}
		
}
