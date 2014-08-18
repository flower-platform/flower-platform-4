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
package org.flowerplatform.util;

import java.io.File;

/**
 * @author Mariana Gheorghe
 */
public final class UtilConstants {
	
	private UtilConstants() {
	}

	public static final String FLOWER_PLATFORM = "flower-platform";
	
	//////////////////////////////////
	// Resources
	//////////////////////////////////
	
	public static final String SERVLET = "servlet";
	
	public static final String PUBLIC_RESOURCES_DIR = "public-resources";
	public static final String PUBLIC_RESOURCES_PATH_PREFIX = "/public-resources";
	public static final String PUBLIC_RESOURCES_SERVLET = SERVLET + PUBLIC_RESOURCES_PATH_PREFIX;
	
	public static final String MESSAGES_FILE = "messages.properties";
		
	public static final String IMAGE_COMPOSER_SERVLET = SERVLET + "/image-composer/";
	public static final String IMAGE_COMPOSER_PATH_PREFIX = "/image-composer";
	
	public static final File TEMP_FOLDER = new File(System.getProperty("java.io.tmpdir"), FLOWER_PLATFORM);
	public static final char RESOURCE_PATH_SEPARATOR = '|';
	
	//////////////////////////////////
	// Categories
	//////////////////////////////////
	
	public static final String CATEGORY_PREFIX = "category.";
	public static final String CATEGORY_ALL = CATEGORY_PREFIX + "all";
	
}
