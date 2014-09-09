package org.flowerplatform.flexutil.iframe {
	
	/**
	 * Meant to be extended for platform-specific implementations
	 * (e.g. IFrame for web, StageWebViewBridge for mobile).
	 * 
	 * <p>
	 * Note:
	 * Mobile editor uses <code>StageWebView</code> and 
	 * doesn't support <code>ExternalInterface</code>.
	 * 
	 * @author Cristina Constantinescu
	 * @author Mariana Gheorghe
	 */
	public interface IFlowerIFrame {
		
		function set url(value:String):void;
		function get url():String;
		
		function callJSFunction(functionName:String, callback:Function = null, ...parameters):void;
		function addCallback(name:String, callback:Function):void;
		
		function addViewCompleteHandler(handler:Function):void;
		function dispose():void;
		
	}
}