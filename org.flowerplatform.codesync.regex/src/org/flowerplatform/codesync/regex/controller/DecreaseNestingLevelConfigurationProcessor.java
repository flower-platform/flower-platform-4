package org.flowerplatform.codesync.regex.controller;

import org.flowerplatform.codesync.regex.action.DecreaseNestingLevelAction;
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
public class DecreaseNestingLevelConfigurationProcessor extends AbstractController implements IConfigNodeProcessor<RegexAction, DelegatingRegexWithAction> {

	@Override
	public RegexAction processConfigNode(Node node, DelegatingRegexWithAction parentProcessedDataStructure, ServiceContext<NodeService> context) {
		RegexAction ra = new DecreaseNestingLevelAction();
		parentProcessedDataStructure.getRegexActions().add(ra);
		return ra;
	}
}
