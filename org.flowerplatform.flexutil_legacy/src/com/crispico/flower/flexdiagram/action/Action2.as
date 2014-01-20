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
	
	
	// TODO sorin : la stergerea vechilor actiuni ar trebui facut ca FlowerContribution 
	// sa extinda BaseAction. 

	/**
	 * @author Cristina
	 * 
	 */
	[SecureSWF(rename="off")]
	public class Action2 implements IAction {
	
		/**
		 * 
		 */
		private var _image:Object;
		
		/**
		 * 
		 */
		private var _label:String;
		
		protected var _context:ActionContext;
		
		private var _sortIndex:int = FlowerContextMenu.DEFAULT_SORT_INDEX;

		/**
		 * 
		 */
		[SecureSWF(rename="off")]
		public function get label():String {
			return _label;
		}
		
		/**
		 * @private 
		 * somne actions might need to reset their label or icon
		 * especially outside our framework (eg: gantt)
		 * @author Luiza
		 */
		[SecureSWF(rename="off")]
		public function set label(newLabel:String):void {
			this._label = newLabel;
		}
		
		/**
		 * 
		 */
		[SecureSWF(rename="off")]
		public function get image():Object {
			return _image;
		}
		
		/**
		 * @private
		 * @author Luiza
		 */
		[SecureSWF(rename="off")]
		public function set image(newImage:Object):void {
			this._image = newImage;
		}
		
		public function getContext():ActionContext {
			return _context;	
		}
		
		/**
		 * Note:Getter does not exist because it would be transmited to Java and it would
		 * request the value for the <code>context</code>.
		 */ 		
		public function set context(value:ActionContext):void {
			_context = value;	
		}
		
		[SecureSWF(rename="off")]
		public function get sortIndex():int {
			return _sortIndex;
		}
		
		[SecureSWF(rename="off")]
		public function set sortIndex(sortIndex:int):void {
			this._sortIndex = sortIndex;
		}
		
		/**
		 * 
		 */
		public function isVisible(selection:ArrayCollection):Boolean {
			return true;
		}
		
		// TODO Cristina : metoda nu corespunde cu noul mecanism de actiuni
		// va trebui stearsa 
		public function isEnabled(selection:ArrayCollection):Boolean {
			return true;
		}
		
		/**
		 * 
		 */
		public function run(selection:ArrayCollection):void {
		}
	}
}