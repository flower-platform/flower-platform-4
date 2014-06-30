package org.flowerplatform.flex_client.text.codemirror_editor {
	
	import es.xperiments.media.StageWebViewBridgeEvent;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class MobileCodeMirrorEditor extends StageWebViewUIComponent implements ICodeMirrorEditor {
				
		public function MobileCodeMirrorEditor() {
			yOffset = 90;
		}
		
		public function load(url:String):void {
			this.url = url;
		}
		
		public function callJavaScriptMethod(method:String, callback:Function = null, ... arguments):void {
			stageWebView.call(method, callback, arguments);
		}
		
		public function addCallbackHandler(name:String, callback:Function):void {
			stageWebView.addCallback(name, callback);
		}
		
		public function addViewCompleteHandler(handler:Function):void	{
			stageWebView.addEventListener(StageWebViewBridgeEvent.DEVICE_READY, handler);
		}
			
	}
}