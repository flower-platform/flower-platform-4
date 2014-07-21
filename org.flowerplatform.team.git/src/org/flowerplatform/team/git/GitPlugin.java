package org.flowerplatform.team.git;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.util.plugin.AbstractFlowerJavaPlugin;
import org.osgi.framework.BundleContext;

/**
 * @author Valentina-Camelia Bojan
 */
public class GitPlugin extends AbstractFlowerJavaPlugin {
	
	protected static GitPlugin INSTANCE;
		
	public static GitPlugin getInstance() {
		return INSTANCE;
	}
		
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		INSTANCE = this;
			
		CorePlugin.getInstance().getServiceRegistry().registerService("GitService", new GitService());
	}

	@Override
	public void registerMessageBundle() throws Exception {
		// nothing to do yet
	}
	
}
