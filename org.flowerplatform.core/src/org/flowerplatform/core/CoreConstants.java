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
package org.flowerplatform.core;

import static org.flowerplatform.util.UtilConstants.CATEGORY_PREFIX;

import org.flowerplatform.util.UtilConstants;

/**
 *@author Mariana Gheorghe
 **/
public final class CoreConstants {

	private CoreConstants() {
	}
	
	////////////////////////////////////////////////////////////////////
	// Constants Java and Flex
	////////////////////////////////////////////////////////////////////

	// from FlexDiagramConstants
	public static final String BASE_RENDERER_FONT_FAMILY = "baseRenderer.fontFamily";
	public static final String BASE_RENDERER_FONT_SIZE = "baseRenderer.fontSize";
	public static final String BASE_RENDERER_FONT_BOLD = "baseRenderer.fontBold";
	public static final String BASE_RENDERER_FONT_ITALIC = "baseRenderer.fontItalic";
	public static final String BASE_RENDERER_TEXT = "baseRenderer.text";
	public static final String BASE_RENDERER_TEXT_COLOR = "baseRenderer.textColor";
	public static final String BASE_RENDERER_BACKGROUND_COLOR = "baseRenderer.backgroundColor";
	public static final String BASE_RENDERER_ICONS = "baseRenderer.icons";
	public static final String BASE_RENDERER_MIN_WIDTH = "baseRenderer.minWidth";
	public static final String BASE_RENDERER_MAX_WIDTH = "baseRenderer.maxWidth";

	public static final String MIND_MAP_RENDERER_CLOUD_TYPE = "mindMapRenderer.cloudType";
	public static final String MIND_MAP_RENDERER_CLOUD_COLOR = "mindMapRenderer.cloudColor";
	public static final String MIND_MAP_RENDERER_HAS_CHILDREN = "mindMapRenderer.hasChildren";
	public static final String MIND_MAP_RENDERER_SIDE = "mindMapRenderer.side";

	// from MindMapConstants
	public static final String MIND_MAP_VALUES_PROVIDER_FEATURE_PREFIX = "mindMap.";
	public static final String FEATURE_SHOW_PROPERTIES_IN_RENDERER = "showPropertiesInRenderer";
	
	////////////////////////////////////////////////////////////////////
	// Constants only in Java (or before the constants cleaning up)
	////////////////////////////////////////////////////////////////////

	public static final String APP_VERSION = "0.1.1";
	public static final String API_VERSION = "0.1.0";
	
	/**
	 * When this class loads, the launcher code (from <code>FlowerFrameworkLauncher</code>) has already
	 * populated this property. So it's safe to use it as a constant throughout the whole plugins.
	 */
	public static final String FLOWER_PLATFORM_HOME = System.getProperty("FLOWER_PLATFORM_HOME");
	public static final String LOGBACK_CONFIG_FILE = "/logback.xml";
	public static final String DEFAULT_LOG_PATH = "META-INF" + LOGBACK_CONFIG_FILE;
	public static final String WORKSPACE = "/workspace";
	public static final String FLOWER_PLATFORM_WORKSPACE = FLOWER_PLATFORM_HOME + WORKSPACE;
	
	//////////////////////////////////
	// Node types and schemes
	//////////////////////////////////
	
	public static final String TYPE_KEY = "type";
	
	// scheme for virtual (non-subscribable) nodes
	public static final String VIRTUAL_NODE_SCHEME = "virtual";
	
	// type for virtual debug node
	public static final String DEBUG = "debug";
	
	// type for virtual root node
	public static final String ROOT_TYPE = "root";
	
	// type for virtual repository type
	public static final String REPOSITORY_TYPE = "repository";
	
	// file system scheme and types
	public static final String FILE_SCHEME = "file";
	public static final String FILE_SYSTEM_NODE_TYPE = "fileSystemNode";
	public static final String FILE_NODE_TYPE = "fileNode";
	public static final String FILE_CONTAINER_CATEGORY = CATEGORY_PREFIX + "fileContainer";
	
	public static final String COMMAND_TYPE = "command";
	public static final String COMMAND_STACK_TYPE = "commandStack";
	public static final String COMMAND_STACK_SCHEME = COMMAND_STACK_TYPE;
	
	public static final String PREFERENCE_TYPE = "preference";
	public static final String PREFERENCE_CATEGORY_TYPE = UtilConstants.CATEGORY_PREFIX + PREFERENCE_TYPE;
		
	//////////////////////////////////
	// Node properties
	//////////////////////////////////
	
	public static final String HAS_CHILDREN = "hasChildren";
	public static final String NAME = "name";
	public static final String ICONS = "icons";
	public static final String ICONS_SEPARATOR = ",";
	
	public static final String IS_DIRTY = "isDirty";
	public static final String RESOURCE_SET = "resourceSet";
	public static final String SUBSCRIBABLE_RESOURCES = "subscribableResources";
	
	public static final String USE_NODE_URI_ON_NEW_EDITOR = "useNodeUriOnNewEditor";
	public static final String AUTO_SUBSCRIBE_ON_EXPAND = "autoSubscribeOnExpand";
	
