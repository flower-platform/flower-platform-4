package org.flowerplatform.core.node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowerplatform.core.node.controller.NodeController;
import org.flowerplatform.util.RunnableWithParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Cristian Spiescu
 */
public class NodeTypeDescriptorRegistry {

	private final static Logger logger = LoggerFactory.getLogger(NodeTypeDescriptorRegistry.class);

	private Map<String, NodeTypeDescriptor> nodeTypeDescriptors = new HashMap<String, NodeTypeDescriptor>();
	
	public NodeTypeDescriptor getOrCreateNodeTypeDescriptor(String type) {
		NodeTypeDescriptor result = nodeTypeDescriptors.get(type);
		if (result == null) {
			result = new NodeTypeDescriptor();
			nodeTypeDescriptors.put(type, result);
		}
		return result;
	}
	
	public NodeTypeDescriptor getExpectedNodeTypeDescriptor(String type) {
		NodeTypeDescriptor result = nodeTypeDescriptors.get(type);
		if (result == null) {
			logger.warn("Operation invoked for nodeType = {}, but there is no associated descriptor registered! Aborting operation.", type);
			return null;
		}
		return result;
	}
	
	public <PROVIDER_DATA_TYPE extends NodeController> List<PROVIDER_DATA_TYPE> getControllersForTypeAndCategories(NodeTypeDescriptor descriptor, RunnableWithParam<List<PROVIDER_DATA_TYPE>, NodeTypeDescriptor> giveMeProvidersListFromDescriptorRunnable) {
		final List<PROVIDER_DATA_TYPE> controllers = new ArrayList<PROVIDER_DATA_TYPE>();
		List<PROVIDER_DATA_TYPE> providers = giveMeProvidersListFromDescriptorRunnable.run(descriptor);
		if (providers != null) {
			controllers.addAll(giveMeProvidersListFromDescriptorRunnable.run(descriptor));
		}
		
		for (String category : descriptor.getCategories()) {
			NodeTypeDescriptor categoryDescriptor = getExpectedNodeTypeDescriptor(category);
			if (categoryDescriptor == null) {
				// semi-error; a WARN is logged
				continue;
			}
			List<PROVIDER_DATA_TYPE> categoryProviders = giveMeProvidersListFromDescriptorRunnable.run(categoryDescriptor);
			if (categoryProviders != null) {
				controllers.addAll(categoryProviders);
			}
		}
		Collections.sort(controllers);
		return controllers;
	}
}
