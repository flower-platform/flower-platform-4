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
	import flash.utils.getQualifiedClassName;
	
	import mx.core.ITextInput;
	
	import spark.components.TextInput;
	import spark.components.supportClasses.SkinnableTextBase;
	
	/**
	 * @author Cristina
	 */ 
	public class Utils {
		
		/**
		 * @author Sebastian Solomon
		 */
		public static const ADD:int = 0;
		public static const REMOVE_FIRST:int = 1;
		public static const REMOVE_LAST:int = 2;
		public static const REMOVE_ALL:int = 3;
		public static const ICONS_SEPARATOR:String = "|";
		
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
		
		/**
		 * @author Cristina Constantinescu
		 */
		public static function defaultIfNull(str:String, defaultStr:String = ""):String {
			return str == null ? defaultStr : str;
		}
		
		/**
		 * Based on <code>type</code>:
		 * <ul>
		 * 	<li> ADD -> adds <code>token</code> to <code>str</code>.
		 *  <li> REMOVE_FIRST/REMOVE_LAST -> removes first/last token from <code>str</code>.
		 *  <li> REMOVE_ALL -> <code>str</code> becomes <code>null</code>
		 * </ul>
		 * 
		 * @param str - a concatenation of substrings separated by <code>separator</code>.
		 * 
		 * @author Sebastian Solomon
		 * @author Cristina Constantinescu
		 */
		public static function computeStringTokens(str:String, separator:String, type:int, token:String = null):String {
			switch (type) {
				case ADD:
					str = (str == null ? "" : (str + separator)) + token;
					break;
				case REMOVE_FIRST:
					if (str != null) {
						var firstIndexOf:int = str.indexOf(separator);
						str = firstIndexOf != -1 ? str.substr(firstIndexOf + 1, str.length) : null;
					}
					break;
				case REMOVE_LAST:
					if (str != null) {
						var lastIndexOf:int = str.lastIndexOf(separator);
						str = lastIndexOf != -1 ? str.substr(0, lastIndexOf) : null;
					}
					break;
				case REMOVE_ALL:
					str = null;
					break;
			}
			return str;
		}
		
	}
}