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
package org.flowerplatform.tests.regex;

/**
 * @author Sorin
 */
public final class RegexUtil {
	
	private RegexUtil() {
		// required for checkstyle
	}
	/**
	 * In regex there are special characters so actually to find a character X, that is considered special, \X must be used.
	 * But in java to write to \ character in a string you must double it so to write regex in java to find X character "\\X" must be inside a string.
	 * 
	 * NOTE : understand the difference between greedy and lazy search (e.g * and *?)
	 */

	public static final String ANY_CHAR = ".";
	public static final String WORD = "\\w"; // letter or digit
	public static final String WHITESPACE = "\\s"; // any character like space, tab , \r , \n
	
	
	// Regex Operators 
	public static final String STOP_BEFORE = "?=";
	public static final String DONT_CAPTURE = "?:";
	public static final String EXCLUDE = "^";
	public static final String MULTIPLE_TIMES = "*"; // It will try to stop as late as the next regex part can be matched , e.g "/* abc */ def*/" stops at the last "*/"
	public static final String MULTIPLE_TIMES_END_AS_SOON_AS_POSSIBLE = "*?"; 
	// It will try to stop as soon as the next regex part can be matched, e.g "/* abc */ def*/" stops at the first "*/"
	public static final String NOT_MORE_THAN_ONCE = "?"; // by default greedy, (if it can parse it then take it)
	public static final String AT_LEAST_ONCE_END_AS_SOON_AS_POSSIBLE = "+?";

	
	// Regex formated characters and group of characters
	public static final String COMMA = ",";
	public static final String OPEN_BRACKET 						= "\\{";
	public static final String CLOSE_BRACKET 						= "\\}";
	public static final String OPEN_PARENTHESIS 					= "\\(";
	public static final String CLOSE_PARENTHESIS 					= "\\)";
	public static final String OPEN_ANGLE_PARENTHESIS 				= "\\<";
	public static final String CLOSE_ANGLE_PARENTHESIS 				= "\\>";
	public static final String OPEN_SQUARE_PARENTHESIS				= "\\[";
	public static final String CLOSE_SQUARE_PARENTHESIS				= "\\]";
	public static final String SLASH_R								= "\\r";
	public static final String SLASH_N								= "\\n";
	public static final String DOLLAR								= "\\$";
	public static final String EXCLAMATION 							= "\\!";
	public static final String UNDERSCORE							= "_";
	public static final String SLASH								= "/"; // represent the character used to open or close a comment  /
	public static final String STAR									= "\\*"; // represents the second character used to open or close a multiline comment *
	public static final String MINUS								= "\\-";
	public static final String IDENTIFIER_BEGINNING_CHAR 			= "[a-zA-Z" + UNDERSCORE + DOLLAR + "]"; // letter , _ , $
	public static final String IDENTIFIER_AFTER_BEGINNING_CHAR		= "[" + WORD + UNDERSCORE + DOLLAR + "]"; // letter, digit, _ , $
	
	public static final String STOP_BEFORE_OPEN_BRACKET_CHAR = "(" + STOP_BEFORE + OPEN_BRACKET + ")"; // matcher will stop before open bracket
	
	public static final String IDENTIFIER = IDENTIFIER_BEGINNING_CHAR + IDENTIFIER_AFTER_BEGINNING_CHAR  + MULTIPLE_TIMES; 
	// longest sequence of identifier characters, with at least a character
	public static final String CAPTURE_IDENTIFIER = "(" + IDENTIFIER + ")";   
	
	public static final String CLASS_KEYWORD 						= "\\bclass\\b"; // word that starts and ends with class
	public static final String INTERFACE_KEYWORD 					= "\\binterface\\b"; // word that starts and ends with interface
	
	public static final String XML_CDATA_START = OPEN_ANGLE_PARENTHESIS + EXCLAMATION + OPEN_SQUARE_PARENTHESIS + "CDATA" + OPEN_SQUARE_PARENTHESIS; 	// <![CDATA[
	public static final String XML_CDATA_END = CLOSE_SQUARE_PARENTHESIS + CLOSE_ANGLE_PARENTHESIS + CLOSE_ANGLE_PARENTHESIS;							// ]]>
	
	// Regex comment utilities
	public static final String MULTI_LINE_COMMENT =	 // something like /* ... */
			SLASH + STAR + 											// start with /*
				ANY_CHAR + MULTIPLE_TIMES_END_AS_SOON_AS_POSSIBLE + 					// it needs greedy to match as soon as the */ is found
			STAR + SLASH;											// end with */
	
	public static final String XML_MULTI_LINE_COMMENT =	 // something like <!-- ... -->
			OPEN_ANGLE_PARENTHESIS + EXCLAMATION + MINUS + MINUS +		// start with <!--
				ANY_CHAR + MULTIPLE_TIMES_END_AS_SOON_AS_POSSIBLE + 					// it needs greedy to match as soon as the */ is found
			MINUS + MINUS + CLOSE_ANGLE_PARENTHESIS;					// end with -->
	
	public static final String SINGLE_LINE_COMMENT = // something like // .... \r\n
			SLASH + SLASH +  															// start with //
				"[" + EXCLUDE + SLASH_R + EXCLUDE + SLASH_N + "]" + MULTIPLE_TIMES + 	// any char except \r or \n
				SLASH_R + NOT_MORE_THAN_ONCE + SLASH_N + NOT_MORE_THAN_ONCE; 			// \r or \n or \r\n
	
	public static final String SPACE_OR_COMMENT = 
			"(" + DONT_CAPTURE
				+ WHITESPACE + "|" + MULTI_LINE_COMMENT + "|" + SINGLE_LINE_COMMENT 
			+ ")" + AT_LEAST_ONCE_END_AS_SOON_AS_POSSIBLE;

	public static final String SPACES_OR_COMMENTS_OPTIONAL = // tries to pass over all whitespaces or comments if there are any 
			"(" + DONT_CAPTURE
				+ WHITESPACE + "|" + 	MULTI_LINE_COMMENT + "|" + SINGLE_LINE_COMMENT 
				+ ")" + MULTIPLE_TIMES;
}
