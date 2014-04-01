package org.flowerplatform.resources;

import org.flowerplatform.util.UtilConstants;
import org.flowerplatform.util.plugin.AbstractFlowerJavaPlugin;
import org.osgi.framework.BundleContext;

public class ResourcesPlugin extends AbstractFlowerJavaPlugin {
	
	protected static ResourcesPlugin INSTANCE;
	
	public static ResourcesPlugin getInstance() {
		return INSTANCE;
	}
	
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		INSTANCE = this;
	}

	@Override
	protected String getMessagesFilePath() {
		return getBundleContext().getBundle().getSymbolicName() + "/" + UtilConstants.PUBLIC_RESOURCES_DIR + "/" + "messages/org_flowerplatform_resources.properties";
	}

}
