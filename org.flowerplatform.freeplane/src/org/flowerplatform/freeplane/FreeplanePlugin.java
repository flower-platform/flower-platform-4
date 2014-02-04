package org.flowerplatform.freeplane;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.NodeTypeDescriptor;
import org.flowerplatform.freeplane.controller.FreeplaneAddNodeController;
import org.flowerplatform.freeplane.controller.FreeplaneChildrenProvider;
import org.flowerplatform.freeplane.controller.FreeplanePropertiesProvider;
import org.flowerplatform.freeplane.controller.FreeplanePropertySetter;
import org.flowerplatform.freeplane.controller.FreeplaneRemoveNodeController;
import org.flowerplatform.freeplane.remote.FreeplaneService;
import org.flowerplatform.util.plugin.AbstractFlowerJavaPlugin;
import org.osgi.framework.BundleContext;

public class FreeplanePlugin extends AbstractFlowerJavaPlugin {

	protected static FreeplanePlugin INSTANCE;
	
	public static FreeplanePlugin getInstance() {
		return INSTANCE;
	}
	
	private FreeplaneUtils freeplaneUtils = new FreeplaneUtils();

	public FreeplaneUtils getFreeplaneUtils() {
		return freeplaneUtils;
	}
	
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		INSTANCE = this;
		
		NodeTypeDescriptor nodeTypeDescriptor = CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateNodeTypeDescriptor("freeplaneNode");
		nodeTypeDescriptor.addChildrenProvider(new FreeplaneChildrenProvider());
		nodeTypeDescriptor.addPropertiesProvider(new FreeplanePropertiesProvider());
		nodeTypeDescriptor.addAddNodeController(new FreeplaneAddNodeController());
		nodeTypeDescriptor.addRemoveNodeController(new FreeplaneRemoveNodeController());
		nodeTypeDescriptor.addPropertySetter(new FreeplanePropertySetter());	
		
		CorePlugin.getInstance().getServiceRegistry().registerService("freeplaneService", new FreeplaneService());
	}

	public void stop(BundleContext bundleContext) throws Exception {
		super.stop(bundleContext);
		INSTANCE = null;
	}
	
	public void registerMessageBundle() throws Exception {
		// not used
	}
	
}
