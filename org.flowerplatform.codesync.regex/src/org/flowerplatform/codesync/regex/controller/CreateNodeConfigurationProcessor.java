package org.flowerplatform.codesync.regex.controller;

import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_TYPE_CREATE_NODE_NEW_NODE_TYPE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_TYPE_CREATE_NODE_PROPERTIES;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.codesync.regex.action.CreateNodeAction;
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
public class CreateNodeConfigurationProcessor extends AbstractController implements IConfigNodeProcessor<RegexAction, RegexWithActions> {

	@Override
	public RegexAction processConfigNode(Node node, RegexWithActions parentProcessedDataStructure, ServiceContext<NodeService> context) {
		String type = (String) node.getPropertyValue(ACTION_TYPE_CREATE_NODE_NEW_NODE_TYPE);
		String csvList = (String) node.getPropertyValue(ACTION_TYPE_CREATE_NODE_PROPERTIES);
		List<String> properties = new ArrayList<String>();
		if (!csvList.equals("")) {
			String[] prop = csvList.split(",");
			for (int x = 0; x < prop.length; x++) {
				properties.add(prop[x]);
			}
		}
		RegexAction ra = new CreateNodeAction(type, properties);
		parentProcessedDataStructure.getRegexActions().add(ra);
		return ra;
	}
}
