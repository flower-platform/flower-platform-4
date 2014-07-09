/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
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