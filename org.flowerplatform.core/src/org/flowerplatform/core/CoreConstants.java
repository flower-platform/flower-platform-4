package org.flowerplatform.core;

import org.flowerplatform.util.UtilConstants;

public class CoreConstants {

	public static final String APP_VERSION = "1.0.0.M1_2014-04-01";
	public static final String API_VERSION = "1.0.0.M1_2014-04-01";
	
	//////////////////////////////////
	// Node types
	//////////////////////////////////
	
	public static final String TYPE_KEY = "type";
	public static final String FILE_SYSTEM_NODE_TYPE = "fileSystem";
	public static final String FILE_NODE_TYPE = "fileNode";
	public static final String ROOT_TYPE = "root";
	public static final String REPOSITORY_TYPE = "repository";
	public static final String CODE_TYPE = "code";
	
	public final static String DEBUG = "debug";
	
	//////////////////////////////////
	// Node properties
	//////////////////////////////////
	
	public static final String HAS_CHILDREN = "hasChildren";
	public static final String NAME = "name";
	
	public static final String ICONS = "icons";
	public static final String ICONS_SEPARATOR = ",";
	
	public static final String CONTENT_TYPE = "contentType";
	public static final String HIDE_ROOT_NODE = "hideRootNode";
	
	public static final String IS_DIRTY = "isDirty";
	public static final String SUBSCRIBABLE_RESOURCES = "subscribableResources";
	
	// TODO delete
	public static final String IS_SUBSCRIBABLE = "isSubscribable";
	
	public static final String IS_OPENABLE_IN_NEW_EDITOR = "isOpenableInNewEditor";
	
	public static final String INSERT_BEFORE_FULL_NODE_ID = "insertBeforeFullNodeId"; 
	
	public static final String SIDE = "side";
	public static final int POSITION_LEFT = -1;
	public static final int POSITION_RIGHT = 1;
	
	public static final String PROPERTY_DEFAULT_FORMAT = "%s.default";
	
	//////////////////////////////////
	// File node properties
	//////////////////////////////////
	
	public static final String FILE_SIZE = "size";
	public static final String FILE_IS_DIRECTORY = "isDirectory";
	public static final String FILE_CREATION_TIME = "creationTime";
	public static final String FILE_LAST_MODIFIED_TIME = "lastModifiedTime";
	public static final String FILE_LAST_ACCESS_TIME = "lastAccessTime";
	
	//////////////////////////////////
	// Controllers
	//////////////////////////////////
	
	public static final String ADD_NODE_CONTROLLER = "addNodeController";
	public static final String CHILDREN_PROVIDER = "childrenProvider";
	public static final String DEFAULT_PROPERTY_PROVIDER = "defaultPropertyProvider";
	public static final String PARENT_PROVIDER = "parentProvider";
	public static final String PROPERTIES_PROVIDER = "propertiesProvider";
	public static final String PROPERTY_SETTER = "propertySetter";
	public static final String RAW_NODE_DATA_PROVIDER = "rawNodeDataProvider";
	public static final String REMOVE_NODE_CONTROLLER = "removeNodeController";
	public static final String RESOURCE_ACCESS_CONTROLLER = "resourceAccessController";
	
	//////////////////////////////////
	// Descriptors
	//////////////////////////////////
	
	// Typed descriptors
	
	public static final String ADD_CHILD_DESCRIPTOR = "addChildDescriptor";
	public static final String MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR = "memberOfChildCategoryDescriptor";
	public static final String PROPERTY_DESCRIPTOR = "propertyDescriptor";
	
		// Property descriptors types
	
		public static final String PROPERTY_DESCRIPTOR_TYPE_STRING = "String";
		public static final String PROPERTY_DESCRIPTOR_TYPE_BOOLEAN = "Boolean";
		public static final String PROPERTY_DESCRIPTOR_TYPE_NUMBER = "Number";
		public static final String PROPERTY_DESCRIPTOR_TYPE_NUMBER_STEPPER = "NumberStepper";
		public static final String PROPERTY_DESCRIPTOR_TYPE_DROP_DOWN_LIST = "DropDownList";
		public static final String PROPERTY_DESCRIPTOR_TYPE_COLOR_PICKER = "ColorPicker";
		public static final String PROPERTY_DESCRIPTOR_DEFAULT_CATEGORY = "";
	
	// Generic value descriptors
	
	public static final String PROPERTY_FOR_TITLE_DESCRIPTOR = "propertyForTitleDescriptor";
	public static final String PROPERTY_FOR_ICON_DESCRIPTOR = "propertyForIconDescriptor";
	public static final String PROPERTY_FOR_SIDE_DESCRIPTOR = "propertyForSideDescriptor";
	
	//////////////////////////////////
	// Service context options
	//////////////////////////////////
	
	/**
	 * An additive controller may set this option, to stop the service from invoking any controllers
	 * with a higher order index.
	 */
	public static final String DONT_PROCESS_OTHER_CONTROLLERS = "dontProcessOtherControllers";
	
	/**
	 * All controllers except the updater controllers should check this option and not execute their logic
	 * if this option is set to true (e.g. persistence controllers should not set the <code>IS_DIRTY</code>
	 * property, but the updater controller must register the update).
	 */
	public static final String EXECUTE_ONLY_FOR_UPDATER = "executeOnlyForUpdater";
	
	public static final String POPULATE_WITH_PROPERTIES = "populateWithProperties";
	
	/**
	 * An additive controller may set this option, to ask the {@link UpdatePropertySetterController} to use
	 * the node given as parameter as the resourceNode where the update will be saved.
	 * 
	 * <p>
	 * An example is {@link CoreConstants#IS_DIRTY}: if option not set, the update will be registered on parent resource -> bad.
	 */
	public static final String NODE_IS_RESOURCE_NODE = "nodeIsResourceNode";
	
	//////////////////////////////////
	// Resource
	//////////////////////////////////
	
	public static final String SELF_RESOURCE = "self";
	public static final String CATEGORY_RESOURCE_PREFIX = UtilConstants.CATEGORY_PREFIX + "resource.";
	
	//////////////////////////////////
	// Updates
	//////////////////////////////////
	
	public static final String UPDATE_CHILD_ADDED = "ADDED";
	public static final String UPDATE_CHILD_REMOVED = "REMOVED";
	
	//////////////////////////////////
	// Content types
	//////////////////////////////////
	
	public static final String TEXT_CONTENT_TYPE = "text";
	
	//////////////////////////////////
	// Remote method invocation params
	//////////////////////////////////
	
	public static final String MESSAGE_RESULT = "messageResult";
	public static final String RESOURCE_NODE_IDS = "resourceNodeIds";
	public static final String RESOURCE_NODE_IDS_NOT_FOUND = "resourceNodeIdsNotFound";
	public static final String LAST_UPDATE_TIMESTAMP = "timestampOfLastUpdate";
	public static final String UPDATES = "updates";
	
	//////////////////////////////////
	// Others
	//////////////////////////////////
	
	public static final String METADATA = ".metadata";
		
}
