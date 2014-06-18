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
	
	public static final String CATEGORY_CODESYNC = UtilConstants.CATEGORY_PREFIX + "codesync";
	
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