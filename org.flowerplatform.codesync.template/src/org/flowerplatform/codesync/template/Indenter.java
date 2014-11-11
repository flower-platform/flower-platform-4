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
