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
package org.eclipse.equinox.servletbridge.flower;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.security.Policy;
import java.util.Map;
import java.util.Properties;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import org.apache.commons.io.FileUtils;
import org.eclipse.equinox.servletbridge.CloseableURLClassLoader;
import org.eclipse.equinox.servletbridge.FrameworkLauncher;

/**
 * Customized launcher class that disables the "deploy" process
 * (done by the original class), and processes some additional parameters
 * from web.xml.
 * 
 * <p>
 * The original classes (form the *.servletbridge package) are modified
 * very little, in order to allow an easy merge from their original source
 * repository. We even had to access a private field using reflection. These
 * modifications are marked with "MODIF_FROM_ORIGINAL".
 * 
 * <p>
 * The reason for using {@link ServletConfigWrapper} and {@link ServletConfigWrapper}:
 * to be able to use this class, in a host that is not a servlet container. E.g. a normal
 * java app like IDEA. And we kept them, in order to preserve the {@link FrameworkLauncher} class
 * mostly not modified. 
 * 
 * <p>
 * While in dev mode, the system relies on "absolute-path-helper.txt". This file is generated at
 * runtime by an ANT builder, that writes the absolute path of the parent project. We need this,
 * so that we can have the workspace relative to the parent project, in the real location (i.e.
 * in the git repo). Before using git, the project was in the workspace, so this wasn't necessary.<br/>
 * This is neede because at dev/runtime, the current dir is something like: WORKSPACE\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\WEBAPP,
 * i.e. the location where WTP publishes the web app.
 * 
 * <p>
 * <strong>History of these classes:</strong>
 * 
 * <p>
 * <b>V1</b><br/>
 * From Eclipse's CVS, dev.eclipse.org; /cvsroot/rt; org.eclipse.equinox/server-side/bundles/org.eclipse.equinox.servletbridge
 * Version v20110502.
 * 
 * <p>
 * <b>V2</b><br/>
 * CVS, an older version: v20080929-1800. CVS is no longer available; the files exist in older Flower Platform repos.
 * 
 * The newer version did not worked when using the security manager (java.lang.ClassCircularityError appeared). 
 * We changed to an older servletbridge version as indicated here: 
 * http://www.eclipse.org/forums/index.php?t=msg&goto=127453&S=5647d1811f8b25b9cbb9757fb10ab336
 * 
 * <p>
 * In case we will need to use the newer version again, here is what we investigated until now:
 * <ul>
 * 	<li>Breakpoint at org.eclipse.osgi.framework.util.SecureAction@345, and then breakpoint
 * 		at java.net.JarURLConnection@161
 * 	<li>This line fails (i.e. new URL("file:/D:/Java/eclipse_3.5_modeling/plugins/org.eclipse.osgi_3.5.2.R35x_v20100126.jar");)
 * 		because it will arrive again in "SecureAction" => infinite loop
 * 	<li>It's interesting to see why this fail for this call, being given that other classes are already loaded
 * 		from Eclipse. A parallel debug might be interesting, between the version that doesn't use this pass
 * </ul>
 * 	
 * The newer version might be needed if jar connections need to be closed (i.e. to physically delete the jar file).
 * 
 * <p>
 * <b>V3</b><br/>
 * From Eclipse's GIT (http://git.eclipse.org/c/equinox/rt.equinox.bundles.git/), at 2014-01-20; this was last git commit:
 * http://git.eclipse.org/c/equinox/rt.equinox.bundles.git/commit/?id=03ad9b1cfc586b4350185115e94742f78e33d1fe
 * 
 * <p>
 * This version may be newer than V1. However, if we experience problems, we may revert to V2. Or just avoid using {@link CloseableURLClassLoader}, which
 * may be the source of the exception from above.
 * 
 * @see web.xml for boot parameter details
 * 
 * @author Cristian Spiescu
 */
public class FlowerFrameworkLauncher extends FrameworkLauncher {
	
	private static final String WEB_APP_CONTEXT_TO_DEV_WS_PATH = "../../../../../../";
	
	private static final String POLICY_FILE = "/WEB-INF/all.policy";
	
	private static final String FLOWER_PLATFORM_HOME = "FLOWER_PLATFORM_HOME";
	
	private static final String LAUNCHER_PROPERTIES = "/launcher.properties";
	
	private static final String WORKSPACE = "/workspace";
	
	private static final String DEFAULT_FLOWER_PLATFORM_HOME_DIR = "/.flower-platform";
	
	private static final String FLOWER_PLATFORM_HOME_DEFAULT_FILES = "/flower-platform-home-default-files";
	
