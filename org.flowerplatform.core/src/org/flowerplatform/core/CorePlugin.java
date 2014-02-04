package org.flowerplatform.core;

import org.flowerplatform.core.mindmap.FreeplaneAddNodeController;
import org.flowerplatform.core.mindmap.FreeplaneChildrenProvider;
import org.flowerplatform.core.mindmap.FreeplanePropertiesProvider;
import org.flowerplatform.core.mindmap.FreeplanePropertySetter;
import org.flowerplatform.core.mindmap.FreeplaneRemoveNodeController;
import org.flowerplatform.core.mindmap.FreeplaneUtils;
import org.flowerplatform.core.node.NodeTypeDescriptor;
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
		
		NodeTypeDescriptor nodeTypeDescriptor = nodeTypeDescriptorRegistry.getOrCreateNodeTypeDescriptor("freeplaneNode");
		nodeTypeDescriptor.addChildrenProvider(new FreeplaneChildrenProvider());
		nodeTypeDescriptor.addPropertiesProvider(new FreeplanePropertiesProvider());
		nodeTypeDescriptor.addAddNodeController(new FreeplaneAddNodeController());
		nodeTypeDescriptor.addRemoveNodeController(new FreeplaneRemoveNodeController());
		nodeTypeDescriptor.addPropertySetter(new FreeplanePropertySetter());
		
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
	
	/**
	 * TODO CC: move to Freeplane plugin
	 * @author Cristina Constantinescu
	 */
	private FreeplaneUtils freeplaneUtils = new FreeplaneUtils();

	/**
	 * @author Cristina Constantinescu
	 */
	public FreeplaneUtils getFreeplaneUtils() {
		return freeplaneUtils;
	}


}
