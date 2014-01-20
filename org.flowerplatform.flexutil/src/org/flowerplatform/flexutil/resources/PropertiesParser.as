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
/*
* Copyright 2007-2009 the original author or authors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.flowerplatform.flexutil.resources {
	import mx.utils.StringUtil;
	
	/**
	 * {@code PropertiesParser} parses a properties source string into a {@link Properties}
	 * instance.
	 * 
	 * <p>The source string contains simple key-value pairs. Multiple pairs are
	 * separated by line terminators (\n or \r or \r\n). Keys are separated from
	 * values with the characters '=', ':' or a white space character.
	 * 
	 * <p>Comments are also supported. Just add a '#' or '!' character at the
	 * beginning of your comment-line.
	 * 
	 * <p>If you want to use any of the special characters in your key or value you
	 * must escape it with a back-slash character '\'.
	 * 
	 * <p>The key contains all of the characters in a line starting from the first
	 * non-white space character up to, but not including, the first unescaped
	 * key-value-separator.
	 * 
	 * <p>The value contains all of the characters in a line starting from the first
	 * non-white space character after the key-value-separator up to the end of the
	 * line. You may of course also escape the line terminator and create a value
	 * across multiple lines.
	 * 
	 * @author Martin Heidegger
	 * @author Simon Wacker
	 * @author Christophe Herreman
	 * @version 1.0
	 */
	public class PropertiesParser {
		
		/**
		 * Constructs a new {@code PropertiesParser} instance.
		 */
		public function PropertiesParser() {
		}
		
		/**
		 * Parses the given {@code source} and creates a {@code Properties} instance from it.
		 * 
		 * @param source the source to parse
		 * @return the properties defined by the given {@code source}
		 */
		public function parseProperties(source:String, result:Object):void {
			var lines:Array = source.split("\n");
			var numLines:Number = lines.length;

			var key:String;
			var value:String;
			var formerKey:String;
			var formerValue:String;
			var useNextLine:Boolean = false;;

			for (var i:int = 0; i<numLines; i++) {
				var line:String = lines[i];
				// Trim the line
				line = StringUtil.trim(line);
				// Ignore Comments
				if ( line.indexOf("#") != 0 && line.indexOf("!") != 0 && line.length != 0) {
					// Line break processing
					if (useNextLine) {
						key = formerKey;
						value = formerValue+line;
						useNextLine = false;
					} else {
						var sep:Number = getSeparation(line);
						key = StringUtil.trim(line.substr(0,sep));
						value = line.substring(sep+1);
						formerKey = key;
						formerValue = value;
					}
					// Trim the content
					value = StringUtil.trim(value);
					// Allow normal lines
					if (value.charAt(value.length-1) == "\\") {
						formerValue = value =  value.substr(0, value.length-1);
						useNextLine = true;
					} else {
						// Replace all \\n with \n (otherwise \n will not be seen as new line)
						value =value.replace(/\\n+/g, "\n");
						// Commit Property														
						result[key] = value;
					}
				}
			}
		}
		
		/**
		 * Returns the position at which key and value are separated.
		 * 
		 * @param line the line that contains the key-value pair
		 * @return the position at which key and value are separated
		 */
		private function getSeparation(line:String):int {
			var l:Number = line.length;
			for (var i:int = 0; i<l; i++) {
				var c:String = line.charAt(i);
				if (c == "'") {
					i++;
				} else {
					if (c == ":" || c == "=" || c == "	") break;
				}
			}
			return ( (i == l) ? line.length : i );
		}
		
	}
	
}