package org.flowerplatform.codesync.regex.controller;

import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_TYPE_VALID_STATES_PROPERTY;

import org.flowerplatform.codesync.regex.action.CheckStateAction;
import org.flowerplatform.codesync.regex.action.DelegatingRegexWithAction;
import org.flowerplatform.core.config_processor.IConfigNodeProcessor;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.AbstractController;
import org.flowerplatform.util.regex.RegexAction;

/**
 * @author Elena Posea
 */
public class CheckStateNodeConfigurationProcessor extends AbstractController implements IConfigNodeProcessor<RegexAction, DelegatingRegexWithAction> {

	@Override
	public RegexAction processConfigNode(Node node, DelegatingRegexWithAction parentProcessedDataStructure, ServiceContext<NodeService> context) {
		String validStates = (String) node.getPropertyValue(ACTION_TYPE_VALID_STATES_PROPERTY);
		RegexAction ra = new CheckStateAction(validStates);
		parentProcessedDataStructure.getRegexActions().add(ra);
		return ra;
	}
}
