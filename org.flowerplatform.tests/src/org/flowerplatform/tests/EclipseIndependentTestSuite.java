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
package org.flowerplatform.tests;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.eclipse.osgi.framework.internal.core.FrameworkProperties;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.resources.ResourcesPlugin;
import org.flowerplatform.tests.codesync.CodeSyncTestSuite;
import org.flowerplatform.tests.core.CoreTestSuite;
import org.flowerplatform.util.plugin.AbstractFlowerJavaPlugin;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * @author Mariana Gheorghe
 */
//CHECKSTYLE:OFF
@SuppressWarnings("restriction")
//CHECKSTYLE:ON
@RunWith(Suite.class)
@SuiteClasses({ 
	CodeSyncTestSuite.class,
	CoreTestSuite.class,
//	FileSystemControllersTest.class
	
//	RegexTestSuite.class
})
public class EclipseIndependentTestSuite {
	
	public static String workspaceLocation = "workspace";
	
	public static NodeService nodeService;
	
	public static String sessionId = "mockSessionId";
	
	/**
	 * @author see class
	 */
	@BeforeClass
	public static void beforeClass() throws Exception {
		// populate from web.xml in the servlet container
		FrameworkProperties.getProperties().put("osgi.instance.area", workspaceLocation);
		
		startPlugin(new ResourcesPlugin());
		startPlugin(new CorePlugin());
		nodeService = CorePlugin.getInstance().getNodeService();
		
		HttpServletRequest req = mock(HttpServletRequest.class);
		HttpSession session = mock(HttpSession.class);
		when(req.getSession()).thenReturn(session);
		when(session.getId()).thenReturn(sessionId);
		CorePlugin.getInstance().getRequestThreadLocal().set(req);
	}
	
	/**
	 * Starts a plugin with a mocked {@link BundleContext} and {@link ResourceBundle}.
	 */
	public static void startPlugin(final AbstractFlowerJavaPlugin plugin) throws Exception {
		BundleContext context = mock(BundleContext.class);
		Bundle bundle = mock(Bundle.class);
		when(context.getBundle()).thenReturn(bundle);
		when(bundle.getSymbolicName()).thenReturn("");
		
		ResourceBundle resourceBundle = new ResourceBundle() {
			
			@Override
			protected Object handleGetObject(String key) {
				return "";
			}
			
			@Override
			public Enumeration<String> getKeys() {
				return null;
			}
		};
		
		Field field = AbstractFlowerJavaPlugin.class.getDeclaredField("resourceBundle");
		field.setAccessible(true);
		field.set(plugin, resourceBundle);
		try {
			plugin.start(context);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @author see class
	 */
	public static void copyFiles(String from, String dir) {
		File to = new File(workspaceLocation, dir);
		try {
			FileUtils.copyDirectory(new File(from), to);
		} catch (IOException e) {
			throw new RuntimeException("Cannot copy files needed for test", e);
		}
	}

	/**
	 * @author see class
	 */
	public static void deleteFiles(String dir) {
		try {
			FileUtils.deleteDirectory(new File(workspaceLocation, dir));
		} catch (IOException e) {
			throw new RuntimeException("Cannot delete files ", e);
		}
	}
	
}