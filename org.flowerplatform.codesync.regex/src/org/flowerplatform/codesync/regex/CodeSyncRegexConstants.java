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
package org.flowerplatform.codesync.regex;

import org.flowerplatform.util.UtilConstants;

/**
 * @author Cristina Constantinescu
 */
public final class CodeSyncRegexConstants {

	private CodeSyncRegexConstants() {
	}
	//////////////////////////////////
	// Node types and categories
	//////////////////////////////////
	
	public static final String REGEX_CONFIG_TYPE = "regexConfig";
	
	public static final String REGEX_TYPE = "regex";
	public static final String REGEX_MACRO_TYPE = "regexMacro";
	
	public static final String CATEGORY_REGEX = UtilConstants.CATEGORY_PREFIX + REGEX_TYPE;
	
	public static final String REGEX_MATCHES_TYPE = "regexMatches";	
	public static final String REGEX_MATCH_TYPE = "regexMatch";
	public static final String VIRTUAL_REGEX_TYPE = "regexVirtual";
		
	public static final String REGEX_ACTIONS_DESCRIPTOR_TYPE = "RegexActions";
	
	//////////////////////////////////
	// File Extensions
	//////////////////////////////////
	
	public static final String REGEX_EXTENSION = ".regex";
	public static final String REGEX_MATCH_EXTENSION = ".regexMatches";
	
	//////////////////////////////////
	// Node properties
	//////////////////////////////////
	
	public static final String REGEX_WITH_MACROS = "regex";
	public static final String FULL_REGEX = "fullRegex";
	public static final String ACTION = "action";
	
	public static final String START = "start";
	public static final String START_L = "startLine";
	public static final String START_C = "startChar";
	public static final String END = "end";
	public static final String END_L = "endLine";
	public static final String END_C = "endChar";
	
	public static final String REGEX_NAME = "regexName";
	
	public static final String RESOURCE_URI = "resourceUri";
	
	//////////////////////////////////
	// Service context options
	//////////////////////////////////
	
	public static final String SKIP_PROVIDER = "skipProvider";
	public static final String SHOW_GROUPED_BY_REGEX = "showGroupedByRegex";
	
}