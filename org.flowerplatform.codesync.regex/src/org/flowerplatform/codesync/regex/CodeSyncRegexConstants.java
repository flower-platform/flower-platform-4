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
	public static final String REGEX_ACTION_TYPE = "regexAction";
	public static final String REGEX_MACRO_TYPE = "regexMacro";
	
	public static final String CATEGORY_REGEX = UtilConstants.CATEGORY_PREFIX + REGEX_TYPE;
	public static final String CATEGORY_REGEX_ACTION = UtilConstants.CATEGORY_PREFIX + REGEX_ACTION_TYPE;

	public static final String REGEX_MATCHES_TYPE = "regexMatches";
	public static final String REGEX_MATCH_TYPE = "regexMatch";
	public static final String VIRTUAL_REGEX_TYPE = "regexVirtual";
		
	public static final String REGEX_ACTIONS_DESCRIPTOR_TYPE = "RegexActions";
	public static final String ACTION_TYPE_KEEP_SPECIFIC_INFO = "KeepSpecificInfoAction";
	public static final String ACTION_TYPE_CREATE_NODE = "CreateNodeAction";
	public static final String ACTION_TYPE_ATTACH_SPECIFIC_INFO = "AttachSpecificInfoAction";
	public static final String ACTION_TYPE_CHECK_STATE = "CheckStateAction";
	public static final String ACTION_TYPE_ENTER_STATE = "EnterStateAction";
	public static final String ACTION_TYPE_EXIT_STATE = "ExistStateAction";
	public static final String ACTION_TYPE_CLEAR_SPECIFIC_INFO = "ClearSpecificInfoAction";
	public static final String ACTION_TYPE_ATTACH_NODE_TO_CURRENT_STATE_ACTION = "AttachNodeToCurrentStateAction";
	public static final String ACTION_TYPE_INCREASE_NESTING_LEVEL = "IncreaseNestingLevelAction";
	public static final String ACTION_TYPE_DECREASE_NESTING_LEVEL = "DecreaseNestingLevelAction";

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

	public static final String ACTION_TYPE_CREATE_NODE_PROPERTIES = "properties";
	public static final String ACTION_TYPE_CREATE_NODE_NEW_NODE_TYPE = "newNodeType";
	public static final String ACTION_TYPE_KEEP_SPECIFIC_INFO_KEY_PROPERTY = "keepInfoKey";
	public static final String ACTION_TYPE_KEEP_SPECIFIC_INFO_IS_LIST_PROPERTY = "isList";
	public static final String ACTION_TYPE_KEEP_SPECIFIC_INFO_IS_CONTAINMENT_PROPERTY = "keepIsContainment";
	public static final String ACTION_TYPE_ATTACH_SPECIFIC_INFO_KEY_PROPERTY = "attachInfoKey";
	public static final String ACTION_TYPE_ATTACH_SPECIFIC_INFO_IS_CONTAINMENT_PROPERTY = "attachIsContainment";
	public static final String ACTION_TYPE_VALID_STATES_PROPERTY = "validStates";
	public static final String ACTION_TYPE_CLEAR_SPECIFIC_INFO_KEY_PROPERTY = "clearInfoKey";

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
