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
package org.flowerplatform.codesync;

import org.flowerplatform.util.UtilConstants;


/**
 * @author Mariana Gheorghe
 */
public class CodeSyncConstants {

	//////////////////////////////////
	// Node types and categories
	//////////////////////////////////
	
	public static final String CODESYNC = "codesync";
	public static final String CATEGORY_CODESYNC = UtilConstants.CATEGORY_PREFIX + CODESYNC;
	public static final String CODESYNC_FILE = getFileExtension(CODESYNC);
	
	public static final String MDA = "mda";
	public static final String MDA_FILE = getFileExtension(MDA);
	
	public static final String CODESYNC_ROOT = "codeSyncRoot";
	public static final String MDA_ROOT = "mdaRoot";
	
	public static final String DIAGRAM = "diagram";
	public static final String DIAGRAM_EXTENSION = getFileExtension(DIAGRAM);
	
	public static final String MATCH = "match";
			
	private static final String getFileExtension(String extension) {
		return "." + extension;
	}
	
	public static final String CATEGORY_MODEL = UtilConstants.CATEGORY_PREFIX + "model";
	
	//////////////////////////////////
	// Controllers
	//////////////////////////////////
	
	public static final String MODEL_ADAPTER_ANCESTOR = "modelAdapterAncestor";
	public static final String MODEL_ADAPTER_LEFT = "modelAdapterLeft";
	public static final String MODEL_ADAPTER_RIGHT = "modelAdapterRight";
	public static final String FEATURE_PROVIDER = "featureProvider";
	
	//////////////////////////////////
	// Node properties
	//////////////////////////////////
	
	public static final String REMOVED = "removed";
	public static final String ADDED = "added";
	public static final String SYNC = "sync";
	public static final String CHILDREN_SYNC = "childrenSync";
	public static final String CONFLICT = "conflict";
	public static final String CHILDREN_CONFLICT = "childrenConflict";
	
	public static final String ORIGINAL_SUFFIX = ".original";
	public static final String CONFLICT_SUFFIX = ".conflict";
	
	public static final String CHILDREN = "children";
	
	public static final String MATCH_TYPE ="matchType";
	public static final String MATCH_FEATURE = "feature";
	public static final String MATCH_MODEL_ELEMENT_TYPE = "modeElementType";
	public static final String MATCH_CHILDREN_MODIFIED_LEFT = "childrenModifiedLeft";
	public static final String MATCH_CHILDREN_MODIFIED_RIGHT = "childrenModifiedRight";
	public static final String MATCH_CHILDREN_CONFLICT = "childrenConflict";
	public static final String MATCH_DIFFS_MODIFIED_LEFT = "diffsModifiedLeft";
	public static final String MATCH_DIFFS_MODIFIED_RIGHT = "diffsModifiedRight";
	public static final String MATCH_DIFFS_CONFLICT = "diffsConflict";
	public static final String MATCH_BODY_MODIFIED = "bodyModified";
	
	//////////////////////////////////
	// Features
	//////////////////////////////////
	
	public static final int FEATURE_TYPE_DONT_PROCESS = 0;
	public static final int FEATURE_TYPE_CONTAINMENT = 1;
	public static final int FEATURE_TYPE_VALUE = 2;
	public static final int FEATURE_TYPE_REFERENCE = 3;
	public static final String FLOWER_UID = "@flowerUID";
	
	/**
	 * The value used in case the model adapter does not know about its value.
	 * It is ignored at equality check, i.e. it equals any other value, including null.
	 * 
	 * @author Mariana
	 */
	public static final String UNDEFINED = "UNDEFINED_VALUE";
	
}
