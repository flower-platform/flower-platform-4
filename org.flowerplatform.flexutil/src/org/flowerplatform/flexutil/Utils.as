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
	import flash.text.Font;
	import flash.text.FontType;
	import flash.text.StyleSheet;
	import flash.utils.getQualifiedClassName;
	
	import mx.core.ITextInput;
	import mx.utils.StringUtil;
	
	import spark.components.TextInput;
	import spark.components.supportClasses.RegExPatterns;
	import spark.components.supportClasses.SkinnableTextBase;
	
	import flashx.textLayout.utils.CharacterUtil;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class Utils {
		
		/**
		 * @author Sebastian Solomon
		 */
		public static const ADD:int = 0;
		public static const REMOVE_FIRST:int = 1;
		public static const REMOVE_LAST:int = 2;
		public static const REMOVE_ALL:int = 3;
		public static const ICONS_SEPARATOR:String = ",";
		
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
		
		/**
		 * For java logical fonts, returns a corresponding flex style that resembles with it.
		 * 
		 * See links for more info. 
		 * http://mindprod.com/jgloss/logicalfonts.html
		 * http://docs.oracle.com/javase/tutorial/2d/text/fonts.html
		 * 
		 */ 
		public static function getSupportedFontFamily(javaFontFamily:String):String {
			switch (javaFontFamily) {
				case "SansSerif":
				case "Dialog":
				case "Lucida Sans":				
					return "Arial";					
				case "Monospaced":
				case "DialogInput":
				case "Lucida Sans Typewriter":
					return "Courier New";					
				case "Serif":
					return "Times New Roman";					
				default:
					return javaFontFamily;
			}			
		}
		
		/**
		 * Inspired from UintPropertyHandler.owningHandlerCheck().
		 * Transforms value given as parameter into color (as uint).
		 * 
		 * <p>
		 * Note: RichText has some problems when setting text color using something else.
		 */ 
		public static function convertValueToColor(value:*):uint {
			if (value is uint) {
				return value;
			}
			var newRslt:Number;
			if (value is String) {
				var str:String = String(value);
				// Normally, we could just cast a string to a uint. However, the casting technique only works for
				// normal numbers and numbers preceded by "0x". We can encounter numbers of the form "#ffffffff"					
				if (str.substr(0, 1) == "#") {
					str = "0x" + str.substr(1, str.length-1);
				}
				newRslt = (str.toLowerCase().substr(0, 2) == "0x") ? parseInt(str) : NaN;
			} else if (value is Number || value is int) {
				newRslt = Number(value);
			} else {
				return undefined;
			}
			
			if (isNaN(newRslt)) {
				return undefined;
			}
			if (newRslt < 0 || newRslt > 0xffffffff) {
				return undefined;
			}
			return newRslt;	
		}
		
		public static function convertColorToString(color:uint):String {
			var hexColor:String = color.toString(16);
			for (var i:int=hexColor.length; i < 6; i++) {
				hexColor = 0 + hexColor;
			}
			return "#" + hexColor; 
		}
		
		/**
		 * Verifies if text contains <html> tag.
		 */ 
		public static function isHTMLText(text:String):Boolean {
			return text.search(/(?s)^\s*<\s*html[^>]*>.*/) != -1;
		}
		
		/**
		 * Issue: TextFieldHtmlImporter doesn't support inline styles.
		 * 
		 * All inline styles from <code>text</code> are replaced with compatible flex format style.
		 */ 
		public static function getCompatibleHTMLText(text:String):String {			
			text = text.replace(/<p(\s*.*?\s?)style=['"](\s*.*?\s?)text-align:\s*(.*?\s*)[;"]/g, "<p$1align='$3' style='$2'");
			text = text.replace(/<p(\s*.*?\s?)style=['"](\s*.*?\s?)text-decoration:\s*(.*?\s*)[;"]/g, "<p$1decoration='$3' style='$2'");
			text = text.replace(/<p(\s*.*?\s?)style=['"](\s*.*?\s?)text-indent:\s*(.*?\s*)[;"]/g, "<p$1indent='$3' style='$2'");
			
			text = text.replace(/color:\s*(.*?)\s*[";]/g, "color='$1' ");			
			text = text.replace(/font-size:\s*(.*?)\s*[";]/g, "size='$1' ");
			text = text.replace(/font-family:\s*(.*?)\s*[";]/g, "face='$1' ");
			
//			text = text.replace(/<p(\s*.*?\s?)font-weight:\s*(.*?\s*)[;"](\s*.*?\s*)>(\s*.*?\s*)<\/p>/g, "<p$1$3><b>$4<\/b><\/p>");
//			text = text.replace(/<p(\s*.*?\s?)font-style:\s*(.*?\s*)[;"](\s*.*?\s*)>(\s*.*?\s*)<\/p>/g, "<p$1$3><i>$4<\/i><\/p>");

			text = text.replace(/<p(\s*.*?\s?)style=['"]{0,2}(.*?)["']{0,2}(\s*.*?\s*)>(\s*.*?\s*)<(\s*.*?\s*)\/p>/g, "<p$1$3><font $2>$4$5<\/font><\/p>");

			// add here other replacements
			
			return text;
		}
		
	}
}