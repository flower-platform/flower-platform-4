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
	import mx.core.IVisualElement;
	
	import org.flowerplatform.flexutil.view_content_host.IViewContent;

	/**
	 * @author Cristina Constantinescu
	 */ 
	public interface IPopupHandler {
		
		function setTitle(value:String):IPopupHandler;
		
		/**
		 * value is Number because we want to use the NaN in case width/height isn't set.
		 */ 
		function setWidth(value:Number):IPopupHandler;
		function setHeight(value:Number):IPopupHandler;
		
		function setViewContent(value:IViewContent):IPopupHandler;
		
		/**
		 * Represents the view id from workbench layout to be shown in a popup.
		 * @author Cristina Constantinescu
		 */ 
		function setViewIdInWorkbench(value:String):IPopupHandler;
		
		function show(modal:Boolean = true):void;
		function showModalOverAllApplication():void;
		
	}
}