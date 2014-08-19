package org.flowerplatform.codesync.regex.controller;

import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_TYPE_VALID_STATES_PROPERTY;

import java.util.List;

import org.flowerplatform.codesync.regex.action.CheckStateAction;
import org.flowerplatform.core.config_processor.IConfigNodeProcessor;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.AbstractController;
import org.flowerplatform.util.regex.RegexAction;

public class CheckStateNodeConfigurationProcessor extends AbstractController implements IConfigNodeProcessor {

	@Override
	public Object processConfigNode(Node node, Object parentProcessedDataStructure, ServiceContext<NodeService> context) {
		String validStates = (String) node.getPropertyValue(ACTION_TYPE_VALID_STATES_PROPERTY);
		CheckStateAction checkStateAction = new CheckStateAction(validStates);
		((List<RegexAction>) parentProcessedDataStructure).add(checkStateAction);
		return null; // a checkStateAction doesn't have any children, so no
						// reference to where to add children instances needed
	}
}
