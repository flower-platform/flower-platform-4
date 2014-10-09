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
import org.flowerplatform.util.plugin.AbstractFlowerJavaPlugin;
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
	protected static NodeService nodeService;
	
	protected static String sessionId = "mockSessionId";
	
	static {
		// populated from FlowerFrameworkLauncher in the servlet container
		FrameworkProperties.getProperties().put("FLOWER_PLATFORM_HOME", new File("").getAbsolutePath());
		
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
