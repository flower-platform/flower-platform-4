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
package org.flowerplatform.flex_client.codesync {
	import org.flowerplatform.flexutil.FlexUtilConstants;
	
	public class CodeSyncConstants {
		
		//////////////////////////////////
		// Node types and categories
		//////////////////////////////////

		public static const CODESYNC:String = "codesync";
		public static const CATEGORY_CODESYNC:String = FlexUtilConstants.CATEGORY_PREFIX + "codesync";
		
		public static const CODE_SYNC_CONFIG_ROOT:String = "codeSyncConfigRoot";
		
		//////////////////////////////////
		// Node properties
		//////////////////////////////////
		
		public static const SYNC:String = "sync";
		public static const CHILDREN_SYNC:String = "childrenSync";
		public static const CONFLICT:String = "conflict";
		public static const CHILDREN_CONFLICT:String = "childrenConflict";
		
		public static const CODE_SYNC_CONFIG_PROPERTY_DIRS_KEY:String = "codeSyncConfigDirsKey";
		
	}
}