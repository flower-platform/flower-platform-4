package org.flowerplatform.codesync.regex.controller;

import java.util.List;

import org.flowerplatform.codesync.regex.action.AttachNodeToCurrentStateAction;
import org.flowerplatform.core.config_processor.IConfigNodeProcessor;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.AbstractController;
import org.flowerplatform.util.regex.RegexAction;

public class AttachNodeToCurrentStateConfigurationProcessor extends AbstractController implements IConfigNodeProcessor {

	@Override
	public Object processConfigNode(Node node, Object parentProcessedDataStructure, ServiceContext<NodeService> context) {
		((List<RegexAction>) parentProcessedDataStructure).add(new AttachNodeToCurrentStateAction());
		return null;
	}
}
