package org.flowerplatform.flex_client.core {
	import org.flowerplatform.flexutil.FlexUtilConstants;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class CoreConstants {
		
		//////////////////////////////////
		// Node types
		//////////////////////////////////
		
		public static const FILE_NODE_TYPE:String = "fileNode";
		
		//////////////////////////////////
		// Node properties
		//////////////////////////////////
		
		public static const CHILDREN:String = "children";
		public static const HAS_CHILDREN:String = "hasChildren";
		public static const NAME:String = "name";
		
		public static const ICONS:String = "icons";
		public static const ICONS_SEPARATOR:String = ",";
		
		public static const CONTENT_TYPE:String = "contentType";
		public static const HIDE_ROOT_NODE:String = "hideRootNode";
		
		public static const IS_SUBSCRIBABLE:String = "isSubscribable";
		public static const IS_DIRTY:String = "isDirty";
		
		public static const IS_OPENABLE_IN_NEW_EDITOR:String = "isOpenableInNewEditor";
		
		//////////////////////////////////
		// File node properties
		//////////////////////////////////
		
		public static const FILE_IS_DIRECTORY:String = "isDirectory";
		
		//////////////////////////////////
		// Controllers
		//////////////////////////////////
		
		// Generic value providers
		
		public static const NODE_TITLE_PROVIDER:String = "nodeTitleProvider";
		public static const NODE_ICONS_PROVIDER:String = "nodeIconProvider";
		public static const NODE_SIDE_PROVIDER:String = "nodeSideProvider";
		
		//////////////////////////////////
		// Descriptors
		//////////////////////////////////
		
		// Typed descriptors
		
		public static const ADD_CHILD_DESCRIPTOR:String = "addChildDescriptor";
		
		// Generic value descriptors
		
		public static const PROPERTY_FOR_TITLE_DESCRIPTOR:String = "propertyForTitleDescriptor";
		public static const PROPERTY_FOR_ICONS_DESCRIPTOR:String = "propertyForIconDescriptor";
		public static const PROPERTY_FOR_SIDE_DESCRIPTOR:String = "propertyForSideDescriptor";
		
		//////////////////////////////////
		// Resource
		//////////////////////////////////
		
		public static const CATEGORY_RESOURCE_PREFIX:String = FlexUtilConstants.CATEGORY_PREFIX + "resource.";
		
		//////////////////////////////////
		// Resource updates
		//////////////////////////////////
	
		public static const UPDATE_CHILD_ADDED:String = "ADDED";
		public static const UPDATE_CHILD_REMOVED:String = "REMOVED";
		
		//////////////////////////////////
		// Remote method invocation params
		//////////////////////////////////
		
		public static const MESSAGE_RESULT:String = "messageResult";
		public static const RESOURCE_NODE_IDS:String = "resourceNodeIds";
		public static const RESOURCE_NODE_IDS_NOT_FOUND:String = "resourceNodeIdsNotFound";
		public static const LAST_UPDATE_TIMESTAMP:String = "timestampOfLastUpdate";
		public static const UPDATES:String = "updates";
		
		//////////////////////////////////
		// Editor
		//////////////////////////////////
		
		public static const OPEN_RESOURCES:String = "openResources";
		public static const OPEN_RESOURCES_SELECT_RESOURCE_AT_INDEX:String = "selectResourceAtIndex";
		public static const OPEN_RESOURCES_SEPARATOR:String = ",";
		
		public static const TEXT_CONTENT_TYPE:String = "text";
		
		//////////////////////////////////
		// Global menu
		//////////////////////////////////
		
		public static const FILE_MENU_ID:String = "file";
		public static const NAVIGATE_MENU_ID:String = "navigate";
		public static const DEBUG:String = "debug";
		
		//////////////////////////////////
		// General
		//////////////////////////////////
		
		public static const FLOWER_PLATFORM:String = "flower-platform";
	}
}
