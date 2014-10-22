package org.flowerplatform.codesync.template;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Mariana Gheorghe
 */
public final class LineSplitter {

	/**
	 * 
	 */
	public static List<String> lines(String text) {
		List<String> results = new ArrayList<String>();
		Pattern pattern = Pattern.compile("(.*?\\R)");
		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			results.add(matcher.group());
		}
		return results;
	}
	
	private LineSplitter() {
		// utility class must have private constructor
	}
	
}
