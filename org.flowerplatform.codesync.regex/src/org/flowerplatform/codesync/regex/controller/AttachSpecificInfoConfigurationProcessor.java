package org.flowerplatform.codesync.regex.controller;

import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_TYPE_ATTACH_SPECIFIC_INFO_IS_CONTAINMENT_PROPERTY;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_TYPE_ATTACH_SPECIFIC_INFO_KEY_PROPERTY;

import org.flowerplatform.codesync.regex.action.AttachSpecificInfoAction;
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
public class AttachSpecificInfoConfigurationProcessor extends AbstractController implements IConfigNodeProcessor<RegexAction, DelegatingRegexWithAction>  {

	@Override
	public RegexAction processConfigNode(Node node, DelegatingRegexWithAction parentProcessedDataStructure, ServiceContext<NodeService> context) {
		String attachInfoKey = (String) node.getPropertyValue(ACTION_TYPE_ATTACH_SPECIFIC_INFO_KEY_PROPERTY);
		Boolean isContainment = (Boolean) node.getPropertyValue(ACTION_TYPE_ATTACH_SPECIFIC_INFO_IS_CONTAINMENT_PROPERTY);
		RegexAction ra = new AttachSpecificInfoAction(attachInfoKey, isContainment);
		parentProcessedDataStructure.getRegexActions().add(ra);
		return null;
	}
}
