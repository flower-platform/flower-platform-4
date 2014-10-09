package org.flowerplatform.freeplane.client;

import org.flowerplatform.util.plugin.AbstractFlowerJavaPlugin;
import org.freeplane.core.util.LogUtils;
import org.freeplane.features.mode.Controller;
import org.freeplane.main.osgi.IControllerExtensionProvider;
import org.osgi.framework.BundleContext;

/**
 * @author Valentina Bojan
 */
public class FreeplaneClientPlugin extends AbstractFlowerJavaPlugin {

	protected static FreeplaneClientPlugin instance;
	
	public static FreeplaneClientPlugin getInstance() {
		return instance;
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		instance = this;
		
		context.registerService(IControllerExtensionProvider.class.getName(), new IControllerExtensionProvider() {
			public void installExtension(Controller controller) {
				FlowerPlatformManager.install();
				LogUtils.info("Workspace controller installed.");
			}
		}, null);
	}
	
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		super.stop(bundleContext);
		instance = null;
	}
	
	@Override
	public void registerMessageBundle() throws Exception {
		// nothing to do yet
	}
}
