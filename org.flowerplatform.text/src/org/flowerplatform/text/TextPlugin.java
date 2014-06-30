package org.flowerplatform.text;

import static org.flowerplatform.core.CoreConstants.ALL;
import static org.flowerplatform.core.CoreConstants.FILE_NODE_TYPE;
import static org.flowerplatform.core.CoreConstants.PROPERTIES_PROVIDER;
import static org.flowerplatform.text.TextConstants.TEXT_CONTENT_TYPE;
import static org.flowerplatform.text.TextConstants.TEXT_RESOURCE_KEY;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.remote.GenericValueDescriptor;
import org.flowerplatform.util.plugin.AbstractFlowerJavaPlugin;
import org.osgi.framework.BundleContext;

/**
 * @author Cristina Constantinescu
 */
public class TextPlugin extends AbstractFlowerJavaPlugin {

	protected static TextPlugin INSTANCE;
	
	public static TextPlugin getInstance() {
		return INSTANCE;
	}
		
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		INSTANCE = this;
		
		CorePlugin.getInstance().getResourceService().addResourceHandler(TEXT_RESOURCE_KEY, new TextResourceHandler());
				
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(FILE_NODE_TYPE)			
			.addAdditiveController(PROPERTIES_PROVIDER, new TextFileSubscribableProvider(ALL, TEXT_RESOURCE_KEY, TEXT_CONTENT_TYPE, false));
	}	
	
	public void stop(BundleContext bundleContext) throws Exception {
		super.stop(bundleContext);
		INSTANCE = null;
	}

	@Override
	public void registerMessageBundle() throws Exception {
		// messages come from .resources
	}
}
