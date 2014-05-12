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
package org.flowerplatform.flexutil.renderer {
	import mx.core.UIComponent;
	
	import org.flowerplatform.flexutil.FlowerArrayList;
	
	/**
	 * @see IconsComponentExtension
	 * @author Cristina Constantinescu
	 */ 
	public interface IIconsComponentExtensionProvider {
		
		function get icons():FlowerArrayList;		
		function set icons(value:FlowerArrayList):void;
		
		/**
		 * @return - index in list of component's children for a new icon. 
		 */ 
		function newIconIndex():int;
		
		function getMainComponent():UIComponent;
		
		/**
		 * Validate methods must be overridden to validate also the icons from extension.
		 */ 
		function validateDisplayList():void;		
		function validateProperties():void;		
		function validateSize(recursive:Boolean = false):void;
		
	}
}