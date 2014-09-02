package org.flowerplatform.js_client.java;

public interface IFunctionInvoker<R, P> {

	@SuppressWarnings("unchecked")
	public R call(Object instance, P... params);

}
