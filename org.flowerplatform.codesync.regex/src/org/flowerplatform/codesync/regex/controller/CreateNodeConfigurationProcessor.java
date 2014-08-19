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

public class CreateNodeConfigurationProcessor extends AbstractController implements IConfigNodeProcessor {

	@Override
	public Object processConfigNode(Node node, Object parentProcessedDataStructure, ServiceContext<NodeService> context) {
		String type = (String) node.getPropertyValue(ACTION_TYPE_CREATE_NODE_NEW_NODE_TYPE);
		String csvList = (String) node.getPropertyValue(ACTION_TYPE_CREATE_NODE_PROPERTIES);
		List<String> properties = new ArrayList<String>();
		if (!csvList.equals("")) {
			String[] prop = csvList.split(",");
			for (int x = 0; x < prop.length; x++)
				properties.add(prop[x]);
		}
		((List<RegexAction>) parentProcessedDataStructure).add(new CreateNodeAction(type, properties));
		return null;
	}
}
