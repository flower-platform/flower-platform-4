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
package org.flowerplatform.flexutil.popup {
	public interface IMessageBox {
		function setTitle(value:String):IMessageBox;
		function setIcon(value:Object):IMessageBox;
		function setText(value:String):IMessageBox;
		function setWidth(value:int):IMessageBox;
		function setHeight(value:int):IMessageBox;
		function setWordWrap(value:Boolean):IMessageBox;
		
		/**
		 * @author Cristina Constantinescu
		 */ 
		function addButton(title:String, handler:Function = null):IMessageBox;
		function setSelectText(value:Boolean):IMessageBox;
		
		function showMessageBox(modal:Boolean = true):void;		
	}
}