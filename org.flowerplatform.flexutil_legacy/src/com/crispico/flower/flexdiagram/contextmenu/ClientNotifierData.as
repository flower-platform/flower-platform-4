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
package com.crispico.flower.flexdiagram.contextmenu
{
	import com.crispico.flower.flexdiagram.action.IActionProvider2;
	
	import flash.display.Stage;
	import flash.system.Capabilities;
	
	import mx.core.UIComponent;
	
	/**
	 * 
	 */
	public class ClientNotifierData {
		
		private var _viewer:UIComponent;
		
		private var _stage:Stage;
		
		private var _actionProvider:IActionProvider2;
		
		private var _beforeFillContextMenuFunction:Function;
		
		private var _beforeShowContextMenuFromFlashFunction:Function;
		
		private var _logicProvider:IContextMenuLogicProvider;
						
		private var _useWholeScreen:Boolean;
				
		private var _contextMenuEnabled:Boolean;	
		
		private var _contextMenuEntryLabelFunction:Function;	
		
		private var _minContextMenuWidth:Number;
		
		private var _rightClickEnabled:Boolean; 
		
		/**
		 * A special selected element is an element, from the client selection,
		 * that in the case of a right click CM logic it is specially treated
		 * with a left click CM logic by the CM manager.
		 *
		 * This callback function should receive a mouseEvent parameter and should decide 
		 * if the mouse is over an selected element.
		 * 
		 * It is used by the CM manager in case of a general right click CM logic 
		 * to decide if, on a mouse move handler, the mouse over selection CM should be showed or not.
		 */    
		private var _isOverSpecialSelectedElementFunction:Function;
				
		/**
		 * @see getter
		 */
		private var _menuIconsWidth:int = 16;
		
		public function ClientNotifierData(viewer:UIComponent, actionProvider:IActionProvider2, beforeFillContextMenuFunction:Function, beforeShowContextMenuFromFlashFunction:Function, contextMenuEntryLabelFunction:Function, logicProvider:IContextMenuLogicProvider, useWholeScreen:Boolean, minContextMenuWidth:Number, rightClickEnabled:Boolean) {
			this._viewer = viewer;
			this._stage = viewer.stage;
			this._actionProvider = actionProvider;
			this._beforeFillContextMenuFunction = beforeFillContextMenuFunction;
			this._beforeShowContextMenuFromFlashFunction = beforeShowContextMenuFromFlashFunction;
			this._logicProvider = logicProvider;			
			this._useWholeScreen = useWholeScreen;			
			this._contextMenuEnabled = true; // By default it is enabled;	
			this._contextMenuEntryLabelFunction = contextMenuEntryLabelFunction;	
			this._minContextMenuWidth = minContextMenuWidth;
			//By default the logic of the context menu manager 
			//for handling any client will be the one for left click context menu 
			this._rightClickEnabled = rightClickEnabled;	
		}
		
		public function get viewer():UIComponent {
			return _viewer;
		}
		
		/**
		 * The stage of the viewer. It is important to recover this reference when the viewer leaves stage to be able 
		 * to remove mouse listeners.
		 */ 
		public function get stage():Stage {
			return _stage;
		}
		
		public function get actionProvider():IActionProvider2 {
			return _actionProvider;
		}
		
		public function get beforeFillContextMenuFunction():Function {
			return _beforeFillContextMenuFunction;
		}
		
		public function get beforeShowContextMenuFromFlashFunction():Function {
			return _beforeShowContextMenuFromFlashFunction;
		}
				
		public function get logicProvider():IContextMenuLogicProvider {
			return _logicProvider;
		}
		
		public function get useWholeScreen():Boolean {
			return _useWholeScreen;
		}
			
		public function get contextMenuEnabled():Boolean {
			return _contextMenuEnabled;
		}
		
		public function get contextMenuEntryLabelFunction():Function {
			return _contextMenuEntryLabelFunction;
		}
		
		public function get minContextMenuWidth():Number {
			return _minContextMenuWidth;
		}
		
		/**
		 * Each client can specify the width of its menu entries.
		 * By default each client it sopose to have the icons size 16X16
		 */ 
		public function get menuIconsWidth():int {
			return _menuIconsWidth;
		}
		
		public function set menuIconsWidth(menuIconsWidth:int):void {
			_menuIconsWidth = menuIconsWidth;
		}
		
		public function get rightClickEnabled():Boolean {
			var flashVersion:Object = new Object();// Object to hold the Flash version details
		    var versionNumber:String = Capabilities.version;// Get the whole version string
		    var versionArray:Array = versionNumber.split(",");// Split it up
		    var length:Number = versionArray.length;
		    var osAndVersion:Array = versionArray[0].split(" ");// The main version contains the OS (e.g. WIN), so we split that off as well.
		    
		    flashVersion["major"] = parseInt(osAndVersion[1]);
		    flashVersion["minor"] = parseInt(versionArray[1]);
			var isSuitableFlashPlayerVersion:Boolean = (flashVersion["major"] > 11) || (flashVersion["major"] == 11 && flashVersion["minor"] >= 2);
			
			return _rightClickEnabled && isSuitableFlashPlayerVersion;
		}
		
		public function set rightClickEnabled(rightClickEnabled:Boolean):void {
			var oldRightClickEnabled:Boolean = this.rightClickEnabled;
			_rightClickEnabled = rightClickEnabled;
			var newRightClickEnabled:Boolean = this.rightClickEnabled;
			
			//If we have a new value for rightClickEnabled we need to change
			//the listenes of the manager. I.e. remove or add the events needed for
			//right click
			if (newRightClickEnabled != oldRightClickEnabled)
				ContextMenuManager.INSTANCE.changeListeners(viewer);
		}
		
		public function set contextMenuEnabled(value:Boolean):void {
			_contextMenuEnabled = value;			
		}
		
		public function get isOverSpecialSelectedElementFunction():Function {
			return _isOverSpecialSelectedElementFunction;
		}
		
		public function set isOverSpecialSelectedElementFunction(isOverSpecialSelectedElementFunction:Function):void {
			_isOverSpecialSelectedElementFunction = isOverSpecialSelectedElementFunction;
		}
	}
}