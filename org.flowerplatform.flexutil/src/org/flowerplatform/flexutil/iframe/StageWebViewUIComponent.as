/* license-start
* 
* Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
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
* Contributors:
*   Crispico - Initial API and implementation
*
* license-end
*/
package org.flowerplatform.flexutil.iframe {
	
	import flash.display.Stage;
	import flash.events.ErrorEvent;
	import flash.events.Event;
	import flash.events.LocationChangeEvent;
	import flash.geom.Rectangle;
	
	import mx.controls.Alert;
	import mx.core.UIComponent;
	
	import es.xperiments.media.StageWebViewBridge;
	import es.xperiments.media.StageWebViewBridgeEvent;
	import es.xperiments.media.StageWebViewDisk;
	
	import org.flowerplatform.flexutil.FlexUtilConstants;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.Utils;
	import org.flowerplatform.flexutil.iframe.IFlowerIFrame;
	
	[Event(name="complete", type="flash.events.Event")]
	[Event(name="locationChanging", type="flash.events.LocationChangeEvent")]
	[Event(name="locationChange", type="flash.events.LocationChangeEvent")]
	
	/**
	 * A <code>StageWebViewBridge</code> wrapped in an <code>UIComponent</code>.
	 * 
	 * @author Cristina Constantinescu
	 */
	public class StageWebViewUIComponent extends UIComponent implements IFlowerIFrame {
		
		public var yOffset:int = 80;
		
		protected var myStage:Stage;
		private var _url:String;
		private var _text:String;
		
		private var _stageWebView:StageWebViewBridge;
		
		public function get stageWebView():StageWebViewBridge {
			return _stageWebView;
		}
		
		public function StageWebViewUIComponent() {
			percentHeight = 100;
			percentWidth = 100;
			addEventListener(Event.ADDED_TO_STAGE, addedToStageHandler);
			addEventListener(Event.REMOVED_FROM_STAGE, removedFromStageHandler);
		}
		
		public function set url(url:String):void {
			_url = FlexUtilGlobals.getInstance().createAbsoluteUrl(url);
			_url = Utils.getUrlWithParameter(_url, FlexUtilConstants.EMBED_IN_FLEX_APP, FlexUtilConstants.EMBED_IN_FLEX_APP_MOBILE);
			
			if (_stageWebView) {
				_stageWebView.loadURL(_url);
			}
		}
		
		public function get url():String {
			return _url;
		}
		
		public function set text(text:String):void {
			_text = text;
			
			if (_stageWebView) {
				_stageWebView.loadString(text);
			}
		}
		
		public function callJSFunction(functionName:String, callback:Function = null, ...parameters):void {
			if (_stageWebView) {
				_stageWebView.call(functionName, callback, parameters);
			}
		}
		
		public function addCallback(name:String, callback:Function):void {
			if (_stageWebView) {
				_stageWebView.addCallback(name, callback);
			}
		}
		
		public function addViewCompleteHandler(handler:Function):void {
			if (_stageWebView) {
				_stageWebView.addEventListener(StageWebViewBridgeEvent.DEVICE_READY, handler);
			}
		}
		
		public function dispose():void {
			_stageWebView.visible = false;
			_stageWebView.dispose();
		}
		
		protected function addedToStageHandler(event:Event):void {
			myStage = event.currentTarget.document.stage;
			removeEventListener(Event.ADDED_TO_STAGE, addedToStageHandler);
			
			StageWebViewDisk.initialize(myStage);
			
			_stageWebView = new StageWebViewBridge();
			_stageWebView.visible = true;
			_stageWebView.viewPort = new Rectangle(0, yOffset, myStage.width, myStage.fullScreenHeight - yOffset);
			_stageWebView.addEventListener(Event.COMPLETE, completeHandler);
			_stageWebView.addEventListener(ErrorEvent.ERROR, errorHandler);
			_stageWebView.addEventListener(LocationChangeEvent.LOCATION_CHANGING, locationChangingHandler);
			_stageWebView.addEventListener(LocationChangeEvent.LOCATION_CHANGE, locationChangeHandler);
			if (_url) {
				_stageWebView.loadURL(_url);
			} else if (_text) {
				_stageWebView.loadString(_text);
			}
		}
		
		protected function removedFromStageHandler(event:Event):void {
			removeEventListener(Event.REMOVED_FROM_STAGE, removedFromStageHandler);
			
			_stageWebView.dispose();
		}
		
		protected function completeHandler(event:Event):void {
			dispatchEvent(event.clone());
		}
		
		protected function locationChangingHandler(event:Event):void {
			dispatchEvent(event.clone());
		}
		
		protected function locationChangeHandler(event:Event):void {
			dispatchEvent(event.clone());
		}
		
		protected function errorHandler(event:Event):void {
			dispatchEvent(event.clone());
		}	
		
	}
}