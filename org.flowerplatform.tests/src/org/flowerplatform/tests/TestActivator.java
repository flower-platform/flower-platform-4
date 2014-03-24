/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
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
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package org.flowerplatform.tests;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * @author Mariana
 * @author Sorin
 */
@SuppressWarnings("restriction")
public class TestActivator implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		System.setProperty("testing", "true");
		
//		// FlowerFrameworkLauncher properties & Tomcat properties
//		createDirectoriesIfNeeded("temp");
//		createDirectoriesIfNeeded("temp/tomcat");
//		System.setProperty("catalina.base", getCanonicalPath("temp/tomcat"));
//		
//		FrameworkProperties.setProperty("flower.server.app.location", "temp/");
//		FrameworkProperties.setProperty("flower.server.app.context", "flower-dev-center");
//		FrameworkProperties.setProperty("flower.linux.subversion.configuration.location", "../org.flowerplatform.web.app/WebContent/WEB-INF/eclipse/subversion_config/");
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		// do nothing
	}

}