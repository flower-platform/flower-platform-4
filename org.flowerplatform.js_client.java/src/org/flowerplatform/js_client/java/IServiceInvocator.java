package org.flowerplatform.js_client.java;

import org.mozilla.javascript.Function;

public interface IServiceInvocator {

	void invoke(String serviceIdAndMethodName, Object[] parameters) throws Exception;
	
	void invoke(String serviceIdAndMethodName, Object[] parameters, Function resultCallback) throws Exception;
	
	void invoke(String serviceIdAndMethodName, Object[] parameters, Function resultCallback, Function faultCallback) throws Exception;
	
}
