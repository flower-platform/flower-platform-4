package org.flowerplatform.codesync.structured_diff;

import static org.flowerplatform.codesync.structured_diff.CodeSyncSdiffConstants.STRUCTURE_DIFF;

import org.flowerplatform.codesync.structured_diff.controller.StructureDiffController;
import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
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
		
		CorePlugin.getInstance().getResourceService().addResourceHandler(STRUCTURE_DIFF, new StructureDiffResourceHandler());
		
		StructureDiffController structureDiffController = new StructureDiffController();
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(STRUCTURE_DIFF)
			.addAdditiveController(CoreConstants.PROPERTIES_PROVIDER, structureDiffController)
			.addAdditiveController(CoreConstants.CHILDREN_PROVIDER, structureDiffController);
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
