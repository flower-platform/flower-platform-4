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

import static org.flowerplatform.tests.regex.RegexUtil.CLASS_KEYWORD;
import static org.flowerplatform.tests.regex.RegexUtil.CLOSE_ANGLE_PARENTHESIS;
import static org.flowerplatform.tests.regex.RegexUtil.COMMA;
import static org.flowerplatform.tests.regex.RegexUtil.DONT_CAPTURE;
import static org.flowerplatform.tests.regex.RegexUtil.EXCLUDE;
import static org.flowerplatform.tests.regex.RegexUtil.IDENTIFIER_AFTER_BEGINNING_CHAR;
import static org.flowerplatform.tests.regex.RegexUtil.INTERFACE_KEYWORD;
import static org.flowerplatform.tests.regex.RegexUtil.MULTIPLE_TIMES;
import static org.flowerplatform.tests.regex.RegexUtil.MULTI_LINE_COMMENT;
import static org.flowerplatform.tests.regex.RegexUtil.OPEN_ANGLE_PARENTHESIS;
import static org.flowerplatform.tests.regex.RegexUtil.OPEN_BRACKET;
import static org.flowerplatform.tests.regex.RegexUtil.SINGLE_LINE_COMMENT;
import static org.flowerplatform.tests.regex.RegexUtil.SLASH;
import static org.flowerplatform.tests.regex.RegexUtil.SPACES_OR_COMMENTS_OPTIONAL;
import static org.flowerplatform.tests.regex.RegexUtil.SPACE_OR_COMMENT;
import static org.flowerplatform.tests.regex.RegexUtil.STAR;
import static org.flowerplatform.tests.regex.RegexUtil.STOP_BEFORE_OPEN_BRACKET_CHAR;

import java.util.regex.Pattern;

import org.flowerplatform.util.regex.IfFindThisAnnounceMatchCandidate;
import org.flowerplatform.util.regex.IfFindThisModifyNesting;
import org.flowerplatform.util.regex.IfFindThisSkip;
import org.flowerplatform.util.regex.RegexConfiguration;
import org.flowerplatform.util.regex.UntilFoundThisIgnoreAll;

/**
 * @author Cristi
 * @author Sorin
 */
public class JavaRegexConfigurationProvider {
	
	public static final String ATTRIBUTE_CATEGORY = "attribute";
	public static final String METHOD_CATEGORY = "method";
	
	private static final int JAVA_NEXTING_LEVEL_FOR_DECLARATIONS = 1; // must pass "class {"
	
	private static final String NEW_KEYWORD 						= "\\bnew\\b"; // word that starts and ends with new
	
	private static final String regExDataType = "\\w[\\w\\[\\]\\<\\>]*?"; // TODO HashMap X < X Integer X , X String X > X
	
	// TODO de facut java type begin in loc de ce exitsta mometan ca nu se supporta interfete, <,> fq com.crispico
	
	private static final String JAVA_TYPE_BEGIN = // something like class ... {
			"(" + DONT_CAPTURE				
				+ CLASS_KEYWORD + "|" + INTERFACE_KEYWORD +										// class or interface 
			")"	+ 																				 
			"(" + DONT_CAPTURE + 																// possibly multiple comments or identifiers or commas or unghiular parentheses
				SPACE_OR_COMMENT + "|" +																			// comment  
				"[" + 
					IDENTIFIER_AFTER_BEGINNING_CHAR + COMMA + OPEN_ANGLE_PARENTHESIS + CLOSE_ANGLE_PARENTHESIS +	// every identifier or enumeration character or generic character
					EXCLUDE + OPEN_BRACKET + EXCLUDE + SLASH + EXCLUDE + STAR +  									// except comment and bracket, because comment is processed as a hole and bracket is the condition to stop.
				"]" +   
			")"	+ MULTIPLE_TIMES +
			STOP_BEFORE_OPEN_BRACKET_CHAR; 	
			
	private static final String JAVA_ATTRIBUTE =			 
			regExDataType + // data type of the attribute 
			SPACE_OR_COMMENT + 											// TODO aici ar trebuie space or comment la fel optional nu neaparat sa existe?
			"(\\w[\\w]*?)" + SPACES_OR_COMMENTS_OPTIONAL + "(?:\\[\\])?" + // name of the attribute; the 2nd part is for int attr[]; or int attr [];
			SPACES_OR_COMMENTS_OPTIONAL + 
			"(?:;|=)";
	
	private static final String JAVA_METHOD =
			regExDataType + // data type of the attribute 
			SPACE_OR_COMMENT + 
			"(\\w[\\w]*?)" + // name of the method
			SPACES_OR_COMMENTS_OPTIONAL +
			"\\(([^\\)]*?)\\)";	
	
	public static void buildJavaConfiguration(RegexConfiguration config) {
		config
		.add(new UntilFoundThisIgnoreAll("Begining of type",  CLASS_KEYWORD + "[\\w\\s\\<\\>]*?" + STOP_BEFORE_OPEN_BRACKET_CHAR)) // TODO CS/RE de adaugat si posibilitatea de coment
		.add(new IfFindThisSkip("Multi-line comment", MULTI_LINE_COMMENT))
		.add(new IfFindThisSkip("Single-line comment", SINGLE_LINE_COMMENT))
		.add(new IfFindThisSkip("new keyword", NEW_KEYWORD))
		.add(new IfFindThisModifyNesting("Opening curly bracket", "\\{", 1))
		.add(new IfFindThisModifyNesting("Closing curly bracket", "\\}", -1))
		.add(new IfFindThisAnnounceMatchCandidate(ATTRIBUTE_CATEGORY, JAVA_ATTRIBUTE, ATTRIBUTE_CATEGORY))
		.add(new IfFindThisAnnounceMatchCandidate(METHOD_CATEGORY, JAVA_METHOD, METHOD_CATEGORY))
		.setTargetNestingForMatches(JAVA_NEXTING_LEVEL_FOR_DECLARATIONS)
		.compile(Pattern.DOTALL);
	}
		
}