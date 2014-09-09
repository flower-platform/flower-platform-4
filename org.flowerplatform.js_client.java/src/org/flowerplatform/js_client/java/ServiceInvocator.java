package org.flowerplatform.js_client.java;

import org.mozilla.javascript.Function;

/**
 * @author Cristina Constantinescu
 */
// TODO rename Abstract*; de asemenea redenumirile facute pe js, sa se reflecte si in clasele din java
public abstract class ServiceInvocator {

	public void invoke(String serviceIdAndMethodName, Object[] parameters) throws Exception {
		this.invoke(serviceIdAndMethodName, parameters, null, null);
	}
	
	public void invoke(String serviceIdAndMethodName, Object[] parameters, Function resultCallback) throws Exception {
		this.invoke(serviceIdAndMethodName, parameters, resultCallback, null);
	}
	
	public abstract void invoke(String serviceIdAndMethodName, Object[] parameters, Function resultCallback, Function faultCallback) throws Exception;
	
}
