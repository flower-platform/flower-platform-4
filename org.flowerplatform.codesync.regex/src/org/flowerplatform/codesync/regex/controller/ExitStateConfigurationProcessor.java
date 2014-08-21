package org.flowerplatform.codesync.regex.controller;

import org.flowerplatform.codesync.regex.action.ExitStateAction;
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
public class ExitStateConfigurationProcessor extends AbstractController implements IConfigNodeProcessor<RegexAction, RegexWithActions> {

	@Override
	public RegexAction processConfigNode(Node node, RegexWithActions parentProcessedDataStructure, ServiceContext<NodeService> context) {
		RegexAction ra = new ExitStateAction();
		parentProcessedDataStructure.getRegexActions().add(ra);
		return ra;
	}
}
