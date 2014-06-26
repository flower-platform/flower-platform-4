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
