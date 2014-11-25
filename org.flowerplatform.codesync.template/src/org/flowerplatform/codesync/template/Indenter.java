/* license-start
 * 
 * Copyright (C) 2008 - 2014 Crispico Software, <http://www.crispico.com/>.
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
package org.flowerplatform.codesync.template;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class used in the <tt>callMacro</tt> velocimacro to indent inner templates.
 * 
 * @author Mariana Gheorghe
 */
public final class Indenter {

	/**
	 */
	public static String indent(String in) {
		String out = "";
		Pattern pattern = Pattern.compile("(.*?\\R)");
		Matcher matcher = pattern.matcher(in);
		String ws = null;
		while (matcher.find()) {
			String line = matcher.group();
			if (ws == null) {
				// get the leading whitespace from the first line
				Matcher wsMatcher = Pattern.compile("(\\s*)").matcher(line);
				wsMatcher.find();
				ws = wsMatcher.group();
			} else {
				// add leading whitespace to other lines
				line = ws + line;
			}
			out += line;
		}
		return out;
	}
	
	private Indenter() {
		// utility class must have private constructor
	}
	
}
