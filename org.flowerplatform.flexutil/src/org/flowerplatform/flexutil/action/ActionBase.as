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
package org.flowerplatform.flexutil.action {
	import mx.collections.IList;
	import mx.messaging.AbstractConsumer;

	/**
	 * @author Cristian Spiescu
	 */
	public class ActionBase implements IAction {

		private var _id:String;
		private var _parentId:String;
		private var _orderIndex:Number;
		private var _preferShowOnActionBar:Boolean;
		private var _visible:Boolean = true;
		private var _enabled:Boolean = true;
		private var _label:String;
		private var _icon:Object;
		private var _selection:IList;
		
		/**
		 * @author Cristina Constantinescu
		 */
		private var _context:Object;
		
		/**
		 * @author Mircea Negreanu
		 */
		private var _functionDelegate:Function;
		
		public function get id():String {
			return _id;
		}
		
		public function set id(value:String):void {
			_id = value;
		}
		
		public function setId(value:String):ActionBase {
			id = value;
			return this;
		}
		
		public function get parentId():String {
			return _parentId;
		}
		
		public function set parentId(value:String):void {
			_parentId = value;
		}
	
		public function setParentId(value:String):ActionBase {
			parentId = value;
			return this;
		}
		
		public function get orderIndex():Number	{
			return _orderIndex;
		}

		public function set orderIndex(value:Number):void {
			_orderIndex = value;
		}

		public function setOrderIndex(value:Number):ActionBase {
			orderIndex = value;
			return this;
		}
		
		public function get label():String {
			return _label;
		}

		public function set label(value:String):void {
			_label = value;
		}

		public function setLabel(value:String):ActionBase {
			label = value;
			return this;
		}
		
		public function get icon():Object {
			return _icon;
		}

		public function set icon(value:Object):void {
			_icon = value;
		}
		
		public function setIcon(value:Object):ActionBase {
			icon = value;
			return this;
		}
		
		public function get preferShowOnActionBar():Boolean	{
			return _preferShowOnActionBar;
		}
		
		public function set preferShowOnActionBar(value:Boolean):void {
			_preferShowOnActionBar = value;
		}
		
		public function setPreferShowOnActionBar(value:Boolean):ActionBase {
			preferShowOnActionBar = value;
			return this;
		}
		
		/**
		 * @author Mircea Negreanu
		 */
		public function get functionDelegate():Function {
			return _functionDelegate;
		}
		
		/**
		 * @author Mircea Negreanu
		 */
		public function set functionDelegate(value:Function):void {
			_functionDelegate = value;
		}
		
		public function setFunctionDelegate(value:Function):ActionBase {
			functionDelegate = value;
			return this;
		}
		
		public function get visible():Boolean {
			return _visible;
		}
		
		public function set visible(value:Boolean):void {
			_visible = value;
		}
		
		public function get enabled():Boolean {
			return _enabled;
		}
		
		public function set enabled(value:Boolean):void {
			_enabled = value;
		}
		
		public function get selection():IList {
			return _selection;
		}
		
		public function set selection(value:IList):void {
			_selection = value;
		}
		
		/**
		 * @author Cristina Constantinescu
		 */
		public function get context():Object {
			return _context;
		}
		
		/**
		 * @author Cristina Constantinescu
		 */
		public function set context(value:Object):void {
			_context = value;
		}
				
		/**
		 * Use functionDelegate, if any exists.
		 * 
		 * @author Cristian Spiescu
		 * @author Mircea Negreanu
		 */
		public function run():void {
			// if we have a functionDelegate than execute that
			if (functionDelegate != null) {
				functionDelegate();
			}
		}
		
	}
}