package org.flowerplatform.flex_client.text.codemirror_editor {
	
	import flash.external.ExternalInterface;
	
	import org.flowerplatform.flexutil.iframe.FlowerIFrame;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class WebCodeMirrorEditor extends FlowerIFrame implements ICodeMirrorEditor {
		
		public function WebCodeMirrorEditor(id:String = null) {
			super(id);
		}
		
		public function load(url:String):void {
			source = url;
		}
		
		public function addCallbackHandler(name:String, callback:Function):void {
			ExternalInterface.addCallback(name, callback);
		}
		
		public function callJavaScriptMethod(method:String, callback:Function = null, ... arguments):void {
			callIFrameFunction(method, arguments != null ? [].concat(arguments) : null, callback);
		}
		
		public function addViewCompleteHandler(handler:Function):void {
			addEventListener("frameLoad", handler);
		}
		
		public function dispose():void {
			removeIFrame();
		}
		
	}
}