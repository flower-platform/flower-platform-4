/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
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

public class CodeSyncSdiffConstants {

	//////////////////////////////////
	// Node types and categories
	//////////////////////////////////
	
	public static final String STRUCTURE_DIFF = "structureDiff";
	public static final String STRUCTURE_DIFF_EXTENSION = ".sdiff";
	
	public static final String COMMENT = "sdiffComment";
	
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
	
	//////////////////////////////////
	// Service context options
	//////////////////////////////////
	
	public static final String SKIP_MATCH_CHILDREN_PROVIDER = "skipMatchChildrenProvider";
	
}