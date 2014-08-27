package org.flowerplatform.codesync.regex.controller;

import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.FULL_REGEX;
import static org.flowerplatform.core.CoreConstants.NAME;

import org.flowerplatform.core.config_processor.IConfigNodeProcessor;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.AbstractController;
import org.flowerplatform.util.regex.RegexConfiguration;
import org.flowerplatform.util.regex.RegexWithActions;

/**
 * @author Elena Posea
 */
public class RegexWithActionsProcessor extends AbstractController implements IConfigNodeProcessor<RegexWithActions, RegexConfiguration> {

	@Override
	public RegexWithActions processConfigNode(Node node, RegexConfiguration parentProcessedDataStructure, ServiceContext<NodeService> context) {
		RegexWithActions rwa = new RegexWithActions();
		rwa.setName((String) node.getPropertyValue(NAME));
		// rwa.setRegexWithMacros((String)
		// node.getPropertyValue(REGEX_WITH_MACROS));
		rwa.setRegex((String) node.getPropertyValue(FULL_REGEX));
		parentProcessedDataStructure.add(rwa);
		return rwa;
	}

}
