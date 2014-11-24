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
package org.flowerplatform.codesync.sdiff;

import org.flowerplatform.util.UtilConstants;

/**
 *@author Mariana Gheorghe
 **/
public final class CodeSyncSdiffConstants {

	private CodeSyncSdiffConstants() {
	}
	//////////////////////////////////
	// Node types and categories
	//////////////////////////////////
	
	public static final String STRUCTURE_DIFF = "structureDiff";
	public static final String STRUCTURE_DIFF_EXTENSION = ".sdiff";
	public static final String COMMENT = "sdiffComment";
	public static final String STRUCTURE_DIFF_LEGEND = "structureDiffLegend";
	public static final String STRUCTURE_DIFF_LEGEND_CHILD = "structureDiffLegendChild";
	
	public static final String ADDED = "added";
	public static final String REMOVED = "removed";
	public static final String MODIFIED = "modified";
	public static final String MODIFIED_BODY = "bodyModified";
	public static final String MODIFIED_CHILDREN = "childrenModified";
	
	public static final String ADDED_COMMENT = "comment";
	public static final String CONTAINS_COMMENT = "containsComment";
	public static final String CATEGORY_CAN_CONTAIN_COMMENT = UtilConstants.CATEGORY_PREFIX + "canContainComment";
	
	// relative to repository
	public static final String STRUCTURE_DIFFS_FOLDER = "sdiffs";

	//////////////////////////////////
	// Node properties
	//////////////////////////////////
	
	public static final String MATCH_COLOR_ADDED = "#33FF33";
	public static final String MATCH_COLOR_REMOVED = "#FF5858";
	public static final String MATCH_COLOR_PROP_MODIFIED = "#9999FF";
	public static final String MATCH_COLOR_BODY_MODIFIED = "#FF00FF";
	public static final String MATCH_COLOR_CHILDREN_MODIFIED = "#FFCC00";
	public static final String MATCH_COLOR_COMMENT = "#FFFF00";
	public static final String PROPERTY_NAME_WITH_PATH = "nameWithPath";
		
	// dirty marker for comments
	public static final String NODE_URI_TO_BE_IGNORED = "nodeURIToBeIgnored";

	//////////////////////////////////
	// Service context options
	//////////////////////////////////
	
	public static final String SKIP_MATCH_CHILDREN_PROVIDER = "skipMatchChildrenProvider";
	
}
