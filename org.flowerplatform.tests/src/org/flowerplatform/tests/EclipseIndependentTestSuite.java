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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.ResourceBundle;

import org.eclipse.osgi.framework.internal.core.FrameworkProperties;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.resources.ResourcesPlugin;
import org.flowerplatform.tests.codesync.CodeSyncTestSuite;
import org.flowerplatform.tests.core.CommandStackTest;
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
@RunWith(Suite.class)
@SuiteClasses({ 
// TODO CS temp hack for test to work!!!
//	CodeSyncTestSuite.class,
//	FileSystemControllersTest.class,
	CommandStackTest.class
	
//	RegexTestSuite.class
})
public class EclipseIndependentTestSuite {
	
	public static NodeService nodeService;
	
	public static String WORKSPACE_LOCATION = "workspace";
	
	@BeforeClass
	public static void beforeClass() throws Exception {
		
		FrameworkProperties.getProperties().put("osgi.instance.area", WORKSPACE_LOCATION);
		
		startPlugin(new ResourcesPlugin());
		startPlugin(new CorePlugin());
		nodeService = CorePlugin.getInstance().getNodeService();
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
		plugin.start(context);
	}
	
}