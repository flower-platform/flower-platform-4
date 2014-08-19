package org.flowerplatform.codesync.regex.controller;

import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_TYPE_CLEAR_SPECIFIC_INFO_KEY_PROPERTY;

import java.util.List;

import org.flowerplatform.codesync.regex.action.ClearSpecificInfoAction;
import org.flowerplatform.core.config_processor.IConfigNodeProcessor;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.AbstractController;
import org.flowerplatform.util.regex.RegexAction;

public class ClearSpecificInfoConfigurationProcessor extends AbstractController implements IConfigNodeProcessor {

	@Override
	public Object processConfigNode(Node node, Object parentProcessedDataStructure, ServiceContext<NodeService> context) {
		String clearInfoKey = (String) node.getPropertyValue(ACTION_TYPE_CLEAR_SPECIFIC_INFO_KEY_PROPERTY);				
		((List<RegexAction>) parentProcessedDataStructure).add(new ClearSpecificInfoAction(clearInfoKey));
		return null;
	}
}
