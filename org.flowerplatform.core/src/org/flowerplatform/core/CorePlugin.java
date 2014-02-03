package org.flowerplatform.core;

import org.flowerplatform.core.node.NodeTypeDescriptorRegistry;
import org.flowerplatform.core.node.remote.NodeService;
import org.flowerplatform.util.plugin.AbstractFlowerJavaPlugin;
import org.osgi.framework.BundleContext;


/**
 * @author Cristian Spiescu
 */
public class CorePlugin extends AbstractFlowerJavaPlugin {

	protected static CorePlugin INSTANCE;
	
	public static CorePlugin getInstance() {
		return INSTANCE;
	}
	
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		INSTANCE = this;
		
		getServiceRegistry().registerService("nodeService", new NodeService(nodeTypeDescriptorRegistry));
	}

	public void stop(BundleContext bundleContext) throws Exception {
		super.stop(bundleContext);
		INSTANCE = null;
	}
	
	private ServiceRegistry serviceRegistry = new ServiceRegistry();

	public ServiceRegistry getServiceRegistry() {
		return serviceRegistry;
	}
	
	private NodeTypeDescriptorRegistry nodeTypeDescriptorRegistry = new NodeTypeDescriptorRegistry();

	public NodeTypeDescriptorRegistry getNodeTypeDescriptorRegistry() {
		return nodeTypeDescriptorRegistry;
	}

}