	public static final String IS_OPENABLE_IN_NEW_EDITOR = "isOpenableInNewEditor";
	
	public static final String INSERT_BEFORE_FULL_NODE_ID = "insertBeforeFullNodeId"; 
	
	public static final String PROPERTY_DEFAULT_FORMAT = "%s.default";

	public static final String CONFIG_SETTING_DISABLED = "configSettingDisabled";
	public static final String CATEGORY_CONFIG_SETTINGS = UtilConstants.CATEGORY_PREFIX + "configSettings";
	
	public static final String OVERWRITE_IF_NECESSARY = "overwriteIfNecessary";
	
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
	public static final String CONFIG_NODE_PROCESSOR = "configNodeProcessor";
	
	//////////////////////////////////
	// Descriptors
	//////////////////////////////////
	
	// Typed descriptors
	
	public static final String ADD_CHILD_DESCRIPTOR = "addChildDescriptor";
	public static final String MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR = "memberOfChildCategoryDescriptor";
	public static final String PROPERTY_DESCRIPTOR = "propertyDescriptor";
	
	// Property descriptors types

	public static final String PROPERTY_DESCRIPTOR_TYPE_NUMBER = "Number";
	public static final String PROPERTY_DESCRIPTOR_TYPE_NUMBER_STEPPER = "NumberStepper";
	public static final String PROPERTY_DESCRIPTOR_TYPE_DROP_DOWN_LIST = "DropDownList";
	public static final String PROPERTY_DESCRIPTOR_TYPE_COLOR_PICKER = "ColorPicker";
	public static final String PROPERTY_DESCRIPTOR_TYPE_DATE = "Date";
	public static final String PROPERTY_DESCRIPTOR_DEFAULT_CATEGORY = "";

	public static final String PROPERTY_LINE_RENDERER_TYPE_DEFAULT = "Default";
	public static final String PROPERTY_LINE_RENDERER_TYPE_STYLABLE = "Stylable";
	public static final String PROPERTY_LINE_RENDERER_TYPE_PREFERENCE = "Preference";
		
	//////////////////////////////////
	// Service context options
	//////////////////////////////////
	
	/**
	 * An additive controller may set this option, to stop the service from invoking any controllers
	 * with a higher order index.
	 */
	public static final String DONT_PROCESS_OTHER_CONTROLLERS = "dontProcessOtherControllers";
	
	public static final String INVOKE_ONLY_CONTROLLERS_WITH_CLASSES = "invokeOnlyControllersWithClasses";
	
	public static final String POPULATE_WITH_PROPERTIES = "populateWithProperties";
	
	public static final String POPULATE_WITH_PROPERTIES_FORCEFULLY = "populateWithPropertiesForcefully";
	
	/**
	 * An additive controller may set this option, to ask the {@link UpdatePropertySetterController} to use
	 * the node given as parameter as the resourceNode where the update will be saved.
	 * 
	 * <p>
	 * An example is {@link CoreConstants#IS_DIRTY}: if option not set, the update will be registered on parent resource -> bad.
	 */
	public static final String NODE_IS_RESOURCE_NODE = "nodeIsResourceNode";

	/**
	 * Used to save the last value of a node's property in context, before the property is updated 
	 * @author Claudiu Matei
	 */
	public static final String OLD_VALUES = "oldValue";

	//////////////////////////////////
	// Resource
	//////////////////////////////////
	
	public static final String SELF_RESOURCE = "self";
	public static final String CATEGORY_RESOURCE_PREFIX = UtilConstants.CATEGORY_PREFIX + "resource.";
	
	//////////////////////////////////
	// Updates
	//////////////////////////////////
	
	public static final String UPDATE_PROPERTY = "UPDATED";
	public static final String UPDATE_CHILD_ADDED = "ADDED";
	public static final String UPDATE_CHILD_REMOVED = "REMOVED";
	public static final String UPDATE_REQUEST_REFRESH = "REQUEST_REFRESH";
	
	//////////////////////////////////
	// Content types
	//////////////////////////////////
	
	public static final String ALL = "all";
	
	//////////////////////////////////
	// Remote method invocation params
	//////////////////////////////////
	
	public static final String MESSAGE_RESULT = "messageResult";
	public static final String RESOURCE_SETS = "resourceSets";
	public static final String RESOURCE_URIS = "resourceUris";
	public static final String RESOURCE_NODE_IDS_NOT_FOUND = "resourceNodeIdsNotFound";
	public static final String LAST_UPDATE_TIMESTAMP = "timestampOfLastUpdate";
	public static final String UPDATES = "updates";
	
	//////////////////////////////////
	// Others
	//////////////////////////////////
	
	public static final String METADATA = ".metadata";
	
	public static final String DEFAULT_SUFFIX = ".default";
	public static final String GLOBAL_SUFFIX = ".global";
	public static final String USER_SUFFIX = ".user";

	public static final String PNG_EXTENSION = ".png";
	
	public static final String LOAD_FILE_SERVLET = UtilConstants.SERVLET + "/load";
	
}
