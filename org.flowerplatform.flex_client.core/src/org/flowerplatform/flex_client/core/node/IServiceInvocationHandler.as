package org.flowerplatform.flex_client.core.node {
		
	public interface IServiceInvocationHandler {
	
		function invoke(serviceIdAndMethodName:String, parameters:Array = null, resultCallback:Function = null, faultCallback:Function = null):void;
		
	}
}