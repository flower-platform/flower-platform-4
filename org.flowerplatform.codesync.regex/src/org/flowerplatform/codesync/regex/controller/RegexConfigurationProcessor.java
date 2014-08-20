package org.flowerplatform.codesync.regex.controller;

import org.flowerplatform.core.config_processor.IConfigNodeProcessor;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.AbstractController;
import org.flowerplatform.util.regex.RegexConfiguration;

/**
 * @author Elena Posea
 */
public class RegexConfigurationProcessor extends AbstractController implements IConfigNodeProcessor<RegexConfiguration, RegexConfiguration> {

	@Override
	public RegexConfiguration processConfigNode(Node node, RegexConfiguration parentProcessedDataStructure, ServiceContext<NodeService> context) {
		return parentProcessedDataStructure;
	}

}
