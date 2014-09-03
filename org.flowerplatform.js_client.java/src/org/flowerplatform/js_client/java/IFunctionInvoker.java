package org.flowerplatform.js_client.java;

/**
 * Used when needed to send a method as parameter.
 * 
 * <p>
 * Corresponds to flex Function.
 * 
 * @author Cristina Constantinescu
 */
public interface IFunctionInvoker {

	public void call(Object instance, Object... params);

}
