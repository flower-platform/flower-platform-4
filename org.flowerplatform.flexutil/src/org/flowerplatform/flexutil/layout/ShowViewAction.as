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
package org.flowerplatform.flexutil.layout {
	
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	/**
	 * @author Cristian Spiescu
	 */ 
	public class ShowViewAction extends ActionBase {
		
		private var _viewId:String;
		
		private var _width:Number;
		
		private var _height:Number;
		
		public function get viewId():String {
			return _viewId;
		}

		public function set viewId(value:String):void {
			_viewId = value;
		}
		
		public function setViewId(value:String):ShowViewAction {
			viewId = value;
			return this;
		}

		public function get width():Number {
			return _width;
		}

		public function set width(value:Number):void {
			_width = value;
		}
		
		public function setWidth(value:Number):ShowViewAction {
			width = value;
			return this;
		}

		public function get height():Number {
			return _height;
		}

		public function set height(value:Number):void {
			_height = value;
		}
		
		public function setHeight(value:Number):ShowViewAction {
			height = value;
			return this;
		}

		override public function run():void {	
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()				
				.setViewIdInWorkbench(viewId)
				.setWidth(width)
				.setHeight(height)
				.show();
		}
	}
}