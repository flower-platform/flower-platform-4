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
package org.flowerplatform.resources;

import org.flowerplatform.util.UtilConstants;
import org.flowerplatform.util.plugin.AbstractFlowerJavaPlugin;
import org.osgi.framework.BundleContext;

/**
 *@author Mariana Gheorghe
 **/
public class ResourcesPlugin extends AbstractFlowerJavaPlugin {
	
	protected static ResourcesPlugin instance;
	
	public static ResourcesPlugin getInstance() {
		return instance;
	}
	
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		instance = this;
	}

	@Override
	protected String getMessagesFilePath() {
		return getBundleContext().getBundle().getSymbolicName() + "/" + UtilConstants.PUBLIC_RESOURCES_DIR + "/" + "messages/org_flowerplatform_resources.properties";
	}

}