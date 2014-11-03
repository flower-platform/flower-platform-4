package org.flowerplatform.codesync.regex.controller;

import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_TYPE_VALID_STATES_PROPERTY;

import org.flowerplatform.codesync.regex.action.CheckStateAction;
import org.flowerplatform.core.config_processor.IConfigNodeProcessor;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.AbstractController;
import org.flowerplatform.util.regex.RegexAction;
import org.flowerplatform.util.regex.RegexWithActions;

/**
 * @author Elena Posea
 */
public class CheckStateNodeConfigurationProcessor extends AbstractController implements IConfigNodeProcessor<RegexAction, RegexWithActions> {

	@Override
	public RegexAction processConfigNode(Node node, RegexWithActions parentProcessedDataStructure, ServiceContext<NodeService> context) {
		String validStates = (String) node.getPropertyValue(ACTION_TYPE_VALID_STATES_PROPERTY);
		RegexAction ra = new CheckStateAction(validStates);
		parentProcessedDataStructure.getRegexActions().add(ra);
		return ra;
	}
}
