package org.flowerplatform.util;

import java.io.File;

public class UtilConstants {

	public static final String FLOWER_PLATFORM = "flower-platform";
	
	//////////////////////////////////
	// Resources
	//////////////////////////////////
	
	public static final String PUBLIC_RESOURCES_DIR = "public-resources";
	public static final String PUBLIC_RESOURCES_PATH_PREFIX = "/public-resources";
	
	public static final String MESSAGES_FILE = "messages.properties";
	
	public static final String IMAGE_COMPOSER_SERVLET = "servlet/image-composer/";
	public static final String IMAGE_COMPOSER_PATH_PREFIX = "/image-composer";
	
	public static final File TEMP_FOLDER = new File(System.getProperty("java.io.tmpdir"), FLOWER_PLATFORM);
	public static final char RESOURCE_PATH_SEPARATOR = '|';
	
	//////////////////////////////////
	// Categories
	//////////////////////////////////
	
	public static final String CATEGORY_PREFIX = "category.";
	public static final String CATEGORY_ALL = CATEGORY_PREFIX + "all";
	
}
