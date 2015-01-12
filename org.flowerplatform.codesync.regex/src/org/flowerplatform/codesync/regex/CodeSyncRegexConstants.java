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

	//////////////////////////////////
	// Node types and categories
	//////////////////////////////////
	
	public static final String REGEX_CONFIGS_NODE_TYPE = "regexConfigs";
	
	public static final String REGEX_CONFIG_TYPE = "regexConfig";
	
	public static final String REGEX_TYPE = "regex";
	public static final String REGEX_ACTION_TYPE = "regexAction";
	
	public static final String CATEGORY_REGEX = UtilConstants.CATEGORY_PREFIX + REGEX_TYPE;
	public static final String CATEGORY_REGEX_ACTION = UtilConstants.CATEGORY_PREFIX + REGEX_ACTION_TYPE;

	public static final String REGEX_MATCHES_TYPE = "regexMatches";
	public static final String REGEX_MATCH_TYPE = "regexMatch";
	public static final String REGEX_RESULT_TYPE = "result";
	public static final String VIRTUAL_REGEX_TYPE = "regexVirtual";
		
	public static final String REGEX_ACTIONS_DESCRIPTOR_TYPE = "RegexActions";
	public static final String ACTION_TYPE_KEEP_SPECIFIC_INFO = "keepSpecificInfoAction";
	public static final String ACTION_TYPE_CREATE_NODE = "createNodeAction";
	public static final String ACTION_TYPE_ATTACH_SPECIFIC_INFO = "attachSpecificInfoAction";
	public static final String ACTION_TYPE_CHECK_STATE = "checkStateAction";
	public static final String ACTION_TYPE_ENTER_STATE = "enterStateAction";
	public static final String ACTION_TYPE_EXIT_STATE = "exitStateAction";
	public static final String ACTION_TYPE_CLEAR_SPECIFIC_INFO = "clearSpecificInfoAction";
	public static final String ACTION_TYPE_ATTACH_NODE_TO_CURRENT_STATE_ACTION = "attachNodeToCurrentStateAction";
	public static final String ACTION_TYPE_INCREASE_NESTING_LEVEL = "increaseNestingLevelAction";
	public static final String ACTION_TYPE_DECREASE_NESTING_LEVEL = "decreaseNestingLevelAction";

	public static final String REGEX_CONFIGS_FOLDER = ".regex-configs";
	public static final String REGEX_CONFIG_FILE = ".regex-config";
	public static final String REGEX_TECHNOLOGY_NODE_TYPE = "regexTechnology";
	public static final String REGEXES_NODE_TYPE = "regExes";

	public static final String REGEX_TEST_FILES_NODE_TYPE = "testFiles";
	public static final String REGEX_TEST_FILES_FOLDER = "test-files";
	public static final String REGEX_TEST_FILE_NODE_TYPE = "testFile";

	public static final String REGEX_MATCH_FILES_FOLDER = "match-files";
	public static final String REGEX_RESULT_FILES_FOLDER = "result-files";
	public static final String REGEX_EXPECTED_MATCHES_FOLDER = "expected-matches";
	public static final String REGEX_EXPECTED_RESULTS_FOLDER = "expected-results";
	
	public static final String REGEX_MODEL_TREE_NODE_TYPE = "modelTree";
	public static final String REGEX_EXPECTED_MODEL_TREE_NODE_TYPE = "expectedModelTree";
	public static final String REGEX_MATCHES_NODE_TYPE = "matches";
	public static final String REGEX_EXPECTED_MATCHES_NODE_TYPE = "expectedMatches";
	
	//////////////////////////////////
	// File Extensions
	//////////////////////////////////
	
	public static final String REGEX_EXTENSION = ".regex";
	public static final String REGEX_MATCH_EXTENSION = ".regexMatches";
	public static final String REGEX_RESULT_EXTENSION = ".result";
	
	//////////////////////////////////
	// Node properties
	//////////////////////////////////
	
	public static final String REGEX_WITH_MACROS = "regex";
	public static final String FULL_REGEX = "fullRegex";

	public static final String ACTION_PROPERTY_INFO_KEY = "infoKey";
	public static final String ACTION_PROPERTY_INFO_IS_LIST = "isList";
	public static final String ACTION_PROPERTY_INFO_IS_CONTAINMENT = "isContainment";
	
	public static final String ACTION_PROPERTY_CREATE_NODE_PROPERTIES = "properties";
	public static final String ACTION_PROPERTY_CREATE_NODE_NEW_NODE_TYPE = "newNodeType";
	
	public static final String ACTION_PROPERTY_ENTER_STATE_IF_PROPERTY_SET = "property";
	
	public static final String ACTION_PROPERTY_VALID_STATES_PROPERTY = "validStates";
	
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
	
	//////////////////////////////////
	// Model tree creation
	//////////////////////////////////
	
	public static final String CURRENT_NODE = "currentNode";
	public static final String CURRENT_NESTING_LEVEL = "currentNestingLevel";
	public static final String STATE_STACK = "stateStack";
	
	private CodeSyncRegexConstants() {
	}
}
