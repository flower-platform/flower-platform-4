/* license-start
 * 
 * Copyright (C) 2008 - 2014 Crispico Software, <http://www.crispico.com/>.
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
package org.flowerplatform.flexutil.iframe {
	
	import flash.display.DisplayObjectContainer;
	import flash.external.ExternalInterface;
	
	import org.flowerplatform.flexutil.FlexUtilConstants;
	import org.flowerplatform.flexutil.Utils;
	import org.flowerplatform.flexutil.view_content_host.IViewContent;
	import org.flowerplatform.flexutil.view_content_host.IViewHost;
	
	/**
	 * <code>IFrame</code> code was copied from here:
	 * https://github.com/flex-users/flex-iframe
	 * 
	 * <p>
	 * This class adds specific flower behavior and styles.
	 * @author Cristina Constantinescu
	 * @author Mariana Gheorghe
	 */
	public class FlowerIFrame extends IFrame implements IFlowerIFrame, IViewContent {
		
		public function FlowerIFrame(id:String = null) {
			super(id);
			
			percentHeight = 100;
			percentWidth = 100;
			
			setStyle("paddingLeft", 0);
			setStyle("paddingRight", 0);
			setStyle("paddingBottom", 0);
			setStyle("paddingTop", 0);
			
			overlayDetection = true;			
		}
		
		override public function parentChanged(p:DisplayObjectContainer):void {
			super.parentChanged(p);
			updateFrameVisibility(true);
		}
		
		override protected function updateFrameVisibility(value:Boolean):Boolean {
			try {
				return super.updateFrameVisibility(value);
			} catch (e:Error) {
				// error thrown when this object changes its parent, but the new parent isn't set yet
				// to avoid it, catch it here and call it after, in parentChanged
			}
			return false;
		}
		
		public function set url(value:String):void {
			value = Utils.getUrlWithParameter(value, FlexUtilConstants.EMBED_IN_APP, FlexUtilConstants.EMBED_IN_FLEX_APP_WEB);
			source = value;
		}
		
		public function get url():String {
			return source;	
		}
		
		public function callJSFunction(functionName:String, callback:Function=null, ...parameters):void {
			callIFrameFunction(functionName, parameters != null ? [].concat(parameters) : null, callback);
		}
		
		public function addCallback(name:String, callback:Function):void {
			ExternalInterface.addCallback(name, callback);
		}
		
		public function addViewCompleteHandler(handler:Function):void {
			addEventListener("frameLoad", handler);
		}
		
		public function dispose():void {
			removeIFrame();
		}
		
		public function set viewHost(viewHost:IViewHost):void {
			// nothing to do
		}
		
		public function additionalCloseHandler():void {
			// nothing to do
		}
		
	}
}
