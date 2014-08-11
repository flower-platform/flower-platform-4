package org.flowerplatform.codesync.regex;

import org.flowerplatform.util.UtilConstants;

/**
 * @author Cristina Constantinescu
 */
public class CodeSyncRegexConstants {

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
	public static final String ACTION_TYPE_KEEP_SPECIFIC_INFO = "KeepSpecificInfoAction";
	public static final String ACTION_TYPE_CREATE_NODE = "CreateNodeAction";
	public static final String ACTION_TYPE_ATTACH_SPECIFIC_INFO = "AttachSpecificInfoAction";

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
//	public static final String ACTION = "action";
	
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
