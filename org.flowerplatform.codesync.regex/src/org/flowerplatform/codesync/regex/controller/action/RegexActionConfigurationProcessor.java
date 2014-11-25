package org.flowerplatform.codesync.regex.controller.action;

import org.flowerplatform.core.config_processor.IConfigNodeProcessor;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.AbstractController;
import org.flowerplatform.util.regex.RegexAction;
import org.flowerplatform.util.regex.RegexWithActions;

/**
 * @author Mariana Gheorghe
 */
public abstract class RegexActionConfigurationProcessor extends AbstractController implements
		IConfigNodeProcessor<RegexAction, RegexWithActions> {

	@Override
	public RegexAction processConfigNode(Node node, RegexWithActions parent, ServiceContext<NodeService> context) {
		RegexAction action = createRegexAction(node);
		parent.getRegexActions().add(action);
		return action;
	}
	
	/**
	 * Create a {@link RegexAction} with the parameters from <code>node</code>.
	 */
	protected abstract RegexAction createRegexAction(Node node);
}
