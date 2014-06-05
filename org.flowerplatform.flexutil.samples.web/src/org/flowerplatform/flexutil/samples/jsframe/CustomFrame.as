package org.flowerplatform.flexutil.samples.jsframe
{
	import flash.external.ExternalInterface;
	import flash.system.Security;
	
	import mx.controls.Alert;
	
	import org.flowerplatform.flexutil.iframe.*;
	
	public class CustomFrame extends FlowerIFrame
	{
		public function CustomFrame() {
			// to allow for urls from local disk like file:///D:/java 
			Security.allowDomain("*"); 
			ExternalInterface.addCallback("callItFromJS", callItFromJS);
		}
		
		public function callGetCurrentDate():void {
			callIFrameFunction('getCurrentDate', null, handleGetCurrentDateResult);
		}
		
		private function handleGetCurrentDateResult(result: Object):void {
			Alert.show("The 'getCurrentDate()' method returned '" + result + "'.");
		}
				
		private function callItFromJS(data:String): void {
			Alert.show("Received data from js: " + data);
		}
		
	}
}