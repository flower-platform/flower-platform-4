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
package org.flowerplatform.flex_client.properties.remote {
	import mx.collections.ArrayCollection;

	public interface IPropertyDescriptor {
		
		function get name():String;
		function set name(value:String):void;
		
		function get title():String;
		function set title(value:String):void;
		
		function get type():String;
		function set type(value:String):void;
		
		function get category():String;
		function set category(value:String):void;
		
		function get propertyLineRenderer():String;
		function set propertyLineRenderer(value:String):void;
		
		function get contributesToCreation():Boolean;
		function set contributesToCreation(value:Boolean):void;
		
		function get mandatory():Boolean;
		function set mandatory(value:Boolean):void;
		
		function get defaultValue():Object;
		function set defaultValue(value:Object):void;
		
		function get possibleValues():ArrayCollection;
		function set possibleValues(value:ArrayCollection):void;
		
		function get readOnly():Boolean;
		function set readOnly(value:Boolean):void;
		
		function get orderIndex():int;
		function set orderIndex(value:int):void;
	
	}
}