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
package org.flowerplatform.tests;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.eclipse.osgi.framework.internal.core.FrameworkProperties;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.RemoteMethodInvocationInfo;
import org.flowerplatform.core.RemoteMethodInvocationListener;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.NodeServiceRemote;
import org.flowerplatform.core.node.remote.ResourceServiceRemote;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.core.node.resource.ResourceService;
import org.flowerplatform.core.node.resource.ResourceSetService;
import org.flowerplatform.resources.ResourcesPlugin;
import org.flowerplatform.util.plugin.AbstractFlowerJavaPlugin;
import org.junit.Before;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

//CHECKSTYLE:OFF
/**
 * Base class for Eclipse independent tests. Ensures that FP plugins are started even
 * when the test is run alone (i.e. not in suite).
 * 
 * @author Mariana Gheorghe
 */
@SuppressWarnings("restriction")
public class EclipseIndependentTestBase {
//CHECKSTYLE:ON
	
	protected static String workspaceLocation = "workspace";
	
	protected static NodeServiceRemote nodeServiceRemote;
	protected static ResourceServiceRemote resourceServiceRemote;
	protected static NodeService nodeService;
	protected static ResourceService resourceService;
	protected static ResourceSetService resourceSetService;
	
	protected static RemoteMethodInvocationListener remoteMethodInvocationListener;
	protected static String sessionId = "mockSessionId";
	
	protected RemoteMethodInvocationInfo remoteMethodInvocationInfo;
	protected ServiceContext<NodeService> context;
	
	static {
		// populated from FlowerFrameworkLauncher in the servlet container
		FrameworkProperties.getProperties().put("FLOWER_PLATFORM_HOME", new File("").getAbsolutePath());
		
		startPlugin(new ResourcesPlugin());
		startPlugin(new CorePlugin());
		nodeService = CorePlugin.getInstance().getNodeService();
		resourceService = CorePlugin.getInstance().getResourceService();
		resourceSetService = CorePlugin.getInstance().getResourceSetService();
		
		resourceServiceRemote = new ResourceServiceRemote();
		nodeServiceRemote = new NodeServiceRemote();
		
		HttpServletRequest req = mock(HttpServletRequest.class);
		HttpSession session = mock(HttpSession.class);
		when(req.getSession()).thenReturn(session);
		when(session.getId()).thenReturn(sessionId);
		CorePlugin.getInstance().getRequestThreadLocal().set(req);
		
		remoteMethodInvocationListener = spy(CorePlugin.getInstance().getRemoteMethodInvocationListener());
		doReturn(sessionId).when(remoteMethodInvocationListener).getSessionId();
	}
	
	/**
	 * Starts a plugin with a mocked {@link BundleContext} and {@link ResourceBundle}.
	 */
	protected static void startPlugin(final AbstractFlowerJavaPlugin plugin) {
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
		
		try {
			Field field = AbstractFlowerJavaPlugin.class.getDeclaredField("resourceBundle");
			field.setAccessible(true);
			field.set(plugin, resourceBundle);
		
			plugin.start(context);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @author Claudiu Matei
	 **/
	@Before
	public void setUp() {
		context = new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService());
		remoteMethodInvocationInfo = spy(new RemoteMethodInvocationInfo());
		doReturn(new ArrayList<String>()).when(remoteMethodInvocationInfo).getResourceUris();
		doReturn(new ArrayList<String>()).when(remoteMethodInvocationInfo).getResourceSets();
		doReturn(-1L).when(remoteMethodInvocationInfo).getTimestampOfLastRequest();
		remoteMethodInvocationInfo.setServiceMethodOrUrl("test");
	}
	
	/**
	 * @author Mariana Gheorghe
	 */
	protected static void copyFiles(String from, String dir) {
		File to = new File(workspaceLocation, dir);
		try {
			FileUtils.copyDirectory(new File(from), to);
		} catch (IOException e) {
			throw new RuntimeException("Cannot copy files needed for test", e);
		}
	}
	
	/**
	 * @author Mariana Gheorghe
	 */
	protected static void deleteFiles(String dir) {
		try {
			FileUtils.deleteDirectory(new File(workspaceLocation, dir));
		} catch (IOException e) {
			throw new RuntimeException("Cannot delete files ", e);
		}
	}
	
}
