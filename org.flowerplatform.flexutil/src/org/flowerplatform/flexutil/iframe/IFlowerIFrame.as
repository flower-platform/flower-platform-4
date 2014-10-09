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