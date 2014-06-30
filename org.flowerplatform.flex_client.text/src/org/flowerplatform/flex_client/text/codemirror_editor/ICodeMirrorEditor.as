package org.flowerplatform.flex_client.text.codemirror_editor {
		
	/**
	 * Web and mobile code mirror editors must implement this interface 
	 * to provide different behavior when communicating with javascript.
	 * 
	 * <p>
	 * Note:
	 * Mobile editor uses <code>StageWebView</code> and 
	 * doesn't support <code>ExternalInterface</code>.
	 * 
	 * @see CodeMirrorEditorFrontend
	 * @author Cristina Constantinescu
	 */
	public interface ICodeMirrorEditor {
		
		function load(url:String):void;		
		
		function callJavaScriptMethod(method:String, callback:Function = null, ... arguments):void;		
		function addCallbackHandler(name:String, callback:Function):void;		
		
		function addViewCompleteHandler(handler:Function):void;
		
		function dispose():void;
	}
}