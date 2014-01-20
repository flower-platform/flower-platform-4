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
package com.crispico.flower.flexdiagram.action {

	import com.crispico.flower.flexdiagram.contextmenu.FlowerContextMenu;
	
	import mx.collections.ArrayCollection;

	/**
	 * Implementation for <code>IAction</code> that adds fields
	 * for the properties.
	 * 
	 * @copy com.crispico.flower.flexdiagram.action.IAction
	 * @see com.crispico.flower.flexdiagram.action.BaseMultipleSelectionAction
	 * 
	 */ 
	
	[RemoteClass(alias="com.crispico.flower.mp.action.BaseAction")]
	[SecureSWF(rename="off")]
	public class BaseAction implements IAction {
	
		private var _label:String;

		private var _image:Object;
		
		private var _sortIndex:int = FlowerContextMenu.DEFAULT_SORT_INDEX;
		
		private var _context:ActionContext;

		/**
		 * @inheritDoc
		 */
		[SecureSWF(rename="off")]
		public function get label():String {
			return _label;
		}
		
		[SecureSWF(rename="off")]
		public function set label(value:String):void {
			_label = value;
		}
		
		/**
		 * @inheritDoc
		 */
		[SecureSWF(rename="off")]
		public function get image():Object {
			return _image;
		}
		
		[SecureSWF(rename="off")]
		public function set image(value:Object):void {
			_image = value;
		} 
		
		/**
		 * @inheritDoc
		 */
		[SecureSWF(rename="off")]
		public function get sortIndex():int {
			return _sortIndex;
		}
		
		[SecureSWF(rename="off")]
		public function set sortIndex(value:int):void {
			this._sortIndex = value;
		}
		
		/**
		 * @inheritDoc
		 * 
		 * @see com.crispico.flower.flexdiagram.action.ActionContext
		 * @see com.crispico.flower.flexdiagram.action.CreateActionContext
		 * @see com.crispico.flower.flexdiagram.gantt.contextmenu.GanttCreateActionContext
		 */
		[SecureSWF(rename="off")]
		public function get context():ActionContext {
			return _context;			
		}
		
		[SecureSWF(rename="off")]
		public function set context(value:ActionContext):void {
			_context = value;
		}
		
		/**
		 * @inheritDoc
		 */
		public function isVisible(selectedEditParts:ArrayCollection):Boolean {
			return true;
		}
		
		/**
		 * @inheritDoc
		 */
		public function run(selectedEditParts:ArrayCollection):void {
			throw new Error("BaseAction.run() needs to be implemented");
		}
	}
	
}