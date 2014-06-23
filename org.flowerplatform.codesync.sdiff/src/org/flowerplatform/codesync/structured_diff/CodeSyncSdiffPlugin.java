package org.flowerplatform.codesync.structured_diff;

import static org.flowerplatform.codesync.structured_diff.CodeSyncSdiffConstants.STRUCTURE_DIFF;
import static org.flowerplatform.codesync.structured_diff.CodeSyncSdiffConstants.STRUCTURE_DIFF_EXTENSION;
import static org.flowerplatform.core.CoreConstants.FILE_NODE_TYPE;
import static org.flowerplatform.core.CoreConstants.PROPERTIES_PROVIDER;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.file.FileSubscribableProvider;
import org.flowerplatform.util.plugin.AbstractFlowerJavaPlugin;
import org.osgi.framework.BundleContext;

/**
 * @author Mariana Gheorghe
 */
public class CodeSyncSdiffPlugin extends AbstractFlowerJavaPlugin {

protected static CodeSyncSdiffPlugin INSTANCE;
	
	public static CodeSyncSdiffPlugin getInstance() {
		return INSTANCE;
	}
	
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		INSTANCE = this;
		
		CorePlugin.getInstance().getServiceRegistry().registerService("structureDiffService", new StructureDiffService());
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(STRUCTURE_DIFF);
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(FILE_NODE_TYPE)
			.addAdditiveController(PROPERTIES_PROVIDER, new FileSubscribableProvider(STRUCTURE_DIFF_EXTENSION, "fpp", null, true));
	}
	
	public void stop(BundleContext bundleContext) throws Exception {
		super.stop(bundleContext);
		INSTANCE = null;
	}

	@Override
	public void registerMessageBundle() throws Exception {
		// nothing to do yet
	}
	
}
