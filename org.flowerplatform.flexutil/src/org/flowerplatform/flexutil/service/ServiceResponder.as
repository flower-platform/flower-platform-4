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
package org.flowerplatform.flexutil.service {
	import mx.collections.ArrayCollection;
	import mx.rpc.IResponder;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	
	/**
	 * @author Cristian Spiescu
	 * @author Cristina Constantinescu
	 */
	public class ServiceResponder implements IResponder {
		
		protected var _resultHandler:Function;
		
		protected var _faultHandler:Function;
		
		protected var serviceLocator:ServiceLocator;
		
		public function get resultHandler():Function {
			return _resultHandler;
		}
		
		public function get faultHandler():Function {
			return _faultHandler;
		}
		
		public function ServiceResponder(serviceLocator:ServiceLocator, resultHandler:Function, faultHandler:Function) {
			this.serviceLocator = serviceLocator;
			this._resultHandler = resultHandler;
			this._faultHandler = faultHandler;
		}
		
		public function result(data:Object):void {
			serviceLocator.resultHandler(ResultEvent(data), this);
		}
		
		public function fault(info:Object):void {
			serviceLocator.faultHandler(FaultEvent(info), this);
		}
		
	}
}