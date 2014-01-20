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
package org.flowerplatform.flexutil {
	import flash.display.DisplayObject;
	import flash.utils.getQualifiedClassName;
	
	import mx.controls.TextInput;
	import mx.core.ITextInput;
	import mx.core.UIComponent;
	import mx.utils.DescribeTypeCache;
	
	import spark.components.TextInput;
	import spark.components.supportClasses.SkinnableTextBase;
	
	/**
	 * @author Cristina
	 */ 
	public class Utils {
		
		/**
		 * Makes the given text input non-editable and applies a grey color as background.
		 */ 
		public static function makePseudoDisabled(object:Object):void {
			if (object is spark.components.TextInput || object is ITextInput || object is SkinnableTextBase) {
				object.editable = false;
				object.setStyle("backgroundColor", "#DEDCDC");
				object.setStyle("color", "#666666");
			}
		}
		
		/**
		 * @author Cristi
		 */
		public static function beginsWith(target:String, beginsWithWhat:String):Boolean {
			if (beginsWithWhat.length > target.length) {
				return false;
			} else if (target.substr(0, beginsWithWhat.length) == beginsWithWhat) {
				return true;
			} else {
				return false;
			}
		}
		
		/**
		 * @author Cristi
		 */
		public static function endsWith(target:String, endsWithWhat:String):Boolean {
			if (endsWithWhat.length > target.length) {
				return false;
			} else if (target.substr(target.length - endsWithWhat.length) == endsWithWhat) {
				return true;
			} else {
				return false;
			}
		}
		
		/**
		 * @author Cristi
		 */
		public static function getClassNameForObject(item:Object, fullyQualified:Boolean):String {
//			var classInfo:XML = DescribeTypeCache.describeType(item).typeDescription;
//			var simpleClassName:String = classInfo.@name;
			var simpleClassName:String = getQualifiedClassName(item);
			
			if (!fullyQualified) {
				simpleClassName = simpleClassName.substr(simpleClassName.search("::") + 2);
			}
			
			return simpleClassName;
		}
		
	}
}