	private static final String FLOWER_PLATFORM_HOME_DEFAULT_FILES_PATH = "/META-INF" + FLOWER_PLATFORM_HOME_DEFAULT_FILES;

	@Override
	public void init() {
		super.init();
		// set security manager
		String policyFile = context.getRealPath(POLICY_FILE);
		System.setProperty("java.security.policy", policyFile);
		Policy.getPolicy().refresh(); // ensure that the policy is reloaded
		System.setSecurityManager(new SecurityManager());
	}
	
	/**
	 * Do nothing. The original implementation was deploying (copying)
	 * Eclipse files into the webapp's working directory. Accesses a private field.
	 */
	@Override
	public synchronized void deploy() {
		// modify this private field; otherwise the start() method would throw an exception
		Field platformDirectory;
		try {
			platformDirectory = FrameworkLauncher.class.getDeclaredField("platformDirectory");
			platformDirectory.setAccessible(true);
			platformDirectory.set(this, new File(""));
		} catch (Exception e) {
			throw new RuntimeException("Unexpected error while accessing field platformDirectory", e);
		}
	}

	/**
	 * Reads the launcher.properties file (if exists) and generates some properties if in dev. mode.
	 * 
	 * @author Cristian Spiescu
	 * @author Cristina Brinza
	 */
	@SuppressWarnings("rawtypes")
	@Override
	protected Map buildInitialPropertyMap() {
		// determining which is the Flower Platform Home dir
		// 1st try: system property
		String flowerPlatformHomeDirectoryPath = System.getProperty(FLOWER_PLATFORM_HOME);
		if (flowerPlatformHomeDirectoryPath == null) {
			// 2nd try: environment variable
			flowerPlatformHomeDirectoryPath = System.getenv(FLOWER_PLATFORM_HOME); 
			if (flowerPlatformHomeDirectoryPath == null) {
				// 3rd try: default location
				flowerPlatformHomeDirectoryPath = System.getProperty("user.home") + DEFAULT_FLOWER_PLATFORM_HOME_DIR;
			}
			// put the value as a property, so others can access it
			System.setProperty(FLOWER_PLATFORM_HOME, flowerPlatformHomeDirectoryPath);
		}		
		File homeDir = new File(flowerPlatformHomeDirectoryPath);
		if (!homeDir.exists()) {
			homeDir.mkdirs();
			
			// copy default files from META-INF/flower-platform-home-default-files
			String defaultFilesDirPath = context.getRealPath(FLOWER_PLATFORM_HOME_DEFAULT_FILES_PATH);
			File defaultFilesDir;			
			try {
				// source directory - META-INF/flower-platform-home-default-files
				defaultFilesDir = new File(defaultFilesDirPath);
				
				// destination directory - FLOWER_PLATFORM_HOME/flower-platform-home-default-files
				String defaultFilesDirInFlowerPlatformHomePath = System.getProperty(FLOWER_PLATFORM_HOME) + FLOWER_PLATFORM_HOME_DEFAULT_FILES;
				File defaultFilesDirInFlowerPlatformHome = new File(defaultFilesDirInFlowerPlatformHomePath);
				
				// copy content from source directory to destination directory
				FileUtils.copyDirectory(defaultFilesDir, defaultFilesDirInFlowerPlatformHome);
			} catch (IOException e) {
				throw new RuntimeException("Error while copying content of flower-platform-home-default-files", e);
			}
		}
		
		String eclipseConfigurationLocation = "WEB-INF/eclipse/configuration";
		String developmentLaunchConfiguration = null;
		
		// loading and parsing launcher properties
		File launcherPropertiesFile = new File(System.getProperty(FLOWER_PLATFORM_HOME) + LAUNCHER_PROPERTIES);
		if (launcherPropertiesFile.exists()) {
			// read the properties from launcher.properties
			Properties launcherProperties = new Properties();
			FileInputStream launcherPropertiesFileInputStream = null;
			try {
				launcherPropertiesFileInputStream = new FileInputStream(launcherPropertiesFile);
				launcherProperties.load(launcherPropertiesFileInputStream);
				developmentLaunchConfiguration = launcherProperties.getProperty("developmentLaunchConfiguration");
				if (developmentLaunchConfiguration == null) {
					eclipseConfigurationLocation = launcherProperties.getProperty("eclipseConfigurationLocation");
				}
			} catch (Exception e) {
				throw new RuntimeException("Error while parsing " + LAUNCHER_PROPERTIES, e);
			} finally {
				if (launcherPropertiesFileInputStream != null)
					try {
						launcherPropertiesFileInputStream.close();
					} catch (IOException e) {
						// ignore
					}
			}
		}
		
		Properties properties = new Properties();
		String configFileLocation;
		
		// if in development mode, generate some properties + config file path
		String osgiConfigurationArea = null;
		String developmentWorkspaceRoot = null;
		if (developmentLaunchConfiguration != null) {
			// dev mode
			developmentWorkspaceRoot = context.getRealPath(WEB_APP_CONTEXT_TO_DEV_WS_PATH);
			// osgi.configuration.area a.k.a. -configuration 
			osgiConfigurationArea = String.format("%s/.metadata/.plugins/org.eclipse.pde.core/%s", developmentWorkspaceRoot, developmentLaunchConfiguration);
			if (!(new File(osgiConfigurationArea).exists()))
				throw new RuntimeException(osgiConfigurationArea + " file not found. Either the 'developmentLaunchConfiguration' is not correctly set or the Eclipse configuration was not generated (=> you need to launch the target launch config " + developmentLaunchConfiguration + " at least once directly from Eclipse).");
		} else {
			// prod mode
			String relativeOsgiConfigurationArea = eclipseConfigurationLocation;
			osgiConfigurationArea = context.getRealPath(relativeOsgiConfigurationArea);
			if (!(new File(osgiConfigurationArea).exists())) {
				throw new RuntimeException("Is the system starting in dev mode? Then make sure 'FLOWER_PLATFORM_HOME/launcher.properties' exists and contains a value for 'developmentLaunchConfiguration'. Otherwise, if the system is starting in prod mode, there's an issue because 'osgiConfigurationArea' = " + osgiConfigurationArea + " doesn't exist.");
			}
			
			String pluginsDir = osgiConfigurationArea + "/../plugins";
			if (!(new File(pluginsDir).exists())) {
				throw new RuntimeException(pluginsDir + " file not found.");
			}
			// bundles with relative paths defined in "osgi.bundles" use the "osgi.syspath" property as current
			// dir (when resolving the absolute path from the relative path). This allows us to specify relative
			// paths for FDC plugins (that will be searched in: e.g. /tomcat/webapps/_contex_/WEB-INF/eclipse/plugins)
			properties.put("osgi.syspath", pluginsDir);
		}

		properties.put("osgi.configuration.area", "file:" + osgiConfigurationArea);
			
		// osgi.dev a.k.a. -dev
		String osgiDev = osgiConfigurationArea + "/dev.properties";
		if (!(new File(osgiDev).exists())) {
			if (developmentLaunchConfiguration != null) {
				// for dev, this is mandatory; for production = optional
				throw new RuntimeException(osgiDev + " file not found. Either the 'developmentLaunchConfiguration' is not correctly set or the Eclipse configuration was not generated (=> you need to launch the target launch config " + developmentLaunchConfiguration + " at least once directly from Eclipse).");
			}
		} else {
			properties.put("osgi.dev", "file:" + osgiDev);
		}
			
		// config file path
		configFileLocation = osgiConfigurationArea + "/config.ini";
		
		// load the properties from the config file
		File file = new File(configFileLocation);
		FileInputStream is = null;
		try {
			is = new FileInputStream(file);
			properties.load(is);
		} catch (Exception e) {
			throw new RuntimeException("'configFileLocation' error; it points towards an unaccesible file or there was another error!", e);
		} finally {
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
					// ignore
				}
		}
		
		String osgiInstanceArea = System.getProperty(FLOWER_PLATFORM_HOME) + WORKSPACE;		
		
		try {
			// if path doesn't exist, create it
			File osgiInstanceAreaFile = new File(osgiInstanceArea);
			if (!osgiInstanceAreaFile.exists()) {
				osgiInstanceAreaFile.mkdirs();
			}
		} catch(Exception e) {
			throw new RuntimeException(osgiInstanceArea + " workspace location not found", e);
		}
		properties.put("osgi.instance.area", "file:" + osgiInstanceArea);
		properties.put(OSGI_INSTALL_AREA, osgiConfigurationArea + "/..");
		
		// check if context path is empty, if the app is ROOT
		properties.put("flower.server.app.context", context.getContextPath().length() == 0 ? "" : context.getContextPath().substring(1));
		properties.put("flower.server.app.location", context.getRealPath(File.separator));
		properties.put("flower.server.tmpdir", ((File) context.getAttribute(context.getTempDir())).getAbsolutePath());
		
		return properties;
	}
}