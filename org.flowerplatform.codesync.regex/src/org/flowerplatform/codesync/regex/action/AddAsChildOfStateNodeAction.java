package org.flowerplatform.codesync.regex.action;

import java.util.ArrayList;

import org.flowerplatform.codesync.regex.CodeSyncRegexConstants;
import org.flowerplatform.codesync.regex.State;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.regex.RegexAction;
import org.flowerplatform.util.regex.RegexProcessingSession;

/**
 * Add the current node as a child of the state node.
 * 
 * @author Elena Posea
 */
public class AddAsChildOfStateNodeAction extends RegexAction {

	@Override
	public void executeAction(RegexProcessingSession param) {
		ServiceContext<NodeService> serviceContext = new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService());
		Node currentNode = (Node) param.context.get(CodeSyncRegexConstants.CURRENT_NODE);
		// make sure to also copy the properties
		serviceContext.setContext(currentNode.getProperties());
		@SuppressWarnings("unchecked")
		State top = (State) ((ArrayList<Object>) param.context.get(CodeSyncRegexConstants.STATE_STACK)).get(0);
		CorePlugin.getInstance().getNodeService().addChild((Node) top.node, currentNode, serviceContext);
	}

}
