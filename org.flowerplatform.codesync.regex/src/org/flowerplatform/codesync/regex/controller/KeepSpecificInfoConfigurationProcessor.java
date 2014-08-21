package org.flowerplatform.codesync.regex.controller;

import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_TYPE_KEEP_SPECIFIC_INFO_IS_CONTAINMENT_PROPERTY;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_TYPE_KEEP_SPECIFIC_INFO_IS_LIST_PROPERTY;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_TYPE_KEEP_SPECIFIC_INFO_KEY_PROPERTY;

import org.flowerplatform.codesync.regex.action.KeepSpecificInfoAction;
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
public class KeepSpecificInfoConfigurationProcessor extends AbstractController implements IConfigNodeProcessor<RegexAction, RegexWithActions> {

	@Override
	public RegexAction processConfigNode(Node node, RegexWithActions parentProcessedDataStructure, ServiceContext<NodeService> context) {
		String keepInfoKey = (String) node.getPropertyValue(ACTION_TYPE_KEEP_SPECIFIC_INFO_KEY_PROPERTY);
		Boolean isList = (Boolean) node.getPropertyValue(ACTION_TYPE_KEEP_SPECIFIC_INFO_IS_LIST_PROPERTY);
		Boolean isContainment = (Boolean) node.getPropertyValue(ACTION_TYPE_KEEP_SPECIFIC_INFO_IS_CONTAINMENT_PROPERTY);
		RegexAction ra = new KeepSpecificInfoAction(keepInfoKey, isList, isContainment);
		parentProcessedDataStructure.getRegexActions().add(ra);
		return ra;
	}
}
