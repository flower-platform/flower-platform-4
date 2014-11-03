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
package org.flowerplatform.tests.regex.sample_configs;

import static org.flowerplatform.tests.regex.RegexUtil.CAPTURE_IDENTIFIER;
import static org.flowerplatform.tests.regex.RegexUtil.CLASS_KEYWORD;
import static org.flowerplatform.tests.regex.RegexUtil.CLOSE_BRACKET;
import static org.flowerplatform.tests.regex.RegexUtil.COMMA;
import static org.flowerplatform.tests.regex.RegexUtil.DONT_CAPTURE;
import static org.flowerplatform.tests.regex.RegexUtil.EXCLUDE;
import static org.flowerplatform.tests.regex.RegexUtil.IDENTIFIER_AFTER_BEGINNING_CHAR;
import static org.flowerplatform.tests.regex.RegexUtil.INTERFACE_KEYWORD;
import static org.flowerplatform.tests.regex.RegexUtil.MULTIPLE_TIMES;
import static org.flowerplatform.tests.regex.RegexUtil.MULTI_LINE_COMMENT;
import static org.flowerplatform.tests.regex.RegexUtil.NOT_MORE_THAN_ONCE;
import static org.flowerplatform.tests.regex.RegexUtil.OPEN_BRACKET;
import static org.flowerplatform.tests.regex.RegexUtil.OPEN_PARENTHESIS;
import static org.flowerplatform.tests.regex.RegexUtil.SINGLE_LINE_COMMENT;
import static org.flowerplatform.tests.regex.RegexUtil.SLASH;
import static org.flowerplatform.tests.regex.RegexUtil.SPACES_OR_COMMENTS_OPTIONAL;
import static org.flowerplatform.tests.regex.RegexUtil.SPACE_OR_COMMENT;
import static org.flowerplatform.tests.regex.RegexUtil.STAR;
import static org.flowerplatform.tests.regex.RegexUtil.STOP_BEFORE_OPEN_BRACKET_CHAR;
import static org.flowerplatform.tests.regex.sample_configs.JavaRegexConfigurationProvider.ATTRIBUTE_CATEGORY;
import static org.flowerplatform.tests.regex.sample_configs.JavaRegexConfigurationProvider.METHOD_CATEGORY;

import java.util.regex.Pattern;

import org.flowerplatform.util.regex.IfFindThisAnnounceMatchCandidate;
import org.flowerplatform.util.regex.IfFindThisModifyNesting;
import org.flowerplatform.util.regex.IfFindThisSkip;
import org.flowerplatform.util.regex.RegexConfiguration;
import org.flowerplatform.util.regex.UntilFoundThisIgnoreAll;

/**
 * @author Sorin
 */
public abstract class ActionscriptRegexConfigurationProvider {

	private static final String VAR_KEYWORD = "\\bvar\\b"; // word that starts
															// and ends with var
	private static final String CONST_KEYWORD = "\\bconst\\b"; // word that
																// starts and
																// ends with
																// const

	private static final String FUNCTION_KEYWORD = "\\bfunction\\b"; // word
																		// that
																		// starts
																		// and
																		// ends
																		// with
																		// function
	private static final String GET_KEYWORD = "\\bget\\b"; // word that starts
															// and ends with get
	private static final String SET_KEYWORD = "\\bset\\b"; // word that starts
															// and ends with set

	private static final int ACTIONSCRIPT_NESTING_LEVEL_FOR_DECLARATIONS = 2; // must
																				// pass
																				// "package {"
																				// and
																				// "class {"

	private static final String ACTIONSCRIPT_TYPE_BEGIN = // something like
															// class ... {
	"(" + DONT_CAPTURE // class or interface
			+ CLASS_KEYWORD + "|" + INTERFACE_KEYWORD + ")" + "(" + DONT_CAPTURE + // possibly
																					// multiple
																					// comments
																					// or
																					// identifiers
																					// or
																					// commas
			SPACE_OR_COMMENT + "|" + // comment
			"[" + IDENTIFIER_AFTER_BEGINNING_CHAR + COMMA + // every identifier or enumeration character
			EXCLUDE + OPEN_BRACKET + EXCLUDE + SLASH + EXCLUDE + STAR
			// except comment and bracket, because comment is processed as a hole and bracket is the condition to stop.
			+ "]" + ")" + MULTIPLE_TIMES + STOP_BEFORE_OPEN_BRACKET_CHAR; // ensure at a moment it will start with {

	protected static final String ACTIONSCRIPT_ATTRIBUTE = // something like var
															// name
	"(" + DONT_CAPTURE + VAR_KEYWORD + "|" + CONST_KEYWORD + ")" + // start with
																	// var or
																	// const
			SPACES_OR_COMMENTS_OPTIONAL + // possibly multiple comments
			CAPTURE_IDENTIFIER; // capture attribute name

	protected static final String ACTIONSCRIPT_METHOD = // something like
														// function get/set?
														// name()
	FUNCTION_KEYWORD + // start with function
			SPACES_OR_COMMENTS_OPTIONAL + // possibly multiple comments
			"(" + DONT_CAPTURE + GET_KEYWORD + "|" + SET_KEYWORD + ")" + NOT_MORE_THAN_ONCE + // optional get or set
			SPACES_OR_COMMENTS_OPTIONAL + // possibly other multiple comments
			CAPTURE_IDENTIFIER + // capture method name
			SPACES_OR_COMMENTS_OPTIONAL + // possibly other multiple comments
			OPEN_PARENTHESIS; // ensure it has (
	/**
	 * 
	 * @param config
	 */
	public static void buildASConfiguration(RegexConfiguration config) {
		config.setTargetNestingForMatches(ACTIONSCRIPT_NESTING_LEVEL_FOR_DECLARATIONS).add(new IfFindThisModifyNesting("Opening curly bracket", OPEN_BRACKET, 1))
				.add(new IfFindThisModifyNesting("Closing curly bracket", CLOSE_BRACKET, -1))
				.add(new UntilFoundThisIgnoreAll("Begining of type ", ACTIONSCRIPT_TYPE_BEGIN))
				.add(new IfFindThisAnnounceMatchCandidate(org.flowerplatform.tests.regex.sample_configs.JavaRegexConfigurationProvider.ATTRIBUTE_CATEGORY, 
						ACTIONSCRIPT_ATTRIBUTE, ATTRIBUTE_CATEGORY))
				.add(new IfFindThisAnnounceMatchCandidate(METHOD_CATEGORY, ACTIONSCRIPT_METHOD, METHOD_CATEGORY))
				.add(new IfFindThisSkip("Multi-line comment", MULTI_LINE_COMMENT))
				.add(new IfFindThisSkip("Single-line comment", SINGLE_LINE_COMMENT)).compile(Pattern.DOTALL);
	}

}
