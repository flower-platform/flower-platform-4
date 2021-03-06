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
package org.flowerplatform.flex_client.core {
	import org.flowerplatform.flexutil.FlexUtilConstants;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class CoreConstants {

		// Java and Flex
		/**
		 * @see CorePlugin#getNodeValuesProviderForMindMap
		 */
		public static const MIND_MAP_VALUES_PROVIDER_FEATURE_PREFIX:String = "mindMap.";

		// Flex only
		/**
		 * @see CorePlugin#getNodeValuesProviderForMindMap
		 */
		public static const MIND_MAP_FEATURE_FOR_VALUES_PROVIDER:String = "mindMapValuesProvider";
		
		//////////////////////////////////
		// Node types
		//////////////////////////////////
		
		public static const TYPE_KEY:String = "type";
		public static const FILE_SYSTEM_NODE_TYPE:String = "fileSystemNode";
		public static const FILE_NODE_TYPE:String = "fileNode";
		public static const ROOT_TYPE:String = "root";
		public static const REPOSITORY_TYPE:String = "repository";
		public static const CODE_TYPE:String = "code";
		
		// scheme for virtual (non-subscribable) nodes
		public static const VIRTUAL_NODE_SCHEME:String = "virtual";
		
		//////////////////////////////////
		// Node properties
		//////////////////////////////////
		
		public static const CHILDREN:String = "children";
		public static const HAS_CHILDREN:String = "hasChildren";
		public static const NAME:String = "name";
		
		public static const ICONS:String = "icons";
		public static const ICONS_SEPARATOR:String = ",";
		
		public static const IS_DIRTY:String = "isDirty";
		public static const SUBSCRIBABLE_RESOURCES:String = "subscribableResources";
		
		public static const USE_NODE_URI_ON_NEW_EDITOR:String = "useNodeUriOnNewEditor";
		public static const AUTO_SUBSCRIBE_ON_EXPAND:String = "autoSubscribeOnExpand";
		
		public static const IS_OPENABLE_IN_NEW_EDITOR:String = "isOpenableInNewEditor";
		
		public static const INSERT_BEFORE_FULL_NODE_ID:String = "insertBeforeFullNodeId"; 
		
		public static const PROPERTY_DESCRIPTOR:String = "propertyDescriptor";
		
		public static const PROPERTY_DEFAULT_FORMAT:String = "{0}.default";
		
		//////////////////////////////////
		// File node properties
		//////////////////////////////////
		
		public static const FILE_IS_DIRECTORY:String = "isDirectory";
		
		//////////////////////////////////
		// Context
		//////////////////////////////////
		
		public static const HANDLER:String = "handler";
		
		public static const POPULATE_WITH_PROPERTIES:String = "populateWithProperties";
		
		//////////////////////////////////
		// Controllers
		//////////////////////////////////
		
		// Generic value providers
		
		public static const NODE_TITLE_PROVIDER:String = "nodeTitleProvider";
		public static const NODE_ICONS_PROVIDER:String = "nodeIconProvider";
		
		//////////////////////////////////
		// Descriptors
		//////////////////////////////////
		
		// Typed descriptors
		
		public static const ADD_CHILD_DESCRIPTOR:String = "addChildDescriptor";
		public static const ACTION_DESCRIPTOR:String = "actionDescriptor";
		
		// Generic value descriptors
		
		public static const PROPERTY_FOR_TITLE_DESCRIPTOR:String = "propertyForTitleDescriptor";
		public static const PROPERTY_FOR_ICONS_DESCRIPTOR:String = "propertyForIconDescriptor";
		
		//////////////////////////////////
		// Resource
		//////////////////////////////////
		
		public static const CATEGORY_RESOURCE_PREFIX:String = FlexUtilConstants.CATEGORY_PREFIX + "resource.";
		
		//////////////////////////////////
		// Resource updates
		//////////////////////////////////
	
		public static const UPDATE_PROPERTY:String = "UPDATED";		
		public static const UPDATE_REQUEST_REFRESH:String = "REQUEST_REFRESH";		
		public static const UPDATE_CHILD_ADDED:String = "ADDED";
		public static const UPDATE_CHILD_REMOVED:String = "REMOVED";
		
		//////////////////////////////////
		// Remote method invocation params
		//////////////////////////////////
		
		public static const MESSAGE_RESULT:String = "messageResult";
		public static const RESOURCE_SETS:String = "resourceSets";
		public static const RESOURCE_URIS:String = "resourceUris";
		public static const RESOURCE_NODE_IDS_NOT_FOUND:String = "resourceNodeIdsNotFound";
		public static const LAST_UPDATE_TIMESTAMP:String = "timestampOfLastUpdate";
		public static const UPDATES:String = "updates";
		
		//////////////////////////////////
		// Command stack
		//////////////////////////////////

		public static const COMMAND_STACK:String = "commandStack";
		public static const COMMAND_TYPE:String = "command";
		
		//////////////////////////////////
		// Editor
		//////////////////////////////////
		
		public static const OPEN_RESOURCES:String = "openResources";
		public static const OPEN_RESOURCES_SELECT_RESOURCE_AT_INDEX:String = "selectResourceAtIndex";
		public static const OPEN_RESOURCES_SEPARATOR:String = ",";
		
		
		//////////////////////////////////
		// Global menu
		//////////////////////////////////
		
		public static const FILE_MENU_ID:String = "file";
		public static const NAVIGATE_MENU_ID:String = "navigate";
		public static const TOOLS_MENU_ID:String = "tools";
		public static const DEBUG:String = "debug";
		public static const HELP:String = "help";
		
		//////////////////////////////////
		// General
		//////////////////////////////////
		
		public static const FLOWER_PLATFORM:String = "flower-platform";
		
	}
}
